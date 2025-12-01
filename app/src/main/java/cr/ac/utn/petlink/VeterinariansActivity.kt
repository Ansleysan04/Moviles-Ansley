package cr.ac.utn.petlink

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import cr.ac.utn.petlink.databinding.ActivityVeterinariansBinding
import cr.ac.utn.petlink.entity.AppData

class VeterinariansActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVeterinariansBinding
    private lateinit var adapter: VeterinarianAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVeterinariansBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set the selected item in the bottom navigation
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
                R.id.nav_veterinarians -> {
                    // Already on this screen, do nothing
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

        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    private fun setupRecyclerView() {
        adapter = VeterinarianAdapter(AppData.veterinarians)
        binding.veterinariansRecyclerView.adapter = adapter
        binding.veterinariansRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.veterinarians_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_veterinarian -> {
                startActivity(Intent(this, AddVeterinarianActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
