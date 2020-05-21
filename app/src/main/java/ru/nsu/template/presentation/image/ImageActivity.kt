package ru.nsu.template.presentation.image

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_image.*
import ru.nsu.template.R
import ru.nsu.template.data.model.picsum.Image


const val IMAGE_KEY = "full_image"

class ImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        val image = intent.getSerializableExtra(IMAGE_KEY) as Image

        Glide.with(this)
                .load(Uri.parse(image.downloadUrl))
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>, isFirstResource: Boolean): Boolean {
                        progress.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: com.bumptech.glide.load.DataSource?, isFirstResource: Boolean): Boolean {
                        progress.visibility = View.GONE
                        full_author.text = image.author
                        return false
                    }
                })
                .into(full_picture)
    }
}
