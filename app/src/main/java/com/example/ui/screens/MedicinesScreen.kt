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
import com.example.model.Medicine
import com.example.model.MedicineTarja
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicinesScreen(
    initialSelectedMedicine: Medicine? = null,
    onBack: (() -> Unit)? = null
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTarjaFilter by remember { mutableStateOf<MedicineTarja?>(null) }
    var activeMedicineDetail by remember { mutableStateOf(initialSelectedMedicine) }

    val filteredMedicines = remember(searchQuery, selectedTarjaFilter) {
        NexfarRepository.medicinesList.filter { med ->
            val matchesSearch = searchQuery.isBlank() ||
                    med.tradeName.contains(searchQuery, ignoreCase = true) ||
                    med.activeIngredient.contains(searchQuery, ignoreCase = true) ||
                    med.manufacturer.contains(searchQuery, ignoreCase = true) ||
                    med.anvisaRegistry.contains(searchQuery, ignoreCase = true)

            val matchesTarja = selectedTarjaFilter == null || med.tarja == selectedTarjaFilter

            matchesSearch && matchesTarja
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NexfarIceBg)
    ) {
        // Top Search Header
        Surface(
            color = Color.White,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Bulário Eletrônico & Medicamentos",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = NexfarNavy
                )
                Text(
                    text = "Bases integradas: ANVISA • Farmacopeia Brasileira • PMC/CMED",
                    fontSize = 11.sp,
                    color = NexfarTextSecondary
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("medicines_search_input"),
                    placeholder = { Text("Buscar por nome comercial, princípio ativo ou MS...", fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Buscar", tint = NexfarTeal)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Limpar")
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        color = NexfarNavy,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = NexfarNavy,
                        unfocusedTextColor = NexfarNavy,
                        focusedBorderColor = NexfarTeal,
                        unfocusedBorderColor = NexfarCardBorder,
                        focusedContainerColor = NexfarIceBg,
                        unfocusedContainerColor = NexfarIceBg,
                        cursorColor = NexfarTeal
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Regulatory Tarja Filter Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedTarjaFilter == null,
                            onClick = { selectedTarjaFilter = null },
                            label = { Text("Todas", fontSize = 11.sp) }
                        )
                    }
                    items(MedicineTarja.values()) { tarja ->
                        FilterChip(
                            selected = selectedTarjaFilter == tarja,
                            onClick = {
                                selectedTarjaFilter = if (selectedTarjaFilter == tarja) null else tarja
                            },
                            label = { Text(tarja.label, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(tarja.colorHex).copy(alpha = 0.2f),
                                selectedLabelColor = Color(tarja.colorHex)
                            )
                        )
                    }
                }
            }
        }

        // Medicines List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredMedicines) { med ->
                MedicineCardItem(medicine = med, onClick = { activeMedicineDetail = med })
            }

            if (filteredMedicines.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = "Nenhum resultado",
                            tint = NexfarTextMuted,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Nenhum medicamento encontrado para \"$searchQuery\"",
                            fontSize = 13.sp,
                            color = NexfarTextSecondary
                        )
                    }
                }
            }
        }
    }

    // Modal Bottom Sheet with Complete ANVISA Drug Monograph
    activeMedicineDetail?.let { med ->
        ModalBottomSheet(
            onDismissRequest = { activeMedicineDetail = null },
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            MedicineDetailSheetContent(medicine = med, onClose = { activeMedicineDetail = null })
        }
    }
}

@Composable
fun MedicineCardItem(
    medicine: Medicine,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("medicine_item_${medicine.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(medicine.tarja.colorHex).copy(alpha = 0.15f)
                ) {
                    Text(
                        text = medicine.tarja.label,
                        color = Color(medicine.tarja.colorHex),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                if (medicine.isGeneric) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFFEF3C7)
                    ) {
                        Text(
                            text = "GENÉRICO LEI 9.787",
                            color = Color(0xFFB45309),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = medicine.tradeName,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = NexfarNavy
            )

            Text(
                text = "${medicine.activeIngredient} • ${medicine.concentration}",
                fontSize = 12.sp,
                color = NexfarTextSecondary
            )

            Text(
                text = "${medicine.dosageForm} • ${medicine.manufacturer}",
                fontSize = 11.sp,
                color = NexfarTextMuted
            )

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = NexfarCardBorder)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Registro ANVISA:", fontSize = 9.sp, color = NexfarTextMuted)
                    Text(text = medicine.anvisaRegistry, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = NexfarNavy)
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "PMC Máximo:", fontSize = 9.sp, color = NexfarTextMuted)
                    Text(
                        text = "R$ ${String.format("%.2f", medicine.pmcPrice)}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = NexfarTealDark
                    )
                }
            }
        }
    }
}

