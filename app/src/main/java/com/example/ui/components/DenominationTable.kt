package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DenominationItem
import com.example.ui.theme.BlackHeader
import com.example.ui.theme.BorderLight
import com.example.ui.theme.NeutralText
import com.example.util.IndianCurrencyUtil

@Composable
fun DenominationTable(
    counts: Map<String, String>,
    onCountChange: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("denomination_table")
    ) {
        // Table Header with solid black background and white text
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BlackHeader)
                .padding(horizontal = 16.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Currency",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.weight(0.28f)
            )
            Text(
                text = "Count",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(0.38f)
            )
            Text(
                text = "Amount",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(0.34f)
            )
        }

        // 10 Denomination rows
        DenominationItem.ITEMS.forEachIndexed { index, item ->
            val countStr = counts[item.id] ?: ""
            val countInt = countStr.toIntOrNull() ?: 0

            val amountDisplay = if (item.isCoin) {
                "-"
            } else {
                IndianCurrencyUtil.formatRupeeDisplay(item.value * countInt)
            }

            DenominationRowItem(
                item = item,
                countText = countStr,
                amountDisplay = amountDisplay,
                onCountChange = { newText -> onCountChange(item.id, newText) },
                isLast = index == DenominationItem.ITEMS.size - 1
            )
        }
    }
}

@Composable
private fun DenominationRowItem(
    item: DenominationItem,
    countText: String,
    amountDisplay: String,
    onCountChange: (String) -> Unit,
    isLast: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("row_${item.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Currency label (e.g. ₹ 500, Coin)
        Text(
            text = item.label,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = NeutralText,
            modifier = Modifier.weight(0.28f)
        )

        // Count input with "×" and clean underline
        Row(
            modifier = Modifier.weight(0.38f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "×",
                fontSize = 19.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF6B7280),
                modifier = Modifier.padding(end = 6.dp)
            )

            // Underline text input box
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .height(38.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                BasicTextField(
                    value = countText,
                    onValueChange = onCountChange,
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = NeutralText,
                        textAlign = TextAlign.Start
                    ),
                    cursorBrush = SolidColor(NeutralText),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = if (isLast) ImeAction.Done else ImeAction.Next
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_${item.id}"),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(36.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            innerTextField()
                            // Bottom underline matching the design
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.2.dp)
                                    .background(Color(0xFF9CA3AF))
                                    .align(Alignment.BottomCenter)
                            )
                        }
                    }
                )
            }
        }

        // Amount display (e.g. ₹ 0 /-, ₹ 1,500 /-, or -)
        Text(
            text = amountDisplay,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = NeutralText,
            textAlign = TextAlign.End,
            modifier = Modifier
                .weight(0.34f)
                .testTag("amount_${item.id}")
        )
    }
}
