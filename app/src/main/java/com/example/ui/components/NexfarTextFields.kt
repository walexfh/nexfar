package com.example.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun nexfarInputColors(
    containerColor: Color = NexfarIceBg,
    textColor: Color = NexfarNavy
) = OutlinedTextFieldDefaults.colors(
    focusedTextColor = textColor,
    unfocusedTextColor = textColor,
    focusedBorderColor = NexfarTeal,
    unfocusedBorderColor = NexfarCardBorder,
    focusedContainerColor = containerColor,
    unfocusedContainerColor = containerColor,
    focusedLabelColor = NexfarTeal,
    unfocusedLabelColor = NexfarTextSecondary,
    focusedPlaceholderColor = NexfarTextMuted,
    unfocusedPlaceholderColor = NexfarTextMuted,
    cursorColor = NexfarTeal
)

@Composable
fun NexfarInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    placeholder: String? = null,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = label?.let { { Text(it, fontSize = 12.sp, color = NexfarTextSecondary) } },
        placeholder = placeholder?.let { { Text(it, fontSize = 13.sp, color = NexfarTextMuted) } },
        modifier = modifier,
        keyboardOptions = keyboardOptions,
        singleLine = singleLine,
        maxLines = maxLines,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        shape = RoundedCornerShape(10.dp),
        textStyle = TextStyle(
            color = NexfarNavy,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        ),
        colors = nexfarInputColors()
    )
}
