package cr.ac.utn.petlink

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.petlink.entity.Pet

class MyPetsAdapter(private val pets: List<Pet>) : RecyclerView.Adapter<MyPetsAdapter.MyPetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_my_pet, parent, false)
        return MyPetViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyPetViewHolder, position: Int) {
        val pet = pets[position]
        holder.bind(pet)
    }

    override fun getItemCount() = pets.size

    class MyPetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val petName: TextView = itemView.findViewById(R.id.pet_name)
        private val petBreed: TextView = itemView.findViewById(R.id.pet_breed)
        private val petAge: TextView = itemView.findViewById(R.id.pet_age)

        fun bind(pet: Pet) {
            petName.text = pet.name
            petBreed.text = "Raza: ${pet.breed}"
            petAge.text = "Edad: ${pet.age} años"
        }
    }
}