@Composable
fun MedicineDetailSheetContent(
    medicine: Medicine,
    onClose: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf("Geral & Registro", "Posologia", "Interações", "Advertências")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
    ) {
        // Header
        Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = medicine.tradeName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = NexfarNavy
                    )
                    Text(
                        text = medicine.activeIngredient,
                        fontSize = 13.sp,
                        color = NexfarTealDark,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${medicine.concentration} • ${medicine.dosageForm}",
                        fontSize = 12.sp,
                        color = NexfarTextSecondary
                    )
                }

                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Fechar")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = NexfarNavy
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title, fontSize = 11.sp, fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal) }
                    )
                }
            }
        }

        HorizontalDivider(color = NexfarCardBorder)

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 380.dp)
                .padding(horizontal = 18.dp, vertical = 12.dp)
        ) {
            when (selectedTab) {
                0 -> {
                    item {
                        DetailSectionItem(label = "Fabricante / Titular do Registro", value = medicine.manufacturer)
                        DetailSectionItem(label = "Número do Registro MS / ANVISA", value = medicine.anvisaRegistry)
                        DetailSectionItem(label = "Classificação Regulatória", value = medicine.tarja.label)
                        DetailSectionItem(label = "Preço Máximo ao Consumidor (PMC)", value = "R$ ${String.format("%.2f", medicine.pmcPrice)}")
                        DetailSectionItem(label = "Fonte Oficial de Dados", value = medicine.anvisaSource)
                        DetailSectionItem(label = "Última Atualização da Monografia", value = medicine.lastUpdated)

                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Indicações Aprovadas em Bula:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NexfarNavy)
                        medicine.approvedIndications.forEach { ind ->
                            Text("• $ind", fontSize = 11.sp, color = NexfarTextPrimary, modifier = Modifier.padding(top = 2.dp))
                        }
                    }
                }
                1 -> {
                    item {
                        Text("Posologia Oficial Aprovada:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NexfarNavy)
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = NexfarIceBg,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = medicine.posologyApproved,
                                fontSize = 12.sp,
                                color = NexfarTextPrimary,
                                modifier = Modifier.padding(10.dp),
                                lineHeight = 18.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Reações Adversas Frequentes:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NexfarNavy)
                        medicine.adverseReactions.forEach { rx ->
                            Text("• $rx", fontSize = 11.sp, color = NexfarTextPrimary, modifier = Modifier.padding(top = 2.dp))
                        }
                    }
                }
                2 -> {
                    item {
                        Text("Principais Interações Medicamentosas:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NexfarNavy)
                        Spacer(modifier = Modifier.height(6.dp))
                        medicine.majorInteractions.forEach { interaction ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFFEF2F2),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(modifier = Modifier.padding(8.dp)) {
                                    Icon(Icons.Default.Warning, contentDescription = null, tint = NexfarTarjaVermelha, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(interaction, fontSize = 11.sp, color = Color(0xFF991B1B))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Contraindicações Estritas:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NexfarNavy)
                        medicine.contraindications.forEach { ci ->
                            Text("• $ci", fontSize = 11.sp, color = NexfarTextPrimary, modifier = Modifier.padding(top = 2.dp))
                        }
                    }
                }
                3 -> {
                    item {
                        Text("Cuidados & Avisos Regulatórios ANVISA:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NexfarNavy)
                        Spacer(modifier = Modifier.height(6.dp))
                        medicine.clinicalWarnings.forEach { warning ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFFFFBEB),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(modifier = Modifier.padding(8.dp)) {
                                    Icon(Icons.Default.Info, contentDescription = null, tint = NexfarWarning, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(warning, fontSize = 11.sp, color = Color(0xFF92400E))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailSectionItem(label: String, value: String) {
    Column(modifier = Modifier.padding(bottom = 6.dp)) {
        Text(text = label, fontSize = 10.sp, color = NexfarTextMuted, fontWeight = FontWeight.Medium)
        Text(text = value, fontSize = 12.sp, color = NexfarNavy, fontWeight = FontWeight.SemiBold)
    }
}
