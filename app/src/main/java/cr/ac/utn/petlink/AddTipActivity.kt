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
import cr.ac.utn.petlink.databinding.ActivityAddTipBinding
import cr.ac.utn.petlink.entity.AppData
import cr.ac.utn.petlink.entity.Tip
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddTipActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTipBinding
    private var editingTip: Tip? = null
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
            binding.tipImagePreview.setImageURI(imageUri)
        }
    }

    private val selectImageFromGalleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            imageUri = it
            binding.tipImagePreview.setImageURI(imageUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTipBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val tipId = intent.getLongExtra("tip_id", -1L)
        if (tipId != -1L) {
            editingTip = AppData.tips.find { it.id == tipId }
            editingTip?.let {
                populateTipDetails(it)
                supportActionBar?.title = "Editar Consejo"
            }
        } else {
            supportActionBar?.title = "Añadir Consejo"
        }

        binding.selectImageButton.setOnClickListener {
            showImageSourceDialog()
        }

        binding.saveTipButton.setOnClickListener {
            showSaveConfirmationDialog()
        }
    }

    private fun populateTipDetails(tip: Tip) {
        binding.etTipTitle.setText(tip.title)
        binding.etTipDescription.setText(tip.description)
        tip.photoUrl?.let {
            if (it.isNotEmpty()) {
                imageUri = Uri.parse(it)
                Glide.with(this).load(imageUri).into(binding.tipImagePreview)
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
        val title = if (editingTip == null) "Confirmar Creación" else "Confirmar Edición"
        val message = if (editingTip == null) "¿Estás seguro de que deseas guardar este nuevo consejo?" else "¿Estás seguro de que deseas guardar los cambios?"

        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Guardar") { _, _ ->
                saveTip()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun saveTip() {
        val title = binding.etTipTitle.text.toString()
        val description = binding.etTipDescription.text.toString()

        if (editingTip == null) {
            val newTip = Tip(
                id = System.currentTimeMillis(),
                title = title,
                description = description,
                photoUrl = imageUri?.toString() ?: ""
            )
            AppData.tips.add(newTip)
            Toast.makeText(this, "Consejo guardado.", Toast.LENGTH_SHORT).show()
        } else {
            editingTip?.apply {
                this.title = title
                this.description = description
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
