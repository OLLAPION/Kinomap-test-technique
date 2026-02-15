package com.example.kinomaptest.ui.navigation


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.kinomaptest.ui.screen.allbadges.AllBadgesScreen
import com.example.kinomaptest.ui.screen.detailsbadge.BadgeDetailsScreen
import com.example.kinomaptest.ui.screen.searchbadges.SearchBadgesByCategoriesScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PropertyNavHost(navController: NavHostController, modifier: Modifier) {

    NavHost(navController, startDestination = "all_badges", modifier = modifier) {

        // List screen (all badges)
        composable("all_badges") { AllBadgesScreen(navController) }

        // Search badges by categories screen
        composable("search_badges_by_categories") {
            SearchBadgesByCategoriesScreen(navController = navController)
        }

        // Details screen: expects a badgeId in the route
        composable("badge_details/{badgeId}") { backStackEntry ->
            // Use toIntOrNull() to avoid crash on bad input
            val badgeId = backStackEntry.arguments?.getString("badgeId")?.toIntOrNull()
            if (badgeId != null) {
                BadgeDetailsScreen(badgeId)
            }
        }


    }
}