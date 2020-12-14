# Effekts

User defined effects for Kotlin multiplatform

# Example

## User defined effects

```kotlin
class IOConsole(
    val printString: (String) -> Effect<Unit>,
    val readString: Effect<String>
)
```

## DSL for effects handling

```kotlin
handle<Unit, IOConsole> { console ->
    val name = console.readString.bind()
    console.printString("Hello $name").bind()
} with {
    IOConsole(
        { text ->
            { k ->
                actions += "printString($text)"
                k(Unit)
            }
        },
        { k ->
            actions += "readStream(World)"
            k("World!")
        }
    )
}
```
