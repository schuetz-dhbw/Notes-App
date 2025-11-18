package de.dhbw.heidenheim.schuetz.notesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import de.dhbw.heidenheim.schuetz.notesapp.ui.NoteDetailScreen
import de.dhbw.heidenheim.schuetz.notesapp.ui.NoteEditScreen
import de.dhbw.heidenheim.schuetz.notesapp.ui.NotesListScreen
import de.dhbw.heidenheim.schuetz.notesapp.ui.theme.NotesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesAppTheme {
                val viewModel: NotesViewModel = viewModel()
                val notes by viewModel.notes.collectAsState()
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = NotesListRoute,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<NotesListRoute> {
                            NotesListScreen(
                                notes = notes,
                                onNoteClick = { noteId ->
                                    navController.navigate(NoteDetailRoute(noteId))
                                },
                                onAddClick = {
                                    navController.navigate(NoteEditRoute(noteId = null))
                                },
                                onDeleteClick = { noteId ->
                                    viewModel.deleteNote(noteId)
                                }
                            )
                        }

                        composable<NoteDetailRoute> { backStackEntry ->
                            val noteDetail = backStackEntry.toRoute<NoteDetailRoute>()
                            val note = viewModel.getNoteById(noteDetail.noteId)

                            NoteDetailScreen(
                                note = note,
                                onBack = {
                                    navController.popBackStack()
                                },
                                onEdit = {
                                    navController.navigate(NoteEditRoute(noteId = noteDetail.noteId))
                                }
                            )
                        }

                        composable<NoteEditRoute> { backStackEntry ->
                            val noteEdit = backStackEntry.toRoute<NoteEditRoute>()
                            val note = noteEdit.noteId?.let { viewModel.getNoteById(it) }

                            NoteEditScreen(
                                note = note,
                                onBack = {
                                    navController.popBackStack()
                                },
                                onSave = { title, content ->
                                    if (noteEdit.noteId != null) {
                                        viewModel.updateNote(noteEdit.noteId, title, content)
                                    } else {
                                        viewModel.addNote(title, content)
                                    }
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}