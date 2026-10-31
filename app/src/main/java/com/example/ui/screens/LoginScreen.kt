package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.NexfarRepository
import com.example.model.UserRole
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToMasterAdmin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var crf by remember { mutableStateOf("") }
    var pharmacyName by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(UserRole.PHARMACIST_RT) }
    var roleMenuExpanded by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NexfarIceBg)
            .verticalScroll(scrollState)
    ) {
        // Hero Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(NexfarNavy, NexfarNavyLight)
                    )
                )
                .padding(vertical = 36.dp, horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    shape = CircleShape,
                    color = NexfarTealAccent,
                    modifier = Modifier.size(68.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.LocalPharmacy,
                            contentDescription = "Logo NEXFAR",
                            tint = Color.White,
                            modifier = Modifier.size(38.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "NEXFAR",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "Plataforma de Inteligência Farmacêutica",
                    color = NexfarTealAccent,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Acesso com seus dados reais ou credencial autorizada",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 11.sp
                )
            }
        }

        // Card Form
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Badge,
                        contentDescription = null,
                        tint = NexfarTealAccent,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Identificação do Profissional",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = NexfarNavy
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Preencha suas informações reais para personalizar seu perfil, receituários e carimbos no app.",
                    fontSize = 12.sp,
                    color = NexfarTextMuted
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome Completo") },
                    placeholder = { Text("Ex: Dr. Carlos Silva ou Juliana Lima") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null, tint = NexfarTealAccent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("E-mail Profissional") },
                    placeholder = { Text("exemplo@farmacia.com.br") },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = null, tint = NexfarTealAccent)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = crf,
                    onValueChange = { crf = it },
                    label = { Text("Registro Profissional (CRF / CRM / Matrícula)") },
                    placeholder = { Text("Ex: CRF-SP 12345") },
                    leadingIcon = {
                        Icon(Icons.Default.CardMembership, contentDescription = null, tint = NexfarTealAccent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = pharmacyName,
                    onValueChange = { pharmacyName = it },
                    label = { Text("Farmácia / Drogaria / Estabelecimento") },
                    placeholder = { Text("Ex: Drogaria Popular Farma") },
                    leadingIcon = {
                        Icon(Icons.Default.Storefront, contentDescription = null, tint = NexfarTealAccent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Role Dropdown
                ExposedDropdownMenuBox(
                    expanded = roleMenuExpanded,
                    onExpandedChange = { roleMenuExpanded = !roleMenuExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedRole.label,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Cargo / Função") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = roleMenuExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = roleMenuExpanded,
                        onDismissRequest = { roleMenuExpanded = false }
                    ) {
                        UserRole.values().forEach { role ->
                            DropdownMenuItem(
                                text = { Text(role.label) },
                                onClick = {
                                    selectedRole = role
                                    roleMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        NexfarRepository.loginUser(
                            name = name,
                            email = email,
                            crf = crf,
                            orgName = pharmacyName,
                            role = selectedRole
                        )
                        Toast.makeText(context, "Bem-vindo ao NEXFAR!", Toast.LENGTH_SHORT).show()
                        onLoginSuccess()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NexfarNavy),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Login, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Entrar com Meus Dados Reais", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = {
                        NexfarRepository.loginUser(
                            name = "Usuário Convidado",
                            email = "convidado@nexfar.com.br",
                            crf = "Demonstração",
                            orgName = "Farmácia Escola",
                            role = UserRole.ATTENDANT
                        )
                        onLoginSuccess()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Acessar em Modo Demonstração / Convidado", color = NexfarNavy)
                }
            }
        }

        // Master SaaS Admin Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = NexfarTealLight.copy(alpha = 0.3f)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = null,
                        tint = NexfarTealAccent,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Painel Master SaaS (Acessos & Planos)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = NexfarNavy
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Gerencie quem tem autorização para usar o aplicativo, altere planos contratados (Individual, Profissional, Estabelecimento) ou acesse o link de controle da nuvem.",
                    fontSize = 12.sp,
                    color = NexfarNavy
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onNavigateToMasterAdmin,
                        colors = ButtonDefaults.buttonColors(containerColor = NexfarNavy),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Abrir Painel Master", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Link Painel Master", "https://speedy-opal-3medrmg.tmp.now")
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Link do Painel copiado!", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp), tint = NexfarNavy)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Copiar Link", fontSize = 12.sp, color = NexfarNavy)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
