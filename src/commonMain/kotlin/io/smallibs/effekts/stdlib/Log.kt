package io.smallibs.effekts.stdlib

import io.smallibs.data.Effect
import io.smallibs.effekts.core.Handler

class Log(val log: (String) -> Effect<Unit>) : Handler
