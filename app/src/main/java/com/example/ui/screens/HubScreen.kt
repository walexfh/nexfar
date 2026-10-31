package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Organization
import com.example.ui.theme.*

@Composable
fun HubScreen(
    currentOrganization: Organization,
    onNavigateToLibrary: () -> Unit,
    onNavigateToPathologies: () -> Unit,
    onNavigateToQuiz: () -> Unit,
    onNavigateToPatients: () -> Unit,
    onNavigateToPlans: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToAdmin: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NexfarIceBg),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 90.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "TODOS OS MÓDULOS NEXFAR",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = NexfarTextSecondary,
                letterSpacing = 1.sp
            )
        }

        item {
            HubMenuItem(
                title = "Biblioteca Farmacêutica",
                subtitle = "Artigos técnicos, Farmacologia e RDCs da ANVISA",
                icon = Icons.AutoMirrored.Filled.MenuBook,
                iconTint = NexfarTeal,
                tag = "hub_library",
                onClick = onNavigateToLibrary
            )
        }

        item {
            HubMenuItem(
                title = "Patologias & Mapas Mentais",
                subtitle = "Fluxogramas visuais, HAS, DM2 e Sinais de Alerta",
                icon = Icons.Default.AccountTree,
                iconTint = Color(0xFFF59E0B),
                tag = "hub_pathologies",
                onClick = onNavigateToPathologies
            )
        }

        item {
            HubMenuItem(
                title = "Simulados & Treinamento",
                subtitle = "Banco de questões com feedback regulatório e ranking",
                icon = Icons.Default.School,
                iconTint = Color(0xFF3B82F6),
                tag = "hub_quiz",
                onClick = onNavigateToQuiz
            )
        }

        item {
            HubMenuItem(
                title = "Caderno de Atenção Farmacêutica",
                subtitle = "Acompanhamento de PA, Glicemia, IMC e conciliação",
                icon = Icons.Default.AssignmentInd,
                iconTint = Color(0xFF8B5CF6),
                tag = "hub_patients",
                onClick = onNavigateToPatients
            )
        }

        item {
            HubMenuItem(
                title = "Planos Comerciais & Assinatura",
                subtitle = "Individual (R$ 19,90), Pro (R$ 39,90) e Estabelecimento",
                icon = Icons.Default.CardMembership,
                iconTint = Color(0xFF10B981),
                tag = "hub_plans",
                onClick = onNavigateToPlans
            )
        }

        item {
            HubMenuItem(
                title = "Perfil & Configurações da Farmácia",
                subtitle = "CRF do farmacêutico, notificações e preferências",
                icon = Icons.Default.Person,
                iconTint = NexfarNavy,
                tag = "hub_profile",
                onClick = onNavigateToProfile
            )
        }

        item {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "AMBIENTE ADMINISTRATIVO",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = NexfarTextSecondary,
                letterSpacing = 1.sp
            )
        }

        item {
            HubMenuItem(
                title = "Painel Administrativo do Proprietário",
                subtitle = "Dashboard executivo, MRR R$ 48.750, gestão de usuários e IA",
                icon = Icons.Default.AdminPanelSettings,
                iconTint = NexfarNavyDark,
                tag = "hub_admin",
                isSpecial = true,
                onClick = onNavigateToAdmin
            )
        }
    }
}

@Composable
fun HubMenuItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    tag: String,
    isSpecial: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(tag)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSpecial) NexfarNavy else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSpecial) 4.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSpecial) NexfarTeal else iconTint.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSpecial) Color.White else iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSpecial) Color.White else NexfarNavy
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = if (isSpecial) NexfarTextMuted else NexfarTextSecondary
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = if (isSpecial) NexfarTealAccent else NexfarTextMuted,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
