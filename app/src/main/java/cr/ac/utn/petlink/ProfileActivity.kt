package cr.ac.utn.petlink

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.widget.LinearLayoutManager
import cr.ac.utn.petlink.databinding.ActivityProfileBinding
import cr.ac.utn.petlink.entity.AppData
import cr.ac.utn.petlink.entity.Pet

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var adapter: MyPetsAdapter
    private var actionMode: ActionMode? = null
    private val myPets = mutableListOf<Pet>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupBottomNavigation()
        setupRecyclerView()
        loadUserData()

        binding.userInfoCard.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("user_id", AppData.currentUser?.id)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadUserData()
        refreshMyPetsList()
    }

    private fun setupRecyclerView() {
        adapter = MyPetsAdapter(myPets, { pet -> onItemClick(pet) }, { pet -> onItemLongClick(pet) })
        binding.myPetsRecyclerView.adapter = adapter
        binding.myPetsRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun refreshMyPetsList() {
        myPets.clear()
        myPets.addAll(AppData.pets.filter { it.ownerId == AppData.currentUser?.id && !it.isForAdoption })
        adapter.notifyDataSetChanged()
    }

    private fun loadUserData() {
        AppData.currentUser?.let {
            binding.userName.text = "${it.firstName} ${it.lastName}"
            binding.userEmail.text = it.email
            binding.userPhone.text = it.phone
        }
    }

    private fun onItemClick(pet: Pet) {
        if (actionMode != null) {
            toggleSelection(pet)
        }
    }

    private fun onItemLongClick(pet: Pet): Boolean {
        if (actionMode == null) {
            actionMode = startSupportActionMode(ActionModeCallback())
        }
        toggleSelection(pet)
        return true
    }

    private fun toggleSelection(pet: Pet) {
        val position = myPets.indexOf(pet)
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
        binding.bottomNavigation.selectedItemId = R.id.nav_profile
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
                R.id.nav_lost -> {
                    startActivity(Intent(this, LostPetsActivity::class.java))
                    true
                }
                R.id.nav_profile -> true
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
                    val intent = Intent(this@ProfileActivity, AddMyPetActivity::class.java)
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
            .setMessage("¿Estás seguro de que deseas eliminar las mascotas seleccionadas?")
            .setPositiveButton("Eliminar") { _, _ ->
                val selectedPets = adapter.getSelectedItems()
                AppData.pets.removeAll(selectedPets)
                mode.finish()
                refreshMyPetsList()
                Toast.makeText(this, "Mascotas eliminadas", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_add_my_pet -> {
                startActivity(Intent(this, AddMyPetActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
