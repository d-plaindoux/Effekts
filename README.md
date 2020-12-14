# Effekts

User defined effects for Kotlin multiplatform

# IOConsole example

## User defined effects

```kotlin
class IOConsole(
    val printString: (String) -> Effect<Unit>,
    val readString: Effect<String>
): Handler
```

## DSL for effects handling

```kotlin
val actions = mutableListOf<String>()

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

# State and Log example

## User defined effects

### Logger

```kotlin
class Log(val log: (String) -> Effect<Unit>) : Handler
```

### State

The corresponding state is a naive specification. State monad should 
be provided for this purpose.

```kotlin
class State<V>(
    val set: (V) -> Effect<Unit>,
    val get: Effect<V>
) : Handler
```

## DSL for effects handling

```kotlin
val log: AtomicRef<String> = atomic("")
val state = atomic(10)

handle<Unit, And<State<Int>, Log>> {
    val value1 = it.left.get.bind()
    it.left.set(value1 + 32).bind()
    val value2 = it.left.get.bind()
    it.right.log("Done with $value2").bind()
} with {
    State<Int>(
        set = { value ->
            { k ->
                state.value = value
                k(Unit)
            }
        },
        get = { k ->
            k(state.value)
        }
    ) and Log { value ->
        { k ->
            log.getAndSet(log.value + value)
            k(Unit)
        }
    }
}
```


