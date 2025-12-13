package cr.ac.utn.petlink

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import cr.ac.utn.petlink.databinding.ActivityAdoptionBinding
import cr.ac.utn.petlink.entity.AppData
import cr.ac.utn.petlink.entity.Pet

class AdoptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdoptionBinding
    private lateinit var adapter: AdoptionAdapter
    private var actionMode: ActionMode? = null
    private var allAdoptionPets = mutableListOf<Pet>()
    private var currentAdoptionPets = mutableListOf<Pet>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdoptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupBottomNavigation()
        setupRecyclerView()

        binding.fabAddAdoption.setOnClickListener {
            startActivity(Intent(this, AddAdoptionActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        refreshAdoptionList()
    }

    private fun setupRecyclerView() {
        adapter = AdoptionAdapter(currentAdoptionPets, 
            { pet -> onItemClick(pet) }, 
            { pet -> onAdoptClick(pet) },
            { pet -> openPetDetails(pet) })
        binding.adoptionsRecyclerView.adapter = adapter
        binding.adoptionsRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun openPetDetails(pet: Pet) {
        val intent = Intent(this, PetDetailActivity::class.java)
        intent.putExtra("pet_id", pet.id)
        startActivity(intent)
    }

    private fun onAdoptClick(pet: Pet) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar Adopción")
            .setMessage("¿Estás seguro de que deseas adoptar a ${pet.name}?")
            .setPositiveButton("Adoptar") { _, _ ->
                val petToUpdate = AppData.pets.find { it.id == pet.id }
                petToUpdate?.isForAdoption = false
                refreshAdoptionList()
                Toast.makeText(this, "${pet.name} ha sido adoptado.", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun refreshAdoptionList() {
        allAdoptionPets.clear()
        allAdoptionPets.addAll(AppData.pets.filter { it.isForAdoption })
        currentAdoptionPets.clear()
        currentAdoptionPets.addAll(allAdoptionPets)
        adapter.notifyDataSetChanged()
    }

    private fun onItemClick(pet: Pet) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(ActionModeCallback())
        }
        toggleSelection(pet)
    }

    private fun toggleSelection(pet: Pet) {
        val position = currentAdoptionPets.indexOf(pet)
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
        binding.bottomNavigation.selectedItemId = R.id.nav_adoptions
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.nav_adoptions -> true
                R.id.nav_veterinarians -> {
                    startActivity(Intent(this, VeterinariansActivity::class.java))
                    true
                }
                R.id.nav_lost -> {
                    startActivity(Intent(this, LostPetsActivity::class.java))
                    true
                }
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
                    val intent = Intent(this@AdoptionActivity, AddAdoptionActivity::class.java)
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
                AppData.pets.removeAll(selectedPets)
                mode.finish()
                refreshAdoptionList()
                 Toast.makeText(this, "Mascotas eliminadas", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.adoption_menu, menu)
        menu?.findItem(R.id.action_add_adoption)?.isVisible = false
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as? SearchView

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                currentAdoptionPets.clear()
                if (newText.isNullOrEmpty()) {
                    currentAdoptionPets.addAll(allAdoptionPets)
                } else {
                    val filteredPets = allAdoptionPets.filter {
                        it.name.contains(newText, ignoreCase = true)
                    }
                    currentAdoptionPets.addAll(filteredPets)
                }
                adapter.notifyDataSetChanged()
                return true
            }
        })

        return true
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
