package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.NexfarRepository
import com.example.model.Organization
import com.example.model.UserRole
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    currentOrganization: Organization,
    onNavigateToPlans: () -> Unit,
    onLogout: () -> Unit = {},
    onNavigateToAdmin: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var notifyAnvisaAlerts by remember { mutableStateOf(true) }
    var autoSaveDrafts by remember { mutableStateOf(true) }

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var editName by remember { mutableStateOf(NexfarRepository.currentUser.name) }
    var editEmail by remember { mutableStateOf(NexfarRepository.currentUser.email) }
    var editCrf by remember { mutableStateOf(NexfarRepository.currentUser.crfNumber) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NexfarIceBg),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // User Profile Header
        item {
            Surface(
                color = NexfarNavy,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(NexfarTeal),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = NexfarRepository.currentUser.avatarInitials,
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = NexfarRepository.currentUser.name,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "${NexfarRepository.currentUser.crfNumber} • ${NexfarRepository.currentUser.role.label}",
                        color = NexfarTealAccent,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = NexfarRepository.currentUser.email,
                        color = NexfarTextMuted,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { showEditProfileDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = NexfarNavyLight),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Editar Meus Dados", fontSize = 11.sp)
                        }

                        Button(
                            onClick = onLogout,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Sair / Trocar Conta", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Master SaaS Access Panel Shortcut Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = NexfarNavyDark)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = NexfarTealAccent, modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("PAINEL MASTER DO PROPRIETÁRIO", fontSize = 11.sp, color = NexfarTealAccent, fontWeight = FontWeight.Bold)
                                Text("Gerenciador de Acessos & Planos", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Aprove novos cadastros de farmácias, libere licenças e gerencie quem pode acessar cada plano contratado.",
                        fontSize = 11.sp,
                        color = NexfarTextMuted
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = onNavigateToAdmin,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NexfarTeal)
                    ) {
                        Icon(Icons.Default.Security, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Acessar Painel Master", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // Active Subscription Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 4.dp),
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
                        Column {
                            Text("STATUS DA ASSINATURA", fontSize = 10.sp, color = NexfarTextMuted, fontWeight = FontWeight.Bold)
                            Text("Plano Profissional Ativo", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = NexfarNavy)
                        }

                        Surface(shape = RoundedCornerShape(4.dp), color = NexfarTealLight) {
                            Text("Em dia", color = NexfarTealDark, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text("• Consultas IA ilimitadas neste ciclo", fontSize = 11.sp, color = NexfarTextSecondary)
                    Text("• Usuários vinculados ao estabelecimento ativos", fontSize = 11.sp, color = NexfarTextSecondary)

                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = onNavigateToPlans,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Gerenciar ou Fazer Upgrade de Plano", color = NexfarNavy, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // Preferences & Settings
        item {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                Text(
                    text = "PREFERÊNCIAS & SEGURANÇA",
                    color = NexfarTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Alertas Regulatórios ANVISA", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = NexfarNavy)
                                Text("Receber notificações de recolhimento de lotes e novas RDCs", fontSize = 11.sp, color = NexfarTextSecondary)
                            }
                            Switch(checked = notifyAnvisaAlerts, onCheckedChange = { notifyAnvisaAlerts = it })
                        }

                        HorizontalDivider(color = NexfarCardBorder)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Salvamento Automático de Caderno", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = NexfarNavy)
                                Text("Preservar dados clínicos dos pacientes localmente", fontSize = 11.sp, color = NexfarTextSecondary)
                            }
                            Switch(checked = autoSaveDrafts, onCheckedChange = { autoSaveDrafts = it })
                        }
                    }
                }
            }
        }

        // Support Card
        item {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)) {
                Text(
                    text = "SUPORTE & CONFORMIDADE",
                    color = NexfarTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("• Versão da Plataforma: NEXFAR v1.0.0 (Build 2026)", fontSize = 12.sp, color = NexfarTextSecondary)
                        Text("• Conformidade LGPD: Ativa e validada", fontSize = 12.sp, color = NexfarTextSecondary)
                        Text("• Suporte Técnico: suporte@nexfar.com.br", fontSize = 12.sp, color = NexfarTeal)

                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = { Toast.makeText(context, "Canal de suporte acionado", Toast.LENGTH_SHORT).show() },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NexfarNavy)
                        ) {
                            Text("Falar com Suporte Clínico", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // Edit Profile Dialog
    if (showEditProfileDialog) {
        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = { Text("Atualizar Meus Dados Profissionais", fontWeight = FontWeight.Bold, color = NexfarNavy) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Nome Completo") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = editEmail,
                        onValueChange = { editEmail = it },
                        label = { Text("E-mail Profissional") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = editCrf,
                        onValueChange = { editCrf = it },
                        label = { Text("Número do CRF (Ex: CRF-SP 12.345)") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        NexfarRepository.loginUser(
                            name = editName,
                            email = editEmail,
                            crf = editCrf,
                            orgName = currentOrganization.name,
                            role = if (editCrf.isNotBlank() && editCrf.contains("CRF", ignoreCase = true)) UserRole.PHARMACIST_RT else UserRole.ATTENDANT
                        )
                        showEditProfileDialog = false
                        Toast.makeText(context, "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NexfarTeal)
                ) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
