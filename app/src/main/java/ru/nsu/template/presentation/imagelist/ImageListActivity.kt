package ru.nsu.template.presentation.imagelist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_image_list.*
import ru.nsu.template.R
import ru.nsu.template.data.model.picsum.Image
import ru.nsu.template.data.model.picsum.ImageList
import ru.nsu.template.presentation.image.IMAGE_KEY
import ru.nsu.template.presentation.image.ImageActivity
import ru.nsu.template.presentation.imagelist.list.ImageListAdapter
import ru.nsu.template.presentation.imagelist.list.ImageSize

const val IMAGE_LIST_KEY = "image_list_key"
const val SIZES_KEY = "sizes_key"

class ImageListActivity : AppCompatActivity() {
    private lateinit var imageListAdapter: ImageListAdapter
    private lateinit var viewModel: ImageListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = intent.extras

        setContentView(R.layout.activity_image_list)

        imageListAdapter = ImageListAdapter { image -> openImage(image) }

        imageList.layoutManager = LinearLayoutManager(this)
        imageList.adapter = imageListAdapter

        val list: ImageList
        val sizes: ImageSize
        if (args != null) {
            list = args.getSerializable(IMAGE_LIST_KEY) as ImageList
            sizes = args.getSerializable(SIZES_KEY) as ImageSize
        } else {
            list = ImageList(listOf(), 0, 10)
            sizes = ImageSize(100, 200)
        }

        viewModel = ViewModelProviders.of(
                this, ImageListViewModelFactory(sizes, list)
        ).get(ImageListViewModel::class.java)

        viewModel.observeImageSizeLiveData().observe(this, Observer { newSize ->
            imageListAdapter.imageSize = newSize
        })

        viewModel.observeImageListLiveData().observe(this, Observer { images ->
            imageListAdapter.setItems(images)
        })

        viewModel.observeErrorLiveData().observe(this, Observer { error ->
            Toast.makeText(this, "Error. $error", Toast.LENGTH_LONG).show()
        })

        imageList.addOnScrollListener( object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val lastVisibleItemPosition = (imageList.layoutManager as LinearLayoutManager)
                        .findLastVisibleItemPosition()

                if(lastVisibleItemPosition > (imageListAdapter.itemCount - 10)) {
                    viewModel.loadMoreItems()
                }
            }
        })
    }

    private fun openImage(image: Image) {
        val intent = Intent(this, ImageActivity::class.java)
        intent.putExtra(IMAGE_KEY, image)

        startActivity(intent)
    }
}
