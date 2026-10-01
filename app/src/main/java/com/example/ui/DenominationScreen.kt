package com.example.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CalculationRecord
import com.example.ui.components.ClearConfirmDialog
import com.example.ui.components.DenominationTable
import com.example.ui.components.HistoryDialog
import com.example.ui.components.SummarySection
import com.example.ui.theme.BlackHeader
import com.example.ui.theme.SubtleGrayBackground
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

    // Listen to toast / feedback events from ViewModel
    LaunchedEffect(Unit) {
        viewModel.toastEvents.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    // Helper functions for sharing and clipboard
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
            // Fallback to clipboard
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
        containerColor = SubtleGrayBackground,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Denomination (Cash Counter)",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                actions = {
                    // Save Button
                    IconButton(
                        onClick = { viewModel.saveCalculation() },
                        modifier = Modifier.testTag("save_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Save Calculation",
                            tint = Color.White
                        )
                    }

                    // Share Button
                    IconButton(
                        onClick = { shareText(viewModel.getShareText()) },
                        modifier = Modifier.testTag("share_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share Calculation",
                            tint = Color.White
                        )
                    }

                    // Clear Button
                    IconButton(
                        onClick = { showClearConfirm = true },
                        modifier = Modifier.testTag("clear_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Clear Current Calculation",
                            tint = Color.White
                        )
                    }

                    // History Button
                    IconButton(
                        onClick = { showHistoryDialog = true },
                        modifier = Modifier.testTag("history_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "History",
                            tint = Color.White
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
                .background(SubtleGrayBackground),
            contentAlignment = Alignment.TopCenter
        ) {
            // Mobile-first content card with natural content height
            // Centered on wide screens (desktop/tablet) with max width 480.dp
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 480.dp)
                    .background(WhiteBackground)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 16.dp)
                    .testTag("main_content_container")
            ) {
                // Summary Section with live date, total notes/coins, total amount, and words
                SummarySection(
                    dateTimeDisplay = currentDateTimeDisplay,
                    totalCount = totalCount,
                    totalAmount = totalAmount,
                    amountInWords = amountInWords
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Denomination Table: Currency | Count | Amount
                DenominationTable(
                    counts = counts,
                    onCountChange = { id, text -> viewModel.onCountChanged(id, text) }
                )

                // The container naturally ends right after the Coin row + normal bottom padding!
                // No artificial spacers or excessive blank space.
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

        // History Dialog / Sheet
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
