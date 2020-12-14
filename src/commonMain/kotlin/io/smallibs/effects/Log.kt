package io.smallibs.effects

import io.smallibs.core.Effect

class Log(val log: (String) -> Effect<Unit>)

