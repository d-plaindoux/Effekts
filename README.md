# Effekts

User defined effects for Kotlin multiplatform

# Example

## User defined effects

```kotlin
interface IOConsole {
    fun printString(text: String) : Effect<Unit>
    fun readString() : Effect<String>
}
```

## DSL for effects handling

```kotlin
handle<Unit, IOConsole> { console ->
            val name = console.readString().bind()
            console.printString("Hello $name").bind()
        } with {
            object : IOConsole {
                override fun printString(text: String) = Effect<Unit> { k ->
                    actions += "printString($text)"
                    k(Unit)
                }

                override fun readString() = Effect<String> { k ->
                    actions += "readStream(World)"
                    k("World!")
                }
            }
        }
```
