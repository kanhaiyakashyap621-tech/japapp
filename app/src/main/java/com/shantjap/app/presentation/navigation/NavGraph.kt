package com.shantjap.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.shantjap.app.domain.model.Mode
import com.shantjap.app.presentation.ui.screens.JourneyScreen
import com.shantjap.app.presentation.ui.screens.LeaderboardScreen
import com.shantjap.app.presentation.ui.screens.MainScreen
import com.shantjap.app.presentation.ui.screens.SankalpScreen
import com.shantjap.app.presentation.ui.screens.SilentJapScreen

@Composable
fun ShantNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavRoutes.MAIN) {
        composable(NavRoutes.MAIN) {
            MainScreen(navController = navController)
        }
        composable(
            route = "${NavRoutes.SILENT}/{mode}",
            arguments = listOf(navArgument("mode") { type = NavType.StringType })
        ) { backStackEntry ->
            val modeArg = backStackEntry.arguments?.getString("mode") ?: Mode.RADHA.name
            val mode = runCatching { Mode.valueOf(modeArg) }.getOrDefault(Mode.RADHA)
            SilentJapScreen(navController = navController, mode = mode)
        }
        composable(NavRoutes.LEADERBOARD) {
            LeaderboardScreen(navController = navController)
        }
        composable(NavRoutes.JOURNEY) {
            JourneyScreen(navController = navController)
        }
        composable(NavRoutes.SANKALP) {
            SankalpScreen(navController = navController)
        }
    }
}
