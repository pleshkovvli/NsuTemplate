package ru.nsu.template.presentation.imagelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import ru.nsu.template.data.model.picsum.Image
import ru.nsu.template.data.model.picsum.ImageList
import ru.nsu.template.data.network.picsum.PicsumApi
import ru.nsu.template.data.network.picsum.PicsumApiClient
import ru.nsu.template.presentation.imagelist.list.ImageSize
import java.util.*

class ImageListViewModel(
        imageSize: ImageSize,
        imageList: ImageList
) : ViewModel() {
    private val api: PicsumApi = PicsumApiClient.getClient().create(PicsumApi::class.java)
    private var loadedPages: MutableList<Int> = Collections.synchronizedList(
            (0..(imageList.pages)).toMutableList()
    )
    private var loadingPages: MutableList<Int> = Collections.synchronizedList(mutableListOf())
    private val pageSize = imageList.pageSize

    private val imageListLiveData = MutableLiveData(imageList.items)
    private val imageSizeLiveData = MutableLiveData(imageSize)
    private val errorLiveData = MutableLiveData<String>()

    fun observeErrorLiveData(): LiveData<String> = errorLiveData
    fun observeImageListLiveData(): LiveData<List<Image>> = imageListLiveData
    fun observeImageSizeLiveData(): LiveData<ImageSize> = imageSizeLiveData

    private val lock = Object()
    fun loadMoreItems() {
        val newPage: Int
        synchronized(lock) {
            newPage = (0..(loadedPages.size + loadingPages.size)).first { it !in loadedPages && it !in loadingPages }
            loadingPages.add(newPage)
        }
        api.getImageList(newPage, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableSingleObserver<List<Image>>() {
                    override fun onSuccess(list: List<Image>) {
                        synchronized(lock) {
                            imageListLiveData.value = (imageListLiveData.value ?: listOf()) + list
                            loadedPages.add(newPage)
                            loadingPages.remove(newPage)
                        }
                    }

                    override fun onError(e: Throwable) {
                        errorLiveData.value = e.message
                        synchronized(lock) {
                            loadingPages.remove(newPage)
                        }
                    }
                })
    }
}
