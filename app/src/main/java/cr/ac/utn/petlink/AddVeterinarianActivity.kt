package cr.ac.utn.petlink

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.petlink.databinding.ActivityAddVeterinarianBinding
import cr.ac.utn.petlink.entity.AppData
import cr.ac.utn.petlink.entity.Veterinarian

class AddVeterinarianActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddVeterinarianBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVeterinarianBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.saveVetButton.setOnClickListener {
            saveVeterinarian()
        }
    }

    private fun saveVeterinarian() {
        val veterinarian = Veterinarian(
            id = System.currentTimeMillis(),
            name = binding.etVetName.text.toString(),
            address = binding.etVetAddress.text.toString(),
            phone = binding.etVetPhone.text.toString(),
            website = binding.etVetWebsite.text.toString(),
            imageUrl = ""
        )

        AppData.veterinarians.add(veterinarian)

        finish()
    }
}
