package com.openclassrooms.realestatemanager.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils
import com.openclassrooms.realestatemanager.theme.AppTheme
import kotlin.math.pow
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanView() {
    var contribution by rememberSaveable { mutableStateOf("") }
    var rate by rememberSaveable { mutableStateOf("") }
    var duration by rememberSaveable { mutableStateOf("") }
    var result by rememberSaveable { mutableFloatStateOf(0.00F) }
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("$", "€")
    var selectedOptionText by remember { mutableStateOf(options[0]) }

    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Text(
                text = stringResource(R.string.property_loan),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 32.dp),
                textAlign = TextAlign.Center,
                fontSize = 32.sp,
            )
            Text(
                text = stringResource(R.string.description_loan),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp, start = 16.dp, end = 16.dp),
                textAlign = TextAlign.Justify,
                fontSize = 16.sp,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                TextField(
                    value = contribution,
                    onValueChange = { contribution = it },
                    label = { Text(stringResource(id = R.string.contribution)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                    singleLine = true,
                    modifier = Modifier.weight(2f),
                )
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f),
                ) {
                    TextField(
                        modifier = Modifier.menuAnchor(),
                        readOnly = true,
                        value = selectedOptionText,
                        onValueChange = {},
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        options.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption) },
                                onClick = {
                                    selectedOptionText = selectionOption
                                    expanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }
                    }
                }
            }

            TextField(
                value = rate,
                onValueChange = { rate = it },
                label = { Text(stringResource(id = R.string.rate)) },
                trailingIcon = { Icon(painterResource(id = R.drawable.ic_percentage), contentDescription = "contribution") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )
            TextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text(stringResource(id = R.string.duration)) },
                trailingIcon = { Icon(painterResource(id = R.drawable.ic_duration), contentDescription = "contribution") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )
            Text(
                text = "$result $selectedOptionText",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp, start = 16.dp, end = 16.dp, top = 16.dp),
                textAlign = TextAlign.Center,
                fontSize = 32.sp,
            )
            Box(
                modifier = Modifier
                    .fillMaxHeight(),
            ) {
                Button(
                    onClick = {
                        val c = if (selectedOptionText == "$") {
                            Utils.convertEuroToDollar(contribution.toInt())
                        } else {
                            contribution.toInt()
                        }
                        val r = (rate.toFloat() / 100) / 12
                        val d = duration.toInt() * 12
                        result = ((((c * r) / (1 - (1 / ((1 + r).pow(d))))) * 100).roundToInt() / 100.0).toFloat()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .align(Alignment.BottomCenter),
                ) {
                    Text(
                        text = "Simulate",
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview() {
    LoanView()
}
