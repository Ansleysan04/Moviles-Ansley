package cr.ac.utn.petlink

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.petlink.databinding.ActivityAddMyPetBinding
import cr.ac.utn.petlink.entity.AppData
import cr.ac.utn.petlink.entity.Pet

class AddMyPetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMyPetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMyPetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Add My Pet"

        binding.savePetButton.setOnClickListener {
            savePet()
        }
    }

    private fun savePet() {
        val pet = Pet(
            id = System.currentTimeMillis(),
            name = binding.etPetName.text.toString(),
            species = binding.etSpecies.text.toString(),
            breed = binding.etBreed.text.toString(),
            age = binding.etAge.text.toString().toIntOrNull() ?: 0,
            ownerId = AppData.currentUser?.id ?: 0, // Associate with the current user
            photoUrl = ""
        )

        AppData.pets.add(pet)

        finish()
    }
}
