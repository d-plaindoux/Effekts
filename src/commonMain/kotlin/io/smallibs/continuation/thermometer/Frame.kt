package io.smallibs.continuation.thermometer

sealed class Frame {
    data class Return(val value: Any) : Frame()
    object Enter : Frame()
}
