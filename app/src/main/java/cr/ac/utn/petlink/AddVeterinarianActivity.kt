package cr.ac.utn.petlink

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.petlink.databinding.ActivityAddVeterinarianBinding
import cr.ac.utn.petlink.entity.AppData
import cr.ac.utn.petlink.entity.Veterinarian

class AddVeterinarianActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddVeterinarianBinding
    private var editingVet: Veterinarian? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVeterinarianBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val vetId = intent.getLongExtra("vet_id", -1L)
        if (vetId != -1L) {
            editingVet = AppData.veterinarians.find { it.id == vetId }
            editingVet?.let {
                populateVetDetails(it)
                supportActionBar?.title = "Editar Veterinario"
            }
        }

        binding.saveVetButton.setOnClickListener {
            showSaveConfirmationDialog()
        }
    }

    private fun populateVetDetails(vet: Veterinarian) {
        binding.etVetName.setText(vet.name)
        binding.etVetAddress.setText(vet.address)
        binding.etVetPhone.setText(vet.phone)
        binding.etVetWebsite.setText(vet.website)
    }

    private fun showSaveConfirmationDialog() {
        val title = if (editingVet == null) "Confirmar Creación" else "Confirmar Edición"
        val message = if (editingVet == null) "¿Estás seguro de que deseas guardar este nuevo veterinario?" else "¿Estás seguro de que deseas guardar los cambios?"

        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Guardar") { _, _ ->
                saveVeterinarian()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun saveVeterinarian() {
        val name = binding.etVetName.text.toString()
        val address = binding.etVetAddress.text.toString()
        val phone = binding.etVetPhone.text.toString()
        val website = binding.etVetWebsite.text.toString()

        if (editingVet == null) {
            // Create new vet
            val newVet = Veterinarian(
                id = System.currentTimeMillis(),
                name = name,
                address = address,
                phone = phone,
                website = website,
                imageUrl = "" // Placeholder
            )
            AppData.veterinarians.add(newVet)
            Toast.makeText(this, "Veterinario guardado.", Toast.LENGTH_SHORT).show()
        } else {
            // Update existing vet
            editingVet?.apply {
                this.name = name
                this.address = address
                this.phone = phone
                this.website = website
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
