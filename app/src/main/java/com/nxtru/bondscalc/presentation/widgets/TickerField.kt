package com.nxtru.bondscalc.presentation.widgets

import android.view.KeyEvent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.nxtru.bondscalc.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TickerField(
    value: String,
    modifier: Modifier = Modifier,
    onDone: (String) -> Unit,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.ticker)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = { onDone(value) },
        ),
        modifier = modifier.onKeyEvent {
            if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER){
                onDone(value)
                true
            } else
                false
        }
    )
}

@Preview
@Composable
fun PreviewTickerField() {
    TickerField("ОФЗ", Modifier.fillMaxWidth(), {
        println(">>> enter1: $it")
    }) {}
}
