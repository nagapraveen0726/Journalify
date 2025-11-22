package uk.ac.tees.mad.journalify.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import uk.ac.tees.mad.journalify.presentation.screen.auth.AuthScreen
import uk.ac.tees.mad.journalify.presentation.screen.entry.DetailEntryScreen
import uk.ac.tees.mad.journalify.presentation.screen.entry.EditEntryScreen
import uk.ac.tees.mad.journalify.presentation.screen.camera.CameraCaptureScreen
import uk.ac.tees.mad.journalify.presentation.screen.entry.create_entry.CreateEntryScreen
import uk.ac.tees.mad.journalify.presentation.screen.home.HomeScreen
import uk.ac.tees.mad.journalify.presentation.screen.settings.SettingsScreen
import uk.ac.tees.mad.journalify.presentation.screen.splash.SplashScreen


@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.AUTH,
        modifier = modifier
    ) {

        // ----------------------
        //    SPLASH SCREEN
        // ----------------------
        composable(Routes.SPLASH) {
            SplashScreen(
                goToAuth = {
                    navController.navigate(Routes.AUTH) {
                        popUpTo(0)
                    }
                },
                goToHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(0)
                    }
                }
            )
        }

        // ----------------------
        //    AUTH SCREEN
        // ----------------------
        composable(Routes.AUTH) {
            AuthScreen(
                onAuthSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(0)
                    }
                }
            )
        }

        // ----------------------
        //    HOME / LIST SCREEN
        // ----------------------
        composable(Routes.HOME) {
            HomeScreen(
                onCreateEntry = {
                    navController.navigate(Routes.CREATE_ENTRY)
                },
                onOpenEntry = { id ->
                    navController.navigate("detail_entry/$id")
                },
                onOpenSettings = {
                    navController.navigate(Routes.SETTINGS)
                }
            )
        }

        // ----------------------
        //    CREATE ENTRY
        // ----------------------
        composable(Routes.CREATE_ENTRY) {
            val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
            val image by savedStateHandle
                ?.getStateFlow<String?>("image", null)
                ?.collectAsState()
                ?: remember { mutableStateOf(null) }

            CreateEntryScreen(
                onBack = { navController.popBackStack() },
                onSaved = {
                    navController.popBackStack()   // return to Home
                },
                onAddImage = { navController.navigate(Routes.CAMERA) },
                imagePath = image
            )
        }

        // ----------------------
        //    EDIT ENTRY
        // ----------------------
        composable(
            route = "edit_entry/{entryId}",
            arguments = listOf(navArgument("entryId") { type = NavType.StringType })
        ) { backStack ->
            val id = backStack.arguments?.getString("entryId") ?: ""
            EditEntryScreen(
//                entryId = id,
//                onBack = { navController.popBackStack() }
            )
        }

        // ----------------------
        //    ENTRY DETAIL
        // ----------------------
        composable(
            route = "detail_entry/{entryId}",
            arguments = listOf(navArgument("entryId") { type = NavType.StringType })
        ) { backStack ->
            val id = backStack.arguments?.getString("entryId") ?: ""
            DetailEntryScreen(
//                entryId = id,
//                onBack = { navController.popBackStack() },
//                onEdit = {
//                    navController.navigate("edit_entry/$id")
//                }
            )
        }

        // ----------------------
        //    SETTINGS SCREEN
        // ----------------------
        composable(Routes.SETTINGS) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onLoggedOut = {
                    navController.navigate(Routes.AUTH) {
                        popUpTo(0)
                    }
                }
            )
        }

        // ----------------------
        //    CAMERA SCREEN
        // ----------------------
        composable(Routes.CAMERA) {
            CameraCaptureScreen(
                onCaptured = { path ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("image", path)

                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }

    }
}
