package cr.ac.utn.petlink

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
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

        binding.userInfoCard.setOnClickListener {
            startActivity(Intent(this, UserDetailActivity::class.java))
        }

        binding.registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.manageTipsButton.setOnClickListener { // Added this listener
            startActivity(Intent(this, TipsActivity::class.java))
        }

        binding.fabAddMyPet.setOnClickListener {
            startActivity(Intent(this, AddMyPetActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadUserData()
        refreshMyPetsList()
    }

    private fun setupRecyclerView() {
        adapter = MyPetsAdapter(myPets, 
            { pet -> onItemClick(pet) },
            { pet -> openPetDetails(pet) })
        binding.myPetsRecyclerView.adapter = adapter
        binding.myPetsRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun openPetDetails(pet: Pet) {
        val intent = Intent(this, PetDetailActivity::class.java)
        intent.putExtra("pet_id", pet.id)
        startActivity(intent)
    }

    private fun refreshMyPetsList() {
        myPets.clear()
        if (AppData.currentUser != null) {
            myPets.addAll(AppData.pets.filter { it.ownerId == AppData.currentUser?.id && !it.isForAdoption })
        }
        adapter.notifyDataSetChanged()
    }

    private fun loadUserData() {
        if (AppData.currentUser != null) {
            binding.userInfoCard.visibility = View.VISIBLE
            binding.noUserLayout.visibility = View.GONE
            AppData.currentUser?.let {
                binding.userName.text = "${it.firstName} ${it.lastName}"
                binding.userEmail.text = it.email
                binding.userPhone.text = it.phone
                it.photoUrl?.let {
                    if (it.isNotEmpty()) {
                        Glide.with(this)
                            .load(Uri.parse(it))
                            .into(binding.userImage)
                    } else {
                        binding.userImage.setImageResource(R.mipmap.ic_launcher) // Placeholder
                    }
                }
            }
        } else {
            binding.userInfoCard.visibility = View.GONE
            binding.noUserLayout.visibility = View.VISIBLE
        }
    }

    private fun onItemClick(pet: Pet) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(ActionModeCallback())
        }
        toggleSelection(pet)
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
