package com.hridaya.app.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hridaya.app.ui.chat.ChatScreen
import com.hridaya.app.ui.home.HomeScreen
import com.hridaya.app.ui.journal.JournalEditorScreen
import com.hridaya.app.ui.journal.JournalScreen
import com.hridaya.app.ui.mood.MoodScreen
import com.hridaya.app.ui.music.MusicScreen
import com.hridaya.app.ui.breathe.BreathingScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Chat : Screen("chat")
    object Mood : Screen("mood")
    object Music : Screen("music")
    object Journal : Screen("journal")
    object JournalEditor : Screen("journal_editor?entryId={entryId}") {
        fun createRoute(entryId: Long? = null) = if (entryId != null) "journal_editor?entryId=$entryId" else "journal_editor"
    }
    object Breathing : Screen("breathing")
}

@Composable
fun HridayaNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        enterTransition = { slideInHorizontally(tween(300)) { it / 2 } + fadeIn(tween(300)) },
        exitTransition = { slideOutHorizontally(tween(300)) { -it / 2 } + fadeOut(tween(200)) },
        popEnterTransition = { slideInHorizontally(tween(300)) { -it / 2 } + fadeIn(tween(300)) },
        popExitTransition = { slideOutHorizontally(tween(300)) { it / 2 } + fadeOut(tween(200)) }
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToChat = { navController.navigate(Screen.Chat.route) },
                onNavigateToMood = { navController.navigate(Screen.Mood.route) },
                onNavigateToMusic = { navController.navigate(Screen.Music.route) },
                onNavigateToJournal = { navController.navigate(Screen.Journal.route) },
                onNavigateToBreathe = { navController.navigate(Screen.Breathing.route) }
            )
        }
        composable(Screen.Chat.route) {
            ChatScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Mood.route) {
            MoodScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Music.route) {
            MusicScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Journal.route) {
            JournalScreen(
                onBack = { navController.popBackStack() },
                onNewEntry = { navController.navigate(Screen.JournalEditor.createRoute()) },
                onEditEntry = { id -> navController.navigate(Screen.JournalEditor.createRoute(id)) }
            )
        }
        composable(
            Screen.JournalEditor.route,
            arguments = listOf(navArgument("entryId") {
                type = NavType.LongType; defaultValue = -1L
            })
        ) {
            JournalEditorScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Breathing.route) {
            BreathingScreen(onBack = { navController.popBackStack() })
        }
    }
}
