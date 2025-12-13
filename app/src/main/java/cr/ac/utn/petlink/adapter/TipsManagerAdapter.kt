package cr.ac.utn.petlink.adapter

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
import cr.ac.utn.petlink.R
import cr.ac.utn.petlink.entity.Tip

class TipsManagerAdapter(
    private val tips: MutableList<Tip>,
    private val clickListener: (Tip) -> Unit,
    private val longClickListener: (Tip) -> Boolean
) : RecyclerView.Adapter<TipsManagerAdapter.TipViewHolder>() {

    private val selectedItems = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tip_manager, parent, false)
        return TipViewHolder(view)
    }

    override fun onBindViewHolder(holder: TipViewHolder, position: Int) {
        val tip = tips[position]
        holder.bind(tip, selectedItems.get(position, false))
        holder.itemView.setOnClickListener { 
            clickListener(tip)
        }
        holder.itemView.setOnLongClickListener { 
            longClickListener(tip)
        }
    }

    override fun getItemCount() = tips.size

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

    fun getSelectedItems(): List<Tip> {
        val items = mutableListOf<Tip>()
        for (i in 0 until selectedItems.size()) {
            items.add(tips[selectedItems.keyAt(i)])
        }
        return items
    }

    class TipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tipImage: ImageView = itemView.findViewById(R.id.tip_image)
        private val tipTitle: TextView = itemView.findViewById(R.id.tip_title)
        private val tipDescription: TextView = itemView.findViewById(R.id.tip_description)

        fun bind(tip: Tip, isSelected: Boolean) {
            tipTitle.text = tip.title
            tipDescription.text = tip.description
            itemView.setBackgroundColor(if (isSelected) Color.LTGRAY else Color.WHITE)

            tip.photoUrl?.let {
                if (it.isNotEmpty()) {
                    Glide.with(itemView.context)
                        .load(Uri.parse(it))
                        .into(tipImage)
                } else {
                    tipImage.setImageResource(R.mipmap.ic_launcher) // Placeholder
                }
            }
        }
    }
}
