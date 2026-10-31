package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun NexfarLogo(
    modifier: Modifier = Modifier,
    isDarkBackground: Boolean = true,
    compact: Boolean = false
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // High-tech Pharmaceutical Emblem
        Box(
            modifier = Modifier
                .size(if (compact) 36.dp else 44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(NexfarNavy, NexfarNavyLight)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Hexagon/Glow border
            Box(
                modifier = Modifier
                    .size(if (compact) 30.dp else 36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(NexfarTeal.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                // Cross + Pill digital icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "NEXFAR Medical Cross",
                        tint = NexfarTealAccent,
                        modifier = Modifier.size(if (compact) 18.dp else 22.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "NEX",
                    color = if (isDarkBackground) Color.White else NexfarNavy,
                    fontSize = if (compact) 18.sp else 22.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "FAR",
                    color = NexfarTeal,
                    fontSize = if (compact) 18.sp else 22.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = NexfarTeal.copy(alpha = 0.2f),
                    contentColor = NexfarTeal
                ) {
                    Text(
                        text = "SAAS",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }

            if (!compact) {
                Text(
                    text = "INTELIGÊNCIA FARMACÊUTICA",
                    color = if (isDarkBackground) NexfarTextMuted else NexfarTextSecondary,
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.8.sp
                )
            }
        }
    }
}
