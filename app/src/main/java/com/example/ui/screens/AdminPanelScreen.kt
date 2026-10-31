package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.NexfarRepository
import com.example.ui.theme.*

@Composable
fun AdminPanelScreen(
    onExitAdminMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedAdminTab by remember { mutableStateOf(0) }
    val adminTabs = listOf("Visão Geral & MRR", "Estabelecimentos", "Usuários", "Consumo IA", "Auditoria")

    val metrics = NexfarRepository.adminMetrics

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NexfarIceBg),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Admin Header
        item {
            Surface(
                color = NexfarNavyDark,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = NexfarTeal
                            ) {
                                Text(
                                    text = "SAAS MASTER ADMIN",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "NEXFAR Control Plane",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = onExitAdminMode,
                            colors = ButtonDefaults.buttonColors(containerColor = NexfarNavyLight),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Sair do Admin", fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // SaaS KPI Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AdminKPICard(
                            modifier = Modifier.weight(1f),
                            title = "Receita Recorrente (MRR)",
                            value = metrics.mrrFormatted,
                            indicator = "+14.2% este mês",
                            isPositive = true
                        )
                        AdminKPICard(
                            modifier = Modifier.weight(1f),
                            title = "Assinaturas Ativas",
                            value = "${metrics.activeSubscriptions}",
                            indicator = "382 clientes pagos",
                            isPositive = true
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AdminKPICard(
                            modifier = Modifier.weight(1f),
                            title = "Usuários Registrados",
                            value = "${metrics.totalUsers}",
                            indicator = "890 ativos hoje",
                            isPositive = true
                        )
                        AdminKPICard(
                            modifier = Modifier.weight(1f),
                            title = "Tokens IA Consumidos",
                            value = metrics.aiTokensConsumed,
                            indicator = "R$ 412 custo operacional",
                            isPositive = false
                        )
                    }
                }
            }
        }

        // Sub Navigation Tabs
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                contentPadding = PaddingValues(horizontal = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(adminTabs.indices.toList()) { idx ->
                    val isSelected = selectedAdminTab == idx
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) NexfarNavy else Color.White,
                        border = if (isSelected) null else CardDefaults.outlinedCardBorder(),
                        modifier = Modifier.clickable { selectedAdminTab = idx }
                    ) {
                        Text(
                            text = adminTabs[idx],
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else NexfarNavy,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // Tab Content
        when (selectedAdminTab) {
            0 -> {
                // Overview & Quick Controls
                item {
                    Column(modifier = Modifier.padding(horizontal = 14.dp)) {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("Ações Rápidas do Proprietário:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NexfarNavy)
                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = { Toast.makeText(context, "Sincronização com Bulário ANVISA iniciada com sucesso", Toast.LENGTH_SHORT).show() },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = NexfarTeal)
                                    ) {
                                        Text("Sincronizar ANVISA", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }

                                    Button(
                                        onClick = { Toast.makeText(context, "Relatório de MRR e Churn gerado", Toast.LENGTH_SHORT).show() },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = NexfarNavy)
                                    ) {
                                        Text("Exportar Relatório", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Text("Últimos Registros de Auditoria:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NexfarTextSecondary)
                        Spacer(modifier = Modifier.height(6.dp))

                        NexfarRepository.auditLogs.forEach { log ->
                            AuditLogItem(log)
                        }
                    }
                }
            }
            1 -> {
                // Establishments List
                item {
                    Column(modifier = Modifier.padding(horizontal = 14.dp)) {
                        Text("Estabelecimentos / Farmácias Clientes:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NexfarNavy)
                        Spacer(modifier = Modifier.height(8.dp))

                        NexfarRepository.organizations.forEach { org ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = org.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NexfarNavy)
                                        Text(text = "CNPJ: ${org.cnpj} • ${org.city}", fontSize = 11.sp, color = NexfarTextSecondary)
                                        Text(text = "Plano: ${org.planName} • ${org.activeMembersCount} operadores", fontSize = 11.sp, color = NexfarTealDark)
                                    }

                                    IconButton(onClick = { Toast.makeText(context, "Gerenciando ${org.name}", Toast.LENGTH_SHORT).show() }) {
                                        Icon(Icons.Default.Settings, contentDescription = "Gerenciar", tint = NexfarTeal)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            2 -> {
                // Users List
                item {
                    Column(modifier = Modifier.padding(horizontal = 14.dp)) {
                        Text("Usuários do SaaS (Farmacêuticos e Balconistas):", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NexfarNavy)
                        Spacer(modifier = Modifier.height(8.dp))

                        val usersMock = listOf(
                            Triple("Dr. Walex Ferreira", "CRF-SP 48.912 • RT", "Ativo"),
                            Triple("Lucas Rodrigues de Oliveira", "Balconista • Farmácia São Paulo", "Ativo"),
                            Triple("Camila Albuquerque", "Farmacêutica Consultora", "Ativo"),
                            Triple("Marcos Vinicius Santos", "Gestor de Filial • FarmaVida", "Ativo")
                        )

                        usersMock.forEach { (name, role, status) ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(text = name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NexfarNavy)
                                        Text(text = role, fontSize = 11.sp, color = NexfarTextSecondary)
                                    }
                                    Surface(shape = RoundedCornerShape(4.dp), color = NexfarTealLight) {
                                        Text(status, color = NexfarTealDark, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
            3 -> {
                // AI Consumption Monitor
                item {
                    Column(modifier = Modifier.padding(horizontal = 14.dp)) {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("Métricas do Motor de IA Farmacêutica:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NexfarNavy)
                                Spacer(modifier = Modifier.height(10.dp))
                                Text("• Modelo LLM: Gemini Pro Healthcare / RAG ANVISA", fontSize = 12.sp, color = NexfarTextPrimary)
                                Text("• Consultas Realizadas no Mês: 24.120", fontSize = 12.sp, color = NexfarTextPrimary)
                                Text("• Tempo Médio de Resposta: 420 ms", fontSize = 12.sp, color = NexfarTextPrimary)
                                Text("• Taxa de Confirmação de Segurança: 99.8%", fontSize = 12.sp, color = NexfarTealDark, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
            4 -> {
                // Audit Logs
                item {
                    Column(modifier = Modifier.padding(horizontal = 14.dp)) {
                        NexfarRepository.auditLogs.forEach { log ->
                            AuditLogItem(log)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminKPICard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    indicator: String,
    isPositive: Boolean
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = NexfarNavyLight
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = title, fontSize = 10.sp, color = NexfarTextMuted)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = indicator,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isPositive) NexfarTealAccent else Color(0xFFFBBF24)
            )
        }
    }
}

@Composable
fun AuditLogItem(log: com.example.model.AuditLogEntry) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.History, contentDescription = null, tint = NexfarTeal, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = log.action, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NexfarNavy)
                Text(text = "${log.actor} • ${log.timeAgo}", fontSize = 10.sp, color = NexfarTextMuted)
            }
        }
    }
}
