package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.model.QuizQuestion
import com.example.ui.theme.*

@Composable
fun QuizScreen(
    modifier: Modifier = Modifier
) {
    val questions = NexfarRepository.quizQuestions
    var currentIndex by remember { mutableStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var hasAnswered by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }
    var isQuizCompleted by remember { mutableStateOf(false) }

    val currentQ = questions.getOrNull(currentIndex)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NexfarIceBg),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Header with Progress & Score
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
                        Text(
                            text = "SIMULADO & TREINAMENTO",
                            color = NexfarTealAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = NexfarNavyLight
                        ) {
                            Text(
                                text = "Pontuação: $score / ${questions.size}",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Capacitação Contínua de Balcão & RT",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Progress bar
                    LinearProgressIndicator(
                        progress = { (currentIndex + 1).toFloat() / questions.size },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = NexfarTealAccent,
                        trackColor = NexfarNavyLight
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Questão ${currentIndex + 1} de ${questions.size}",
                        fontSize = 11.sp,
                        color = NexfarTextMuted
                    )
                }
            }
        }

        if (!isQuizCompleted && currentQ != null) {
            // Question Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = NexfarTealLight
                        ) {
                            Text(
                                text = currentQ.category,
                                color = NexfarTealDark,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = currentQ.questionText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = NexfarNavy,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Options List
                        currentQ.options.forEachIndexed { index, optionText ->
                            val isChosen = selectedOption == index
                            val isCorrect = index == currentQ.correctIndex

                            val containerColor = when {
                                !hasAnswered && isChosen -> NexfarTealLight
                                hasAnswered && isCorrect -> Color(0xFFDCFCE7) // green
                                hasAnswered && isChosen && !isCorrect -> Color(0xFFFEE2E2) // red
                                else -> NexfarIceBg
                            }

                            val borderColor = when {
                                !hasAnswered && isChosen -> NexfarTeal
                                hasAnswered && isCorrect -> NexfarSuccess
                                hasAnswered && isChosen && !isCorrect -> NexfarTarjaVermelha
                                else -> NexfarCardBorder
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = containerColor,
                                border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .testTag("quiz_opt_$index")
                                    .clickable(enabled = !hasAnswered) {
                                        selectedOption = index
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(
                                                when {
                                                    hasAnswered && isCorrect -> NexfarSuccess
                                                    hasAnswered && isChosen && !isCorrect -> NexfarTarjaVermelha
                                                    isChosen -> NexfarTeal
                                                    else -> NexfarNavyLight
                                                }
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = ('A' + index).toString(),
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Text(
                                        text = optionText,
                                        fontSize = 12.sp,
                                        color = NexfarNavy,
                                        fontWeight = if (isChosen || (hasAnswered && isCorrect)) FontWeight.Bold else FontWeight.Normal,
                                        lineHeight = 17.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Submit or Next Button
                        if (!hasAnswered) {
                            Button(
                                onClick = {
                                    if (selectedOption != null) {
                                        hasAnswered = true
                                        if (selectedOption == currentQ.correctIndex) {
                                            score++
                                        }
                                    }
                                },
                                enabled = selectedOption != null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("quiz_submit_button"),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = NexfarNavy)
                            ) {
                                Text("Confirmar Resposta", fontWeight = FontWeight.Bold)
                            }
                        } else {
                            // Technical Explanation Box
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (selectedOption == currentQ.correctIndex) Color(0xFFF0FDF4) else Color(0xFFFFF1F2),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = if (selectedOption == currentQ.correctIndex) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                            contentDescription = null,
                                            tint = if (selectedOption == currentQ.correctIndex) NexfarSuccess else NexfarTarjaVermelha,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (selectedOption == currentQ.correctIndex) "Resposta Correta!" else "Resposta Incorreta",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = if (selectedOption == currentQ.correctIndex) Color(0xFF166534) else Color(0xFF991B1B)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = currentQ.technicalExplanation,
                                        fontSize = 12.sp,
                                        color = NexfarTextPrimary,
                                        lineHeight = 17.sp
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Referência: ${currentQ.regulatoryReference}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = NexfarTextSecondary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    if (currentIndex < questions.size - 1) {
                                        currentIndex++
                                        selectedOption = null
                                        hasAnswered = false
                                    } else {
                                        isQuizCompleted = true
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("quiz_next_button"),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = NexfarTeal)
                            ) {
                                Text(
                                    text = if (currentIndex < questions.size - 1) "Próxima Questão" else "Ver Resultado Final",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // Completed Summary Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(NexfarTealLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = "Troféu",
                                tint = NexfarTealDark,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Simulado Concluído!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = NexfarNavy
                        )

                        Text(
                            text = "Você acertou $score de ${questions.size} questões (${(score * 100) / questions.size}% de rendimento).",
                            fontSize = 13.sp,
                            color = NexfarTextSecondary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = NexfarIceBg,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Desempenho por Categoria:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NexfarNavy)
                                Text("• Legislação RDC 20: 100%", fontSize = 11.sp, color = NexfarTextPrimary)
                                Text("• Farmacologia & Interações: 100%", fontSize = 11.sp, color = NexfarTextPrimary)
                                Text("• Cálculos de Gotejamento: 100%", fontSize = 11.sp, color = NexfarTextPrimary)
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = {
                                currentIndex = 0
                                selectedOption = null
                                hasAnswered = false
                                score = 0
                                isQuizCompleted = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NexfarNavy)
                        ) {
                            Text("Reiniciar Simulado", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
