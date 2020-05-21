package ru.nsu.template.presentation.start.picsum

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_start_picsum.*
import ru.nsu.template.R
import ru.nsu.template.data.model.picsum.ImageList
import ru.nsu.template.presentation.imagelist.*
import ru.nsu.template.presentation.imagelist.list.ImageSize

class StartPicsumActivity : AppCompatActivity() {
    private val viewModel: StartPicsumViewModel by viewModels()
    private var loadList: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_picsum)

        viewModel.observeImageListLiveData().observe(this, Observer { imageList ->
            if(loadList) {
                openImageList(imageList)
            }
        })

        viewModel.observeErrorLiveData().observe(this, Observer { error ->
            Toast.makeText(this, "Error. $error", Toast.LENGTH_LONG).show()
        })

        viewModel.observeSizesLiveData()?.observe(this, Observer { sizes ->
            if(sizes.setWidth or width.text.isNullOrEmpty()) {
                width.text = sizes.width.toString()
            }
            if(sizes.setHeight or height.text.isNullOrEmpty()) {
                height.text = sizes.height.toString()
            }
        })

        increaseWidth.setOnClickListener { viewModel.increaseWidth() }
        decreaseWidth.setOnClickListener { viewModel.decreaseWidth() }
        increaseHeight.setOnClickListener { viewModel.increaseHeight() }
        decreaseHeight.setOnClickListener { viewModel.decreaseHeight() }

        showItems.setOnClickListener {
            loadList = true
            viewModel.showItems(3, 10)
        }
    }

    private fun openImageList(imageList: ImageList) {
        val bundle = Bundle()
        bundle.putSerializable(IMAGE_LIST_KEY, imageList)

        val width = viewModel.observeSizesLiveData()?.value?.width
        val height = viewModel.observeSizesLiveData()?.value?.height
        bundle.putSerializable(SIZES_KEY, ImageSize(width ?: 1, height ?: 1))

        val intent = Intent(this@StartPicsumActivity, ImageListActivity::class.java)
        intent.putExtras(bundle)

        startActivity(intent)
    }
}
