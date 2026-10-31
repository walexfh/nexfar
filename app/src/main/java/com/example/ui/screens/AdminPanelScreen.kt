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
import com.example.model.AuditLogEntry
import com.example.model.PlanTier
import com.example.model.UserAccessAccount
import com.example.ui.theme.*

@Composable
fun AdminPanelScreen(
    onExitAdminMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedAdminTab by remember { mutableStateOf(0) }
    val adminTabs = listOf("Acessos & Planos", "Visão Geral & MRR", "Estabelecimentos", "Consumo IA", "Auditoria")

    val metrics = NexfarRepository.adminMetrics

    val masterPanelWebUrl = "https://speedy-opal-3medrmg.tmp.now"

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
                                    text = "PAINEL MASTER SAAS",
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
                            Text("Sair do Painel", fontSize = 11.sp)
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
                            indicator = "382 clientes B2B",
                            isPositive = true
                        )
                    }
                }
            }
        }

        // Master Link Banner with Copy Button
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF1E3A5F)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Link, contentDescription = null, tint = NexfarTealAccent, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Link do Painel Master na Nuvem (Web / Vercel):", color = NexfarTealAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(
                            text = masterPanelWebUrl,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Button(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Link Master NEXFAR", masterPanelWebUrl))
                            Toast.makeText(context, "Link do Painel Master copiado com sucesso!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NexfarTeal),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Copiar Link", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Admin Tab Bar
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(adminTabs.indices.toList()) { index ->
                    FilterChip(
                        selected = selectedAdminTab == index,
                        onClick = { selectedAdminTab = index },
                        label = { Text(adminTabs[index], fontSize = 11.sp, fontWeight = if (selectedAdminTab == index) FontWeight.Bold else FontWeight.Normal) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NexfarNavy,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Active Tab Content
        when (selectedAdminTab) {
            0 -> {
                // TAB 0: Gestão de Acessos & Planos (Master Control)
                item {
                    Column(modifier = Modifier.padding(horizontal = 14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Controle de Acessos por Plano:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = NexfarNavy
                            )

                            Text(
                                text = "${NexfarRepository.accessAccounts.size} contas cadastradas",
                                fontSize = 11.sp,
                                color = NexfarTextSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        NexfarRepository.accessAccounts.forEach { acc ->
                            AccessAccountCard(
                                account = acc,
                                onApprove = { newPlan ->
                                    NexfarRepository.approveAccount(acc.id, newPlan)
                                    Toast.makeText(context, "Acesso aprovado para ${acc.userName}!", Toast.LENGTH_SHORT).show()
                                },
                                onToggleBlock = {
                                    NexfarRepository.toggleBlockAccount(acc.id)
                                    val action = if (acc.status == "BLOQUEADO") "desbloqueada" else "bloqueada"
                                    Toast.makeText(context, "Conta $action!", Toast.LENGTH_SHORT).show()
                                },
                                onChangePlan = { plan ->
                                    NexfarRepository.approveAccount(acc.id, plan)
                                    Toast.makeText(context, "Plano atualizado para ${plan.planName}!", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }

            1 -> {
                // TAB 1: Visão Geral & MRR
                item {
                    Column(modifier = Modifier.padding(horizontal = 14.dp)) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("Métricas Comerciais do SaaS:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NexfarNavy)
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("MRR Mensal:", fontSize = 12.sp, color = NexfarTextSecondary)
                                    Text(metrics.mrrFormatted, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NexfarTealDark)
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Usuários Ativos (Diários):", fontSize = 12.sp, color = NexfarTextSecondary)
                                    Text("${metrics.activeUsersDaily} de ${metrics.totalUsers}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NexfarNavy)
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Taxa de Cancelamento (Churn):", fontSize = 12.sp, color = NexfarTextSecondary)
                                    Text(metrics.monthlyChurnRate, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF059669))
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Uptime de Infraestrutura:", fontSize = 12.sp, color = NexfarTextSecondary)
                                    Text(metrics.systemUptime, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NexfarTealDark)
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

            2 -> {
                // TAB 2: Estabelecimentos
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

                                    IconButton(onClick = { Toast.makeText(context, "Configurações de ${org.name}", Toast.LENGTH_SHORT).show() }) {
                                        Icon(Icons.Default.Settings, contentDescription = "Gerenciar", tint = NexfarTeal)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            3 -> {
                // TAB 3: Consumo IA
                item {
                    Column(modifier = Modifier.padding(horizontal = 14.dp)) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("Métricas do Motor de Inteligência Artificial:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NexfarNavy)
                                Spacer(modifier = Modifier.height(10.dp))
                                Text("• Total de Tokens Processados: ${metrics.aiTokensConsumed}", fontSize = 12.sp, color = NexfarTextSecondary)
                                Text("• Consultas Clínicas e de Bula este mês: 12.480 requisições", fontSize = 12.sp, color = NexfarTextSecondary)
                                Text("• Latência média de resposta: 420 ms", fontSize = 12.sp, color = NexfarTextSecondary)
                                Text("• RAG ANVISA: 100% dos dados atualizados", fontSize = 12.sp, color = NexfarTealDark, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            4 -> {
                // TAB 4: Auditoria
                item {
                    Column(modifier = Modifier.padding(horizontal = 14.dp)) {
                        Text("Trilha de Auditoria SNGPC / ANVISA:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NexfarNavy)
                        Spacer(modifier = Modifier.height(8.dp))

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
fun AccessAccountCard(
    account: UserAccessAccount,
    onApprove: (PlanTier) -> Unit,
    onToggleBlock: () -> Unit,
    onChangePlan: (PlanTier) -> Unit
) {
    var showPlanSelector by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header with status badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = account.userName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = NexfarNavy
                    )
                    Text(
                        text = "${account.pharmacyName} • CNPJ: ${account.cnpj}",
                        fontSize = 11.sp,
                        color = NexfarTextSecondary
                    )
                    Text(
                        text = "${account.email} • ${account.crfNumber.ifBlank { "Sem CRF" }}",
                        fontSize = 10.sp,
                        color = NexfarTextMuted
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    // Status Badge
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = when (account.status) {
                            "ATIVO" -> Color(0xFFD1FAE5)
                            "PENDENTE_APROVACAO" -> Color(0xFFFEF3C7)
                            else -> Color(0xFFFEE2E2)
                        }
                    ) {
                        Text(
                            text = when (account.status) {
                                "ATIVO" -> "ATIVO"
                                "PENDENTE_APROVACAO" -> "PENDENTE"
                                else -> "BLOQUEADO"
                            },
                            color = when (account.status) {
                                "ATIVO" -> Color(0xFF065F46)
                                "PENDENTE_APROVACAO" -> Color(0xFF92400E)
                                else -> Color(0xFF991B1B)
                            },
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Plan Tier Badge
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = NexfarNavyDark
                    ) {
                        Text(
                            text = account.planTier.planName,
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = NexfarCardBorder)
            Spacer(modifier = Modifier.height(8.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (account.status == "PENDENTE_APROVACAO") {
                    Button(
                        onClick = { onApprove(account.planTier) },
                        colors = ButtonDefaults.buttonColors(containerColor = NexfarTeal),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Aprovar", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                OutlinedButton(
                    onClick = { showPlanSelector = true },
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Trocar Plano", fontSize = 11.sp, color = NexfarNavy, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = onToggleBlock,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (account.status == "BLOQUEADO") Color(0xFF059669) else Color(0xFFDC2626)
                    ),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = if (account.status == "BLOQUEADO") "Desbloquear" else "Bloquear",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    if (showPlanSelector) {
        AlertDialog(
            onDismissRequest = { showPlanSelector = false },
            title = { Text("Alterar Plano de ${account.userName}", fontSize = 14.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    PlanTier.values().forEach { tier ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onChangePlan(tier)
                                    showPlanSelector = false
                                },
                            shape = RoundedCornerShape(8.dp),
                            color = if (account.planTier == tier) NexfarTealLight else NexfarIceBg,
                            border = if (account.planTier == tier) CardDefaults.outlinedCardBorder() else null
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(tier.planName, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NexfarNavy)
                                    Text(tier.targetAudience, fontSize = 10.sp, color = NexfarTextSecondary)
                                }
                                Text(tier.priceMonthly, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NexfarTealDark)
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showPlanSelector = false }) {
                    Text("Cancelar")
                }
            }
        )
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
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = title, fontSize = 10.sp, color = NexfarTextMuted)
            Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color.White)
            Text(
                text = indicator,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (isPositive) NexfarTealAccent else NexfarTarjaVermelha
            )
        }
    }
}

@Composable
fun AuditLogItem(log: AuditLogEntry) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = log.action, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = NexfarNavy)
                Text(text = "${log.actor} • ${log.organization}", fontSize = 10.sp, color = NexfarTextSecondary)
            }
            Text(text = log.timeAgo, fontSize = 9.sp, color = NexfarTextMuted)
        }
    }
}
