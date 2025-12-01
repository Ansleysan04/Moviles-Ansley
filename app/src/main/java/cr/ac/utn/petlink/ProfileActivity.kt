package cr.ac.utn.petlink

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import cr.ac.utn.petlink.databinding.ActivityProfileBinding
import cr.ac.utn.petlink.entity.AppData

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var adapter: MyPetsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
                R.id.nav_profile -> {
                    // Already on this screen, do nothing
                    true
                }
                else -> false
            }
        }

        binding.editProfileIcon.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        setupRecyclerView()
        loadUserData()
    }

    override fun onResume() {
        super.onResume()
        loadUserData()
        adapter.notifyDataSetChanged()
    }

    private fun setupRecyclerView() {
        adapter = MyPetsAdapter(AppData.pets.filter { it.ownerId == AppData.currentUser?.id })
        binding.myPetsRecyclerView.adapter = adapter
        binding.myPetsRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun loadUserData() {
        AppData.currentUser?.let {
            binding.userName.text = "${it.firstName} ${it.lastName}"
            binding.userEmail.text = it.email
            binding.userPhone.text = it.phone
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_my_pet -> {
                startActivity(Intent(this, AddMyPetActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
