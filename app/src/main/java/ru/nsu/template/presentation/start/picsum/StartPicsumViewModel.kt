package ru.nsu.template.presentation.start.picsum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import ru.nsu.template.data.model.picsum.Image
import ru.nsu.template.data.model.picsum.ImageList
import ru.nsu.template.data.network.picsum.PicsumApi
import ru.nsu.template.data.network.picsum.PicsumApiClient
import ru.nsu.template.presentation.imagelist.list.ImageSize
import kotlin.math.max

private const val SIZES_STEP = 30

private const val IMAGE_SIZE_KEY = "Image_size"

class StartPicsumViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val api: PicsumApi = PicsumApiClient.getClient().create(PicsumApi::class.java)
    private val sizes: PreviewSize

    init {
        val validator = { w: Int, h: Int ->
            (w > 0) && (h > 0) && max(w.toDouble() / h, h.toDouble() / w) <= 2
        }

        val imageSize = savedStateHandle.get<ImageSize>(IMAGE_SIZE_KEY)
        if(imageSize != null) {
            sizes = PreviewSize(SIZES_STEP, imageSize.width, imageSize.height, validator)
            savedStateHandle.set(
                    IMAGE_SIZE_KEY,
                    ImageSize(sizes.width, sizes.height, setWidth = true, setHeight = true)
            )
        } else {
            sizes = PreviewSize(SIZES_STEP, validator = validator)
            savedStateHandle.set(
                    IMAGE_SIZE_KEY,
                    ImageSize(sizes.width, sizes.height, setWidth = true, setHeight = true)
            )
        }
    }

    private val imageListLiveData = MutableLiveData<ImageList>()
    private val imageErrorLiveData = MutableLiveData<String>()

    fun observeImageListLiveData(): LiveData<ImageList> = imageListLiveData
    fun observeErrorLiveData(): LiveData<String> = imageErrorLiveData
    fun observeSizesLiveData(): LiveData<ImageSize>? = savedStateHandle.getLiveData(IMAGE_SIZE_KEY)

    fun showItems(pages: Int, pageSize: Int) {
        api.getImageList(1, pages * pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableSingleObserver<List<Image>>() {
                    override fun onSuccess(list: List<Image>) {
                        imageListLiveData.value = ImageList(list, pages, pageSize)
                    }

                    override fun onError(e: Throwable) {
                        imageErrorLiveData.value = e.message
                    }
                })
    }

    fun increaseWidth() {
        sizes.increaseWidth()
        savedStateHandle.set(
                IMAGE_SIZE_KEY,
                ImageSize(sizes.width, sizes.height, setWidth = true)
        )
    }

    fun decreaseWidth() {
        sizes.decreaseWidth()
        savedStateHandle.set(
                IMAGE_SIZE_KEY,
                ImageSize(sizes.width, sizes.height, setWidth = true)
        )
    }

    fun increaseHeight() {
        sizes.increaseHeight()
        savedStateHandle.set(
                IMAGE_SIZE_KEY,
                ImageSize(sizes.width, sizes.height, setHeight = true)
        )
    }

    fun decreaseHeight() {
        sizes.decreaseHeight()
        savedStateHandle.set(
                IMAGE_SIZE_KEY,
                ImageSize(sizes.width, sizes.height, setHeight = true)
        )
    }

}