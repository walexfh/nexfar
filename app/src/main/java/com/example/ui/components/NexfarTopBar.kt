package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Organization
import com.example.ui.theme.*

@Composable
fun NexfarTopBar(
    currentOrganization: Organization,
    organizations: List<Organization>,
    onSelectOrganization: (Organization) -> Unit,
    isAdminMode: Boolean,
    onToggleAdminMode: () -> Unit,
    onOpenSearch: () -> Unit,
    onOpenProfile: () -> Unit
) {
    var showOrgMenu by remember { mutableStateOf(false) }

    Surface(
        color = NexfarNavy,
        tonalElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            // First Row: Logo & Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                NexfarLogo(compact = false)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Admin Toggle Badge
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isAdminMode) NexfarTeal else NexfarNavyLight,
                        contentColor = Color.White,
                        modifier = Modifier
                            .testTag("admin_mode_toggle")
                            .clickable { onToggleAdminMode() }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Icon(
                                imageVector = if (isAdminMode) Icons.Default.Shield else Icons.Default.Storefront,
                                contentDescription = "Modo de Operação",
                                modifier = Modifier.size(14.dp),
                                tint = if (isAdminMode) Color.White else NexfarTealAccent
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isAdminMode) "Admin SaaS" else "Farmácia",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Search button
                    IconButton(
                        onClick = onOpenSearch,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("top_search_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Pesquisar",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // User Profile Avatar
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(NexfarTeal)
                            .clickable { onOpenProfile() }
                            .testTag("top_profile_avatar"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "WF",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Second Row: Multi-tenant Organization Switcher Pill
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = NexfarNavyDark.copy(alpha = 0.6f),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showOrgMenu = true }
                    .testTag("organization_selector")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Business,
                            contentDescription = "Estabelecimento",
                            tint = NexfarTealAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = currentOrganization.name,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${currentOrganization.planName} • ${currentOrganization.city}",
                                color = NexfarTextMuted,
                                fontSize = 10.sp
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Trocar Farmácia",
                        tint = NexfarTextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Organization Switcher Dropdown
            DropdownMenu(
                expanded = showOrgMenu,
                onDismissRequest = { showOrgMenu = false },
                modifier = Modifier.background(NexfarNavyDark)
            ) {
                Text(
                    text = "SELECIONE O ESTABELECIMENTO (MULTI-TENANT):",
                    color = NexfarTextMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )

                organizations.forEach { org ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(
                                    text = org.name,
                                    color = if (org.id == currentOrganization.id) NexfarTealAccent else Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = if (org.id == currentOrganization.id) FontWeight.Bold else FontWeight.Normal
                                )
                                Text(
                                    text = "${org.planName} • CNPJ: ${org.cnpj}",
                                    color = NexfarTextMuted,
                                    fontSize = 11.sp
                                )
                            }
                        },
                        onClick = {
                            onSelectOrganization(org)
                            showOrgMenu = false
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = if (org.id == currentOrganization.id) Icons.Default.CheckCircle else Icons.Default.Storefront,
                                contentDescription = null,
                                tint = if (org.id == currentOrganization.id) NexfarTealAccent else NexfarTextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    )
                }
            }
        }
    }
}
