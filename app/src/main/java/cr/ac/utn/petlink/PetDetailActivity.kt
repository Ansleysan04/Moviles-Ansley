package cr.ac.utn.petlink

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import cr.ac.utn.petlink.databinding.ActivityPetDetailBinding
import cr.ac.utn.petlink.entity.AppData

class PetDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPetDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val petId = intent.getLongExtra("pet_id", -1L)
        if (petId != -1L) {
            val pet = AppData.pets.find { it.id == petId }
            pet?.let {
                binding.toolbar.title = it.name
                binding.petDetailName.text = it.name
                binding.petDetailAge.text = "${it.age} años"
                binding.petDetailBreed.text = it.breed
                binding.petDetailDescription.text = it.description
                binding.petDetailPersonality.text = "Personalidad: ${it.personality}"
                binding.petDetailBehavior.text = "Comportamiento: ${it.behavior}"
                binding.petDetailHealth.text = "Salud: ${it.health}"
                binding.petDetailNeeds.text = "Necesidades: ${it.needs}"

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
