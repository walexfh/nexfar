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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.NexfarRepository
import com.example.model.Pathology
import com.example.ui.theme.*

@Composable
fun PathologiesScreen(
    modifier: Modifier = Modifier
) {
    var selectedPathology by remember { mutableStateOf(NexfarRepository.pathologiesList.first()) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NexfarIceBg),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Header
        item {
            Surface(
                color = NexfarNavy,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "CONDUTA CLÍNICA & TRIAGEM",
                        color = NexfarTealAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Patologias & Mapas Mentais",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Fluxogramas visuais para triagem no balcão e identificação de sinais de alarme.",
                        color = NexfarTextMuted,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Pathology Selector Carousel
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentPadding = PaddingValues(horizontal = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(NexfarRepository.pathologiesList) { pat ->
                    val isSelected = pat.id == selectedPathology.id
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) NexfarNavy else Color.White,
                        border = if (isSelected) null else CardDefaults.outlinedCardBorder(),
                        modifier = Modifier
                            .testTag("pat_${pat.id}")
                            .clickable { selectedPathology = pat }
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                            Text(
                                text = pat.name,
                                color = if (isSelected) Color.White else NexfarNavy,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = pat.icdCode,
                                color = if (isSelected) NexfarTealAccent else NexfarTextSecondary,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }

        // Selected Pathology Overview Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedPathology.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = NexfarNavy
                        )
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = NexfarTealLight
                        ) {
                            Text(
                                text = selectedPathology.icdCode,
                                color = NexfarTealDark,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = selectedPathology.shortSummary,
                        fontSize = 12.sp,
                        color = NexfarTextSecondary,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = NexfarCardBorder)
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Sintomas mais Comuns:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NexfarNavy)
                    selectedPathology.symptoms.forEach { s ->
                        Text("• $s", fontSize = 11.sp, color = NexfarTextPrimary, modifier = Modifier.padding(top = 2.dp))
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Tratamento de 1ª Linha Recomendado:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NexfarNavy)
                    selectedPathology.firstLineTreatments.forEach { t ->
                        Text("• $t", fontSize = 11.sp, color = NexfarTextPrimary, modifier = Modifier.padding(top = 2.dp))
                    }
                }
            }
        }

        // Visual Mental Map
        item {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccountTree, contentDescription = null, tint = NexfarTeal, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "MAPA MENTAL DE CONDUTA NO BALCÃO",
                        color = NexfarNavy,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                selectedPathology.mentalMap.forEachIndexed { index, step ->
                    MentalMapNodeView(
                        step = step,
                        isLast = index == selectedPathology.mentalMap.size - 1
                    )
                }
            }
        }

        // Red Flags & Urgent Referral Box
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Dangerous, contentDescription = null, tint = NexfarTarjaVermelha, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Sinais de Alerta Vermelhos (Red Flags):",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF991B1B)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    selectedPathology.redFlags.forEach { flag ->
                        Text(
                            text = "⚠ $flag",
                            fontSize = 11.sp,
                            color = Color(0xFFB91C1C),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = Color(0xFFFECACA))
                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Conduta de Encaminhamento: ${selectedPathology.referralGuidance}",
                        fontSize = 11.sp,
                        color = Color(0xFF7F1D1D),
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun MentalMapNodeView(
    step: com.example.model.MentalMapStep,
    isLast: Boolean
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        // Vertical step connector line
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(36.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(if (step.isRedFlag) NexfarTarjaVermelha else NexfarNavy),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${step.stepNumber}",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(36.dp)
                        .background(NexfarCardBorder)
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Node Content Box
        Card(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = if (isLast) 0.dp else 10.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (step.isRedFlag) Color(0xFFFFF1F2) else Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = step.title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (step.isRedFlag) NexfarTarjaVermelha else NexfarNavy
                    )

                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (step.isRedFlag) NexfarTarjaVermelha.copy(alpha = 0.15f) else NexfarTealLight
                    ) {
                        Text(
                            text = step.badgeLabel,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (step.isRedFlag) NexfarTarjaVermelha else NexfarTealDark,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = step.description,
                    fontSize = 11.sp,
                    color = NexfarTextSecondary,
                    lineHeight = 15.sp
                )
            }
        }
    }
}
