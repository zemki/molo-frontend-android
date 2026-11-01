package news.molo.core.designsystem.component.organism

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import news.molo.core.designsystem.component.atom.TextButton
import news.molo.core.designsystem.component.molecule.ClickableField
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.MoloOrange
import news.molo.core.util.preview.ComponentPreview
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventTimeRangeDialog(
    onDismiss: () -> Unit,
    onSave: (OffsetDateTime, OffsetDateTime) -> Unit,
    initialStartDate: OffsetDateTime? = null,
    initialEndDate: OffsetDateTime? = null
) {
    var fromDateTime: LocalDateTime by remember {
        mutableStateOf(
            initialStartDate?.toLocalDateTime() ?: LocalDateTime.now().plusHours(1).withMinute(0)
                .withSecond(0).withNano(0)
        )
    }
    var untilDateTime: LocalDateTime by remember {
        mutableStateOf(initialEndDate?.toLocalDateTime() ?: fromDateTime.plusHours(1))
    }

    val context = LocalContext.current

    var showFromDatePicker by remember { mutableStateOf(false) }
    var showFromTimePicker by remember { mutableStateOf(false) }
    val fromDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = fromDateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
    )

    val fromTimePickerState = rememberTimePickerState(
        initialHour = fromDateTime.hour,
        initialMinute = fromDateTime.minute,
        is24Hour = true
    )

    var showUntilDatePicker by remember { mutableStateOf(false) }
    var showUntilTimePicker by remember { mutableStateOf(false) }
    val untilDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = untilDateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
    )

    val untilTimePickerState = rememberTimePickerState(
        initialHour = untilDateTime.hour,
        initialMinute = untilDateTime.minute,
        is24Hour = true
    )

    val dateFormatter = remember { DateTimeFormatter.ofPattern("EEE, d. MMM yyyy") }
    val timeFormatter = remember { DateTimeFormatter.ofPattern("HH:mm 'Uhr'") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Zeitraum auswählen") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                Text(
                    text = "Von",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.MoloOrange,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ClickableField(
                        text = fromDateTime.format(dateFormatter),
                        icon = Icons.Default.DateRange,
                        onClick = { showFromDatePicker = true },
                    )
                    ClickableField(
                        text = fromDateTime.format(timeFormatter),
                        icon = Icons.Default.Schedule,
                        onClick = { showFromTimePicker = true },
                    )
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Bis",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.MoloOrange,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ClickableField(
                        text = untilDateTime.format(dateFormatter),
                        icon = Icons.Default.DateRange,
                        onClick = { showUntilDatePicker = true },
                    )
                    ClickableField(
                        text = untilDateTime.format(timeFormatter),
                        icon = Icons.Default.Schedule,
                        onClick = { showUntilTimePicker = true },
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (untilDateTime.isAfter(fromDateTime)) {
                    val zoneId = ZoneId.systemDefault()
                    val fromOffsetDateTime = fromDateTime.atZone(zoneId).toOffsetDateTime()
                    val untilOffsetDateTime = untilDateTime.atZone(zoneId).toOffsetDateTime()
                    onSave(fromOffsetDateTime, untilOffsetDateTime)
                } else {
                    Toast.makeText(
                        context,
                        "Endzeit muss nach der Startzeit liegen",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }, text = "Speichern")
        },
        dismissButton = {
            TextButton(onClick = onDismiss, text = "Abbrechen")
        }
    )

    if (showFromDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showFromDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showFromDatePicker = false
                    fromDatePickerState.selectedDateMillis?.let { millis ->
                        val newDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        fromDateTime = fromDateTime.with(newDate)

                        if (untilDateTime.toLocalDate().isBefore(newDate)) {
                            untilDateTime = untilDateTime.with(newDate)
                            untilDatePickerState.selectedDateMillis =
                                untilDateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
                        }
                    }
                }, text = "OK")
            },
            dismissButton = {
                TextButton(onClick = { showFromDatePicker = false }, text = "Abbrechen")
            }
        ) {
            DatePicker(
                state = fromDatePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = Color.MoloOrange,
                    selectedYearContainerColor = Color.MoloOrange,
                    todayDateBorderColor = Color.MoloOrange,
                    todayContentColor = Color.MoloOrange
                ),
                showModeToggle = false
            )
        }
    }

    if (showFromTimePicker) {
        TimePickerDialog(
            onDismissRequest = { showFromTimePicker = false },
            title = { Text("Startzeit auswählen") },
            confirmButton = {
                TextButton(onClick = {
                    showFromTimePicker = false
                    val newTime = LocalTime.of(fromTimePickerState.hour, fromTimePickerState.minute)
                    fromDateTime = fromDateTime.with(newTime)

                    if (untilDateTime.isBefore(fromDateTime)) {
                        untilDateTime = fromDateTime.plusHours(1)
                        untilDatePickerState.selectedDateMillis =
                            untilDateTime.toInstant(ZoneOffset.UTC).toEpochMilli()

                        untilTimePickerState.hour = untilDateTime.hour
                        untilTimePickerState.minute = untilDateTime.minute
                    }
                }, text = "OK")
            },
            dismissButton = {
                TextButton(onClick = { showFromTimePicker = false }, text = "Abbrechen")
            }
        ) {
            TimePicker(
                state = fromTimePickerState,
                colors = TimePickerDefaults.colors(selectorColor = Color.MoloOrange)
            )
        }
    }

    if (showUntilDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showUntilDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showUntilDatePicker = false
                    untilDatePickerState.selectedDateMillis?.let { millis ->
                        val newDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()

                        if (newDate.isBefore(fromDateTime.toLocalDate())) {
                            untilDateTime = untilDateTime.with(fromDateTime.toLocalDate())
                            untilDatePickerState.selectedDateMillis =
                                fromDateTime.toInstant(ZoneOffset.UTC).toEpochMilli()

                        } else {
                            untilDateTime = untilDateTime.with(newDate)
                        }
                    }
                }, text = "OK")
            },
            dismissButton = {
                TextButton(onClick = { showUntilDatePicker = false }, text = "Abbrechen")
            }
        ) {
            DatePicker(
                state = untilDatePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = Color.MoloOrange,
                    selectedYearContainerColor = Color.MoloOrange,
                    todayDateBorderColor = Color.MoloOrange,
                    todayContentColor = Color.MoloOrange
                ),
                showModeToggle = false
            )
        }
    }

    if (showUntilTimePicker) {
        TimePickerDialog(
            onDismissRequest = { showUntilTimePicker = false },
            title = { Text("Endzeit auswählen") },
            confirmButton = {
                TextButton(onClick = {
                    showUntilTimePicker = false
                    val newTime =
                        LocalTime.of(untilTimePickerState.hour, untilTimePickerState.minute)
                    val newDateTime = untilDateTime.with(newTime)

                    if (newDateTime.isBefore(fromDateTime)) {
                        untilDateTime = fromDateTime.plusHours(1)
                        untilTimePickerState.hour = untilDateTime.hour
                        untilTimePickerState.minute = untilDateTime.minute
                        untilDatePickerState.selectedDateMillis =
                            untilDateTime.toInstant(ZoneOffset.UTC).toEpochMilli()

                    } else {
                        untilDateTime = newDateTime
                    }
                }, text = "OK")
            },
            dismissButton = {
                TextButton(onClick = { showUntilTimePicker = false }, text = "Abbrechen")
            }
        ) {
            TimePicker(
                state = untilTimePickerState,
                colors = TimePickerDefaults.colors(selectorColor = Color.MoloOrange)
            )
        }
    }
}


@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        EventTimeRangeDialog(
            onDismiss = {},
            onSave = { _, _ -> }
        )
    }
}