package ru.nsu.template.presentation.imagelist.list

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.nsu.template.R
import ru.nsu.template.data.model.picsum.Image
import ru.nsu.template.data.network.picsum.PicsumApiClient

class ImageListAdapter(
        showImageListener: ((Image) -> Unit)?
) : RecyclerView.Adapter<ImageListAdapter.ViewHolder>() {
    private var items: List<Image> = listOf()
    private var onImageClickListener: ((Image) -> Unit)? = showImageListener

    var imageSize: ImageSize = ImageSize(100, 100)

    fun setItems(items: List<Image>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false),
                imageSize
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = items[position]
        holder.author.text = image.author
        val imageUrl = PicsumApiClient.getImageUrl(image.id.toInt(), image.width, image.height)


        holder.picture.layoutParams.width = imageSize.width ?: 100
        holder.picture.layoutParams.height = imageSize.height ?: 100


        Glide.with(holder.itemView.context)
                .load(Uri.parse(imageUrl))
                .into(holder.picture)

        holder.itemView.setOnClickListener { onImageClickListener?.invoke(image) }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView: View, imageSize: ImageSize) : RecyclerView.ViewHolder(itemView) {
        var picture: ImageView
        var author: TextView

        init {
            picture = itemView.findViewById(R.id.picture)
            author = itemView.findViewById(R.id.author)
        }
    }
}