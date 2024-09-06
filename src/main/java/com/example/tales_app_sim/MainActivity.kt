package com.example.tales_app_sim

import android.app.Activity
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tales_app_sim.ui.theme.Tales_app_simTheme
import kotlinx.coroutines.delay
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Tales_app_simTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Nav()
                }
            }
        }
    }
}

@Composable
fun Nav() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main_menu") {
        composable("main_menu") {
            MainMenu(navController)
        }
        composable("game") {
            Game(navController)
        }
        composable("settings") {
            Settings(navController)
        }
    }
}

@Composable
fun MainMenu(navController: NavHostController) {
    val gradientColors = listOf(Color(0xFF6200EE), Color(0xFF3700B3), Color(0xFF03DAC5))
    val brush = remember {
        Brush.verticalGradient(gradientColors)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedTitle()
            Spacer(modifier = Modifier.height(64.dp))
            AnimatedMenuItems(navController)
        }
    }
}

@Composable
fun AnimatedTitle() {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + expandVertically(expandFrom = Alignment.Top)
    ) {
        Text(
            "Tiles Matcher",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun AnimatedMenuItems(navController: NavHostController) {
    val context = LocalContext.current
    val menuItems = listOf(
        "Play" to { navController.navigate("game") },
        "Settings" to { navController.navigate("settings") },
        "Exit" to { (context as? Activity)?.finish() }
    )

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        menuItems.forEachIndexed { index, (text, onClick) ->
            var visible by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                delay(500L + (index * 100L))
                visible = true
            }

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn() + expandHorizontally(expandFrom = Alignment.Start)
            ) {
                Button(
                    onClick = { onClick() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f))
                ) {
                    Text(text, color = Color.White, fontSize = 18.sp)
                }
            }
        }
    }
}

data class TileData(val icon: androidx.compose.ui.graphics.vector.ImageVector, var isRevealed: Boolean = false, var isMatched: Boolean = false)

data class GameState(val tiles: List<TileData>, val lastAction: String)

