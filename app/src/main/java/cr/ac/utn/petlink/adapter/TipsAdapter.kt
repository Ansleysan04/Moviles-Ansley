package cr.ac.utn.petlink.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import cr.ac.utn.petlink.R
import cr.ac.utn.petlink.entity.Tip

class TipsAdapter(private val tips: List<Tip>) : RecyclerView.Adapter<TipsAdapter.TipViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tip, parent, false)
        return TipViewHolder(view)
    }

    override fun onBindViewHolder(holder: TipViewHolder, position: Int) {
        holder.bind(tips[position])
    }

    override fun getItemCount() = tips.size

    class TipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tipImage: ImageView = itemView.findViewById(R.id.tip_image)
        private val tipTitle: TextView = itemView.findViewById(R.id.tip_title)
        private val tipDescription: TextView = itemView.findViewById(R.id.tip_description)

        fun bind(tip: Tip) {
            tipTitle.text = tip.title
            tipDescription.text = tip.description

            if (!tip.photoUrl.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(Uri.parse(tip.photoUrl))
                    .into(tipImage)
            } else {
                tipImage.setImageResource(R.drawable.ic_launcher_background) // Placeholder image
            }
        }
    }
}
