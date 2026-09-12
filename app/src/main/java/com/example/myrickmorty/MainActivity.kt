package com.example.myrickmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import kotlinx.coroutines.delay

// --- PALETA DE COLORES ---
val BgDark = Color(0xFF1E2025)
val CardDark = Color(0xFF2C2F36)
val RickGreen = Color(0xFF97CE4C)
val MortyYellow = Color(0xFFF1DE2F)
val TextGray = Color(0xFF8B929B)
val DividerGray = Color(0xFF383B43)

// --- MODELO DE DATOS Y MOCKS ---
data class Character(
    val id: String, val name: String, val species: String, val gender: String,
    val origin: String, val lastLocation: String, val status: String,
    val episodes: String, val image: String
)

val mockCharacters = listOf(
    Character("1", "Rick Sanchez", "Human", "Male", "Earth (C-137)", "Citadel of Ricks", "Alive", "51", "https://rickandmortyapi.com/api/character/avatar/1.jpeg"),
    Character("2", "Morty Smith", "Human", "Male", "Earth (C-137)", "Citadel of Ricks", "Alive", "51", "https://rickandmortyapi.com/api/character/avatar/2.jpeg"),
    Character("3", "Summer Smith", "Human", "Female", "Earth (Replacement Dimension)", "Earth (Replacement Dimension)", "Alive", "42", "https://rickandmortyapi.com/api/character/avatar/3.jpeg"),
    Character("4", "Beth Smith", "Human", "Female", "Earth (Replacement Dimension)", "Earth (Replacement Dimension)", "Alive", "42", "https://rickandmortyapi.com/api/character/avatar/4.jpeg")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(color = BgDark, modifier = Modifier.fillMaxSize()) {
                    RickAndMortyNavGraph()
                }
            }
        }
    }
}

// --- NAVEGACIÓN ---
@Composable
fun RickAndMortyNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreenMatch(navController) }
        composable("home") { HomeScreenMatch(navController) }
        composable("detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            val character = mockCharacters.find { it.id == id } ?: mockCharacters.first()
            DetailScreenMatch(navController, character)
        }
    }
}

// ==========================================
// 1. VISTA: SPLASH SCREEN
// ==========================================
@Composable
fun SplashScreenMatch(navController: NavHostController) {
    LaunchedEffect(Unit) {
        delay(2500)
        navController.navigate("home") { popUpTo("splash") { inclusive = true } }
    }

    Box(modifier = Modifier.fillMaxSize().background(BgDark), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Efecto Portal
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val radius = size.minDimension / 2
                    drawCircle(color = RickGreen.copy(alpha = 0.2f), radius = radius)
                    drawCircle(color = RickGreen, radius = radius * 0.8f, style = Stroke(width = 8f))
                    drawCircle(color = RickGreen.copy(alpha = 0.5f), radius = radius * 0.5f, style = Stroke(width = 4f))
                    drawCircle(color = MortyYellow, radius = radius * 0.2f)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text("RICK", color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, fontStyle = FontStyle.Italic)
            Text("AND", color = MortyYellow, fontSize = 16.sp, fontWeight = FontWeight.Bold, letterSpacing = 4.sp)
            Text("MORTY", color = MortyYellow, fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, fontStyle = FontStyle.Italic)
        }

        Column(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("LOADING.", color = TextGray, fontSize = 14.sp, letterSpacing = 2.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(32.dp))
            Text("v2.0 - Galactic Edition", color = TextGray.copy(alpha = 0.5f), fontSize = 12.sp)
        }
    }
}

