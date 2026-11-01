package news.molo.core.designsystem.component.molecule

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.util.helper.millisToOffsetDateTime
import news.molo.core.util.preview.ComponentPreview
import java.time.OffsetDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerModal(
    onDateRangeSelected: (from: OffsetDateTime, to: OffsetDateTime) -> Unit,
    onDismiss: () -> Unit
) {
    val dateRangePickerState = rememberDateRangePickerState()

    val dateRange by remember(
        dateRangePickerState.selectedStartDateMillis,
        dateRangePickerState.selectedEndDateMillis
    ) {

        val startDate = dateRangePickerState.selectedStartDateMillis?.millisToOffsetDateTime()
        val endDate = dateRangePickerState.selectedEndDateMillis?.millisToOffsetDateTime()

        mutableStateOf(
            if (startDate != null && endDate != null) {
                startDate to endDate
            } else {
                null
            }
        )
    }

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    dateRange?.let {
                        onDateRangeSelected(it.first, it.second)
                    }
                    onDismiss()
                },
                enabled = dateRange != null
            ) {
                Text("OK",)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            title = {
                Text(
                    text = "Select date range"
                )
            },
            showModeToggle = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(16.dp)
        )
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        DateRangePickerModal(
            onDismiss = {},
            onDateRangeSelected = {_, _ ->}
        )
    }
}