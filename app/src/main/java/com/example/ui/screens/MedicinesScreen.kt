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
import com.example.model.InterchangeableDrug
import com.example.model.Medicine
import com.example.model.MedicineTarja
import com.example.model.PrescriptionType
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
    var viewMode by remember { mutableStateOf<Int>(0) } // 0 = Bulário, 1 = Tabela de Intercambialidade (Guia Balconista)

    val filteredMedicines = remember(searchQuery, selectedTarjaFilter) {
        NexfarRepository.medicinesList.filter { med ->
            val matchesSearch = searchQuery.isBlank() ||
                    med.tradeName.contains(searchQuery, ignoreCase = true) ||
                    med.activeIngredient.contains(searchQuery, ignoreCase = true) ||
                    med.manufacturer.contains(searchQuery, ignoreCase = true) ||
                    med.anvisaRegistry.contains(searchQuery, ignoreCase = true) ||
                    med.referenceBrand.contains(searchQuery, ignoreCase = true) ||
                    med.genericName.contains(searchQuery, ignoreCase = true) ||
                    med.similarBrands.any { it.contains(searchQuery, ignoreCase = true) }

            val matchesTarja = selectedTarjaFilter == null || med.tarja == selectedTarjaFilter

            matchesSearch && matchesTarja
        }
    }

    val filteredInterchangeable = remember(searchQuery) {
        NexfarRepository.interchangeableDrugsList.filter { item ->
            searchQuery.isBlank() ||
                    item.activeIngredient.contains(searchQuery, ignoreCase = true) ||
                    item.referenceBrand.contains(searchQuery, ignoreCase = true) ||
                    item.genericName.contains(searchQuery, ignoreCase = true) ||
                    item.similarBrands.any { it.contains(searchQuery, ignoreCase = true) } ||
                    item.therapeuticClass.contains(searchQuery, ignoreCase = true)
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Bulário & Intercambialidade ANVISA",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = NexfarNavy
                        )
                        Text(
                            text = "Classificação de Receita • Referência • Genérico • Similar (EQ)",
                            fontSize = 11.sp,
                            color = NexfarTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Toggle View Mode: Bulário vs Tabela Intercambialidade
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(NexfarIceBg)
                        .padding(2.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewMode = 0 },
                        shape = RoundedCornerShape(6.dp),
                        color = if (viewMode == 0) NexfarNavy else Color.Transparent
                    ) {
                        Text(
                            text = "Bulário Oficial",
                            color = if (viewMode == 0) Color.White else NexfarNavy,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 6.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewMode = 1 },
                        shape = RoundedCornerShape(6.dp),
                        color = if (viewMode == 1) NexfarTeal else Color.Transparent
                    ) {
                        Text(
                            text = "Tabela Ético x Genérico x Similar",
                            color = if (viewMode == 1) Color.White else NexfarNavy,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 6.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("medicines_search_input"),
                    placeholder = { Text("Buscar por medicamento, princípio ativo, ético ou similar...", fontSize = 12.sp) },
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

                if (viewMode == 0) {
                    Spacer(modifier = Modifier.height(8.dp))

                    // Regulatory Tarja Filter Chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = selectedTarjaFilter == null,
                                onClick = { selectedTarjaFilter = null },
                                label = { Text("Todas as Tarjas", fontSize = 11.sp) }
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
        }

        // Content Area
        if (viewMode == 0) {
            // VIEW 1: Standard Bulário with Prescription Badges
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
                        EmptySearchResults(searchQuery)
                    }
                }
            }
        } else {
            // VIEW 2: Dedicated Guia do Balconista (Intercambialidade: Ético x Genérico x Similar)
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = NexfarNavyDark,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.School, contentDescription = null, tint = NexfarTealAccent, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Guia do Balconista: A Lei 9.787/99 e RDC 58/2014 autorizam a substituição do Medicamento de Referência pelo Genérico e pelo Similar Equivalente (EQ).",
                                color = Color.White,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }

                items(filteredInterchangeable) { drug ->
                    InterchangeableDrugCard(drug = drug)
                }

                if (filteredInterchangeable.isEmpty()) {
                    item {
                        EmptySearchResults(searchQuery)
                    }
                }
            }
        }
    }

    // Modal Bottom Sheet with Complete ANVISA Drug Monograph
    if (activeMedicineDetail != null) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { activeMedicineDetail = null },
            sheetState = sheetState,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            MedicineDetailSheetContent(
                medicine = activeMedicineDetail!!,
                onClose = { activeMedicineDetail = null }
            )
        }
    }
}

