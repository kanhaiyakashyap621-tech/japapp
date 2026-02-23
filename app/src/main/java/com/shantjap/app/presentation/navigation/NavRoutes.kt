package com.shantjap.app.presentation.navigation

import com.shantjap.app.domain.model.Mode

object NavRoutes {
    const val MAIN = "main"
    const val SILENT = "silent"
    const val LEADERBOARD = "leaderboard"
    const val JOURNEY = "journey"
    const val SANKALP = "sankalp"

    fun silentRoute(mode: Mode): String = "silent/${mode.name}"
}
