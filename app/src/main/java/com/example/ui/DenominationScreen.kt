package com.example.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ClearConfirmDialog
import com.example.ui.components.DenominationTable
import com.example.ui.components.HistoryDialog
import com.example.ui.components.SummarySection
import com.example.ui.theme.BlackHeader
import com.example.ui.theme.WhiteBackground
import com.example.viewmodel.DenominationViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DenominationScreen(
    viewModel: DenominationViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val counts by viewModel.counts.collectAsState()
    val totalCount by viewModel.totalCount.collectAsState()
    val totalAmount by viewModel.totalAmount.collectAsState()
    val amountInWords by viewModel.amountInWords.collectAsState()
    val currentDateTimeDisplay by viewModel.currentDateTimeDisplay.collectAsState()
    val historyRecords by viewModel.historyRecords.collectAsState()

    var showClearConfirm by remember { mutableStateOf(false) }
    var showHistoryDialog by remember { mutableStateOf(false) }

    // Intercept back button if dialogs are visible
    BackHandler(enabled = showHistoryDialog || showClearConfirm) {
        if (showClearConfirm) {
            showClearConfirm = false
        } else if (showHistoryDialog) {
            showHistoryDialog = false
        }
    }

    // Listen to toast / feedback events from ViewModel
    LaunchedEffect(Unit) {
        viewModel.toastEvents.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    // Native sharing with clipboard fallback
    fun shareText(text: String) {
        try {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, "Share Denomination Breakdown")
            context.startActivity(shareIntent)
        } catch (e: Exception) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Denomination Breakdown", text)
            clipboard.setPrimaryClip(clip)
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Copied breakdown to clipboard")
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = WhiteBackground,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        modifier = Modifier.padding(vertical = 3.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "DENOMINATION",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            softWrap = false
                        )
                        Text(
                            text = "(CASH COUNTER)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFE5E7EB),
                            softWrap = false
                        )
                        Text(
                            text = "@yuzaki_x_nasa | @ঔৣ፝ N4!",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF9CA3AF),
                            softWrap = false
                        )
                    }
                },
                actions = {
                    // Save Button
                    IconButton(
                        onClick = { viewModel.saveCalculation() },
                        modifier = Modifier
                            .size(38.dp)
                            .testTag("save_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Save",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Share Button
                    IconButton(
                        onClick = { shareText(viewModel.getShareText()) },
                        modifier = Modifier
                            .size(38.dp)
                            .testTag("share_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Clear Button
                    IconButton(
                        onClick = { showClearConfirm = true },
                        modifier = Modifier
                            .size(38.dp)
                            .testTag("clear_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Clear",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // History Button
                    IconButton(
                        onClick = { showHistoryDialog = true },
                        modifier = Modifier
                            .size(38.dp)
                            .testTag("history_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "History",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BlackHeader)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(WhiteBackground),
            contentAlignment = Alignment.TopCenter
        ) {
            // Mobile-first natural content height column
            // Centers on tablets/large screens up to 480.dp
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 480.dp)
                    .background(WhiteBackground)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 16.dp)
                    .testTag("main_content_container")
            ) {
                // Summary Section: Date & Time, Total Notes/Coins, Total Amount, Grand Total in Words
                SummarySection(
                    dateTimeDisplay = currentDateTimeDisplay,
                    totalCount = totalCount,
                    totalAmount = totalAmount,
                    amountInWords = amountInWords
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Denomination Table: Currency | Count | Amount
                // Strictly 10 rows: ₹500, ₹200, ₹100, ₹50, ₹20, ₹10, ₹5, ₹2, ₹1, Coin
                DenominationTable(
                    counts = counts,
                    onCountChange = { id, text -> viewModel.onCountChanged(id, text) }
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Bottom credit placed naturally near the bottom of the content (Section 11)
                Text(
                    text = "@yuzaki_x_nasa | @ঔৣ፝ N4!",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF9CA3AF),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("bottom_credit")
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Clear Confirmation Dialog
        if (showClearConfirm) {
            ClearConfirmDialog(
                onConfirm = {
                    showClearConfirm = false
                    viewModel.clearCurrent()
                },
                onDismiss = { showClearConfirm = false }
            )
        }

        // History Dialog
        if (showHistoryDialog) {
            HistoryDialog(
                records = historyRecords,
                onLoadRecord = { record -> viewModel.loadCalculation(record) },
                onShareRecord = { record -> shareText(viewModel.getShareTextForRecord(record)) },
                onDeleteRecord = { record -> viewModel.deleteRecord(record) },
                onClearAll = { viewModel.clearAllHistory() },
                onDismiss = { showHistoryDialog = false }
            )
        }
    }
}
