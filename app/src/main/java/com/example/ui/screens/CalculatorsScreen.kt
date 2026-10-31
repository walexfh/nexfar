package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.model.CalculatorHistoryItem
import com.example.model.CalculatorType
import com.example.ui.theme.*
import com.example.ui.components.NexfarInputField

@Composable
fun CalculatorsScreen(
    modifier: Modifier = Modifier
) {
    var selectedCalc by remember { mutableStateOf(CalculatorType.DROPS_INFUSION) }
    var historyList by remember { mutableStateOf(listOf<CalculatorHistoryItem>()) }

    // State for Drops
    var dropsVolume by remember { mutableStateOf("500") }
    var dropsTimeHours by remember { mutableStateOf("8") }
    var dropsResult by remember { mutableStateOf<Pair<Double, Double>?>(null) }

    // State for Dilution (C1*V1 = C2*V2)
    var dilC1 by remember { mutableStateOf("10") } // e.g. 10%
    var dilV1 by remember { mutableStateOf("50") } // e.g. 50 mL
    var dilC2 by remember { mutableStateOf("2") }  // e.g. 2%
    var dilResultV2 by remember { mutableStateOf<Double?>(null) }

    // State for Pediatric
    var pedWeight by remember { mutableStateOf("18") } // kg
    var pedDoseMgKg by remember { mutableStateOf("50") } // mg/kg/dia
    var pedSyrupConc by remember { mutableStateOf("50") } // 250mg/5mL = 50mg/mL
    var pedTakes by remember { mutableStateOf("3") } // 3x ao dia
    var pedResult by remember { mutableStateOf<Triple<Double, Double, Double>?>(null) }

    // State for Creatinine Clearance
    var clAge by remember { mutableStateOf("65") }
    var clWeight by remember { mutableStateOf("70") }
    var clSerumCreatinine by remember { mutableStateOf("1.4") }
    var clIsFemale by remember { mutableStateOf(false) }
    var clResult by remember { mutableStateOf<Double?>(null) }

    // State for Concentration Converter
    var concInputVal by remember { mutableStateOf("2") } // 2%
    var concResultText by remember { mutableStateOf<String?>(null) }

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
                        text = "CENTRAL DE CÁLCULOS CLÍNICOS",
                        color = NexfarTealAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Calculadoras Farmacêuticas Validadas",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Algoritmos farmacotécnicos com memória de cálculo passo a passo.",
                        color = NexfarTextMuted,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Calculator Selector Tabs
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                contentPadding = PaddingValues(horizontal = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(CalculatorType.values()) { calc ->
                    val isSelected = calc == selectedCalc
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) NexfarNavy else Color.White,
                        tonalElevation = if (isSelected) 4.dp else 1.dp,
                        border = if (isSelected) null else CardDefaults.outlinedCardBorder(),
                        modifier = Modifier
                            .testTag("calc_tab_${calc.name.lowercase()}")
                            .clickable { selectedCalc = calc }
                    ) {
                        Text(
                            text = calc.title,
                            color = if (isSelected) Color.White else NexfarNavy,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // Active Calculator Panel
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(NexfarTealLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Calculate,
                                contentDescription = null,
                                tint = NexfarTealDark,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = selectedCalc.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = NexfarNavy
                            )
                            Text(
                                text = selectedCalc.subtitle,
                                fontSize = 11.sp,
                                color = NexfarTextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Formula Box
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = NexfarIceBg,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Functions, contentDescription = null, tint = NexfarTeal, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Fórmula: ${selectedCalc.formula}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = NexfarNavy
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Dynamic Fields based on calculator
                    when (selectedCalc) {
                        CalculatorType.DROPS_INFUSION -> {
                            DropsCalculatorContent(
                                volume = dropsVolume,
                                onVolumeChange = { dropsVolume = it },
                                time = dropsTimeHours,
                                onTimeChange = { dropsTimeHours = it },
                                result = dropsResult,
                                onCalculate = {
                                    val vol = dropsVolume.toDoubleOrNull() ?: 0.0
                                    val t = dropsTimeHours.toDoubleOrNull() ?: 0.0
                                    val res = NexfarRepository.calculateDropsInfusion(vol, t)
                                    dropsResult = res
                                    historyList = listOf(
                                        CalculatorHistoryItem(
                                            id = "${System.currentTimeMillis()}",
                                            type = CalculatorType.DROPS_INFUSION,
                                            inputsSummary = "Vol: ${vol}mL • Tempo: ${t}h",
                                            result = "${String.format("%.1f", res.first)} gotas/min (${Math.round(res.first)} gts/min)",
                                            timestamp = "Hoje"
                                        )
                                    ) + historyList
                                }
                            )
                        }
                        CalculatorType.DILUTION -> {
                            DilutionCalculatorContent(
                                c1 = dilC1, onC1Change = { dilC1 = it },
                                v1 = dilV1, onV1Change = { dilV1 = it },
                                c2 = dilC2, onC2Change = { dilC2 = it },
                                resultV2 = dilResultV2,
                                onCalculate = {
                                    val c1 = dilC1.toDoubleOrNull() ?: 0.0
                                    val v1 = dilV1.toDoubleOrNull() ?: 0.0
                                    val c2 = dilC2.toDoubleOrNull() ?: 0.0
                                    val res = NexfarRepository.calculateDilution(c1, v1, c2, 0.0)
                                    dilResultV2 = res
                                    if (res != null) {
                                        historyList = listOf(
                                            CalculatorHistoryItem(
                                                id = "${System.currentTimeMillis()}",
                                                type = CalculatorType.DILUTION,
                                                inputsSummary = "C1: $c1% • V1: ${v1}mL -> C2: $c2%",
                                                result = "Volume Final (V2): ${String.format("%.1f", res)} mL",
                                                timestamp = "Hoje"
                                            )
                                        ) + historyList
                                    }
                                }
                            )
                        }
                        CalculatorType.PEDIATRIC_DOSE -> {
                            PediatricCalculatorContent(
                                weight = pedWeight, onWeightChange = { pedWeight = it },
                                doseMgKg = pedDoseMgKg, onDoseMgKgChange = { pedDoseMgKg = it },
                                syrupConc = pedSyrupConc, onSyrupConcChange = { pedSyrupConc = it },
                                takes = pedTakes, onTakesChange = { pedTakes = it },
                                result = pedResult,
                                onCalculate = {
                                    val w = pedWeight.toDoubleOrNull() ?: 0.0
                                    val d = pedDoseMgKg.toDoubleOrNull() ?: 0.0
                                    val c = pedSyrupConc.toDoubleOrNull() ?: 0.0
                                    val t = pedTakes.toIntOrNull() ?: 3
                                    val res = NexfarRepository.calculatePediatricDose(w, d, c, t)
                                    pedResult = res
                                    historyList = listOf(
                                        CalculatorHistoryItem(
                                            id = "${System.currentTimeMillis()}",
                                            type = CalculatorType.PEDIATRIC_DOSE,
                                            inputsSummary = "Peso: ${w}kg • Dose: ${d}mg/kg/dia (${t}x)",
                                            result = "${String.format("%.1f", res.third)} mL por tomada (${String.format("%.1f", res.second)} mg)",
                                            timestamp = "Hoje"
                                        )
                                    ) + historyList
                                }
                            )
                        }
                        CalculatorType.CREATININE_CLEARANCE -> {
                            CreatinineClearanceContent(
                                age = clAge, onAgeChange = { clAge = it },
                                weight = clWeight, onWeightChange = { clWeight = it },
                                serumCreatinine = clSerumCreatinine, onSerumCreatinineChange = { clSerumCreatinine = it },
                                isFemale = clIsFemale, onGenderToggle = { clIsFemale = it },
                                result = clResult,
                                onCalculate = {
                                    val age = clAge.toIntOrNull() ?: 0
                                    val w = clWeight.toDoubleOrNull() ?: 0.0
                                    val cr = clSerumCreatinine.toDoubleOrNull() ?: 0.0
                                    val res = NexfarRepository.calculateCreatinineClearance(age, w, cr, clIsFemale)
                                    clResult = res
                                    historyList = listOf(
                                        CalculatorHistoryItem(
                                            id = "${System.currentTimeMillis()}",
                                            type = CalculatorType.CREATININE_CLEARANCE,
                                            inputsSummary = "Idade: $age • Peso: ${w}kg • Cr: $cr",
                                            result = "ClCr: ${String.format("%.1f", res)} mL/min",
                                            timestamp = "Hoje"
                                        )
                                    ) + historyList
                                }
                            )
                        }
                        CalculatorType.CONCENTRATION_CONVERTER -> {
                            ConcentrationConverterContent(
                                inputVal = concInputVal,
                                onValChange = { concInputVal = it },
                                result = concResultText,
                                onCalculate = {
                                    val p = concInputVal.toDoubleOrNull() ?: 0.0
                                    val mgMl = p * 10.0
                                    val ppm = p * 10000.0
                                    val text = "$p% (m/v) equivale a $mgMl mg/mL e ${String.format("%,.0f", ppm)} ppm."
                                    concResultText = text
                                }
                            )
                        }
                        CalculatorType.UNIT_CONVERTER -> {
                            UnitConverterContent()
                        }
                    }
                }
            }
        }

        // Recent Calculations History Section
        item {
            if (historyList.isNotEmpty()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "HISTÓRICO RECENTE DE CÁLCULOS",
                        color = NexfarTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    historyList.take(5).forEach { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(text = item.type.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NexfarNavy)
                                    Text(text = item.inputsSummary, fontSize = 11.sp, color = NexfarTextSecondary)
                                }
                                Text(text = item.result, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NexfarTealDark)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DropsCalculatorContent(
    volume: String, onVolumeChange: (String) -> Unit,
    time: String, onTimeChange: (String) -> Unit,
    result: Pair<Double, Double>?,
    onCalculate: () -> Unit
) {
    Column {
        NexfarInputField(
            value = volume,
            onValueChange = onVolumeChange,
            label = "Volume Total a Infundir (mL)",
            placeholder = "Ex: 500",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        NexfarInputField(
            value = time,
            onValueChange = onTimeChange,
            label = "Tempo de Infusão (Horas)",
            placeholder = "Ex: 8",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = onCalculate,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("calc_action_drops"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NexfarNavy)
        ) {
            Text("Calcular Gotejamento", fontWeight = FontWeight.Bold)
        }

        result?.let { (macro, micro) ->
            Spacer(modifier = Modifier.height(14.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = NexfarTealLight,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("RESULTADO DO GOTEJAMENTO:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NexfarTealDark)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${Math.round(macro)} gotas / minuto",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = NexfarNavy
                    )
                    Text(
                        text = "Exato: ${String.format("%.2f", macro)} macrogotas/min",
                        fontSize = 12.sp,
                        color = NexfarTextSecondary
                    )
                    HorizontalDivider(color = NexfarTeal.copy(alpha = 0.3f), modifier = Modifier.padding(vertical = 6.dp))
                    Text(
                        text = "Equipo Microgotas: ${Math.round(micro)} microgotas / minuto",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = NexfarNavy
                    )
                }
            }
        }
    }
}

@Composable
fun DilutionCalculatorContent(
    c1: String, onC1Change: (String) -> Unit,
    v1: String, onV1Change: (String) -> Unit,
    c2: String, onC2Change: (String) -> Unit,
    resultV2: Double?,
    onCalculate: () -> Unit
) {
    Column {
        NexfarInputField(
            value = c1, onValueChange = onC1Change,
            label = "Concentração Inicial C1 (%)",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        NexfarInputField(
            value = v1, onValueChange = onV1Change,
            label = "Volume Inicial V1 (mL)",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        NexfarInputField(
            value = c2, onValueChange = onC2Change,
            label = "Concentração Desejada C2 (%)",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = onCalculate,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NexfarNavy)
        ) {
            Text("Calcular Volume Final (V2)", fontWeight = FontWeight.Bold)
        }

        resultV2?.let { v2 ->
            Spacer(modifier = Modifier.height(14.dp))
            val diluentNeeded = v2 - (v1.toDoubleOrNull() ?: 0.0)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = NexfarTealLight,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("VOLUME FINAL CALCULADO:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NexfarTealDark)
                    Text(
                        text = "V2 = ${String.format("%.2f", v2)} mL",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = NexfarNavy
                    )
                    Text(
                        text = "Adicionar ${String.format("%.2f", diluentNeeded)} mL de diluente (água p/ injeção ou soro) aos ${v1}mL iniciais.",
                        fontSize = 12.sp,
                        color = NexfarNavy
                    )
                }
            }
        }
    }
}

@Composable
fun PediatricCalculatorContent(
    weight: String, onWeightChange: (String) -> Unit,
    doseMgKg: String, onDoseMgKgChange: (String) -> Unit,
    syrupConc: String, onSyrupConcChange: (String) -> Unit,
    takes: String, onTakesChange: (String) -> Unit,
    result: Triple<Double, Double, Double>?,
    onCalculate: () -> Unit
) {
    Column {
        NexfarInputField(
            value = weight, onValueChange = onWeightChange,
            label = "Peso da Criança (kg)",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        NexfarInputField(
            value = doseMgKg, onValueChange = onDoseMgKgChange,
            label = "Dose Recomendada (mg/kg/dia)",
            placeholder = "Ex: 50 para Amoxicilina",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        NexfarInputField(
            value = syrupConc, onValueChange = onSyrupConcChange,
            label = "Concentração do Xarope (mg/mL)",
            placeholder = "Ex: 250mg/5mL = 50 mg/mL",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        NexfarInputField(
            value = takes, onValueChange = onTakesChange,
            label = "Número de Tomadas ao Dia (Ex: 3 = a cada 8h)",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = onCalculate,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NexfarNavy)
        ) {
            Text("Calcular Dose Pediátrica", fontWeight = FontWeight.Bold)
        }

        result?.let { (dailyMg, doseMg, volMl) ->
            Spacer(modifier = Modifier.height(14.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = NexfarTealLight,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("DOSE PEDIÁTRICA RECOMENDADA:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NexfarTealDark)
                    Text(
                        text = "${String.format("%.1f", volMl)} mL por tomada",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = NexfarNavy
                    )
                    Text(
                        text = "Equivalente a ${String.format("%.1f", doseMg)} mg por dose (${takes}x ao dia).",
                        fontSize = 12.sp,
                        color = NexfarNavy
                    )
                    Text(
                        text = "Dose diária total: ${String.format("%.1f", dailyMg)} mg/dia.",
                        fontSize = 11.sp,
                        color = NexfarTextSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun CreatinineClearanceContent(
    age: String, onAgeChange: (String) -> Unit,
    weight: String, onWeightChange: (String) -> Unit,
    serumCreatinine: String, onSerumCreatinineChange: (String) -> Unit,
    isFemale: Boolean, onGenderToggle: (Boolean) -> Unit,
    result: Double?,
    onCalculate: () -> Unit
) {
    Column {
        NexfarInputField(
            value = age, onValueChange = onAgeChange,
            label = "Idade do Paciente (Anos)",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        NexfarInputField(
            value = weight, onValueChange = onWeightChange,
            label = "Peso Atual (kg)",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        NexfarInputField(
            value = serumCreatinine, onValueChange = onSerumCreatinineChange,
            label = "Creatinina Sérica (mg/dL)",
            placeholder = "Ex: 1.2",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Sexo do Paciente:", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = NexfarNavy)
            Spacer(modifier = Modifier.width(12.dp))
            FilterChip(
                selected = !isFemale,
                onClick = { onGenderToggle(false) },
                label = { Text("Masculino", fontSize = 11.sp) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(
                selected = isFemale,
                onClick = { onGenderToggle(true) },
                label = { Text("Feminino (×0.85)", fontSize = 11.sp) }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = onCalculate,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NexfarNavy)
        ) {
            Text("Calcular Clearance (Cockcroft-Gault)", fontWeight = FontWeight.Bold)
        }

        result?.let { clcr ->
            Spacer(modifier = Modifier.height(14.dp))
            val stage = when {
                clcr >= 90 -> "Estágio 1 (Função Renal Normal)"
                clcr >= 60 -> "Estágio 2 (Levemente Reduzida)"
                clcr >= 30 -> "Estágio 3 (Redução Moderada — Ajustar Doses)"
                clcr >= 15 -> "Estágio 4 (Redução Grave)"
                else -> "Estágio 5 (Falência Renal / Diálise)"
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = NexfarTealLight,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("CLEARANCE DE CREATININA:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NexfarTealDark)
                    Text(
                        text = "${String.format("%.1f", clcr)} mL/min",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = NexfarNavy
                    )
                    Text(text = "Classificação: $stage", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = NexfarNavy)
                }
            }
        }
    }
}

@Composable
fun ConcentrationConverterContent(
    inputVal: String, onValChange: (String) -> Unit,
    result: String?, onCalculate: () -> Unit
) {
    Column {
        NexfarInputField(
            value = inputVal, onValueChange = onValChange,
            label = "Concentração em % (m/v)",
            placeholder = "Ex: 2",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(14.dp))
        Button(
            onClick = onCalculate,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NexfarNavy)
        ) {
            Text("Converter Concentrações", fontWeight = FontWeight.Bold)
        }
        result?.let { txt ->
            Spacer(modifier = Modifier.height(14.dp))
            Surface(shape = RoundedCornerShape(10.dp), color = NexfarTealLight, modifier = Modifier.fillMaxWidth()) {
                Text(txt, modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold, color = NexfarNavy)
            }
        }
    }
}

@Composable
fun UnitConverterContent() {
    Column {
        Surface(shape = RoundedCornerShape(10.dp), color = NexfarIceBg, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Tabela de Conversão Imediata:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NexfarNavy)
                Spacer(modifier = Modifier.height(6.dp))
                Text("• 1 g = 1.000 mg = 1.000.000 mcg (µg)", fontSize = 12.sp, color = NexfarTextPrimary)
                Text("• 1 mg = 1.000 mcg (µg)", fontSize = 12.sp, color = NexfarTextPrimary)
                Text("• 1 L = 1.000 mL = 1.000 cm³", fontSize = 12.sp, color = NexfarTextPrimary)
                Text("• 1 mL de água = 20 gotas padrão = 60 microgotas", fontSize = 12.sp, color = NexfarTextPrimary)
                Text("• 1 colher de chá = 5 mL | 1 colher de sopa = 15 mL", fontSize = 12.sp, color = NexfarTextPrimary)
            }
        }
    }
}
