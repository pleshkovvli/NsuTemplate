package ru.nsu.template.presentation.imagelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.nsu.template.data.model.picsum.ImageList
import ru.nsu.template.presentation.imagelist.list.ImageSize
import ru.nsu.template.presentation.start.picsum.PreviewSize

class ImageListViewModelFactory(
        private val imageSize: ImageSize,
        private val imageList: ImageList
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ImageListViewModel(imageSize, imageList) as T
    }
}