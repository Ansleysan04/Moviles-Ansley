package cr.ac.utn.petlink

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.petlink.databinding.ActivityAddAdoptionBinding
import cr.ac.utn.petlink.entity.AppData
import cr.ac.utn.petlink.entity.Pet

class AddAdoptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddAdoptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAdoptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
            ownerId = 0, // Placeholder for owner ID
            photoUrl = ""
        )

        AppData.pets.add(pet)

        finish()
    }
}