@Composable
fun Tile(tile: TileData, onClick: () -> Unit) {
    var isFlipped by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing), label = ""
    )
    val scale by animateFloatAsState(
        targetValue = if (tile.isMatched) 0.8f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow), label = ""
    )

    LaunchedEffect(tile.isRevealed, tile.isMatched) {
        isFlipped = tile.isRevealed || tile.isMatched
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(70.dp)
            .padding(4.dp)
            .scale(scale)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .clip(RoundedCornerShape(8.dp))
            .background(
                when {
                    tile.isMatched -> Color(0xFF81C784)
                    tile.isRevealed -> Color(0xFFFFB74D)
                    else -> Color(0xFF64B5F6)
                }
            )
            .border(2.dp, Color.White, RoundedCornerShape(8.dp))
            .clickable(enabled = !tile.isRevealed && !tile.isMatched, onClick = onClick)
    ) {
        if (rotation <= 90f) {
            Box(Modifier.fillMaxSize())
        } else {
            Icon(
                imageVector = tile.icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(40.dp)
                    .graphicsLayer {
                        rotationY = 180f
                    }
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(navController: NavHostController) {
    val gradientColors = listOf(Color(0xFF6200EE), Color(0xFF3700B3), Color(0xFF03DAC5))
    val brush = remember {
        Brush.verticalGradient(gradientColors)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedSettingsTitle()
            Spacer(modifier = Modifier.height(64.dp))
            AnimatedSettingsItems(navController)
        }
    }
}

@Composable
fun AnimatedSettingsTitle() {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + expandVertically(expandFrom = Alignment.Top)
    ) {
        Text(
            "Settings",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun AnimatedSettingsItems(navController: NavHostController) {
    var volume by remember { mutableStateOf(0.5f) }
    var vibrationEnabled by remember { mutableStateOf(true) }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Volume Control
        AnimatedSettingItem(
            text = "Volume",
            icon = ImageVector.vectorResource(id = R.drawable.volume_up),
            content = {
                Column {
                    Text("Volume: ${(volume * 100).toInt()}%", color = Color.White)
                    Slider(
                        value = volume,
                        onValueChange = { volume = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = SliderDefaults.colors(
                            thumbColor = Color.White,
                            activeTrackColor = Color.White,
                            inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                        )
                    )
                }
            }
        )

        // Vibration Control
        AnimatedSettingItem(
            text = "Vibration",
            icon = ImageVector.vectorResource(id = R.drawable.vibration)  ,
            content = {
                Switch(
                    checked = vibrationEnabled,
                    onCheckedChange = { vibrationEnabled = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color.White.copy(alpha = 0.5f),
                        uncheckedThumbColor = Color.White.copy(alpha = 0.5f),
                        uncheckedTrackColor = Color.White.copy(alpha = 0.1f)
                    )
                )
            }
        )

        // Back to Menu
        AnimatedSettingItem(
            text = "Back to Menu",
            icon = Icons.Filled.ArrowBack,
            content = {
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f))
                ) {
                    Text("Return", color = Color.White)
                }
            }
        )
    }
}

@Composable
fun AnimatedSettingItem(
    text: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(500L)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + expandHorizontally(expandFrom = Alignment.Start)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(icon, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(16.dp))
                    Text(text, color = Color.White, fontSize = 18.sp)
                }
                Spacer(Modifier.height(8.dp))
                content()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun Game(navController: NavHostController) {
    val gradientColors = listOf(Color(0xFF6200EE), Color(0xFF3700B3), Color(0xFF03DAC5))
    val brush = remember { Brush.verticalGradient(gradientColors) }

    val icons = listOf(
        Icons.Filled.Favorite,
        Icons.Filled.Star,
        Icons.Filled.Face,
        Icons.Filled.Home,
        Icons.Filled.Settings,
        Icons.Filled.ThumbUp,
        Icons.Filled.ShoppingCart,
        Icons.Filled.Email
    )

    var tiles by remember { mutableStateOf((icons + icons).shuffled().map { TileData(it) }) }
    var selectedTiles by remember { mutableStateOf<List<Int>>(emptyList()) }
    var history by remember { mutableStateOf(listOf(GameState(tiles, "Game Start"))) }
    var currentHistoryIndex by remember { mutableStateOf(0) }
    var isPaused by remember { mutableStateOf(false) }
    var showConfetti by remember { mutableStateOf(false) }

    LaunchedEffect(selectedTiles) {
        if (selectedTiles.size == 2) {
            delay(1000)
            val (first, second) = selectedTiles
            if (tiles[first].icon == tiles[second].icon) {
                tiles = tiles.mapIndexed { index, tile ->
                    if (index in selectedTiles) tile.copy(isMatched = true) else tile
                }
                history = history.take(currentHistoryIndex + 1) + GameState(tiles, "Matched: $first and $second")
            } else {
                tiles = tiles.mapIndexed { index, tile ->
                    if (index in selectedTiles) tile.copy(isRevealed = false) else tile
                }
                history = history.take(currentHistoryIndex + 1) + GameState(tiles, "Unmatched: $first and $second")
            }
            selectedTiles = emptyList()
            currentHistoryIndex = history.size - 1
        }
    }

    LaunchedEffect(tiles) {
        if (tiles.all { it.isMatched }) {
            showConfetti = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TopAppBar(
                title = { Text("Memory Match", color = Color.White) },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent),
                actions = {
                    IconButton(onClick = { isPaused = !isPaused }) {
                        Icon(
                            if (isPaused) Icons.Filled.PlayArrow else ImageVector.vectorResource(id = R.drawable.pause),
                            contentDescription = if (isPaused) "Resume" else "Pause",
                            tint = Color.White
                        )
                    }
                }
            )

            AnimatedVisibility(
                visible = !isPaused,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(tiles.size) { index ->
                        Tile(tile = tiles[index]) {
                            if (!isPaused && selectedTiles.size < 2 && !tiles[index].isRevealed) {
                                tiles = tiles.mapIndexed { i, tile ->
                                    if (i == index) tile.copy(isRevealed = true) else tile
                                }
                                selectedTiles = selectedTiles + index
                                history = history.take(currentHistoryIndex + 1) + GameState(tiles, "Revealed: $index")
                                currentHistoryIndex = history.size - 1
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = !isPaused,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    Text("Game History", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                if (currentHistoryIndex > 0) {
                                    currentHistoryIndex--
                                    tiles = history[currentHistoryIndex].tiles
                                }
                            },
                            enabled = currentHistoryIndex > 0,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f))
                        ) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Previous", tint = Color.White)
                            Spacer(Modifier.width(4.dp))
                            Text("Previous", color = Color.White)
                        }
                        Button(
                            onClick = {
                                if (currentHistoryIndex < history.size - 1) {
                                    currentHistoryIndex++
                                    tiles = history[currentHistoryIndex].tiles
                                }
                            },
                            enabled = currentHistoryIndex < history.size - 1,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f))
                        ) {
                            Text("Next", color = Color.White)
                            Spacer(Modifier.width(4.dp))
                            Icon(Icons.Filled.ArrowForward, contentDescription = "Next", tint = Color.White)
                        }
                    }
                    AnimatedContent(
                        targetState = currentHistoryIndex,
                        transitionSpec = {
                            slideInVertically { height -> height } + fadeIn() with
                                    slideOutVertically { height -> -height } + fadeOut()
                        }, label = ""
                    ) { targetIndex ->
                        LazyColumn(
                            modifier = Modifier
                                .weight(0.5f)
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                                .heightIn(max = 200.dp)
                        ) {
                            itemsIndexed(history.take(targetIndex + 1)) { index, state ->
                                AnimatedVisibility(
                                    visible = true,
                                    enter = slideInVertically() + expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                                    modifier = Modifier.animateItemPlacement()
                                ) {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f))
                                    ) {
                                        Text(
                                            state.lastAction,
                                            modifier = Modifier.padding(8.dp),
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = isPaused,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "PAUSED",
                        color = Color.White,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                    PauseMenuItems(
                        onResume = { isPaused = false },
                        onRestart = {
                            isPaused = false
                            tiles = (icons + icons).shuffled().map { TileData(it) }
                            selectedTiles = emptyList()
                            history = listOf(GameState(tiles, "Game Restarted"))
                            currentHistoryIndex = 0
                        },
                        onMainMenu = { navController.navigate("main_menu") }
                    )
                }
            }
        }

        if (showConfetti) {
            LottieConfettiOverlay(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f),
                onRestart = {
                    // Reset the game
                    tiles = (icons + icons).shuffled().map { TileData(it) }
                    selectedTiles = emptyList()
                    history = listOf(GameState(tiles, "Game Restarted"))
                    currentHistoryIndex = 0
                    showConfetti = false
                },
                onMainMenu = {
                    navController.navigate("main_menu")
                    showConfetti = false
                }
            )
        }
    }
}

@Composable
fun LottieConfettiOverlay(
    modifier: Modifier = Modifier,
    onRestart: () -> Unit,
    onMainMenu: () -> Unit
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("confetti.json"))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f))
    ) {
        // Confetti animation
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Congratulatory text and buttons
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "CONGRATULATIONS!",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            Text(
                "You've completed the game!",
                color = Color.White,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 48.dp)
            )
            PauseMenuItems(
                onResume = { /* Not used in this context */ },
                onRestart = onRestart,
                onMainMenu = onMainMenu
            )
        }
    }
}

@Composable
fun PauseMenuItems(
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onMainMenu: () -> Unit
) {
    val menuItems = listOf(
        Triple("Play Again", Icons.Filled.Refresh, onRestart),
        Triple("Main Menu", Icons.Filled.Home, onMainMenu)
    )

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        menuItems.forEachIndexed { index, (text, icon, onClick) ->
            var visible by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                delay(100L + (index * 100L))
                visible = true
            }

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn() + expandHorizontally(expandFrom = Alignment.Start)
            ) {
                Button(
                    onClick = onClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f))
                ) {
                    Icon(icon, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text(text, color = Color.White, fontSize = 18.sp)
                }
            }
        }
    }
}