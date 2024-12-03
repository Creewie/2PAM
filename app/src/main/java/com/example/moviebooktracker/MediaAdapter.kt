package com.example.moviebooktracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MediaAdapter(
    private val items: List<MediaItem>,
    private val onItemClicked: (MediaItem) -> Unit
) : RecyclerView.Adapter<MediaAdapter.MediaViewHolder>() {

    inner class MediaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.textMediaTitle)
        val genre: TextView = view.findViewById(R.id.textMediaGenre)
        val icon: ImageView = view.findViewById(R.id.imageMediaIcon)
        val completedCheckBox: CheckBox = view.findViewById(R.id.checkboxCompleted)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_media, parent, false)
        return MediaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title
        holder.genre.text = item.genre
        holder.completedCheckBox.isChecked = item.isCompleted

        val iconRes = if (item.type == "Book") R.drawable.ic_book else R.drawable.ic_movie
        holder.icon.setImageResource(iconRes)

        holder.itemView.setOnClickListener { onItemClicked(item) }
        holder.completedCheckBox.setOnCheckedChangeListener { _, isChecked ->
            item.isCompleted = isChecked
        }
    }

    override fun getItemCount(): Int = items.size
}
