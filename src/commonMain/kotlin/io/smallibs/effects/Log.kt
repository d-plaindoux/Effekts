package io.smallibs.effects

import io.smallibs.core.Effect
import io.smallibs.core.Handler

class Log(val log: (String) -> Effect<Unit>) : Handler

