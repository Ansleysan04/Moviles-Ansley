package cr.ac.utn.petlink

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.petlink.databinding.ActivityAddMyPetBinding
import cr.ac.utn.petlink.entity.AppData
import cr.ac.utn.petlink.entity.Pet

class AddMyPetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMyPetBinding
    private var editingPet: Pet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMyPetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val petId = intent.getLongExtra("pet_id", -1L)
        if (petId != -1L) {
            editingPet = AppData.pets.find { it.id == petId }
            editingPet?.let {
                populatePetDetails(it)
                supportActionBar?.title = "Editar Mascota"
            }
        } else {
            supportActionBar?.title = "Añadir Mascota"
        }

        binding.savePetButton.setOnClickListener {
            showSaveConfirmationDialog()
        }
    }

    private fun populatePetDetails(pet: Pet) {
        binding.etPetName.setText(pet.name)
        binding.etSpecies.setText(pet.species)
        binding.etBreed.setText(pet.breed)
        binding.etAge.setText(pet.age.toString())
    }

    private fun showSaveConfirmationDialog() {
        val title = if (editingPet == null) "Confirmar Creación" else "Confirmar Edición"
        val message = if (editingPet == null) "¿Estás seguro de que deseas guardar esta nueva mascota?" else "¿Estás seguro de que deseas guardar los cambios?"

        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Guardar") { _, _ ->
                savePet()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun savePet() {
        val name = binding.etPetName.text.toString()
        val species = binding.etSpecies.text.toString()
        val breed = binding.etBreed.text.toString()
        val age = binding.etAge.text.toString().toIntOrNull() ?: 0

        if (editingPet == null) {
            // Create new pet
            val newPet = Pet(
                id = System.currentTimeMillis(),
                name = name,
                species = species,
                breed = breed,
                age = age,
                ownerId = AppData.currentUser?.id ?: 0,
                location = "", // Not specified in this form
                description = "", // Not specified in this form
                photoUrl = "",
                isForAdoption = false
            )
            AppData.pets.add(newPet)
            Toast.makeText(this, "Mascota guardada.", Toast.LENGTH_SHORT).show()
        } else {
            // Update existing pet
            editingPet?.apply {
                this.name = name
                this.species = species
                this.breed = breed
                this.age = age
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
