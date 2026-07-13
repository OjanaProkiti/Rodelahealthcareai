package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.R
import com.example.data.ResearchNote
import com.example.data.SimulationResult
import com.example.ui.theme.*

// Screen Routes
const val ROUTE_EXPLORER = "explorer"
const val ROUTE_LAB = "lab"
const val ROUTE_HISTORY = "history"
const val ROUTE_PODCAST = "podcast"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RodelaApp(viewModel: RodelaViewModel) {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            RodelaBottomNavigation(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ROUTE_EXPLORER,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(ROUTE_EXPLORER) {
                ExplorerScreen(viewModel = viewModel)
            }
            composable(ROUTE_LAB) {
                LabScreen(viewModel = viewModel)
            }
            composable(ROUTE_HISTORY) {
                HistoryScreen(viewModel = viewModel)
            }
            composable(ROUTE_PODCAST) {
                PodcastScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun RodelaBottomNavigation(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentRoute == ROUTE_EXPLORER,
            onClick = {
                if (currentRoute != ROUTE_EXPLORER) {
                    navController.navigate(ROUTE_EXPLORER) {
                        popUpTo(ROUTE_EXPLORER) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            icon = {
                Icon(
                    imageVector = if (currentRoute == ROUTE_EXPLORER) Icons.Filled.Dashboard else Icons.Outlined.Dashboard,
                    contentDescription = "Explorer"
                )
            },
            label = { Text("Explorer") },
            modifier = Modifier.testTag("nav_explorer")
        )
        NavigationBarItem(
            selected = currentRoute == ROUTE_LAB,
            onClick = {
                if (currentRoute != ROUTE_LAB) {
                    navController.navigate(ROUTE_LAB) {
                        popUpTo(ROUTE_EXPLORER) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            icon = {
                Icon(
                    imageVector = if (currentRoute == ROUTE_LAB) Icons.Filled.Science else Icons.Outlined.Science,
                    contentDescription = "Agent Lab"
                )
            },
            label = { Text("Agent Lab") },
            modifier = Modifier.testTag("nav_lab")
        )
        NavigationBarItem(
            selected = currentRoute == ROUTE_HISTORY,
            onClick = {
                if (currentRoute != ROUTE_HISTORY) {
                    navController.navigate(ROUTE_HISTORY) {
                        popUpTo(ROUTE_EXPLORER) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            icon = {
                Icon(
                    imageVector = if (currentRoute == ROUTE_HISTORY) Icons.Filled.Book else Icons.Outlined.Book,
                    contentDescription = "History & Notes"
                )
            },
            label = { Text("Records") },
            modifier = Modifier.testTag("nav_records")
        )
        NavigationBarItem(
            selected = currentRoute == ROUTE_PODCAST,
            onClick = {
                if (currentRoute != ROUTE_PODCAST) {
                    navController.navigate(ROUTE_PODCAST) {
                        popUpTo(ROUTE_EXPLORER) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            icon = {
                Icon(
                    imageVector = if (currentRoute == ROUTE_PODCAST) Icons.Filled.Podcasts else Icons.Outlined.Podcasts,
                    contentDescription = "Podcast Player"
                )
            },
            label = { Text("Podcast") },
            modifier = Modifier.testTag("nav_podcast")
        )
    }
}

// ==========================================
// SCREEN 1: EXPLORER (DASHBOARD)
// ==========================================
@Composable
fun ExplorerScreen(viewModel: RodelaViewModel) {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
    ) {
        // Hero Image Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_hero_banner),
                contentDescription = "Medical AI Banner",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f)),
                            startY = 200f
                        )
                    )
            )
            
            // App Title Overlay
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = "RODELA",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "Health Care Engine v2",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Research & Diagnostic Companion",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Author Card / Introduction
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Engine Info",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Autonomous Clinical AI",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Developed by engineer Palash Md. Kamal Uddin at Arshan Solution, Rodela v2 marks a critical shift from mere clinical 'decision support' to autonomous, zero-shot diagnostics. Configured under strict pharmacological safety standards, it aims to eliminate generic chatbot hallucinations and deliver traceable, expert-level clinical outputs.",
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Core Concepts Section (Horizontal Scroll)
        Text(
            text = "Core Architecture Concepts",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                ConceptCard(
                    title = "Zero-Shot Reasoning",
                    icon = Icons.Default.Bolt,
                    desc = "Leverages deep medical foundation models holding broad baseline knowledge to diagnose novel conditions and predict outcomes directly out of the box, bypasssing the need for large task-specific training data."
                )
            }
            item {
                ConceptCard(
                    title = "Autonomous Triage",
                    icon = Icons.Default.Sync,
                    desc = "Moves beyond passive assistance. Designed to autonomously categorize disease severity (like FDA-cleared IDx-DR eye screening), alerting clinicians immediately to critical risks."
                )
            }
            item {
                ConceptCard(
                    title = "Modular Agent Mesh",
                    icon = Icons.Default.Build,
                    desc = "Rather than a single 'black box', Rodela divides tasks into specialized agents: Perception, Zero-Shot Reasoning, Pharmacological Safety, and Explainable Conclusion synthesis."
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Clinical Applications (Vertical Cards)
        Text(
            text = "Strategic Clinical Impact",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))

        ClinicalAppItem(
            title = "Early Screenings (e.g., Retina & Derm)",
            details = "Studies show autonomous diagnostics identifying diabetic retinopathy with 87% sensitivity and melanoma with 99.8% precision, optimizing care before disease progress.",
            icon = Icons.Default.Healing
        )
        ClinicalAppItem(
            title = "Hospital Triage Optimization",
            details = "Reduces critical latencies between historical archives and real-time medical workflows, automatically querying notes/labs to flag sepsis or deterioration.",
            icon = Icons.Default.Speed
        )
        ClinicalAppItem(
            title = "Resource Allocation & Efficiency",
            details = "Integrates with EHR systems to forecast bed occupancy, manage staffing schedules, and predict supply needs, lowering overhead through 280-fold lower compute costs.",
            icon = Icons.Default.TrendingUp
        )
        ClinicalAppItem(
            title = "Clinician & Patient Trust Shield",
            details = "Designed with native explainability. Every single diagnostic result is mapped directly back to parsed EHR symptom vectors, avoiding clinical distrust.",
            icon = Icons.Default.Shield
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Comparison Table
        Text(
            text = "Comparative Paradigm",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Table Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f))
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Metric",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Traditional DSS",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1.2f),
                        color = Color.Gray
                    )
                    Text(
                        text = "Rodela v2 Engine",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1.5f),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Divider()
                
                // Row 1
                ComparisonRow(
                    metric = "Data Needs",
                    traditional = "Massive, curated labeled datasets required.",
                    rodela = "Zero-shot foundation models (baseline clinical maps)."
                )
                Divider()
                
                // Row 2
                ComparisonRow(
                    metric = "Action Level",
                    traditional = "Supports clinicians; no direct output.",
                    rodela = "Generates autonomous diagnostics & triage."
                )
                Divider()
                
                // Row 3
                ComparisonRow(
                    metric = "Architecture",
                    traditional = "Monolithic, black-box networks.",
                    rodela = "Modular agents (perception, reasoning, safety)."
                )
                Divider()
                
                // Row 4
                ComparisonRow(
                    metric = "Traceability",
                    traditional = "Opaque reasoning, high hallucination.",
                    rodela = "Inherent clinical guidelines alignment."
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
fun ConceptCard(title: String, icon: ImageVector, desc: String) {
    Card(
        modifier = Modifier
            .width(260.dp)
            .height(200.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun ClinicalAppItem(title: String, details: String, icon: ImageVector) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = details,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun ComparisonRow(metric: String, traditional: String, rodela: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 8.dp)
    ) {
        Text(
            text = metric,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = traditional,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1.2f),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
        )
        Text(
            text = rodela,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1.5f),
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}


// ==========================================
// SCREEN 2: AGENT DIAGNOSTIC LAB (SIMULATOR)
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabScreen(viewModel: RodelaViewModel) {
    val patientName by viewModel.patientName.collectAsStateWithLifecycle()
    val clinicalNotes by viewModel.clinicalNotes.collectAsStateWithLifecycle()
    val selectedCondition by viewModel.selectedCondition.collectAsStateWithLifecycle()
    val simulationState by viewModel.simulationState.collectAsStateWithLifecycle()

    val conditions = listOf(
        "Diabetic Retinopathy Screen",
        "Melanoma Skin Lesion Screening",
        "Acute Sepsis Hospital Triage",
        "Cardiovascular Emergency (STEMI)"
    )

    var showDropdown by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Lab Screen Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Science,
                contentDescription = "Diagnostic Lab",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Agent Diagnostic Lab",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Autonomous, Zero-shot Engine Simulator",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Case Template Selector
        ExposedDropdownMenuBox(
            expanded = showDropdown,
            onExpandedChange = { showDropdown = !showDropdown }
        ) {
            OutlinedTextField(
                value = selectedCondition,
                onValueChange = {},
                readOnly = true,
                label = { Text("Case Focus Template") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showDropdown) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = showDropdown,
                onDismissRequest = { showDropdown = false }
            ) {
                conditions.forEach { condition ->
                    DropdownMenuItem(
                        text = { Text(condition) },
                        onClick = {
                            viewModel.updateSelectedCondition(condition)
                            showDropdown = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Patient Name Input
        OutlinedTextField(
            value = patientName,
            onValueChange = { viewModel.updatePatientName(it) },
            label = { Text("Patient Identifier Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // EHR Clinical notes
        OutlinedTextField(
            value = clinicalNotes,
            onValueChange = { viewModel.updateClinicalNotes(it) },
            label = { Text("EHR / Clinical Notes Summary") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            maxLines = 8
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Actions: Run Engine & Reset
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { viewModel.runDiagnosticSimulation() },
                enabled = !simulationState.isRunning,
                modifier = Modifier
                    .weight(1.5f)
                    .height(48.dp)
                    .testTag("run_diagnostic_button"),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                if (simulationState.isRunning) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Analyze Case", fontWeight = FontWeight.Bold)
                    }
                }
            }

            OutlinedButton(
                onClick = { viewModel.clearSimulation() },
                enabled = !simulationState.isRunning,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text("Reset")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // API Key Security Notice
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "If GEMINI_API_KEY is configured in AI Studio Secrets, actual zero-shot models generate diagnoses. Otherwise, high-fidelity local clinical mapping simulates.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                )
            }
        }

        // Diagnostic Agents Progress Indicator
        if (simulationState.isRunning || simulationState.progressPercent > 0) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Rodela Autonomous Pipeline State",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Current State: " + simulationState.currentAgent,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { simulationState.progressPercent },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${(simulationState.progressPercent * 100).toInt()}% Pipeline Completed",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.align(Alignment.End),
                        color = Color.Gray
                    )
                }
            }
        }

        // Multi-Agent Pipeline Outputs
        AnimatedVisibility(
            visible = simulationState.perceptionOutput.isNotEmpty(),
            enter = fadeIn() + expandVertically()
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Column {
                Text(
                    text = "Active Clinical Agents Output",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))

                // AGENT 1: Perception
                AgentResultCard(
                    agentName = "Perception Agent (Signal Extraction)",
                    icon = Icons.Default.Visibility,
                    accentColor = MaterialTheme.colorScheme.primary,
                    content = simulationState.perceptionOutput
                )

                // AGENT 2: Reasoning (Zero-Shot)
                if (simulationState.reasoningOutput.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    AgentResultCard(
                        agentName = "Reasoning Agent (Zero-Shot Medical Synthesis)",
                        icon = Icons.Default.Psychology,
                        accentColor = MaterialTheme.colorScheme.secondary,
                        content = simulationState.reasoningOutput
                    )
                }

                // AGENT 3: Safety (Pharmacological Guardrails)
                if (simulationState.safetyCheck.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    AgentResultCard(
                        agentName = "Safety Agent (Pharmacological Guardrails)",
                        icon = Icons.Default.Warning,
                        accentColor = Color(0xFFE11D48), // Urgent Rose
                        content = simulationState.safetyCheck
                    )
                }

                // AGENT 4: Conclusion (Traceable Output)
                if (simulationState.conclusion.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    AgentResultCard(
                        agentName = "Autonomous Diagnostic Conclusion",
                        icon = Icons.Default.CheckCircle,
                        accentColor = MaterialTheme.colorScheme.primary,
                        content = simulationState.conclusion,
                        isHighlight = true
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
fun AgentResultCard(
    agentName: String,
    icon: ImageVector,
    accentColor: Color,
    content: String,
    isHighlight: Boolean = false
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isHighlight) accentColor.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isHighlight) 4.dp else 1.dp),
        border = BorderStroke(
            width = if (isHighlight) 2.dp else 1.dp,
            color = accentColor.copy(alpha = if (isHighlight) 0.6f else 0.2f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = agentName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 22.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}


// ==========================================
// SCREEN 3: CASE HISTORY & RECORDS
// ==========================================
@Composable
fun HistoryScreen(viewModel: RodelaViewModel) {
    val notes by viewModel.notes.collectAsStateWithLifecycle()
    val savedSimulations by viewModel.savedSimulations.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableStateOf(0) } // 0 = Simulations, 1 = Research Insights
    
    var showNoteDialog by remember { mutableStateOf(false) }
    var noteTitle by remember { mutableStateOf("") }
    var noteContent by remember { mutableStateOf("") }
    var noteCategory by remember { mutableStateOf("Concept") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Tab Headers
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Simulation History (${savedSimulations.size})", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Research Notes (${notes.size})", fontWeight = FontWeight.Bold) }
            )
        }

        if (selectedTab == 0) {
            // Simulations Tab
            if (savedSimulations.isEmpty()) {
                EmptyStateView(
                    title = "No Simulated Clinical Cases",
                    description = "Run diagnostic pipelines in the 'Agent Lab' tab to automatically persist diagnostic traces here.",
                    icon = Icons.Default.Science
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(savedSimulations) { simulation ->
                        SimulationHistoryItem(
                            simulation = simulation,
                            onDelete = { viewModel.deleteSimulation(simulation.id) }
                        )
                    }
                }
            }
        } else {
            // Research Notes Tab
            Box(modifier = Modifier.fillMaxSize()) {
                if (notes.isEmpty()) {
                    EmptyStateView(
                        title = "No Research Notes Saved",
                        description = "Log your custom thoughts, clinician feedback, or theoretical safety benchmarks below.",
                        icon = Icons.Default.EditNote
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(notes) { note ->
                            NoteHistoryItem(
                                note = note,
                                onDelete = { viewModel.deleteNote(note) }
                            )
                        }
                        item { Spacer(modifier = Modifier.height(72.dp)) }
                    }
                }

                // Add Floating Action Button for adding note
                FloatingActionButton(
                    onClick = {
                        noteTitle = ""
                        noteContent = ""
                        noteCategory = "Concept"
                        showNoteDialog = true
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Note", tint = Color.White)
                }
            }
        }
    }

    // New Note Dialog
    if (showNoteDialog) {
        AlertDialog(
            onDismissRequest = { showNoteDialog = false },
            title = { Text("Log Clinical Insight", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = noteTitle,
                        onValueChange = { noteTitle = it },
                        label = { Text("Topic/Title") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Simple Category Selector
                    Text("Category", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Concept", "Safety", "Regulatory").forEach { cat ->
                            FilterChip(
                                selected = noteCategory == cat,
                                onClick = { noteCategory = cat },
                                label = { Text(cat) }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = noteContent,
                        onValueChange = { noteContent = it },
                        label = { Text("Notes/Feedback content") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        maxLines = 6
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (noteTitle.isNotBlank() && noteContent.isNotBlank()) {
                            viewModel.addNote(noteTitle, noteContent, noteCategory)
                            showNoteDialog = false
                        }
                    }
                ) {
                    Text("Save Note")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNoteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SimulationHistoryItem(simulation: SimulationResult, onDelete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Patient: " + simulation.patientName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Focus: " + simulation.selectedCondition,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Simulation", tint = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            
            // Preview of conclusion
            Text(
                text = simulation.conclusion.take(120) + if (simulation.conclusion.length > 120) "..." else "",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
            )

            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Patient Records / Inputs:", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Text(simulation.clinicalSummary, style = MaterialTheme.typography.bodyMedium)
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Perception Extraction Trace:", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text(simulation.perceptionOutput, style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Zero-Shot Reasoning Trace:", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                    Text(simulation.reasoningOutput, style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Pharmacological Safeguards Check:", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = Color(0xFFE11D48))
                    Text(simulation.safetyCheck, style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Full Diagnostic Conclusion:", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text(simulation.conclusion, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = java.text.SimpleDateFormat("MMM dd, yyyy - HH:mm").format(java.util.Date(simulation.timestamp)),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
                Text(
                    text = if (expanded) "Tap to collapse" else "Tap to expand details",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun NoteHistoryItem(note: ResearchNote, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = when (note.category) {
                                    "Safety" -> Color(0xFFFFE4E6)
                                    "Regulatory" -> Color(0xFFE0F2FE)
                                    else -> Color(0xFFF1F5F9)
                                },
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = note.category,
                            style = MaterialTheme.typography.labelSmall,
                            color = when (note.category) {
                                "Safety" -> Color(0xFFBE123C)
                                "Regulatory" -> Color(0xFF0369A1)
                                else -> Color(0xFF475569)
                            },
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = note.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Note", tint = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 20.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Logged: " + java.text.SimpleDateFormat("MMM dd, yyyy - HH:mm").format(java.util.Date(note.timestamp)),
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun EmptyStateView(title: String, description: String, icon: ImageVector) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
            modifier = Modifier.size(72.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
    }
}


// ==========================================
// SCREEN 4: INNOVATION PODCAST (AUDIO COMPANION)
// ==========================================
@Composable
fun PodcastScreen(viewModel: RodelaViewModel) {
    val currentChapterIndex by viewModel.currentChapterIndex.collectAsStateWithLifecycle()
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
    val progressSeconds by viewModel.playbackProgressSeconds.collectAsStateWithLifecycle()

    val currentChapter = viewModel.chapters[currentChapterIndex]
    
    val formatTime = { secs: Int ->
        val m = secs / 60
        val s = secs % 60
        String.format("%02d:%02d", m, s)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Podcast Screen Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Podcasts,
                contentDescription = "Podcast Companion",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Innovation Briefings",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Clinical Audio Seminars",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Large Player Card
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Cassette / Waveform representation
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)),
                    contentAlignment = Alignment.Center
                ) {
                    // Custom Waveform Animation
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in 0..15) {
                            val waveHeight = if (isPlaying) {
                                remember { (10..80).random() }.dp
                            } else {
                                12.dp
                            }
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(waveHeight)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(
                                        if (isPlaying) MaterialTheme.colorScheme.primary else Color.Gray.copy(
                                            alpha = 0.5f
                                        )
                                    )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Track Info
                Text(
                    text = currentChapter.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Lead: " + currentChapter.speaker,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Progress Slider / Line
                Column(modifier = Modifier.fillMaxWidth()) {
                    val progressRatio = progressSeconds.toFloat() / currentChapter.durationSeconds.toFloat()
                    LinearProgressIndicator(
                        progress = { progressRatio },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = formatTime(progressSeconds), style = MaterialTheme.typography.labelSmall)
                        Text(text = formatTime(currentChapter.durationSeconds), style = MaterialTheme.typography.labelSmall)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Playback controls
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    IconButton(onClick = { viewModel.skipBackward() }) {
                        Icon(
                            imageVector = Icons.Default.SkipPrevious,
                            contentDescription = "Rewind 10 seconds",
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(32.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable { viewModel.togglePlayPause() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    IconButton(onClick = { viewModel.skipForward() }) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Forward 10 seconds",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Episode / Chapters List
        Text(
            text = "Seminar Chapters",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(viewModel.chapters.size) { index ->
                val ch = viewModel.chapters[index]
                val isSelected = index == currentChapterIndex
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.selectChapter(index) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
                    ),
                    border = if (isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)) else null
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isSelected && isPlaying) Icons.Default.VolumeUp else Icons.Default.PlayCircle,
                            contentDescription = null,
                            tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = ch.title,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = formatTime(ch.durationSeconds),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Real-Time Highlight Transcript
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
                    .verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.TopStart
            ) {
                Text(
                    text = currentChapter.transcript,
                    style = MaterialTheme.typography.bodySmall,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
