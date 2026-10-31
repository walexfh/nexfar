package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.model.PlanTier
import com.example.ui.theme.*

@Composable
fun PlansScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedPlanForCheckout by remember { mutableStateOf<PlanTier?>(null) }
    var billingIntervalAnnual by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NexfarIceBg),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Commercial Header
        item {
            Surface(
                color = NexfarNavy,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = NexfarTeal.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "NEXFAR SAAS COMMERCIAL",
                            color = NexfarTealAccent,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Planos & Assinaturas",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Ferramentas essenciais para o balcão da farmácia, RT e redes farmacêuticas.",
                        color = NexfarTextMuted,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Billing Cycle Switch
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = NexfarNavyDark
                    ) {
                        Row(
                            modifier = Modifier.padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FilterChip(
                                selected = !billingIntervalAnnual,
                                onClick = { billingIntervalAnnual = false },
                                label = { Text("Mensal", fontSize = 11.sp) }
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            FilterChip(
                                selected = billingIntervalAnnual,
                                onClick = { billingIntervalAnnual = true },
                                label = { Text("Anual (15% OFF)", fontSize = 11.sp) }
                            )
                        }
                    }
                }
            }
        }

        // Plans Cards List
        items(PlanTier.values()) { plan ->
            val isHighlighted = plan.isHighlighted

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .testTag("plan_card_${plan.name.lowercase()}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isHighlighted) Color.White else Color.White
                ),
                border = if (isHighlighted) androidx.compose.foundation.BorderStroke(2.dp, NexfarTeal) else CardDefaults.outlinedCardBorder(),
                elevation = CardDefaults.cardElevation(defaultElevation = if (isHighlighted) 6.dp else 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    if (isHighlighted) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = NexfarTeal
                        ) {
                            Text(
                                text = "MAIS ESCOLHIDO POR FARMACÊUTICOS",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = plan.planName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = NexfarNavy
                            )
                            Text(
                                text = plan.targetAudience,
                                fontSize = 11.sp,
                                color = NexfarTextSecondary
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = plan.priceMonthly,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = NexfarTealDark
                            )
                            Text(
                                text = plan.billingNote,
                                fontSize = 10.sp,
                                color = NexfarTextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = NexfarCardBorder)
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Recursos Inclusos:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NexfarNavy)
                    plan.features.forEach { feat ->
                        Row(
                            modifier = Modifier.padding(top = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = NexfarTeal,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = feat,
                                fontSize = 12.sp,
                                color = NexfarTextPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { selectedPlanForCheckout = plan },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("subscribe_btn_${plan.name.lowercase()}"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isHighlighted) NexfarTeal else NexfarNavy
                        )
                    ) {
                        Text(
                            text = "Contratar ${plan.planName}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }

    // Demonstrative Checkout Dialog
    selectedPlanForCheckout?.let { plan ->
        AlertDialog(
            onDismissRequest = { selectedPlanForCheckout = null },
            title = {
                Text(
                    text = "Contratação — ${plan.planName}",
                    fontWeight = FontWeight.Bold,
                    color = NexfarNavy
                )
            },
            text = {
                Column {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFFEF3C7)
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFFB45309), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "AMBIENTE DEMONSTRATIVO: Nenhum valor real será cobrado nesta etapa. Gateways definitivos serão acoplados pelo Antigravity.",
                                fontSize = 10.sp,
                                color = Color(0xFF78350F)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Resumo do Pedido:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NexfarNavy)
                    Text("• Plano: ${plan.planName} (${plan.priceMonthly})", fontSize = 12.sp, color = NexfarTextPrimary)
                    Text("• Método Demonstrativo: PIX Instantâneo / Cartão de Crédito", fontSize = 12.sp, color = NexfarTextPrimary)
                    Text("• Renovação: Recorrente mensal com cancelamento a qualquer momento", fontSize = 11.sp, color = NexfarTextMuted)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        Toast.makeText(context, "Plano ${plan.planName} ativado com sucesso em modo demonstração!", Toast.LENGTH_LONG).show()
                        selectedPlanForCheckout = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NexfarNavy)
                ) {
                    Text("Confirmar Assinatura (Demonstração)")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedPlanForCheckout = null }) {
                    Text("Fechar")
                }
            }
        )
    }
}
