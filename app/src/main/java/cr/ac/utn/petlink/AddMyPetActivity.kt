package cr.ac.utn.petlink

import android.Manifest
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
import cr.ac.utn.petlink.databinding.ActivityAddMyPetBinding
import cr.ac.utn.petlink.entity.AppData
import cr.ac.utn.petlink.entity.Pet
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddMyPetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMyPetBinding
    private var editingPet: Pet? = null
    private var imageUri: Uri? = null
    private lateinit var currentPhotoPath: String

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            openCamera()
        } else {
            Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            binding.petImagePreview.setImageURI(imageUri)
        }
    }

    private val selectImageFromGalleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            imageUri = it
            binding.petImagePreview.setImageURI(imageUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMyPetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val petId = intent.getLongExtra("pet_id", -1L)
        if (petId != -1L) {
            editingPet = AppData.pets.find { it.id == petId }
            editingPet?.let {
                populatePetDetails(it)
                supportActionBar?.title = "Editar Mascota"
            }
        } else {
            supportActionBar?.title = "Añadir Mascota"
        }

        binding.selectImageButton.setOnClickListener {
            showImageSourceDialog()
        }

        binding.savePetButton.setOnClickListener {
            showSaveConfirmationDialog()
        }
    }

    private fun populatePetDetails(pet: Pet) {
        binding.etPetName.setText(pet.name)
        binding.etSpecies.setText(pet.species)
        binding.etBreed.setText(pet.breed)
        binding.etAge.setText(pet.age.toString())
        binding.etLocation.setText(pet.location)
        binding.etPersonality.setText(pet.personality)
        binding.etBehavior.setText(pet.behavior)
        binding.etHealth.setText(pet.health)
        binding.etNeeds.setText(pet.needs)
        binding.etDescription.setText(pet.description)
        pet.photoUrl?.let {
            if (it.isNotEmpty()) {
                imageUri = Uri.parse(it)
                Glide.with(this).load(imageUri).into(binding.petImagePreview)
            }
        }
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
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                // Continue only if the File was successfully created
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
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun openGallery() {
        selectImageFromGalleryLauncher.launch("image/*")
    }

    private fun showSaveConfirmationDialog() {
        val title = if (editingPet == null) "Confirmar Creación" else "Confirmar Edición"
        val message = if (editingPet == null) "¿Estás seguro de que deseas guardar esta nueva mascota?" else "¿Estás seguro de que deseas guardar los cambios?"

        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Guardar") { _, _ ->
                savePet()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun savePet() {
        val name = binding.etPetName.text.toString()
        val species = binding.etSpecies.text.toString()
        val breed = binding.etBreed.text.toString()
        val age = binding.etAge.text.toString().toIntOrNull() ?: 0
        val location = binding.etLocation.text.toString()
        val personality = binding.etPersonality.text.toString()
        val behavior = binding.etBehavior.text.toString()
        val health = binding.etHealth.text.toString()
        val needs = binding.etNeeds.text.toString()
        val description = binding.etDescription.text.toString()

        if (editingPet == null) {
            val newPet = Pet(
                id = System.currentTimeMillis(),
                name = name,
                species = species,
                breed = breed,
                age = age,
                ownerId = AppData.currentUser?.id ?: 0,
                location = location,
                description = description,
                photoUrl = imageUri?.toString() ?: "",
                isForAdoption = false,
                personality = personality,
                behavior = behavior,
                health = health,
                needs = needs
            )
            AppData.pets.add(newPet)
            Toast.makeText(this, "Mascota guardada.", Toast.LENGTH_SHORT).show()
        } else {
            editingPet?.apply {
                this.name = name
                this.species = species
                this.breed = breed
                this.age = age
                this.location = location
                this.description = description
                this.personality = personality
                this.behavior = behavior
                this.health = health
                this.needs = needs
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
