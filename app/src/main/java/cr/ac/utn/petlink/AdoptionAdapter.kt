package cr.ac.utn.petlink

import android.graphics.Color
import android.net.Uri
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import cr.ac.utn.petlink.R
import cr.ac.utn.petlink.entity.Pet

class AdoptionAdapter(
    private val pets: MutableList<Pet>,
    private val clickListener: (Pet) -> Unit,
    private val adoptClickListener: (Pet) -> Unit,
    private val detailsClickListener: (Pet) -> Unit
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
        holder.adoptButton.setOnClickListener { 
            adoptClickListener(pet)
        }
        holder.detailsButton.setOnClickListener {
            detailsClickListener(pet)
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
        private val petAge: TextView = itemView.findViewById(R.id.pet_age)
        private val petBreed: TextView = itemView.findViewById(R.id.pet_breed)
        private val petLocation: TextView = itemView.findViewById(R.id.pet_location)
        val adoptButton: Button = itemView.findViewById(R.id.adopt_button)
        val detailsButton: Button = itemView.findViewById(R.id.details_button)

        fun bind(pet: Pet, isSelected: Boolean) {
            petName.text = pet.name
            petAge.text = "${pet.age} años"
            petBreed.text = pet.breed
            petLocation.text = pet.location
            
            if (!pet.photoUrl.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(Uri.parse(pet.photoUrl))
                    .into(petImage)
            } else {
                petImage.setImageResource(R.drawable.ic_launcher_background) // Placeholder image
            }

            itemView.setBackgroundColor(if (isSelected) Color.LTGRAY else Color.WHITE)
        }
    }
}
