package cr.ac.utn.petlink

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import cr.ac.utn.petlink.databinding.ActivityLostPetDetailBinding
import cr.ac.utn.petlink.entity.AppData
import java.text.SimpleDateFormat
import java.util.Locale

class LostPetDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLostPetDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLostPetDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val petId = intent.getLongExtra("pet_id", -1L)
        if (petId != -1L) {
            val pet = AppData.lostPets.find { it.id == petId }
            pet?.let {
                binding.toolbar.title = it.name
                binding.petDetailName.text = it.name
                binding.petDetailDescription.text = it.description
                binding.petDetailSpecies.text = "Especie: ${it.species}"
                binding.petDetailBreed.text = "Raza: ${it.breed}"
                binding.petDetailLocation.text = it.lastSeenLocation
                val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                binding.petDetailLostDate.text = "Visto por última vez: ${format.format(it.lostDate)}"
                binding.petDetailContact.text = "Contacto: ${it.contactPhone}"

                if (!it.photoUrl.isNullOrEmpty()) {
                    Glide.with(this)
                        .load(Uri.parse(it.photoUrl))
                        .into(binding.petDetailImage)
                } else {
                    binding.petDetailImage.setImageResource(R.drawable.ic_launcher_background)
                }
            }
        }
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
