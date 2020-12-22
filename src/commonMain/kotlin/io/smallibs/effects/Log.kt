package io.smallibs.effects

import io.smallibs.data.Effect
import io.smallibs.effect.Handler

class Log(val log: (String) -> Effect<Unit>) : Handler

