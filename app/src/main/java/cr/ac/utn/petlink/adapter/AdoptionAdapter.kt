package cr.ac.utn.petlink.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.petlink.R
import cr.ac.utn.petlink.entity.Pet

class AdoptionAdapter(private var pets: List<Pet>) : RecyclerView.Adapter<AdoptionAdapter.PetViewHolder>() {

    // This class holds the views for each item in the list
    class PetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val petImage: ImageView = itemView.findViewById(R.id.pet_image)
        val petName: TextView = itemView.findViewById(R.id.pet_name)
        val petAge: TextView = itemView.findViewById(R.id.pet_age)
        val petBreed: TextView = itemView.findViewById(R.id.pet_breed)
        val petLocation: TextView = itemView.findViewById(R.id.pet_location) // Assuming location is part of Pet
        val adoptButton: Button = itemView.findViewById(R.id.adopt_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_adoption_pet, parent, false)
        return PetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val pet = pets[position]
        holder.petName.text = pet.name
        holder.petAge.text = "${pet.age} years"
        holder.petBreed.text = pet.breed
        // holder.petLocation.text = pet.location // We will add location to the pet entity later

        // TODO: Add logic for image loading (e.g., using Glide or Picasso)
        // holder.petImage.setImageResource(R.drawable.placeholder)

        holder.adoptButton.setOnClickListener {
            // TODO: Implement adoption logic
        }
    }

    override fun getItemCount(): Int = pets.size

    // Function to update the list of pets in the adapter
    fun updatePets(newPets: List<Pet>) {
        pets = newPets
        notifyDataSetChanged()
    }
}
