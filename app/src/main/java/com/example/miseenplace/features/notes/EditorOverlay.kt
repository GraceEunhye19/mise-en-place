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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.miseenplace.R
import com.example.miseenplace.data.model.Note

val noteCardColor = Color(0xFFA6C8FF)

@Composable
fun EditorOverlay(
    existingNote: Note?,
    onSave: (String) -> Unit,
    onCancel: () -> Unit,
){
    val viewModel: NotesViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    var text by remember { mutableStateOf(existingNote?.body ?: viewModel.draftText.value) }

    LaunchedEffect(text) {
        viewModel.draftText.value = text
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
                .background(noteCardColor)
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
                IconButton(onClick = { onSave(text) }) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = stringResource(R.string.save),
                        tint = Color.Black.copy(alpha = 0.6f)
                    )
                }}

                Spacer(modifier = Modifier.height(8.dp))

                BasicTextField(
                    value = text,
                    onValueChange = {text = it},
                    modifier = Modifier
                        .fillMaxSize()
                        .focusRequester(inputFocusRequester),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.Black
                    ),
                    decorationBox = { innerTextField ->
                        Box(modifier = Modifier.fillMaxSize()){
                            if (text.isEmpty()){
                                Text(
                                    text = stringResource(R.string.empty_note_text),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.Gray.copy(alpha = 0.7f)
                                )
                            }
                            innerTextField()
                        }
                    }
                )

//                Box(
//                    modifier = Modifier.fillMaxSize()
//                ){
//                    if(text.isEmpty()){
//                        Text(
//                            text = "Log what you have eaten or recipes you want to try...",
//                            style = MaterialTheme.typography.bodyLarge,
//                            color = Color.Gray.copy(alpha = 0.6f), //should be black and why does ir have a shadow
//                            modifier = Modifier.padding(4.dp)
//                        )
//                    }
//
//                }

        }
    }
}