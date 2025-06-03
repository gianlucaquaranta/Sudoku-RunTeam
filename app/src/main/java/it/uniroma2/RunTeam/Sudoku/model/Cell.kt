package it.uniroma2.RunTeam.Sudoku.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

// Non è necessario @Stable esplicitamente per le data class se tutti i parametri del costruttore sono val
// e i tipi sono stabili (primitivi, String, altre classi stabili, o State<T>).
// In questo caso, con `var value by mutableStateOf`, è implicitamente gestito per la ricomposizione.
data class Cell(
    val row: Int,
    val col: Int,
    val isStartingCell: Boolean = false, // Questo è il tuo "isInitial"
    val initialValue: Int? = null, // Questo è il tuo "initialValue"
    val oldValue: Int?,
    val newValue: Int?
) {
    var value: Int by mutableStateOf(initialValue ?: 0) // Valore iniziale, 0 per vuoto
        private set // Rendiamo il setter privato se vogliamo che sia modificato solo tramite metodi specifici della classe o da GameScreen

    var isSelected: Boolean by mutableStateOf(false) // Useremo uno stato separato in GameScreen per la cella attiva
    // ma questo può essere usato per altri scopi di UI
    var isIncorrect: Boolean by mutableStateOf(false)
    var notes: Set<Int> by mutableStateOf(emptySet())


    // Metodo pubblico per cambiare il valore (se non è una cella iniziale)
    fun updateValue(newValue: Int) {
        if (!isStartingCell) {
            value = newValue
            if (newValue != 0) { // Se si imposta un valore, le note vengono cancellate
                clearNotes()
                isIncorrect = false // Resetta lo stato di errore quando si cambia valore (la validazione avverrà dopo)
            }
        }
    }

    fun clearValue() {
        if (!isStartingCell) {
            value = 0 // 0 per vuoto
            isIncorrect = false
            // Non cancellare le note quando si cancella solo il valore, potrebbe essere intenzionale
        }
    }

    fun toggleNote(noteValue: Int) {
        if (!isStartingCell && value == 0) { // Le note si possono aggiungere solo se la cella è vuota
            notes = if (notes.contains(noteValue)) {
                notes - noteValue
            } else {
                notes + noteValue
            }
        }
    }

    fun clearNotes() {
        if (!isStartingCell) {
            notes = emptySet()
        }
    }

    // Proprietà calcolata per la UI
    val displayValue: String
        get() = if (value != 0) value.toString() else ""

    // Per il confronto corretto nelle selezioni
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Cell
        return row == other.row && col == other.col
    }

    override fun hashCode(): Int {
        var result = row
        result = 31 * result + col
        return result
    }
}