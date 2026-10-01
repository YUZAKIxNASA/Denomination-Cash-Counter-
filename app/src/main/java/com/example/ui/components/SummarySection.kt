package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NeutralText
import com.example.ui.theme.YellowDateHighlight
import com.example.ui.theme.YellowDateText
import com.example.util.IndianCurrencyUtil

@Composable
fun SummarySection(
    dateTimeDisplay: String,
    totalCount: Int,
    totalAmount: Long,
    amountInWords: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .testTag("summary_section")
    ) {
        // Date & Time row with yellow highlight badge on the right
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Date :",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = NeutralText
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(3.dp))
                    .background(YellowDateHighlight)
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
                Text(
                    text = dateTimeDisplay,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = YellowDateText
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Total Notes / Coins row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total Notes / Coins :",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = NeutralText
            )
            Text(
                text = if (totalCount == 0) "0" else IndianCurrencyUtil.formatIndianNumber(totalCount.toLong()),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = NeutralText,
                modifier = Modifier.testTag("total_count_text")
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Total Amount row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total Amount :",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = NeutralText
            )
            Text(
                text = IndianCurrencyUtil.formatRupeeDisplay(totalAmount),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = NeutralText,
                modifier = Modifier.testTag("total_amount_text")
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Amount in Words
        Text(
            text = "Grand Total in Words",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = NeutralText,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = amountInWords,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1E293B),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .testTag("amount_in_words_text")
        )
    }
}
