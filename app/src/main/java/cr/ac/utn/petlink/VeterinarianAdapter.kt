package cr.ac.utn.petlink

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.petlink.entity.Veterinarian

class VeterinarianAdapter(private val veterinarians: List<Veterinarian>) : RecyclerView.Adapter<VeterinarianAdapter.VeterinarianViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VeterinarianViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_veterinarian, parent, false)
        return VeterinarianViewHolder(view)
    }

    override fun onBindViewHolder(holder: VeterinarianViewHolder, position: Int) {
        val veterinarian = veterinarians[position]
        holder.bind(veterinarian)
    }

    override fun getItemCount() = veterinarians.size

    class VeterinarianViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val vetName: TextView = itemView.findViewById(R.id.vet_name)
        private val vetAddress: TextView = itemView.findViewById(R.id.vet_address)

        fun bind(veterinarian: Veterinarian) {
            vetName.text = veterinarian.name
            vetAddress.text = veterinarian.address
        }
    }
}
