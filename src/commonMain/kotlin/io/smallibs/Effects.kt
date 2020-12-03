package io.smallibs

class Effects<O, E : Effect<*>>(var effect: suspend (E) -> Any) {           // O is not used (phantom type)
    suspend fun <A> perform(action: E): A = this.effect(action) as A        // Ugly cast remaining here
}

