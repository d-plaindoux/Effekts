# Effekts

User defined effects for Kotlin multiplatform

# Example

## User define effects

```kotlin
sealed class IOConsole<T> : Effect<T>() {
    data class printString(val text: String) : IOConsole<Unit>()
    class readString : IOConsole<String>()
}
```

## DSL for effects handling

```kotlin
handle<Unit, IOConsole<*>> {
    val name: String = perform(readString())
    perform(printString("Hello $name"))
} with {
    when (it) {
        is printString -> {
            println(it.text)
            it.resume(Unit)
        }
        is readString -> {            
            it.resume("World")
        }
    }
}
```
