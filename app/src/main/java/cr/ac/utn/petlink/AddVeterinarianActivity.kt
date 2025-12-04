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
import cr.ac.utn.petlink.databinding.ActivityAddVeterinarianBinding
import cr.ac.utn.petlink.entity.AppData
import cr.ac.utn.petlink.entity.Veterinarian
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddVeterinarianActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddVeterinarianBinding
    private var editingVet: Veterinarian? = null
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
            binding.vetImage.setImageURI(imageUri)
        }
    }

    private val selectImageFromGalleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            imageUri = it
            binding.vetImage.setImageURI(imageUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVeterinarianBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val vetId = intent.getLongExtra("vet_id", -1L)
        if (vetId != -1L) {
            editingVet = AppData.veterinarians.find { it.id == vetId }
            editingVet?.let {
                populateVetDetails(it)
                supportActionBar?.title = "Editar Veterinario"
            }
        } else {
            supportActionBar?.title = "Añadir Veterinario"
        }

        binding.addPhotoButton.setOnClickListener {
            showImageSourceDialog()
        }

        binding.saveVetButton.setOnClickListener {
            showSaveConfirmationDialog()
        }
    }

    private fun populateVetDetails(vet: Veterinarian) {
        binding.etVetName.setText(vet.name)
        binding.etVetAddress.setText(vet.address)
        binding.etVetPhone.setText(vet.phone)
        binding.etVetWebsite.setText(vet.website)
        vet.imageUrl?.let {
            if (it.isNotEmpty()) {
                imageUri = Uri.parse(it)
                Glide.with(this).load(imageUri).into(binding.vetImage)
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
        val title = if (editingVet == null) "Confirmar Creación" else "Confirmar Edición"
        val message = if (editingVet == null) "¿Estás seguro de que deseas guardar este nuevo veterinario?" else "¿Estás seguro de que deseas guardar los cambios?"

        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Guardar") { _, _ ->
                saveVeterinarian()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun saveVeterinarian() {
        val name = binding.etVetName.text.toString()
        val address = binding.etVetAddress.text.toString()
        val phone = binding.etVetPhone.text.toString()
        val website = binding.etVetWebsite.text.toString()

        if (editingVet == null) {
            val newVet = Veterinarian(
                id = System.currentTimeMillis(),
                name = name,
                address = address,
                phone = phone,
                website = website,
                imageUrl = imageUri?.toString() ?: ""
            )
            AppData.veterinarians.add(newVet)
            Toast.makeText(this, "Veterinario guardado.", Toast.LENGTH_SHORT).show()
        } else {
            editingVet?.apply {
                this.name = name
                this.address = address
                this.phone = phone
                this.website = website
                this.imageUrl = imageUri?.toString() ?: this.imageUrl
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
