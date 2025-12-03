package cr.ac.utn.petlink

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.petlink.databinding.ActivityAddLostPetBinding
import cr.ac.utn.petlink.entity.AppData
import cr.ac.utn.petlink.entity.LostPet
import java.util.Date

class AddLostPetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddLostPetBinding
    private var editingPet: LostPet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLostPetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val petId = intent.getLongExtra("pet_id", -1L)
        if (petId != -1L) {
            editingPet = AppData.lostPets.find { it.id == petId }
            editingPet?.let {
                populatePetDetails(it)
                supportActionBar?.title = "Editar Mascota Perdida"
            }
        }

        binding.saveLostPetButton.setOnClickListener {
            showSaveConfirmationDialog()
        }
    }

    private fun populatePetDetails(pet: LostPet) {
        binding.etPetName.setText(pet.name)
        binding.etSpecies.setText(pet.species)
        binding.etBreed.setText(pet.breed)
        binding.etLastSeen.setText(pet.lastSeenLocation)
        binding.etContactPhone.setText(pet.contactPhone)
        binding.etDescription.setText(pet.description)
    }

    private fun showSaveConfirmationDialog() {
        val title = if (editingPet == null) "Confirmar Creación" else "Confirmar Edición"
        val message = if (editingPet == null) "¿Estás seguro de que deseas guardar esta nueva mascota perdida?" else "¿Estás seguro de que deseas guardar los cambios?"

        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Guardar") { _, _ ->
                saveLostPet()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun saveLostPet() {
        val name = binding.etPetName.text.toString()
        val species = binding.etSpecies.text.toString()
        val breed = binding.etBreed.text.toString()
        val lastSeenLocation = binding.etLastSeen.text.toString()
        val contactPhone = binding.etContactPhone.text.toString()
        val description = binding.etDescription.text.toString()

        if (editingPet == null) {
            // Create new pet
            val newPet = LostPet(
                id = System.currentTimeMillis(),
                name = name,
                species = species,
                breed = breed,
                lastSeenLocation = lastSeenLocation,
                lostDate = Date(), // Placeholder
                contactPhone = contactPhone,
                description = description,
                photoUrl = "" // Placeholder
            )
            AppData.lostPets.add(newPet)
            Toast.makeText(this, "Mascota perdida guardada.", Toast.LENGTH_SHORT).show()
        } else {
            // Update existing pet
            editingPet?.apply {
                this.name = name
                this.species = species
                this.breed = breed
                this.lastSeenLocation = lastSeenLocation
                this.contactPhone = contactPhone
                this.description = description
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
