package cr.ac.utn.petlink

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.widget.LinearLayoutManager
import cr.ac.utn.petlink.databinding.ActivityLostPetsBinding
import cr.ac.utn.petlink.entity.AppData
import cr.ac.utn.petlink.entity.LostPet

class LostPetsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLostPetsBinding
    private lateinit var adapter: LostPetAdapter
    private var actionMode: ActionMode? = null
    private val lostPets = mutableListOf<LostPet>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLostPetsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupBottomNavigation()
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        refreshLostPetList()
    }

    private fun setupRecyclerView() {
        adapter = LostPetAdapter(lostPets, 
            { pet -> onItemClick(pet) }, 
            { pet -> onItemLongClick(pet) },
            { pet -> onContactClick(pet) },
            { pet -> onCallClick(pet) })
        binding.lostPetsRecyclerView.adapter = adapter
        binding.lostPetsRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun onContactClick(pet: LostPet) {
        // Handle contact button click (e.g., open a chat screen)
        Toast.makeText(this, "Contactando a ${pet.contactPhone}", Toast.LENGTH_SHORT).show()
    }

    private fun onCallClick(pet: LostPet) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${pet.contactPhone}"))
        startActivity(intent)
    }

    private fun refreshLostPetList() {
        lostPets.clear()
        lostPets.addAll(AppData.lostPets)
        adapter.notifyDataSetChanged()
    }

    private fun onItemClick(pet: LostPet) {
        if (actionMode != null) {
            toggleSelection(pet)
        }
    }

    private fun onItemLongClick(pet: LostPet): Boolean {
        if (actionMode == null) {
            actionMode = startSupportActionMode(ActionModeCallback())
        }
        toggleSelection(pet)
        return true
    }

    private fun toggleSelection(pet: LostPet) {
        val position = lostPets.indexOf(pet)
        if (position != -1) {
            adapter.toggleSelection(position)
            val count = adapter.getSelectedItemCount()
            if (count == 0) {
                actionMode?.finish()
            } else {
                actionMode?.title = "$count seleccionados"
                actionMode?.invalidate()
            }
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.selectedItemId = R.id.nav_lost
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.nav_adoptions -> {
                    startActivity(Intent(this, AdoptionActivity::class.java))
                    true
                }
                R.id.nav_veterinarians -> {
                    startActivity(Intent(this, VeterinariansActivity::class.java))
                    true
                }
                R.id.nav_lost -> true
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    inner class ActionModeCallback : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            mode.menuInflater.inflate(R.menu.selection_menu, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            menu.findItem(R.id.action_edit).isVisible = adapter.getSelectedItemCount() == 1
            return true
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.action_delete -> {
                    showDeleteConfirmationDialog(mode)
                    return true
                }
                R.id.action_edit -> {
                    val selectedPet = adapter.getSelectedItems().first()
                    val intent = Intent(this@LostPetsActivity, AddLostPetActivity::class.java)
                    intent.putExtra("pet_id", selectedPet.id)
                    startActivity(intent)
                    mode.finish()
                    return true
                }
            }
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            adapter.clearSelections()
            actionMode = null
        }
    }

    private fun showDeleteConfirmationDialog(mode: ActionMode) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar Eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar los items seleccionados?")
            .setPositiveButton("Eliminar") { _, _ ->
                val selectedPets = adapter.getSelectedItems()
                AppData.lostPets.removeAll(selectedPets)
                mode.finish()
                refreshLostPetList()
                Toast.makeText(this, "Mascotas eliminadas", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.lost_pets_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_add_lost_pet -> {
                startActivity(Intent(this, AddLostPetActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
