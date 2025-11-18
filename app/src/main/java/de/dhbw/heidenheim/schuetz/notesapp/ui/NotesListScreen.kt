package de.dhbw.heidenheim.schuetz.notesapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.dhbw.heidenheim.schuetz.notesapp.NotesViewModel

@Composable
fun NotesListScreen(
    modifier: Modifier = Modifier, viewModel: NotesViewModel = viewModel()
) {
    val notes by viewModel.notes.collectAsState()

    // State für Dialog
    var showDialog by remember { mutableStateOf(false) }
    var editingNoteId by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    )
    {
        Button(
            onClick = {
                editingNoteId = null
                showDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
        )
        {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add icon",
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = "Neue Notiz hinzufügen",
                fontSize = 16.sp
            )
        }
        if (notes.isEmpty()) {
            EmptyNotesMessage(
                modifier = Modifier.fillMaxSize()
            )
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items = notes, key = { note -> note.id }
                ) { note ->
                    NoteItem(
                        note = note,
                        onClick = {
                            editingNoteId = note.id
                            showDialog = true
                        },
                        onDeleteClick = {
                            viewModel.deleteNote(note.id)
                        }
                    )
                }
            }
        }
    }

    // Dialog für Hinzufügen/Bearbeiten
    if (showDialog) {
        val noteToEdit = editingNoteId?.let { viewModel.getNoteById(it) }

        NoteDialog(
            note = noteToEdit,
            onDismiss = {
                showDialog = false
                editingNoteId = null
            },
            onSave = { title, content ->
                if (editingNoteId != null) {
                    viewModel.updateNote(editingNoteId!!, title, content)
                } else {
                    viewModel.addNote(title, content)
                }
                showDialog = false
                editingNoteId = null
            }
        )
    }
}