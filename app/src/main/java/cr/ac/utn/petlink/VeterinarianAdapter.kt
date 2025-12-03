package cr.ac.utn.petlink

import android.graphics.Color
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.petlink.entity.Veterinarian

class VeterinarianAdapter(
    private val veterinarians: MutableList<Veterinarian>,
    private val clickListener: (Veterinarian) -> Unit,
    private val longClickListener: (Veterinarian) -> Boolean
) : RecyclerView.Adapter<VeterinarianAdapter.VeterinarianViewHolder>() {

    private val selectedItems = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VeterinarianViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_veterinarian, parent, false)
        return VeterinarianViewHolder(view)
    }

    override fun onBindViewHolder(holder: VeterinarianViewHolder, position: Int) {
        val veterinarian = veterinarians[position]
        holder.bind(veterinarian, selectedItems.get(position, false))
        holder.itemView.setOnClickListener { 
            clickListener(veterinarian)
        }
        holder.itemView.setOnLongClickListener { 
            longClickListener(veterinarian)
        }
    }

    override fun getItemCount() = veterinarians.size

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

    fun getSelectedItems(): List<Veterinarian> {
        val items = mutableListOf<Veterinarian>()
        for (i in 0 until selectedItems.size()) {
            items.add(veterinarians[selectedItems.keyAt(i)])
        }
        return items
    }

    class VeterinarianViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val vetName: TextView = itemView.findViewById(R.id.vet_name)
        private val vetAddress: TextView = itemView.findViewById(R.id.vet_address)
        private val vetPhone: TextView = itemView.findViewById(R.id.vet_phone)
        private val vetWebsite: TextView = itemView.findViewById(R.id.vet_website)

        fun bind(veterinarian: Veterinarian, isSelected: Boolean) {
            vetName.text = veterinarian.name
            vetAddress.text = veterinarian.address
            vetPhone.text = veterinarian.phone
            vetWebsite.text = veterinarian.website
            itemView.setBackgroundColor(if (isSelected) Color.LTGRAY else Color.WHITE)
        }
    }
}
