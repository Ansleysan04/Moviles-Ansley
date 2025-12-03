package cr.ac.utn.petlink

import android.graphics.Color
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import cr.ac.utn.petlink.entity.Pet

class AdoptionAdapter(
    private val pets: MutableList<Pet>,
    private val clickListener: (Pet) -> Unit,
    private val longClickListener: (Pet) -> Boolean,
    private val adoptClickListener: (Pet) -> Unit
) : RecyclerView.Adapter<AdoptionAdapter.PetViewHolder>() {

    private val selectedItems = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_adoption_pet, parent, false)
        return PetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val pet = pets[position]
        holder.bind(pet, selectedItems.get(position, false))
        holder.itemView.setOnClickListener { 
            clickListener(pet)
        }
        holder.itemView.setOnLongClickListener { 
            longClickListener(pet)
        }
        holder.adoptButton.setOnClickListener { 
            adoptClickListener(pet)
        }
    }

    override fun getItemCount() = pets.size

    fun toggleSelection(position: Int) {
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position)
        } else {
            selectedItems.put(position, true)
        }
        notifyItemChanged(position)
    }

    fun clearSelections() {
        selectedItems.clear()
        notifyDataSetChanged()
    }

    fun getSelectedItemCount(): Int {
        return selectedItems.size()
    }

    fun getSelectedItems(): List<Pet> {
        val items = mutableListOf<Pet>()
        for (i in 0 until selectedItems.size()) {
            items.add(pets[selectedItems.keyAt(i)])
        }
        return items
    }

    class PetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val petImage: ImageView = itemView.findViewById(R.id.pet_image)
        private val petName: TextView = itemView.findViewById(R.id.pet_name)
        private val petSpecies: TextView = itemView.findViewById(R.id.pet_species)
        private val petAge: TextView = itemView.findViewById(R.id.pet_age)
        private val petBreed: TextView = itemView.findViewById(R.id.pet_breed)
        private val petLocation: TextView = itemView.findViewById(R.id.pet_location)
        private val petVaccinations: TextView = itemView.findViewById(R.id.pet_vaccinations)
        private val petDescription: TextView = itemView.findViewById(R.id.pet_description)
        val adoptButton: Button = itemView.findViewById(R.id.adopt_button)

        fun bind(pet: Pet, isSelected: Boolean) {
            petName.text = pet.name
            petSpecies.text = pet.species
            petAge.text = "${pet.age} años"
            petBreed.text = pet.breed
            petLocation.text = pet.location
            petVaccinations.text = if (pet.vaccinationRecords.isNotEmpty()) "Sí" else "No"
            petDescription.text = pet.description
            
            pet.photoUrl?.let {
                Glide.with(itemView.context)
                    .load(it)
                    .into(petImage)
            }

            itemView.setBackgroundColor(if (isSelected) Color.LTGRAY else Color.WHITE)
        }
    }
}
