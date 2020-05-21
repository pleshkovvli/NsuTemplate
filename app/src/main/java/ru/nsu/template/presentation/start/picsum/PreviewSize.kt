package ru.nsu.template.presentation.start.picsum

class PreviewSize(
        private val step: Int,
        initWidth: Int = step * 9,
        initHeight: Int = step * 12,
        private val validator: (
                @ParameterName("width") Int,
                @ParameterName("height") Int
        ) -> Boolean
) {
    var width: Int = initWidth
        private set
    var height: Int = initHeight
        private set

    fun increaseWidth() {
        if(validator(width + step, height)) {
            width += step
        }
    }

    fun decreaseWidth() {
        if(validator(width - step, height)) {
            width -= step
        }
    }

    fun increaseHeight() {
        if(validator(width, height + step)) {
            height += step
        }
    }

    fun decreaseHeight() {
        if(validator(width, height - step)) {
            height -= step
        }
    }
}