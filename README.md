# Effekts

User defined effects for Kotlin multiplatform

# Example

## User defined effects

```kotlin
class IOConsole(
    val printString: (String) -> Effect<Unit>,
    val readString: Effect<String>
): Handler
```

## DSL for effects handling

```kotlin
handle<Unit, IOConsole> { console ->
    val name = console.readString.bind()
    console.printString("Hello $name").bind()
} with IOConsole(
    printString = { text ->
        { k ->
            actions += "printString($text)"
            k(Unit)
        }
    },
    readString = { k ->
        actions += "readStream(World)"
        k("World!")
    }
)
```
