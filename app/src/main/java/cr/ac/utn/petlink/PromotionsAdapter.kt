package cr.ac.utn.petlink

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.petlink.entity.Promotion

class PromotionsAdapter(private val promotions: List<Promotion>) : RecyclerView.Adapter<PromotionsAdapter.PromotionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromotionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_promotion, parent, false)
        return PromotionViewHolder(view)
    }

    override fun onBindViewHolder(holder: PromotionViewHolder, position: Int) {
        val promotion = promotions[position]
        holder.bind(promotion)
    }

    override fun getItemCount() = promotions.size

    class PromotionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.promotion_title)
        private val description: TextView = itemView.findViewById(R.id.promotion_description)

        fun bind(promotion: Promotion) {
            title.text = promotion.title
            description.text = promotion.description
            // Here you would load the image with a library like Glide or Picasso
        }
    }
}
