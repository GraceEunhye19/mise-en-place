package com.example.miseenplace.features.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.miseenplace.R
import com.example.miseenplace.data.model.Task
import com.example.miseenplace.data.model.TaskItem

val taskCardColor = Color(0xFFA6C8FF)

@Composable
fun TaskEditorOverlay(
    existingTask: Task?,
    onSave: (String, List<TaskItem>) -> Unit,
    onCancel: () -> Unit
){
    var title by remember { mutableStateOf(existingTask?.title ?: "") }
    var items by remember {
        mutableStateOf(
            if (existingTask != null) existingTask.items.map { it.text to it.isChecked }.toMutableList()
            else mutableListOf("" to false)
        )
    }
    val inputFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {inputFocusRequester.requestFocus() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.75f)
                .clip(RoundedCornerShape(28.dp))
                .background(taskCardColor)
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onCancel) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.close),
                        tint = Color.Black.copy(alpha = 0.6f)
                    )
                }
                IconButton(onClick = { onSave(title, items.map { TaskItem(it.first, it.second) }) }) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = stringResource(R.string.save),
                        tint = Color.Black.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            BasicTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(inputFocusRequester),
                textStyle = MaterialTheme.typography.titleMedium.copy(
                    color = Color.Black
                ),
                decorationBox = { innerTextField ->
                    Box{
                        if (title.isEmpty()){
                            Text(
                                text = stringResource(R.string.list_title),
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Black.copy(alpha = 0.4f)
                            )
                        }
                        innerTextField()
                    }

                }

            )

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(color = Color.Black.copy(alpha = 0.15f))

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items.forEachIndexed { index, (text, isChecked) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { checked ->
                                items = items.toMutableList().also {
                                    it[index] = text to checked
                                }
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color.Black.copy(alpha = 0.5f),
                                uncheckedColor = Color.Black.copy(alpha = 0.4f)
                            )
                        )

                        BasicTextField(
                            value = text,
                            onValueChange = { newText ->
                                items = items.toMutableList().also {
                                    it[index] = newText to isChecked
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .alpha(if (isChecked) 0.4f else 1f),
                            textStyle = TextStyle(
                                color = Color.Black,
                                textDecoration = if (isChecked)
                                    TextDecoration.LineThrough else TextDecoration.None,
                                fontSize = MaterialTheme.typography.bodyLarge.fontSize
                            ),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    items = items.toMutableList().also {
                                        it.add(index + 1, "" to false)
                                    }
                                }
                            ),
                            decorationBox = { innerTextField ->
                                Box{
                                    if (text.isEmpty()){
                                        Text(
                                            text = stringResource(R.string.list_items),
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color.Black.copy(alpha = 0.4f)
                                        )
                                    }
                                    innerTextField()
                                }

                            }
                        )
                    }
                }
            }
        }
    }
}
