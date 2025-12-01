package cr.ac.utn.petlink

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.petlink.entity.LostPet
import java.text.SimpleDateFormat
import java.util.Locale

class LostPetAdapter(private val lostPets: List<LostPet>) : RecyclerView.Adapter<LostPetAdapter.LostPetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LostPetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lost_pet, parent, false)
        return LostPetViewHolder(view)
    }

    override fun onBindViewHolder(holder: LostPetViewHolder, position: Int) {
        val lostPet = lostPets[position]
        holder.bind(lostPet)
    }

    override fun getItemCount() = lostPets.size

    class LostPetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val petName: TextView = itemView.findViewById(R.id.pet_name)
        private val petBreed: TextView = itemView.findViewById(R.id.pet_breed)
        private val lastSeen: TextView = itemView.findViewById(R.id.last_seen)

        fun bind(lostPet: LostPet) {
            petName.text = lostPet.name
            petBreed.text = lostPet.breed
            lastSeen.text = "Visto por última vez en: ${lostPet.lastSeenLocation}"
        }
    }
}
