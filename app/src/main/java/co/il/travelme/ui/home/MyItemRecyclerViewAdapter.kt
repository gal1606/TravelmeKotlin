package co.il.travelme.ui.home
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.il.travelme.R
import co.il.travelme.databinding.ItemCardBinding
import co.il.travelme.models.Trip
import com.bumptech.glide.Glide
import kotlinx.coroutines.withContext

class MyItemRecyclerViewAdapter : ListAdapter<Trip, MyItemRecyclerViewAdapter.ViewHolder>(TripDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("StringFormatMatches")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.descriptionText.text = item.description
        holder.level_text.text = holder.itemView.context.getString(R.string.level_and_length, item.level, item.length)
        holder.time_text.text = holder.itemView.context.getString(R.string.time, item.time)

        Glide.with(holder.itemView.context) // זה ישתמש ב-context של ה-view של ה-ViewHolder
            .load(item.imageUrl) // ודא שאתה מעביר את URL של התמונה מהאובייקט `item`
            .placeholder(R.drawable.imageexample) // תמונת טעינה זמנית
            .error(R.drawable.imageexample) // תמונה במקרה של שגיאת טעינה
            .centerCrop() // חיתוך התמונה כדי להתאים ל-ImageView
            .into(holder.image_view)

    }

    class ViewHolder(binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root) {
        val descriptionText: TextView = binding.descriptionText
        val image_view: ImageView = binding.imageView
        val level_text: TextView = binding.levelText
        val time_text: TextView = binding.timeText
        val SignVButton: ImageButton = binding.SignVButton
    }
//    override fun getItemCount(): Int = values.size
}

class TripDiffCallback : DiffUtil.ItemCallback<Trip>() {
    override fun areItemsTheSame(oldItem: Trip, newItem: Trip): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Trip, newItem: Trip): Boolean {
        return oldItem == newItem
    }
}