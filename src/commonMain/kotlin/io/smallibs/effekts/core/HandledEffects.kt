package io.smallibs.effekts.core

import io.smallibs.utils.Await
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class HandledEffects<O>(val result: suspend () -> O) {

    @ExperimentalCoroutinesApi
    fun unsafeRun(durationInSeconds: Long = STANDARD_DURATION): O {
        val deferred: Deferred<O> = GlobalScope.async { result() }

        Await() atMost (durationInSeconds * 1000) until {
            !deferred.isActive
        }

        if (deferred.isCompleted) {
            return deferred.getCompleted()
        }

        throw deferred.getCompletionExceptionOrNull()!!
    }

    companion object {
        private const val STANDARD_DURATION: Long = 60 * 3
    }
}
