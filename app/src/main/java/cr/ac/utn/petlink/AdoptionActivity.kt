package cr.ac.utn.petlink

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import cr.ac.utn.petlink.databinding.ActivityAdoptionBinding
import cr.ac.utn.petlink.entity.AppData

class AdoptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdoptionBinding
    private lateinit var adapter: AdoptionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdoptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.bottomNavigation.selectedItemId = R.id.nav_adoptions

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.nav_adoptions -> {
                    // Already on this screen, do nothing
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
        adapter = AdoptionAdapter(AppData.pets)
        binding.adoptionsRecyclerView.adapter = adapter
        binding.adoptionsRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.adoption_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_adoption -> {
                startActivity(Intent(this, AddAdoptionActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
