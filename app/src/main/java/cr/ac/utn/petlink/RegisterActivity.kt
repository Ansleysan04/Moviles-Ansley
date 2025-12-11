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
import cr.ac.utn.petlink.databinding.ActivityRegisterBinding
import cr.ac.utn.petlink.entity.AppData
import cr.ac.utn.petlink.entity.User
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private var editingUser: User? = null
    private var imageUri: Uri? = null
    private lateinit var currentPhotoPath: String
    private var birthDate: Date? = null

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            openCamera()
        } else {
            Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            binding.profileImage.setImageURI(imageUri)
        }
    }

    private val selectImageFromGalleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            imageUri = it
            binding.profileImage.setImageURI(imageUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val userId = intent.getLongExtra("user_id", -1L)
        if (userId != -1L) {
            editingUser = AppData.users.find { it.id == userId }
            editingUser?.let {
                populateUserDetails(it)
                supportActionBar?.title = "Editar Perfil"
                binding.registerButton.text = "Guardar Cambios"
            }
        } else {
            supportActionBar?.title = "Registro"
        }

        binding.addPhotoButton.setOnClickListener {
            showImageSourceDialog()
        }

        binding.birthDateButton.setOnClickListener {
            showDatePickerDialog()
        }

        binding.registerButton.setOnClickListener {
            showSaveConfirmationDialog()
        }
    }

    private fun populateUserDetails(user: User) {
        binding.etFirstName.setText(user.firstName)
        binding.etLastName.setText(user.lastName)
        binding.etAddress.setText(user.address)
        binding.etEmail.setText(user.email)
        binding.etPhone.setText(user.phone)
        binding.etPassword.hint = "Nueva contraseña (opcional)"
        user.photoUrl?.let {
            if (it.isNotEmpty()) {
                imageUri = Uri.parse(it)
                Glide.with(this).load(imageUri).into(binding.profileImage)
            }
        }
        user.birthDate?.let {
            birthDate = it
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            binding.birthDateButton.text = format.format(it)
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
            birthDate = selectedDate.time
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            binding.birthDateButton.text = format.format(birthDate!!)
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
        val title = if (editingUser == null) "Confirmar Registro" else "Confirmar Edición"
        val message = if (editingUser == null) "¿Estás seguro de que deseas registrarte?" else "¿Estás seguro de que deseas guardar los cambios en tu perfil?"

        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Guardar") { _, _ ->
                saveUser()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun saveUser() {
        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val address = binding.etAddress.text.toString()
        val email = binding.etEmail.text.toString()
        val phone = binding.etPhone.text.toString()
        val password = binding.etPassword.text.toString()

        if (editingUser == null) {
            val newUser = User(
                id = System.currentTimeMillis(),
                firstName = firstName,
                lastName = lastName,
                email = email,
                phone = phone,
                password = password,
                photoUrl = imageUri?.toString() ?: "",
                birthDate = birthDate,
                address = address
            )
            AppData.users.add(newUser)
            AppData.currentUser = newUser
            Toast.makeText(this, "Usuario registrado con éxito.", Toast.LENGTH_SHORT).show()
        } else {
            editingUser?.apply {
                this.firstName = firstName
                this.lastName = lastName
                this.address = address
                this.email = email
                this.phone = phone
                this.birthDate = birthDate
                if (password.isNotBlank()) {
                    this.password = password
                }
                this.photoUrl = imageUri?.toString() ?: this.photoUrl
            }
            AppData.currentUser = editingUser
            Toast.makeText(this, "Perfil actualizado con éxito.", Toast.LENGTH_SHORT).show()
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
