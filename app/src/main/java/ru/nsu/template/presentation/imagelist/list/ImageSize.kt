package ru.nsu.template.presentation.imagelist.list

import java.io.Serializable

class ImageSize(
        val width: Int,
        val height: Int,
        val setWidth: Boolean = false,
        val setHeight: Boolean = false
) : Serializable