package cr.ac.utn.petlink

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.petlink.entity.Pet

class AdoptionAdapter(private val pets: List<Pet>) : RecyclerView.Adapter<AdoptionAdapter.PetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_adoption_pet, parent, false)
        return PetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val pet = pets[position]
        holder.bind(pet)
    }

    override fun getItemCount() = pets.size

    class PetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val petName: TextView = itemView.findViewById(R.id.pet_name)
        private val petAge: TextView = itemView.findViewById(R.id.pet_age)
        private val petBreed: TextView = itemView.findViewById(R.id.pet_breed)

        fun bind(pet: Pet) {
            petName.text = pet.name
            petAge.text = "${pet.age} años"
            petBreed.text = pet.breed
        }
    }
}
