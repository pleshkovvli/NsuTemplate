package ru.nsu.template.data.model.picsum

import java.io.Serializable

class ImageList(
        var items: List<Image>,
        var pages: Int,
        var pageSize: Int
) : Serializable