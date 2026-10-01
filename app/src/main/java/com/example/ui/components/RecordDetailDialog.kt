package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.CalculationRecord
import com.example.ui.theme.BlackHeader
import com.example.ui.theme.NeutralText
import com.example.util.IndianCurrencyUtil

@Composable
fun RecordDetailDialog(
    record: CalculationRecord,
    onLoad: () -> Unit,
    onShare: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("record_detail_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Header with title and close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Calculation Details",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeutralText
                        )
                        Text(
                            text = "${record.formattedDate} • ${record.formattedTime}",
                            fontSize = 13.sp,
                            color = Color(0xFF6B7280)
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_detail_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = NeutralText
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                // Breakdown list in scrollable container
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 280.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    record.toBreakdownList().forEach { (label, count) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = label,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = NeutralText,
                                modifier = Modifier.weight(0.3f)
                            )
                            Text(
                                text = "× $count",
                                fontSize = 14.sp,
                                color = Color(0xFF4B5563),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(0.3f)
                            )
                            val amountStr = if (label == "Coin") {
                                "-"
                            } else {
                                val value = label.replace("₹", "").trim().toLongOrNull() ?: 0L
                                IndianCurrencyUtil.formatRupeeDisplay(value * count)
                            }
                            Text(
                                text = amountStr,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = NeutralText,
                                textAlign = TextAlign.End,
                                modifier = Modifier.weight(0.4f)
                            )
                        }
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                // Totals
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Notes / Coins:", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    Text("${record.totalCount}", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Amount:", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    Text(
                        IndianCurrencyUtil.formatRupeeDisplay(record.totalAmount),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF047857)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = record.amountInWords,
                    fontSize = 13.sp,
                    color = Color(0xFF4B5563),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Action buttons: Load Calculation & Share
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onShare,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("share_detail_button"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text("Share")
                    }

                    Button(
                        onClick = onLoad,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("load_detail_button"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BlackHeader)
                    ) {
                        Text("Load", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
