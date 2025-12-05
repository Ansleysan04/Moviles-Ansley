package cr.ac.utn.petlink

import android.graphics.Color
import android.net.Uri
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import cr.ac.utn.petlink.R
import cr.ac.utn.petlink.entity.Veterinarian

class VeterinarianAdapter(
    private val veterinarians: MutableList<Veterinarian>,
    private val clickListener: (Veterinarian) -> Unit,
    private val longClickListener: (Veterinarian) -> Boolean,
    private val detailsClickListener: (Veterinarian) -> Unit
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
        holder.detailsButton.setOnClickListener {
            detailsClickListener(veterinarian)
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
        val detailsButton: Button = itemView.findViewById(R.id.details_button)
        private val vetImage: ImageView = itemView.findViewById(R.id.vet_image)
        private val vetName: TextView = itemView.findViewById(R.id.vet_name)
        private val vetAddress: TextView = itemView.findViewById(R.id.vet_address)
        private val vetRating: RatingBar = itemView.findViewById(R.id.vet_rating)
        private val vetDistance: TextView = itemView.findViewById(R.id.vet_distance)

        fun bind(veterinarian: Veterinarian, isSelected: Boolean) {
            vetName.text = veterinarian.name
            vetAddress.text = veterinarian.address
            vetRating.rating = veterinarian.rating
            vetDistance.text = veterinarian.distance.toString()
            itemView.setBackgroundColor(if (isSelected) Color.LTGRAY else Color.WHITE)

            if (!veterinarian.imageUrl.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(Uri.parse(veterinarian.imageUrl))
                    .into(vetImage)
            } else {
                vetImage.setImageResource(R.drawable.ic_launcher_background) // Placeholder image
            }
        }
    }
}
