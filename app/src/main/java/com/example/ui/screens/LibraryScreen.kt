package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.model.KnowledgeArticle
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var activeArticle by remember { mutableStateOf<KnowledgeArticle?>(null) }

    val categories = listOf("Todas", "Legislação & Regulação", "Farmacologia Clínica", "Atenção Farmacêutica", "Fisiologia")

    val filteredArticles = remember(selectedCategory) {
        if (selectedCategory == null || selectedCategory == "Todas") {
            NexfarRepository.knowledgeArticles
        } else {
            NexfarRepository.knowledgeArticles.filter { it.category == selectedCategory }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NexfarIceBg)
    ) {
        // Top Header
        Surface(
            color = Color.White,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Biblioteca Farmacêutica",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NexfarNavy
                )
                Text(
                    text = "Artigos técnicos, RDCs da ANVISA e condutas farmacoterapêuticas.",
                    fontSize = 12.sp,
                    color = NexfarTextSecondary
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(categories) { cat ->
                        val isSelected = (selectedCategory == null && cat == "Todas") || selectedCategory == cat
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategory = if (cat == "Todas") null else cat },
                            label = { Text(cat, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NexfarTealLight,
                                selectedLabelColor = NexfarTealDark
                            )
                        )
                    }
                }
            }
        }

        // Articles List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredArticles) { article ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { activeArticle = article }
                        .testTag("article_${article.id}"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = NexfarTealLight
                            ) {
                                Text(
                                    text = article.category,
                                    color = NexfarTealDark,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Schedule, contentDescription = null, tint = NexfarTextMuted, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(text = "${article.readTimeMinutes} min", fontSize = 11.sp, color = NexfarTextMuted)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = article.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = NexfarNavy
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = article.summary,
                            fontSize = 12.sp,
                            color = NexfarTextSecondary,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Ler Artigo Completo",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = NexfarTeal
                            )
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = NexfarTeal, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }

    // Modal Sheet for Full Article Reader
    activeArticle?.let { article ->
        ModalBottomSheet(
            onDismissRequest = { activeArticle = null },
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 8.dp)
                    .padding(bottom = 28.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = article.category.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NexfarTeal)
                        Text(text = article.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = NexfarNavy)
                    }
                    IconButton(onClick = { activeArticle = null }) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = NexfarCardBorder)
                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(modifier = Modifier.heightIn(max = 420.dp)) {
                    item {
                        Text(
                            text = article.fullContent,
                            fontSize = 13.sp,
                            color = NexfarTextPrimary,
                            lineHeight = 19.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                        Text("Pontos Críticos:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NexfarNavy)
                        article.keyPoints.forEach { pt ->
                            Row(modifier = Modifier.padding(top = 4.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NexfarTeal, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(pt, fontSize = 12.sp, color = NexfarTextPrimary)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Text("Referências Técnicas:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = NexfarTextMuted)
                        article.references.forEach { ref ->
                            Text("• $ref", fontSize = 10.sp, color = NexfarTextMuted)
                        }
                    }
                }
            }
        }
    }
}