// ==========================================
// 2. VISTA: HOME SCREEN
// ==========================================
@Composable
fun HomeScreenMatch(navController: NavHostController) {
    Scaffold(
        containerColor = BgDark,
        bottomBar = { CustomBottomNavigation() }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("CHARACTERS", color = RickGreen, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, fontStyle = FontStyle.Italic)
                Box(modifier = Modifier.size(40.dp).background(CardDark, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = TextGray)
                }
            }

            Text("12 CHARACTERS", color = TextGray, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp))

            // Lista
            LazyColumn(contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)) {
                items(mockCharacters) { char ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CardDark),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp).clickable { navController.navigate("detail/${char.id}") }
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = char.image, contentDescription = char.name,
                                modifier = Modifier.size(70.dp).clip(RoundedCornerShape(16.dp))
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(char.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Text("${char.species} • ${char.origin}", color = TextGray, fontSize = 12.sp, maxLines = 1)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(8.dp).background(RickGreen, CircleShape))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(char.status, color = RickGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = TextGray)
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 3. VISTA: DETAIL SCREEN
// ==========================================
@Composable
fun DetailScreenMatch(navController: NavHostController, character: Character) {
    Scaffold(
        containerColor = BgDark,
        bottomBar = { CustomBottomNavigation() }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(horizontal = 24.dp)) {
            // Top Bar
            Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.background(CardDark, CircleShape).size(40.dp)) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                IconButton(onClick = { }, modifier = Modifier.background(CardDark, CircleShape).size(40.dp)) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "Fav", tint = MortyYellow)
                }
            }

            // Imagen con Arco y resplandor verde
            Box(modifier = Modifier.fillMaxWidth().height(260.dp), contentAlignment = Alignment.Center) {
                Box(modifier = Modifier.size(200.dp).background(Brush.radialGradient(listOf(RickGreen.copy(alpha = 0.3f), Color.Transparent))), contentAlignment = Alignment.Center) {}
                AsyncImage(
                    model = character.image,
                    contentDescription = null,
                    modifier = Modifier.size(220.dp, 240.dp).clip(RoundedCornerShape(topStart = 120.dp, topEnd = 120.dp, bottomStart = 16.dp, bottomEnd = 16.dp))
                )
            }

            // Nombre y Estado
            Text(character.name.uppercase(), color = RickGreen, fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, fontStyle = FontStyle.Italic)
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
                Box(modifier = Modifier.size(10.dp).background(RickGreen, CircleShape))
                Spacer(modifier = Modifier.width(8.dp))
                Text("${character.status} • ${character.species} • ${character.gender}", color = TextGray, fontSize = 14.sp)
            }

            // Tarjetas de Estadísticas
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(modifier = Modifier.weight(1f), value = character.episodes, label = "EPISODES")
                StatCard(modifier = Modifier.weight(1f), value = character.species.uppercase(), label = "SPECIES")
                StatCard(modifier = Modifier.weight(1f), value = character.gender.uppercase(), label = "GENDER")
            }

            // Detalles en Lista
            Text("CHARACTER INFO", color = TextGray, fontSize = 16.sp, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic, modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
            Divider(color = DividerGray, thickness = 1.dp)
            InfoRow("STATUS", character.status, RickGreen)
            Divider(color = DividerGray, thickness = 1.dp)
            InfoRow("SPECIES", character.species, Color.White)
            Divider(color = DividerGray, thickness = 1.dp)
            InfoRow("GENDER", character.gender, Color.White)
            Divider(color = DividerGray, thickness = 1.dp)
            InfoRow("ORIGIN", character.origin, Color.White)
            Divider(color = DividerGray, thickness = 1.dp)
            InfoRow("LAST KNOWN LOCATION", character.lastLocation, Color.White)
        }
    }
}

// --- COMPONENTES REUTILIZABLES ---
@Composable
fun CustomBottomNavigation() {
    Row(
        modifier = Modifier.fillMaxWidth().background(BgDark).padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        BottomNavItem(Icons.Default.Home, "Home", true)
        BottomNavItem(Icons.Default.Search, "Search", false)
        BottomNavItem(Icons.Default.PlayArrow, "Episodes", false)
        BottomNavItem(Icons.Default.Person, "Profile", false)
    }
}

@Composable
fun BottomNavItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, isSelected: Boolean) {
    val color = if (isSelected) RickGreen else TextGray
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { }) {
        Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, color = color, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun StatCard(modifier: Modifier = Modifier, value: String, label: String) {
    Column(
        modifier = modifier.background(CardDark, RoundedCornerShape(12.dp)).padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, fontStyle = FontStyle.Italic)
        Text(label, color = TextGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun InfoRow(label: String, value: String, valueColor: Color) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = TextGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Text(value, color = valueColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}