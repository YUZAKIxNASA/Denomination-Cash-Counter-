package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.CalculationRecord
import com.example.ui.theme.BlackHeader
import com.example.ui.theme.NeutralText
import com.example.util.IndianCurrencyUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDialog(
    records: List<CalculationRecord>,
    onLoadRecord: (CalculationRecord) -> Unit,
    onShareRecord: (CalculationRecord) -> Unit,
    onDeleteRecord: (CalculationRecord) -> Unit,
    onClearAll: () -> Unit,
    onDismiss: () -> Unit
) {
    var selectedRecordForDetail by remember { mutableStateOf<CalculationRecord?>(null) }
    var recordToDelete by remember { mutableStateOf<CalculationRecord?>(null) }
    var showClearAllConfirm by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFF9FAFB)
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Calculation History",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = onDismiss,
                                modifier = Modifier.testTag("history_back_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White
                                )
                            }
                        },
                        actions = {
                            if (records.isNotEmpty()) {
                                IconButton(
                                    onClick = { showClearAllConfirm = true },
                                    modifier = Modifier.testTag("clear_all_history_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteSweep,
                                        contentDescription = "Clear All History",
                                        tint = Color.White
                                    )
                                }
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
                ) {
                    if (records.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                modifier = Modifier.height(64.dp).width(64.dp),
                                tint = Color(0xFF9CA3AF)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No saved calculations yet",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF4B5563)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Tap the save icon in the header to save current calculations.",
                                fontSize = 14.sp,
                                color = Color(0xFF6B7280),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .testTag("history_list"),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(records, key = { it.id }) { record ->
                                HistoryCardItem(
                                    record = record,
                                    onViewDetail = { selectedRecordForDetail = record },
                                    onLoad = {
                                        onLoadRecord(record)
                                        onDismiss()
                                    },
                                    onShare = { onShareRecord(record) },
                                    onDeleteRequest = { recordToDelete = record }
                                )
                            }
                        }
                    }
                }
            }

            // Detail Dialog
            selectedRecordForDetail?.let { record ->
                RecordDetailDialog(
                    record = record,
                    onLoad = {
                        onLoadRecord(record)
                        selectedRecordForDetail = null
                        onDismiss()
                    },
                    onShare = {
                        onShareRecord(record)
                    },
                    onDismiss = { selectedRecordForDetail = null }
                )
            }

            // Single Record Delete Confirm Dialog
            recordToDelete?.let { record ->
                AlertDialog(
                    onDismissRequest = { recordToDelete = null },
                    title = {
                        Text("Delete this record?", fontWeight = FontWeight.Bold)
                    },
                    text = {
                        Text("This calculation from ${record.formattedDate} will be permanently removed.")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                val toDelete = recordToDelete
                                recordToDelete = null
                                if (toDelete != null) onDeleteRecord(toDelete)
                            },
                            colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFDC2626))
                        ) {
                            Text("Delete", fontWeight = FontWeight.Bold)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { recordToDelete = null }) {
                            Text("Cancel")
                        }
                    }
                )
            }

            // Clear All Confirm Dialog
            if (showClearAllConfirm) {
                AlertDialog(
                    onDismissRequest = { showClearAllConfirm = false },
                    title = {
                        Text("Clear all history?", fontWeight = FontWeight.Bold)
                    },
                    text = {
                        Text("Are you sure you want to delete all saved calculations? This action cannot be undone.")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showClearAllConfirm = false
                                onClearAll()
                            },
                            colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFDC2626))
                        ) {
                            Text("Clear All", fontWeight = FontWeight.Bold)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showClearAllConfirm = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun HistoryCardItem(
    record: CalculationRecord,
    onViewDetail: () -> Unit,
    onLoad: () -> Unit,
    onShare: () -> Unit,
    onDeleteRequest: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onViewDetail)
            .testTag("history_item_${record.id}"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Date & Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${record.formattedDate} • ${record.formattedTime}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF4B5563)
                )
                Text(
                    text = "${record.totalCount} notes/coins",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2563EB)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Amount
            Text(
                text = IndianCurrencyUtil.formatRupeeDisplay(record.totalAmount),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = NeutralText
            )

            // Words preview
            Text(
                text = record.amountInWords,
                fontSize = 12.sp,
                color = Color(0xFF6B7280),
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 2.dp)
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFF3F4F6))

            // Actions row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // View details
                TextButton(
                    onClick = onViewDetail,
                    modifier = Modifier.testTag("view_details_${record.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier.height(16.dp).width(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("View", fontSize = 13.sp)
                }

                // Load calculation
                TextButton(
                    onClick = onLoad,
                    modifier = Modifier.testTag("load_${record.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.height(16.dp).width(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Load", fontSize = 13.sp)
                }

                // Share
                IconButton(
                    onClick = onShare,
                    modifier = Modifier.testTag("share_${record.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color(0xFF4B5563),
                        modifier = Modifier.height(18.dp).width(18.dp)
                    )
                }

                // Delete
                IconButton(
                    onClick = onDeleteRequest,
                    modifier = Modifier.testTag("delete_${record.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.height(18.dp).width(18.dp)
                    )
                }
            }
        }
    }
}
