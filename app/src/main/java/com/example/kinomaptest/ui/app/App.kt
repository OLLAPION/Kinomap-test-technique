package com.example.kinomaptest.ui.app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.kinomaptest.ui.navigation.PropertyNavHost
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import com.google.firebase.auth.FirebaseAuth
import com.example.kinomaptest.ui.screen.login.LoginScreen
import com.bumptech.glide.integration.compose.GlideImage
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi


// drawer (firebase design) + top bar + bottom bar + login
// And the NavHost Compose.
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DrawerContent(
    user: com.google.firebase.auth.FirebaseUser?
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // User profil with avatar + name + email
        if (user != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                GlideImage(
                    model = user.photoUrl,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(
                        text = user.displayName ?: "Utilisateur",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = user.email ?: "",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Divider()
        }

        TextButton(onClick = {
            FirebaseAuth.getInstance().signOut()
        }) {
            Text("Se déconnecter")
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val auth = remember { FirebaseAuth.getInstance() }
    var isLoggedIn by remember { mutableStateOf(auth.currentUser != null) }
    val user = auth.currentUser

    DisposableEffect(auth) {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            isLoggedIn = firebaseAuth.currentUser != null
        }
        auth.addAuthStateListener(listener)
        onDispose { auth.removeAuthStateListener(listener) }
    }

    if (!isLoggedIn) {
        LoginScreen(onLoggedIn = { /* le listener it's enough */ })
        return
    }

    // Main nav controller for Compose Navigation
    val navController = rememberNavController()

    // Drawer state + coroutine scope to open/close it
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )
    val scope = rememberCoroutineScope()

    // Modal drawer around the whole app
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(user = user)
            }
        }
    ) {
        // App scaffold with top bar and bottom navigation
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Kinomap Test Technique")
                    },
                    navigationIcon = {
                        // Menu icon toggles drawer; when closing, also navigate all_badges
                        IconButton(onClick = { /* Open Left Menu Future Usage */ }) {
                            Icon(
                                Icons.Filled.Menu,
                                contentDescription = "Settings",
                                modifier = Modifier.clickable {
                                    scope.launch {
                                        drawerState.apply {
                                            if(isClosed) open() else {
                                                close()
                                                navController.navigate("all_badges")
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    }
                )
            },
            bottomBar = {
                BottomNavigationBar(navController)
            }
        ) { paddingValues ->
            // NavHost with the destination app (list and search)
            PropertyNavHost(
                navController = navController,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

// Simple modele for a bottom nav item (route + label + icon)
data class NavigationItem(
    val route: String,
    val longLabel: String,
    val shortLabel: String,
    val icon: ImageVector
)


@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        NavigationItem("all_badges", "All badges", "All", Icons.Filled.Home),
        NavigationItem("search_badges_by_categories", "Search badges", "Search", Icons.Filled.Search)
    )

    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp > 600

    BottomNavigation {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                BottomNavigationItem(
                    selected = false,
                    //avoids stacking screens
                    onClick = {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                        }
                    },
                    label = {
                        val displayLabel = if (isTablet) item.longLabel else item.shortLabel
                        Text(displayLabel)
                    },
                    icon = {
                        Icon(item.icon, contentDescription = item.longLabel)
                    }
                )
            }
        }
    }
}


@Composable
fun BottomNavigation(content: @Composable () -> Unit) {
    content()
}

// Single item in the custom bottom bar (uses a TextButton for simplicity)
@Composable
fun BottomNavigationItem(selected: Boolean, onClick: () -> Unit, label: @Composable () -> Unit, icon: @Composable () -> Unit) {
    TextButton(onClick = onClick) {
        icon()
        label()
    }
}