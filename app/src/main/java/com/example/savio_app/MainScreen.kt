package com.example.savio_app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.savio_app.data.local.SessionManager
import com.example.savio_app.data.repository.NoteRepository
import com.example.savio_app.pages.*
import com.example.savio_app.ui.theme.Savio_appTheme
import com.example.savio_app.viewmodel.EventViewModel
import com.example.savio_app.viewmodel.ShoppingListViewModel
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {
    var isDarkMode by remember { mutableStateOf(false) }

    val navItemList = listOf(
        NavItem("Home", Icons.Default.Home, 5, "homePage"),
        NavItem("Aplicaciones", Icons.Default.Apps, 0, "appPage"),
        NavItem("Perfil", Icons.Default.Person, 0, "profilePage")
    )

    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val noteRepository = remember { NoteRepository() }
    val shoppingListViewModel = remember { ShoppingListViewModel(sessionManager) }
    val eventViewModel = remember { EventViewModel(sessionManager) }

    val creationResult by shoppingListViewModel.creationResult.collectAsState()
    val shoppingListsData by shoppingListViewModel.shoppingLists.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val selectedIndex = navItemList.indexOfFirst { it.route == currentRoute }.takeIf { it >= 0 } ?: 0

    LaunchedEffect(Unit) {
        shoppingListViewModel.loadShoppingLists()
    }

    Savio_appTheme(darkTheme = isDarkMode) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                if (currentRoute != "loginPage" &&
                    currentRoute != "splashScreen" &&
                    currentRoute != "registerPage") {
                    NavigationBar {
                        navItemList.forEachIndexed { index, navItem ->
                            NavigationBarItem(
                                selected = selectedIndex == index,
                                onClick = {
                                    if (currentRoute != navItem.route) {
                                        navController.navigate(navItem.route) {
                                            launchSingleTop = true
                                            restoreState = true
                                            popUpTo(navController.graph.startDestinationRoute!!) {
                                                saveState = true
                                            }
                                        }
                                    }
                                },
                                icon = {
                                    if (navItem.badgeCount > 0) {
                                        BadgedBox(
                                            badge = { Badge { Text(navItem.badgeCount.toString()) } }
                                        ) {
                                            Icon(imageVector = navItem.icon, contentDescription = navItem.label)
                                        }
                                    } else {
                                        Icon(imageVector = navItem.icon, contentDescription = navItem.label)
                                    }
                                },
                                label = { Text(navItem.label) }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->

            LaunchedEffect(creationResult) {
                creationResult?.let { message ->
                    snackbarHostState.showSnackbar(message)
                    if (message.startsWith("Lista creada:")) {
                        shoppingListViewModel.loadShoppingLists()
                    }
                }
            }

            NavHost(
                navController = navController,
                startDestination = "splashScreen",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("splashScreen") {
                    SplashScreen(navController = navController)
                }

                composable("loginPage") {
                    LoginPage(onLoginSuccess = { loginResponse ->
                        coroutineScope.launch {
                            sessionManager.saveSession(
                                token = loginResponse.token,
                                email = loginResponse.correo_electronico,
                                rol = loginResponse.rol,
                                nombre = loginResponse.nombre,
                                idusuario = loginResponse.userId
                            )
                        }
                        navController.navigate("homePage") {
                            popUpTo("loginPage") { inclusive = true }
                        }
                    }, navController = navController)
                }

                composable("forgotPasswordPage") {
                    ForgotPasswordPage(navController = navController)
                }

                composable("homePage") {
                    HomePage()
                }

                composable("appPage") {
                    AppPage(
                        navController = navController,
                        isDarkMode = isDarkMode,
                        toggleTheme = { isDarkMode = !isDarkMode }
                    )
                }

                composable("profilePage") {
                    ProfilePage(
                        isDarkMode = isDarkMode,
                        toggleTheme = { isDarkMode = !isDarkMode },
                        navController = navController,
                        sessionManager = sessionManager
                    )
                }

                composable("createNotePage") {
                    NotesPage(
                        isDarkMode = isDarkMode,
                        sessionManager = sessionManager,
                        noteRepository = noteRepository
                    )
                }

                composable("eventListPage") {
                    EventListPage(viewModel = eventViewModel)
                }

                composable("todoPage") {
                    ToDoPage(navController = navController)
                }

                composable("weatherPage") {
                    WeatherPage()
                }

                composable("chatPage") {
                    ChatPage()
                }

                composable("shoppingListPage") {
                    ShoppingListsPage(
                        shoppingLists = shoppingListsData,
                        viewModel = shoppingListViewModel,
                        onListClick = { list ->
                            navController.navigate("shoppingListDetail/${list.idlista}")
                        }
                    )
                }

                composable(
                    "shoppingListDetail/{listId}",
                    arguments = listOf(navArgument("listId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val listId = backStackEntry.arguments?.getInt("listId") ?: return@composable
                    val selectedList = shoppingListsData.firstOrNull { it.idlista == listId }
                    var token: String? by remember { mutableStateOf(null) }

                    LaunchedEffect(Unit) {
                        token = sessionManager.getToken()
                    }

                    Box(modifier = Modifier.fillMaxSize()) {
                        when {
                            token === null -> {
                                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                            }
                            token?.isEmpty() == true -> {
                                LaunchedEffect(Unit) {
                                    navController.navigate("loginPage") {
                                        popUpTo(navController.graph.startDestinationId)
                                    }
                                }
                            }
                            selectedList == null -> {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text("Lista no encontrada")
                                    Button(onClick = { navController.navigateUp() }) {
                                        Text("Volver")
                                    }
                                }
                            }
                            else -> {
                                val currentToken = token
                                if (currentToken != null) {
                                    ShoppingListDetailPage(
                                        idlista = selectedList.idlista,
                                        listTitle = selectedList.titulo,
                                        token = currentToken,
                                        onBack = { navController.navigateUp() }
                                    )
                                }
                            }
                        }
                    }
                }

                composable("registerPage") {
                    RegisterPage(
                        onRegisterSuccess = {
                            navController.navigate("loginPage") {
                                popUpTo("registerPage") { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}
