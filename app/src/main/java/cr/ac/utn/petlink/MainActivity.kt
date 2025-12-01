package cr.ac.utn.petlink

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.petlink.databinding.ActivityMainBinding
import cr.ac.utn.petlink.entity.AppData
import cr.ac.utn.petlink.entity.Promotion

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadUserData()
        setupPromotionsCarousel()
        setupQuickAccessButtons()
        setupBottomNavigation()
    }

    private fun loadUserData() {
        AppData.currentUser?.let {
            binding.userNameText.text = "${it.firstName} ${it.lastName}"
            // Here you would load the user's profile image into binding.userProfileImage
        }
    }

    private fun setupPromotionsCarousel() {
        val promotions = listOf(
            Promotion("Consejos de Salud Preventiva", "Mantén a tu mascota feliz y sana", ""),
            Promotion("Descuentos en Alimentos", "Aprovecha nuestras ofertas de temporada", ""),
            Promotion("Jornada de Vacunación", "Protege a tu mascota de enfermedades", "")
        )
        binding.promotionsViewPager.adapter = PromotionsAdapter(promotions)
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
