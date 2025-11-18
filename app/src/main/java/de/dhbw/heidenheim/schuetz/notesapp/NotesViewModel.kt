package de.dhbw.heidenheim.schuetz.notesapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.ZonedDateTime

class NotesViewModel : ViewModel() {
    // State für die Notizen-Liste
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    // Zähler für IDs
    private var nextId = 4

    init {
        // Beispiel-Notizen beim Start
        _notes.value = listOf(
            Note(
                id = 1,
                title = "Einkaufsliste",
                note = "Milch, Brot, Käse, Eier, Butter",
                timestamp = ZonedDateTime.now().minusDays(2)
            ),
            Note(
                id = 2,
                title = "Meeting Notizen",
                note = "Projekt-Update besprechen, Deadline klären, Team-Feedback einholen",
                timestamp = ZonedDateTime.now().minusDays(1)
            ),
            Note(
                id = 3,
                title = "Ideen für App",
                note = "Dark Mode implementieren, Suchfunktion hinzufügen, Cloud-Sync überlegen",
                timestamp = ZonedDateTime.now()
            )
        ).sortedByDescending { it.timestamp }
    }

    // Neue Notiz hinzufügen
    fun addNote(title: String, content: String) {
        val newNote = Note(
            id = nextId++,
            title = title,
            note = content,
            timestamp = ZonedDateTime.now()
        )
        _notes.value = (_notes.value + newNote).sortedByDescending { it.timestamp }
    }

    // Notiz löschen
    fun deleteNote(id: Int) {
        _notes.value = _notes.value.filter { it.id != id }
    }

    // Notiz bearbeiten
    fun updateNote(id: Int, title: String, content: String) {
        _notes.value = _notes.value.map { note ->
            if (note.id == id) {
                note.copy(
                    title = title,
                    note = content,
                    timestamp = ZonedDateTime.now()
                )
            } else {
                note
            }
        }.sortedByDescending { it.timestamp }
    }

    // Notiz nach ID finden
    fun getNoteById(id: Int): Note? {
        return _notes.value.find { it.id == id }
    }
}

data class Note(
    val id: Int,
    val title: String = "",
    val note: String = "",
    val timestamp: ZonedDateTime? = null
)