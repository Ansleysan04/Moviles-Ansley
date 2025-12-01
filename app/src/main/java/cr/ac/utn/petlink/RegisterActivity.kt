package cr.ac.utn.petlink

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.petlink.databinding.ActivityRegisterBinding
import cr.ac.utn.petlink.entity.AppData
import cr.ac.utn.petlink.entity.User

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.registerButton.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val user = User(
            id = System.currentTimeMillis(),
            firstName = binding.etFirstName.text.toString(),
            lastName = binding.etLastName.text.toString(),
            email = binding.etEmail.text.toString(),
            phone = binding.etPhone.text.toString(),
            password = binding.etPassword.text.toString()
        )

        AppData.users.add(user)
        AppData.currentUser = user

        finish()
    }
}
