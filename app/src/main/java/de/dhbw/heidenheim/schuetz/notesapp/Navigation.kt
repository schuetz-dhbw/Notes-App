package de.dhbw.heidenheim.schuetz.notesapp

import kotlinx.serialization.Serializable

// Basis-Routen
@Serializable
object NotesListRoute

@Serializable
data class NoteDetailRoute(val noteId: Int)

@Serializable
data class NoteEditRoute(val noteId: Int? = null)