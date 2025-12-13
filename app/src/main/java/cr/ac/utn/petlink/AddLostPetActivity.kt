package cr.ac.utn.petlink

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.petlink.databinding.ActivityAddLostPetBinding
import cr.ac.utn.petlink.entity.AppData
import cr.ac.utn.petlink.entity.LostPet
import java.util.Date

class AddLostPetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddLostPetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLostPetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.saveLostPetButton.setOnClickListener {
            saveLostPet()
        }
    }

    private fun saveLostPet() {
        val lostPet = LostPet(
            id = System.currentTimeMillis(),
            name = binding.etPetName.text.toString(),
            species = binding.etSpecies.text.toString(),
            breed = binding.etBreed.text.toString(),
            lastSeenLocation = binding.etLastSeen.text.toString(),
            lostDate = Date(), // Placeholder for date picker
            contactPhone = binding.etContactPhone.text.toString(),
            description = binding.etDescription.text.toString(),
            photoUrl = ""
        )

        AppData.lostPets.add(lostPet)

        finish()
    }
}
