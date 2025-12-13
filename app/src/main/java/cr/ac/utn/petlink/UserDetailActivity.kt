package cr.ac.utn.petlink

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import cr.ac.utn.petlink.databinding.ActivityUserDetailBinding
import cr.ac.utn.petlink.entity.AppData
import java.util.Calendar
import java.util.Date

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loadUserData()

        binding.fabEditUser.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("user_id", AppData.currentUser?.id)
            startActivity(intent)
        }
    }

    private fun loadUserData() {
        AppData.currentUser?.let {
            binding.userDetailName.text = "${it.firstName} ${it.lastName}"
            binding.userDetailEmail.text = it.email
            binding.userDetailAddress.text = it.address
            binding.userDetailPhone.text = it.phone

            it.birthDate?.let {
                val age = calculateAge(it)
                binding.userDetailAge.text = "$age años"
            }

            if (!it.photoUrl.isNullOrEmpty()) {
                Glide.with(this)
                    .load(Uri.parse(it.photoUrl))
                    .into(binding.userDetailImage)
            } else {
                binding.userDetailImage.setImageResource(R.mipmap.ic_launcher)
            }
        }
    }

    private fun calculateAge(birthDate: Date): Int {
        val today = Calendar.getInstance()
        val birth = Calendar.getInstance()
        birth.time = birthDate
        var age = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR)
        if (today.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
            age--
        }
        return age
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
