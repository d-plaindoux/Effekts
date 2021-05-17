# Effekts

**NOTE**: This project is no more maintained and has been merged in [Pilin](https://github.com/d-plaindoux/pilin)

User defined effects for Kotlin multiplatform.

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
    EffectMonad().fluent {
        console.readString bind { name ->
            console.printString("Hello $name")
        }
    }.perform()
} with IOConsole(
    printString = Effect { text ->
        { k ->
            actions += "printString($text)"
            k(Unit)
        }
    },
    readString = Effect { k ->
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
    val state = effect.left
    val logger = effect.right

    EffectMonad().fluent {
        state.get bind {
            state.set(it + 32)
        } bind {
            state.get
        } bind {
            logger.log("Done with $it")
        }
    }.perform()
} with {
    State<Int>(
        set = { value ->
            Effect { k ->
                state.value = value
                k.resume(Unit)
            }
        },
        get = Effect { k ->
            k.resume(state.value)
        }
    ) and Log { value ->
        Effect { k ->
            log.getAndSet(log.value + value)
            k.resume(Unit)
        }
    }
}
```


