package cr.ac.utn.petlink

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import cr.ac.utn.petlink.databinding.ActivityVeterinariansBinding
import cr.ac.utn.petlink.entity.AppData
import cr.ac.utn.petlink.entity.Veterinarian

class VeterinariansActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVeterinariansBinding
    private lateinit var adapter: VeterinarianAdapter
    private var actionMode: ActionMode? = null
    private var allVeterinarians = mutableListOf<Veterinarian>()
    private var currentVeterinarians = mutableListOf<Veterinarian>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVeterinariansBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupBottomNavigation()
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        refreshVeterinarianList()
    }

    private fun setupRecyclerView() {
        adapter = VeterinarianAdapter(currentVeterinarians, { vet -> onItemClick(vet) }, { vet -> onItemLongClick(vet) })
        binding.veterinariansRecyclerView.adapter = adapter
        binding.veterinariansRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun refreshVeterinarianList() {
        allVeterinarians.clear()
        allVeterinarians.addAll(AppData.veterinarians)
        currentVeterinarians.clear()
        currentVeterinarians.addAll(allVeterinarians)
        adapter.notifyDataSetChanged()
    }

    private fun onItemClick(vet: Veterinarian) {
        if (actionMode != null) {
            toggleSelection(vet)
        }
    }

    private fun onItemLongClick(vet: Veterinarian): Boolean {
        if (actionMode == null) {
            actionMode = startSupportActionMode(ActionModeCallback())
        }
        toggleSelection(vet)
        return true
    }

    private fun toggleSelection(vet: Veterinarian) {
        val position = currentVeterinarians.indexOf(vet)
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
        binding.bottomNavigation.selectedItemId = R.id.nav_veterinarians
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
                R.id.nav_veterinarians -> true
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
                    val selectedVet = adapter.getSelectedItems().first()
                    val intent = Intent(this@VeterinariansActivity, AddVeterinarianActivity::class.java)
                    intent.putExtra("vet_id", selectedVet.id)
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
                val selectedVets = adapter.getSelectedItems()
                AppData.veterinarians.removeAll(selectedVets)
                mode.finish()
                refreshVeterinarianList()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.veterinarians_menu, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as? SearchView

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                currentVeterinarians.clear()
                if (newText.isNullOrEmpty()) {
                    currentVeterinarians.addAll(allVeterinarians)
                } else {
                    val filteredVets = allVeterinarians.filter {
                        it.name.contains(newText, ignoreCase = true)
                    }
                    currentVeterinarians.addAll(filteredVets)
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
            R.id.action_add_veterinarian -> {
                startActivity(Intent(this, AddVeterinarianActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