@Composable
fun EmptySearchResults(query: String) {
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
            text = "Nenhum medicamento encontrado para \"$query\"",
            fontSize = 13.sp,
            color = NexfarTextSecondary
        )
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
            // Badges row: Tarja + Tipo de Receita
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
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

                    // Prescription Type Badge (Branca, Azul, Amarela, etc.)
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(medicine.prescriptionType.colorHex)
                    ) {
                        Text(
                            text = medicine.prescriptionType.badgeShort,
                            color = Color(medicine.prescriptionType.textColorHex),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                if (medicine.isGeneric) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFFEF3C7)
                    ) {
                        Text(
                            text = "GENÉRICO",
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

            if (medicine.referenceBrand.isNotBlank() || medicine.similarBrands.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ref: ${medicine.referenceBrand.ifBlank { medicine.tradeName }} | Similar: ${medicine.similarBrands.take(2).joinToString(", ")}",
                    fontSize = 10.sp,
                    color = NexfarTealDark,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = NexfarCardBorder)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Exigência de Receita:", fontSize = 9.sp, color = NexfarTextMuted)
                    Text(text = medicine.prescriptionType.label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = NexfarNavy)
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
fun InterchangeableDrugCard(drug: InterchangeableDrug) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Molecule & Prescription badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = drug.activeIngredient,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = NexfarNavy
                    )
                    Text(
                        text = drug.therapeuticClass,
                        fontSize = 10.sp,
                        color = NexfarTextSecondary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(drug.prescriptionType.colorHex)
                ) {
                    Text(
                        text = drug.prescriptionType.label,
                        color = Color(drug.prescriptionType.textColorHex),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 3-Columns Table: Ético vs Genérico vs Similar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(NexfarIceBg)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Coluna 1: Ético (Referência)
                Column(modifier = Modifier.weight(1f).padding(end = 4.dp)) {
                    Surface(shape = RoundedCornerShape(4.dp), color = NexfarNavy) {
                        Text(
                            text = "ÉTICO (REF)",
                            color = Color.White,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = drug.referenceBrand,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = NexfarNavy
                    )
                }

                // Coluna 2: Genérico
                Column(modifier = Modifier.weight(1f).padding(horizontal = 4.dp)) {
                    Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFF59E0B)) {
                        Text(
                            text = "GENÉRICO (G)",
                            color = Color.White,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = drug.genericName,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF92400E)
                    )
                }

                // Coluna 3: Similar Equivalente (EQ)
                Column(modifier = Modifier.weight(1f).padding(start = 4.dp)) {
                    Surface(shape = RoundedCornerShape(4.dp), color = NexfarTeal) {
                        Text(
                            text = "SIMILAR (EQ)",
                            color = Color.White,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = drug.similarBrands.joinToString(", "),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = NexfarTextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Clinical / Attendant Tip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEFF6FF), RoundedCornerShape(6.dp))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF2563EB), modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = drug.clinicalTip,
                    fontSize = 10.sp,
                    color = Color(0xFF1E40AF),
                    lineHeight = 13.sp
                )
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
    val tabTitles = listOf("Geral & Receita", "Posologia", "Interações", "Intercambialidade")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 30.dp)
    ) {
        // Sheet Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = medicine.tradeName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NexfarNavy
                )
                Text(
                    text = "${medicine.activeIngredient} (${medicine.concentration})",
                    fontSize = 12.sp,
                    color = NexfarTealDark,
                    fontWeight = FontWeight.SemiBold
                )
            }

            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Fechar", tint = NexfarTextSecondary)
            }
        }

        // Prescription Warning Alert
        Surface(
            color = Color(medicine.prescriptionType.colorHex),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = Color(medicine.prescriptionType.textColorHex), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Exigência Regulatória: ${medicine.prescriptionType.label}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = Color(medicine.prescriptionType.textColorHex)
                    )
                    Text(
                        text = "${medicine.prescriptionType.description} • ${medicine.prescriptionType.validityDays}",
                        fontSize = 10.sp,
                        color = Color(medicine.prescriptionType.textColorHex)
                    )
                }
            }
        }

        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
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
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            InfoRow(label = "Fabricante / Laboratório", value = medicine.manufacturer)
                            InfoRow(label = "Registro ANVISA (MS)", value = medicine.anvisaRegistry)
                            InfoRow(label = "Forma Farmacêutica", value = medicine.dosageForm)
                            InfoRow(label = "Classificação de Tarja", value = medicine.tarja.label)
                            InfoRow(label = "Preço Máximo ao Consumidor (PMC)", value = "R$ ${String.format("%.2f", medicine.pmcPrice)}")
                            InfoRow(label = "Regras de Receita", value = "${medicine.prescriptionType.label} (${medicine.prescriptionType.validityDays})")

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Indicações Aprovadas em Bula:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NexfarNavy)
                            medicine.approvedIndications.forEach { ind ->
                                Text(text = "• $ind", fontSize = 11.sp, color = NexfarTextPrimary, modifier = Modifier.padding(start = 6.dp))
                            }
                        }
                    }
                }
                1 -> {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(text = "Posologia Oficial Aprovada pela ANVISA:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NexfarNavy)
                            Text(text = medicine.posologyApproved, fontSize = 12.sp, color = NexfarTextPrimary, lineHeight = 18.sp)

                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = "Contraindicações Absolutas:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NexfarTarjaVermelha)
                            medicine.contraindications.forEach { c ->
                                Text(text = "• $c", fontSize = 11.sp, color = NexfarTextPrimary, modifier = Modifier.padding(start = 6.dp))
                            }
                        }
                    }
                }
                2 -> {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(text = "Interações Medicamentosas de Maior Relevância Clínica:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NexfarNavy)
                            medicine.majorInteractions.forEach { inter ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFFEF2F2),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "⚠ $inter",
                                        fontSize = 11.sp,
                                        color = Color(0xFF991B1B),
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Reações Adversas mais Frequentes:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NexfarNavy)
                            medicine.adverseReactions.forEach { r ->
                                Text(text = "• $r", fontSize = 11.sp, color = NexfarTextPrimary, modifier = Modifier.padding(start = 6.dp))
                            }
                        }
                    }
                }
                3 -> {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(text = "Intercambialidade (Lei 9.787/99 & RDC 58/2014):", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NexfarNavy)

                            Surface(shape = RoundedCornerShape(8.dp), color = NexfarIceBg, modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(text = "• Medicamento de Referência (Ético):", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = NexfarNavy)
                                    Text(text = medicine.referenceBrand.ifBlank { medicine.tradeName }, fontSize = 12.sp, color = NexfarTealDark)

                                    Text(text = "• Genérico Intercambiável:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = NexfarNavy)
                                    Text(text = medicine.genericName.ifBlank { medicine.activeIngredient }, fontSize = 12.sp, color = Color(0xFFB45309))

                                    Text(text = "• Similares Equivalentes (EQ) aprovados:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = NexfarNavy)
                                    Text(text = if (medicine.similarBrands.isNotEmpty()) medicine.similarBrands.joinToString(", ") else "Consulte lista de similares da ANVISA", fontSize = 12.sp, color = NexfarTextPrimary)
                                }
                            }

                            Text(
                                text = "Nota ao Balconista: A substituição do medicamento prescrito pelo genérico ou similar equivalente deve ser realizada pelo farmacêutico, com carimbo e assinatura no verso da receita.",
                                fontSize = 10.sp,
                                color = NexfarTextSecondary,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 11.sp, color = NexfarTextSecondary)
        Text(text = value, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = NexfarNavy)
    }
}
