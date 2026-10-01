package com.example.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BlackHeader
import com.example.ui.theme.NeutralText

@Composable
fun ClearConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Clear current calculation?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = NeutralText
            )
        },
        text = {
            Text(
                text = "All entered counts will be reset.",
                fontSize = 15.sp,
                color = Color(0xFF4B5563)
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                modifier = Modifier.testTag("confirm_clear_button"),
                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFDC2626))
            ) {
                Text("Clear", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("cancel_clear_button"),
                colors = ButtonDefaults.textButtonColors(contentColor = NeutralText)
            ) {
                Text("Cancel")
            }
        },
        containerColor = Color.White
    )
}
