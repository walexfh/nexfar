package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.NexfarRepository
import com.example.model.Medicine
import com.example.model.Organization
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    currentOrganization: Organization,
    onNavigateToAssistant: () -> Unit,
    onNavigateToMedicines: () -> Unit,
    onNavigateToCalculators: () -> Unit,
    onNavigateToLibrary: () -> Unit,
    onNavigateToPathologies: () -> Unit,
    onNavigateToQuiz: () -> Unit,
    onNavigateToPatientTracker: () -> Unit,
    onNavigateToPlans: () -> Unit,
    onSelectMedicine: (Medicine) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(NexfarIceBg),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Hero Greeting Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(NexfarNavy, NexfarNavyLight)
                        )
                    )
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = if (NexfarRepository.currentUser.name.isNotBlank() && NexfarRepository.isUserLoggedIn) 
                                    "BOM DIA, ${NexfarRepository.currentUser.name.uppercase()}" 
                                else 
                                    "BOM DIA, PROFISSIONAL DE SAÚDE",
                                color = NexfarTealAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Inteligência Farmacêutica",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${currentOrganization.name} • Sistema Conectado",
                                color = NexfarTextMuted,
                                fontSize = 12.sp
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = NexfarTeal.copy(alpha = 0.2f),
                            contentColor = NexfarTealAccent
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(NexfarSuccess)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "ANVISA Sinc.",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Universal Search Input
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("dashboard_search_input"),
                        placeholder = {
                            Text(
                                text = "Buscar medicamento, bula, interação ou cálculo...",
                                fontSize = 13.sp,
                                color = NexfarTextMuted
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Pesquisar",
                                tint = NexfarTealAccent
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Limpar",
                                        tint = Color.White
                                    )
                                }
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NexfarTeal,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            focusedContainerColor = NexfarNavyDark,
                            unfocusedContainerColor = NexfarNavyDark.copy(alpha = 0.7f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = NexfarTealAccent
                        ),
                        singleLine = true
                    )
                }
            }
        }

        // AI Assistant Featured Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .testTag("featured_ai_card")
                    .clickable { onNavigateToAssistant() },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NexfarNavyDark),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(NexfarTeal),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "IA Farmacêutica",
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Assistente Farmacêutico IA",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = NexfarTealAccent.copy(alpha = 0.25f)
                            ) {
                                Text(
                                    text = "RAG CLÍNICO",
                                    color = NexfarTealAccent,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Checagem de interações com CYP450, ajustes renais e alertas de bulas ANVISA.",
                            color = NexfarTextMuted,
                            fontSize = 12.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Acessar",
                        tint = NexfarTealAccent
                    )
                }
            }
        }

        // Quick Access Modules Grid
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                Text(
                    text = "FERRAMENTAS PRINCIPAIS",
                    color = NexfarTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickToolCard(
                        modifier = Modifier.weight(1f),
                        title = "Bulário ANVISA",
                        subtitle = "Bulas & Registro MS",
                        icon = Icons.Default.Medication,
                        iconTint = NexfarTeal,
                        bgColor = Color.White,
                        onClick = onNavigateToMedicines
                    )
                    QuickToolCard(
                        modifier = Modifier.weight(1f),
                        title = "Calculadoras",
                        subtitle = "Gotas, Diluição, ClCr",
                        icon = Icons.Default.Calculate,
                        iconTint = Color(0xFF3B82F6),
                        bgColor = Color.White,
                        onClick = onNavigateToCalculators
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickToolCard(
                        modifier = Modifier.weight(1f),
                        title = "Atenção ao Paciente",
                        subtitle = "PA, Glicemia, IMC",
                        icon = Icons.Default.AssignmentInd,
                        iconTint = Color(0xFF8B5CF6),
                        bgColor = Color.White,
                        onClick = onNavigateToPatientTracker
                    )
                    QuickToolCard(
                        modifier = Modifier.weight(1f),
                        title = "Mapas Mentais",
                        subtitle = "HAS, DM2, Asma",
                        icon = Icons.Default.AccountTree,
                        iconTint = Color(0xFFF59E0B),
                        bgColor = Color.White,
                        onClick = onNavigateToPathologies
                    )
                }
            }
        }

        // Recent Medicines Consulted
        item {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "MEDICAMENTOS CONSULTADOS RECENTEMENTE",
                        color = NexfarTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )

                    Text(
                        text = "Ver todos",
                        color = NexfarTeal,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onNavigateToMedicines() }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(NexfarRepository.medicinesList) { med ->
                        Card(
                            modifier = Modifier
                                .width(220.dp)
                                .clickable { onSelectMedicine(med) },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color(med.tarja.colorHex).copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = med.tarja.label,
                                        color = Color(med.tarja.colorHex),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = med.tradeName,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NexfarNavy,
                                    maxLines = 1
                                )

                                Text(
                                    text = med.activeIngredient,
                                    fontSize = 11.sp,
                                    color = NexfarTextSecondary,
                                    maxLines = 1
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "PMC: R$ ${String.format("%.2f", med.pmcPrice)}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NexfarTealDark
                                    )

                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "Detalhes",
                                        tint = NexfarTeal,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Learning & Quiz Progress Section
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
                    .clickable { onNavigateToQuiz() },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NexfarTealLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = "Simulados",
                                    tint = NexfarTealDark,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Simulados & Treinamento",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NexfarNavy
                                )
                                Text(
                                    text = "RDC 20/2011 • Portaria 344 • Dispensação",
                                    fontSize = 11.sp,
                                    color = NexfarTextSecondary
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = NexfarTealLight
                        ) {
                            Text(
                                text = "82% Acertos",
                                color = NexfarTealDark,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { 0.82f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = NexfarTeal,
                        trackColor = NexfarIceBg
                    )
                }
            }
        }

        // SaaS Commercial Plan Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clickable { onNavigateToPlans() },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = NexfarNavyLight)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "PLANO PROFISSIONAL ATIVO",
                            color = NexfarTealAccent,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Conheça o Plano Estabelecimento",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Cadastre múltiplos funcionários e centralize o atendimento.",
                            color = NexfarTextMuted,
                            fontSize = 11.sp
                        )
                    }

                    Button(
                        onClick = onNavigateToPlans,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NexfarTeal)
                    ) {
                        Text("Ver Planos", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun QuickToolCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    bgColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(iconTint.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = NexfarNavy
            )

            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = NexfarTextSecondary,
                maxLines = 1
            )
        }
    }
}
