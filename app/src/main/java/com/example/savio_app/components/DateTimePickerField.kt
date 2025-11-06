package com.example.savio_app.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickerField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    var dateTime = try {
        LocalDateTime.parse(value, formatter)
    } catch (e: Exception) {
        LocalDateTime.now()
    }

    OutlinedTextField(
        value = value,
        onValueChange = { },
        label = { Text(label) },
        readOnly = true,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = "Seleccionar fecha y hora",
                modifier = Modifier.clickable {
                    DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            TimePickerDialog(
                                context,
                                { _, hourOfDay, minute ->
                                    dateTime = LocalDateTime.of(year, month + 1, dayOfMonth, hourOfDay, minute)
                                    onValueChange(dateTime.format(formatter))
                                },
                                dateTime.hour,
                                dateTime.minute,
                                true
                            ).show()
                        },
                        dateTime.year,
                        dateTime.monthValue - 1,
                        dateTime.dayOfMonth
                    ).show()
                }
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        TimePickerDialog(
                            context,
                            { _, hourOfDay, minute ->
                                dateTime = LocalDateTime.of(year, month + 1, dayOfMonth, hourOfDay, minute)
                                onValueChange(dateTime.format(formatter))
                            },
                            dateTime.hour,
                            dateTime.minute,
                            true
                        ).show()
                    },
                    dateTime.year,
                    dateTime.monthValue - 1,
                    dateTime.dayOfMonth
                ).show()
            }
    )
}
