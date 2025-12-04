package cr.ac.utn.petlink

import android.graphics.Color
import android.net.Uri
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import cr.ac.utn.petlink.entity.Pet

class MyPetsAdapter(
    private val pets: MutableList<Pet>,
    private val clickListener: (Pet) -> Unit,
    private val longClickListener: (Pet) -> Boolean
) : RecyclerView.Adapter<MyPetsAdapter.MyPetViewHolder>() {

    private val selectedItems = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_my_pet, parent, false)
        return MyPetViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyPetViewHolder, position: Int) {
        val pet = pets[position]
        holder.bind(pet, selectedItems.get(position, false))
        holder.itemView.setOnClickListener { 
            clickListener(pet)
        }
        holder.itemView.setOnLongClickListener { 
            longClickListener(pet)
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

    class MyPetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val petImage: ImageView = itemView.findViewById(R.id.pet_image)
        private val petName: TextView = itemView.findViewById(R.id.pet_name)
        private val petBreed: TextView = itemView.findViewById(R.id.pet_breed)
        private val petAge: TextView = itemView.findViewById(R.id.pet_age)

        fun bind(pet: Pet, isSelected: Boolean) {
            petName.text = pet.name
            petBreed.text = "Raza: ${pet.breed}"
            petAge.text = "Edad: ${pet.age} años"
            itemView.setBackgroundColor(if (isSelected) Color.LTGRAY else Color.WHITE)

            pet.photoUrl?.let {
                if (it.isNotEmpty()) {
                    Glide.with(itemView.context)
                        .load(Uri.parse(it))
                        .into(petImage)
                } else {
                    petImage.setImageResource(R.mipmap.ic_launcher) // Placeholder
                }
            }
        }
    }
}
