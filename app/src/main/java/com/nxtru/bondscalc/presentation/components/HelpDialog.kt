package com.nxtru.bondscalc.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nxtru.bondscalc.R

@Composable
fun HelpDialog(
    title: String,
    text: Array<String>,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = title)
        },
        text = {
            Column() {
                text.map { Text(text = it) }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(stringResource(R.string.help_dialog_ok))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CurrentYieldHelpDialogPreview() {
    HelpDialog(
        title = "Текущая доходность",
        text = arrayOf("Соотношение купона к цене покупки с учетом налога"),
        onDismissRequest = {}
    )
}
