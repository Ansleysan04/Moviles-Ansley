package cr.ac.utn.petlink

import android.graphics.Color
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import cr.ac.utn.petlink.entity.LostPet
import java.text.SimpleDateFormat
import java.util.Locale

class LostPetAdapter(
    private val lostPets: MutableList<LostPet>,
    private val clickListener: (LostPet) -> Unit,
    private val longClickListener: (LostPet) -> Boolean
) : RecyclerView.Adapter<LostPetAdapter.LostPetViewHolder>() {

    private val selectedItems = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LostPetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lost_pet, parent, false)
        return LostPetViewHolder(view)
    }

    override fun onBindViewHolder(holder: LostPetViewHolder, position: Int) {
        val lostPet = lostPets[position]
        holder.bind(lostPet, selectedItems.get(position, false))
        holder.itemView.setOnClickListener { 
            clickListener(lostPet)
        }
        holder.itemView.setOnLongClickListener { 
            longClickListener(lostPet)
        }
    }

    override fun getItemCount() = lostPets.size

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

    fun getSelectedItems(): List<LostPet> {
        val items = mutableListOf<LostPet>()
        for (i in 0 until selectedItems.size()) {
            items.add(lostPets[selectedItems.keyAt(i)])
        }
        return items
    }

    class LostPetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val petImage: ImageView = itemView.findViewById(R.id.pet_image)
        private val petName: TextView = itemView.findViewById(R.id.pet_name)
        private val petSpecies: TextView = itemView.findViewById(R.id.pet_species)
        private val petBreed: TextView = itemView.findViewById(R.id.pet_breed)
        private val lastSeenLocation: TextView = itemView.findViewById(R.id.last_seen_location)
        private val lostDate: TextView = itemView.findViewById(R.id.lost_date)
        private val contactPhone: TextView = itemView.findViewById(R.id.contact_phone)
        private val petDescription: TextView = itemView.findViewById(R.id.pet_description)

        fun bind(lostPet: LostPet, isSelected: Boolean) {
            petName.text = lostPet.name
            petSpecies.text = lostPet.species
            petBreed.text = lostPet.breed
            lastSeenLocation.text = lostPet.lastSeenLocation
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            lostDate.text = format.format(lostPet.lostDate)
            contactPhone.text = lostPet.contactPhone
            petDescription.text = lostPet.description

            lostPet.photoUrl?.let {
                Glide.with(itemView.context)
                    .load(it)
                    .into(petImage)
            }

            itemView.setBackgroundColor(if (isSelected) Color.LTGRAY else Color.WHITE)
        }
    }
}
