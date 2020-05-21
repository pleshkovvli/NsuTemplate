package ru.nsu.template.data.network.picsum

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.nsu.template.data.model.picsum.Image
import ru.nsu.template.data.model.picsum.ImageList

interface PicsumApi {
    @GET("/v2/list")
    fun getImageList(@Query("page") page: Int, @Query("limit") limit: Int) : Single<List<Image>>
}