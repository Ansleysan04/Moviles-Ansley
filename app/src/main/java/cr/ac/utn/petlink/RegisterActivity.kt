package cr.ac.utn.petlink

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.petlink.databinding.ActivityRegisterBinding
import cr.ac.utn.petlink.entity.AppData
import cr.ac.utn.petlink.entity.User

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private var editingUser: User? = null

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

        binding.registerButton.setOnClickListener {
            showSaveConfirmationDialog()
        }
    }

    private fun populateUserDetails(user: User) {
        binding.etFirstName.setText(user.firstName)
        binding.etLastName.setText(user.lastName)
        binding.etEmail.setText(user.email)
        binding.etEmail.isEnabled = false // Email cannot be changed
        binding.etPhone.setText(user.phone)
        binding.etPassword.hint = "Nueva contraseña (opcional)"
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
        val phone = binding.etPhone.text.toString()
        val password = binding.etPassword.text.toString()

        if (editingUser == null) {
            // Create new user
            val email = binding.etEmail.text.toString()
            val newUser = User(
                id = System.currentTimeMillis(),
                firstName = firstName,
                lastName = lastName,
                email = email,
                phone = phone,
                password = password
            )
            AppData.users.add(newUser)
            AppData.currentUser = newUser
            Toast.makeText(this, "Usuario registrado con éxito.", Toast.LENGTH_SHORT).show()
        } else {
            // Update existing user
            editingUser?.apply {
                this.firstName = firstName
                this.lastName = lastName
                this.phone = phone
                if (password.isNotBlank()) {
                    this.password = password
                }
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
