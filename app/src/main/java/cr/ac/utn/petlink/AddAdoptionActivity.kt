package cr.ac.utn.petlink

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.petlink.databinding.ActivityAddAdoptionBinding
import cr.ac.utn.petlink.entity.AppData
import cr.ac.utn.petlink.entity.Pet

class AddAdoptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddAdoptionBinding
    private var petToEdit: Pet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAdoptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val petId = intent.getLongExtra("pet_id", -1)
        if (petId != -1L) {
            petToEdit = AppData.pets.find { it.id == petId }
            petToEdit?.let { pet ->
                binding.etPetName.setText(pet.name)
                binding.etSpecies.setText(pet.species)
                binding.etBreed.setText(pet.breed)
                binding.etAge.setText(pet.age.toString())
                binding.etLocation.setText(pet.location)
                binding.etDescription.setText(pet.description)
                // Load image here if you have a URL
            }
        }

        binding.savePetButton.setOnClickListener {
            showSaveConfirmationDialog()
        }
    }

    private fun showSaveConfirmationDialog() {
        val message = if (petToEdit == null) "¿Estás seguro de que deseas guardar esta nueva mascota?" else "¿Estás seguro de que deseas actualizar los datos de esta mascota?"
        AlertDialog.Builder(this)
            .setTitle("Confirmar Guardado")
            .setMessage(message)
            .setPositiveButton("Guardar") { _, _ ->
                savePet()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun savePet() {
        if (petToEdit == null) {
            // Create new pet
            val newPet = Pet(
                id = System.currentTimeMillis(),
                name = binding.etPetName.text.toString(),
                species = binding.etSpecies.text.toString(),
                breed = binding.etBreed.text.toString(),
                age = binding.etAge.text.toString().toIntOrNull() ?: 0,
                ownerId = AppData.currentUser?.id ?: 0,
                location = binding.etLocation.text.toString(),
                description = binding.etDescription.text.toString(),
                photoUrl = "",
                isForAdoption = true
            )
            AppData.pets.add(newPet)
        } else {
            // Update existing pet
            petToEdit?.apply {
                name = binding.etPetName.text.toString()
                species = binding.etSpecies.text.toString()
                breed = binding.etBreed.text.toString()
                age = binding.etAge.text.toString().toIntOrNull() ?: 0
                location = binding.etLocation.text.toString()
                description = binding.etDescription.text.toString()
            }
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
