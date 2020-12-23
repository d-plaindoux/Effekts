package io.smallibs.continuation

internal sealed class Frame {
    data class Return(val value: Any) : Frame()
    object Enter : Frame()
}
