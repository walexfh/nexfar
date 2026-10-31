package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.NexfarRepository
import com.example.model.ChatMessage
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun AssistantScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var messages by remember { mutableStateOf(NexfarRepository.initialChatMessages) }
    var inputText by remember { mutableStateOf("") }
    var isThinking by remember { mutableStateOf(false) }

    val presetPrompts = listOf(
        "Interação entre Fluoxetina e Tramadol",
        "Ajuste de Ciprofloxacino no Idoso",
        "Diferença entre Dipirona e Paracetamol",
        "Reconstituição de Clavulin Injetável",
        "Manejo de Hipoglicemia por Sulfonilureias"
    )

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        val userMsg = ChatMessage(
            id = "usr_${System.currentTimeMillis()}",
            isUser = true,
            messageText = text
        )
        messages = messages + userMsg
        inputText = ""
        isThinking = true

        coroutineScope.launch {
            kotlinx.coroutines.delay(600) // Realistic clinical analysis latency
            val aiResponse = NexfarRepository.generateClinicalAIResponse(text)
            messages = messages + aiResponse
            isThinking = false
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NexfarIceBg)
    ) {
        // Clinical AI Disclaimer Bar
        Surface(
            color = NexfarNavyDark,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = "Aviso Clínico",
                    tint = NexfarTealAccent,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Apoio à decisão farmacêutica baseado em bulas ANVISA e farmacopeia. Não substitui consulta médica.",
                    color = NexfarTextMuted,
                    fontSize = 10.sp,
                    lineHeight = 13.sp
                )
            }
        }

        // Conversation Header & Action Bar
        Surface(
            color = Color.White,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(NexfarTealLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "IA",
                            tint = NexfarTealDark,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Assistente Farmacêutico IA",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = NexfarNavy
                        )
                        Text(
                            text = "Motor de Regras Clínicas e RAG ANVISA",
                            fontSize = 10.sp,
                            color = NexfarTextSecondary
                        )
                    }
                }

                TextButton(
                    onClick = {
                        messages = NexfarRepository.initialChatMessages
                        Toast.makeText(context, "Nova conversa iniciada", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.AddComment,
                        contentDescription = "Nova Conversa",
                        tint = NexfarTeal,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Limpar", fontSize = 12.sp, color = NexfarTeal, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Quick Suggestion Chips
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            contentPadding = PaddingValues(horizontal = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(presetPrompts) { prompt ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = CardDefaults.outlinedCardBorder(),
                    modifier = Modifier.clickable { sendMessage(prompt) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = NexfarTeal,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = prompt,
                            fontSize = 11.sp,
                            color = NexfarNavy,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Chat Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 10.dp)
        ) {
            items(messages) { msg ->
                ChatMessageItem(message = msg, onCopy = { textToCopy ->
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("NEXFAR Resposta", textToCopy))
                    Toast.makeText(context, "Resposta copiada para a área de transferência!", Toast.LENGTH_SHORT).show()
                })
            }

            if (isThinking) {
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        tonalElevation = 2.dp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = NexfarTeal
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Consultando bulário oficial ANVISA e literatura clínica...",
                                fontSize = 12.sp,
                                color = NexfarTextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Bottom Input Area
        Surface(
            color = Color.White,
            tonalElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            "Dúvida clínica, posologia ou interação...",
                            fontSize = 13.sp,
                            color = NexfarTextMuted
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("assistant_input_field"),
                    shape = RoundedCornerShape(24.dp),
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
                    maxLines = 3
                )

                Spacer(modifier = Modifier.width(8.dp))

                FloatingActionButton(
                    onClick = { sendMessage(inputText) },
                    containerColor = NexfarNavy,
                    contentColor = Color.White,
                    modifier = Modifier
                        .size(44.dp)
                        .testTag("assistant_send_button"),
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Enviar Pergunta",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatMessageItem(
    message: ChatMessage,
    onCopy: (String) -> Unit
) {
    if (message.isUser) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .widthIn(max = 280.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomStart = 16.dp, bottomEnd = 16.dp))
                    .background(NexfarNavy)
                    .padding(12.dp)
            ) {
                Text(
                    text = message.messageText,
                    color = Color.White,
                    fontSize = 13.sp
                )
            }
        }
    } else {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                // Header with badge & copy
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = NexfarTealLight
                        ) {
                            Text(
                                text = "ANÁLISE CLÍNICA VERIFICADA",
                                color = NexfarTealDark,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    IconButton(
                        onClick = { onCopy(message.messageText + "\n\n" + message.mechanismOfAction) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copiar resposta",
                            tint = NexfarTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Core answer text
                Text(
                    text = message.messageText,
                    color = NexfarNavy,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 20.sp
                )

                if (message.technicalSummary.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFEFF6FF),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = message.technicalSummary,
                            color = Color(0xFF1E40AF),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                if (message.mechanismOfAction.isNotBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Mecanismo Farmacológico:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NexfarTextSecondary
                    )
                    Text(
                        text = message.mechanismOfAction,
                        fontSize = 12.sp,
                        color = NexfarTextPrimary,
                        lineHeight = 17.sp
                    )
                }

                if (message.safetyAlerts.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Alertas de Segurança & Conduta:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NexfarTarjaVermelha
                    )
                    message.safetyAlerts.forEach { alert ->
                        Row(
                            modifier = Modifier.padding(top = 3.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text("• ", color = NexfarTarjaVermelha, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text(alert, fontSize = 11.sp, color = NexfarTextPrimary, lineHeight = 16.sp)
                        }
                    }
                }

                if (message.citations.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = NexfarCardBorder)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = "Fontes",
                            tint = NexfarTextMuted,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Fontes: " + message.citations.joinToString(" • "),
                            fontSize = 10.sp,
                            color = NexfarTextMuted
                        )
                    }
                }
            }
        }
    }
}
