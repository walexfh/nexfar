package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import com.example.ui.theme.*

enum class NexfarTab(
    val title: String,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector
) {
    DASHBOARD("Início", Icons.Filled.Home, Icons.Outlined.Home),
    ASSISTANT("IA Clínica", Icons.Filled.AutoAwesome, Icons.Outlined.AutoAwesome),
    MEDICINES("Bulário", Icons.Filled.Medication, Icons.Outlined.Medication),
    CALCULATORS("Cálculos", Icons.Filled.Calculate, Icons.Outlined.Calculate),
    HUB("Módulos", Icons.Filled.GridView, Icons.Outlined.GridView)
}

@Composable
fun NexfarBottomBar(
    selectedTab: NexfarTab,
    onTabSelected: (NexfarTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.White,
        tonalElevation = 8.dp,
        shadowElevation = 16.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            NexfarTab.values().forEach { tab ->
                val isSelected = tab == selectedTab
                val isAITab = tab == NexfarTab.ASSISTANT

                Column(
                    modifier = Modifier
                        .testTag("nav_tab_${tab.name.lowercase()}")
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onTabSelected(tab) }
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                when {
                                    isSelected && isAITab -> NexfarTeal
                                    isSelected -> NexfarTealLight
                                    else -> Color.Transparent
                                }
                            )
                            .padding(horizontal = 14.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isSelected) tab.activeIcon else tab.inactiveIcon,
                            contentDescription = tab.title,
                            tint = when {
                                isSelected && isAITab -> Color.White
                                isSelected -> NexfarTealDark
                                else -> NexfarTextSecondary
                            },
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = tab.title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = when {
                            isSelected -> NexfarNavy
                            else -> NexfarTextSecondary
                        }
                    )
                }
            }
        }
    }
}
