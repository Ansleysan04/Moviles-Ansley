package cr.ac.utn.petlink

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import cr.ac.utn.petlink.databinding.ActivityAddLostPetBinding
import cr.ac.utn.petlink.entity.AppData
import cr.ac.utn.petlink.entity.LostPet
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddLostPetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddLostPetBinding
    private var editingPet: LostPet? = null
    private var imageUri: Uri? = null
    private lateinit var currentPhotoPath: String
    private var lostDate: Date? = null

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            openCamera()
        } else {
            Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            binding.petImage.setImageURI(imageUri)
        }
    }

    private val selectImageFromGalleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            imageUri = it
            binding.petImage.setImageURI(imageUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLostPetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val petId = intent.getLongExtra("pet_id", -1L)
        if (petId != -1L) {
            editingPet = AppData.lostPets.find { it.id == petId }
            editingPet?.let {
                populatePetDetails(it)
                supportActionBar?.title = "Editar Mascota Perdida"
            }
        } else {
            supportActionBar?.title = "Añadir Mascota Perdida"
        }

        binding.addPhotoButton.setOnClickListener {
            showImageSourceDialog()
        }

        binding.lostDateButton.setOnClickListener {
            showDatePickerDialog()
        }

        binding.saveLostPetButton.setOnClickListener {
            showSaveConfirmationDialog()
        }
    }

    private fun populatePetDetails(pet: LostPet) {
        binding.etPetName.setText(pet.name)
        binding.etSpecies.setText(pet.species)
        binding.etBreed.setText(pet.breed)
        binding.etLastSeen.setText(pet.lastSeenLocation)
        binding.etContactPhone.setText(pet.contactPhone)
        binding.etDescription.setText(pet.description)
        pet.photoUrl?.let {
            if (it.isNotEmpty()) {
                imageUri = Uri.parse(it)
                Glide.with(this).load(imageUri).into(binding.petImage)
            }
        }
        pet.lostDate.let {
            lostDate = it
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            binding.lostDateButton.text = format.format(it)
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDay)
            lostDate = selectedDate.time
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            binding.lostDateButton.text = format.format(lostDate!!)
        }, year, month, day).show()
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Cámara", "Galería")
        AlertDialog.Builder(this)
            .setTitle("Seleccionar Imagen")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> checkCameraPermission()
                    1 -> openGallery()
                }
            }
            .show()
    }

    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "cr.ac.utn.petlink.provider",
                        it
                    )
                    imageUri = photoURI
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    takePictureLauncher.launch(takePictureIntent)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun openGallery() {
        selectImageFromGalleryLauncher.launch("image/*")
    }

    private fun showSaveConfirmationDialog() {
        val title = if (editingPet == null) "Confirmar Creación" else "Confirmar Edición"
        val message = if (editingPet == null) "¿Estás seguro de que deseas guardar esta nueva mascota perdida?" else "¿Estás seguro de que deseas guardar los cambios?"

        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Guardar") { _, _ ->
                saveLostPet()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun saveLostPet() {
        val name = binding.etPetName.text.toString()
        val species = binding.etSpecies.text.toString()
        val breed = binding.etBreed.text.toString()
        val lastSeenLocation = binding.etLastSeen.text.toString()
        val contactPhone = binding.etContactPhone.text.toString()
        val description = binding.etDescription.text.toString()

        if (editingPet == null) {
            val newPet = LostPet(
                id = System.currentTimeMillis(),
                name = name,
                species = species,
                breed = breed,
                lastSeenLocation = lastSeenLocation,
                lostDate = lostDate ?: Date(),
                contactPhone = contactPhone,
                description = description,
                photoUrl = imageUri?.toString() ?: ""
            )
            AppData.lostPets.add(newPet)
            Toast.makeText(this, "Mascota perdida guardada.", Toast.LENGTH_SHORT).show()
        } else {
            editingPet?.apply {
                this.name = name
                this.species = species
                this.breed = breed
                this.lastSeenLocation = lastSeenLocation
                this.contactPhone = contactPhone
                this.description = description
                this.lostDate = lostDate ?: this.lostDate
                this.photoUrl = imageUri?.toString() ?: this.photoUrl
            }
            Toast.makeText(this, "Cambios guardados.", Toast.LENGTH_SHORT).show()
        }

        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
