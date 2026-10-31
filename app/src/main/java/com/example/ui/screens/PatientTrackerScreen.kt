package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.NexfarRepository
import com.example.model.PatientRecord
import com.example.ui.theme.*
import com.example.ui.components.NexfarInputField
import com.example.ui.components.nexfarInputColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientTrackerScreen(
    modifier: Modifier = Modifier
) {
    var patientsList by remember { mutableStateOf(NexfarRepository.demoPatients.toList()) }
    var showNewPatientDialog by remember { mutableStateOf(false) }
    var selectedPatientDetail by remember { mutableStateOf<PatientRecord?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredPatients = remember(searchQuery, patientsList) {
        if (searchQuery.isBlank()) patientsList
        else patientsList.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
            it.continuousMedicines.any { med -> med.contains(searchQuery, ignoreCase = true) }
        }
    }

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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "ATENÇÃO FARMACÊUTICA",
                                color = NexfarTealAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Caderno de Pacientes",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = { showNewPatientDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = NexfarTeal),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("new_patient_button")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Novo Registro", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // LGPD Banner
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = NexfarNavyDark
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = NexfarTealAccent, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "LGPD & CFF: Dados estritamente clínicos para acompanhamento farmacoterapêutico.",
                                color = NexfarTextMuted,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }

        // Search Input
        item {
            Surface(
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                shape = RoundedCornerShape(12.dp),
                tonalElevation = 1.dp
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Buscar paciente ou medicamento em uso...", fontSize = 13.sp, color = NexfarTextMuted) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NexfarTeal) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = nexfarInputColors(containerColor = Color.White),
                    singleLine = true
                )
            }
        }

        // Patients List
        items(filteredPatients) { patient ->
            val imc = if (patient.heightCm > 0) patient.weightKg / Math.pow(patient.heightCm / 100.0, 2.0) else 0.0

            val bpCategory = when {
                patient.systolicBP < 120 && patient.diastolicBP < 80 -> "Ótima"
                patient.systolicBP <= 129 && patient.diastolicBP <= 84 -> "Normal"
                patient.systolicBP <= 139 || patient.diastolicBP <= 89 -> "Pré-Hipertensão"
                patient.systolicBP <= 159 || patient.diastolicBP <= 99 -> "HAS Estágio 1"
                else -> "HAS Estágio 2/3 (Alerta)"
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 5.dp)
                    .clickable { selectedPatientDetail = patient }
                    .testTag("patient_card_${patient.id}"),
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(NexfarTealLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = patient.name.take(2).uppercase(),
                                    color = NexfarTealDark,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(text = patient.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = NexfarNavy)
                                Text(text = "${patient.age} anos • Última consulta: ${patient.lastVisitDate}", fontSize = 11.sp, color = NexfarTextSecondary)
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (patient.systolicBP >= 140) Color(0xFFFEE2E2) else Color(0xFFDCFCE7)
                        ) {
                            Text(
                                text = bpCategory,
                                color = if (patient.systolicBP >= 140) NexfarTarjaVermelha else Color(0xFF166534),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Clinical Measurements Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ClinicalMetricBadge(
                            label = "Pressão Arterial",
                            value = "${patient.systolicBP}/${patient.diastolicBP} mmHg",
                            isWarning = patient.systolicBP >= 140
                        )
                        ClinicalMetricBadge(
                            label = "Glicemia Capilar",
                            value = "${patient.bloodGlucose} mg/dL",
                            isWarning = patient.bloodGlucose >= 140
                        )
                        ClinicalMetricBadge(
                            label = "IMC Calculado",
                            value = "${String.format("%.1f", imc)} kg/m²",
                            isWarning = imc >= 30.0
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Medicamentos em uso contínuo:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NexfarTextSecondary
                    )
                    patient.continuousMedicines.forEach { med ->
                        Text("• $med", fontSize = 11.sp, color = NexfarTextPrimary, modifier = Modifier.padding(top = 2.dp))
                    }
                }
            }
        }
    }

    // Modal to Add New Patient
    if (showNewPatientDialog) {
        var newName by remember { mutableStateOf("") }
        var newAge by remember { mutableStateOf("") }
        var newSystolic by remember { mutableStateOf("120") }
        var newDiastolic by remember { mutableStateOf("80") }
        var newGlucose by remember { mutableStateOf("95") }
        var newWeight by remember { mutableStateOf("70") }
        var newHeight by remember { mutableStateOf("170") }
        var newMeds by remember { mutableStateOf("") }
        var newNotes by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showNewPatientDialog = false },
            title = { Text("Novo Registro de Acompanhamento", fontWeight = FontWeight.Bold, color = NexfarNavy) },
            text = {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    item {
                        NexfarInputField(
                            value = newName,
                            onValueChange = { newName = it },
                            label = "Nome Completo do Paciente",
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        NexfarInputField(
                            value = newAge,
                            onValueChange = { newAge = it },
                            label = "Idade (Anos)",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            NexfarInputField(
                                value = newSystolic,
                                onValueChange = { newSystolic = it },
                                label = "PA Sistólica",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            NexfarInputField(
                                value = newDiastolic,
                                onValueChange = { newDiastolic = it },
                                label = "PA Diastólica",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        NexfarInputField(
                            value = newGlucose,
                            onValueChange = { newGlucose = it },
                            label = "Glicemia Capilar (mg/dL)",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        NexfarInputField(
                            value = newMeds,
                            onValueChange = { newMeds = it },
                            label = "Medicamentos em Uso (separados por vírgula)",
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        NexfarInputField(
                            value = newNotes,
                            onValueChange = { newNotes = it },
                            label = "Observações Farmacêuticas",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newName.isNotBlank()) {
                            val newRecord = PatientRecord(
                                id = "pat_${System.currentTimeMillis()}",
                                name = newName,
                                age = newAge.toIntOrNull() ?: 50,
                                systolicBP = newSystolic.toIntOrNull() ?: 120,
                                diastolicBP = newDiastolic.toIntOrNull() ?: 80,
                                bloodGlucose = newGlucose.toIntOrNull() ?: 95,
                                weightKg = newWeight.toDoubleOrNull() ?: 70.0,
                                heightCm = newHeight.toIntOrNull() ?: 170,
                                continuousMedicines = if (newMeds.isNotBlank()) newMeds.split(",").map { it.trim() } else listOf("Em avaliação"),
                                notes = newNotes.ifBlank { "Atendimento de rotina realizado no balcão da farmácia." }
                            )
                            patientsList = listOf(newRecord) + patientsList
                            showNewPatientDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NexfarNavy)
                ) {
                    Text("Salvar Registro")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewPatientDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun ClinicalMetricBadge(label: String, value: String, isWarning: Boolean) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isWarning) Color(0xFFFEF2F2) else NexfarIceBg,
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isWarning) Color(0xFFFECACA) else NexfarCardBorder)
    ) {
        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
            Text(text = label, fontSize = 9.sp, color = NexfarTextMuted)
            Text(
                text = value,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isWarning) NexfarTarjaVermelha else NexfarNavy
            )
        }
    }
}
