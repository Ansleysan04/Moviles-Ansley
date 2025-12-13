package cr.ac.utn.petlink

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import cr.ac.utn.petlink.adapter.TipsAdapter
import cr.ac.utn.petlink.databinding.ActivityMainBinding
import cr.ac.utn.petlink.entity.AppData

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadUserData()
        setupTipsCarousel()
        setupQuickAccessButtons()
        setupBottomNavigation()
    }

    override fun onResume() {
        super.onResume()
        // Refresh the tips carousel to show any new or edited tips
        binding.promotionsViewPager.adapter?.notifyDataSetChanged()
        updateTipsVisibility()
    }

    private fun loadUserData() {
        AppData.currentUser?.let {
            binding.userNameText.text = "${it.firstName} ${it.lastName}"
            it.photoUrl?.let {
                if (it.isNotEmpty()) {
                    Glide.with(this)
                        .load(Uri.parse(it))
                        .into(binding.userProfileImage)
                } else {
                    binding.userProfileImage.setImageResource(R.mipmap.ic_launcher)
                }
            }
        }
    }

    private fun setupTipsCarousel() {
        binding.promotionsViewPager.adapter = TipsAdapter(AppData.tips)
        updateTipsVisibility()
    }

    private fun updateTipsVisibility() {
        if (AppData.tips.isEmpty()) {
            binding.promotionsViewPager.visibility = View.GONE
        } else {
            binding.promotionsViewPager.visibility = View.VISIBLE
        }
    }

    private fun setupQuickAccessButtons() {
        binding.adoptionsCard.setOnClickListener {
            startActivity(Intent(this, AdoptionActivity::class.java))
        }
        binding.veterinariansCard.setOnClickListener {
            startActivity(Intent(this, VeterinariansActivity::class.java))
        }
        binding.lostPetsCard.setOnClickListener {
            startActivity(Intent(this, LostPetsActivity::class.java))
        }
        binding.profileCard.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.selectedItemId = R.id.nav_home
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
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
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
