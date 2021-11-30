package dtu.ux62550.wheeloffortune.ui.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

const val START_LIVES = 5

class GameViewModel : ViewModel() {
    private val _lives = MutableLiveData(START_LIVES)
    val lives: LiveData<Int>
        get() = _lives

    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    private val _wordPuzzle = MutableLiveData<String>()
    val wordPuzzle: LiveData<String>
        get() = _wordPuzzle

    private val _category = MutableLiveData<String>()
    val category: LiveData<String>
        get() = _category

    private val _guessedCharacters = MutableLiveData<MutableList<Char>>()
    val guessedCharacters: LiveData<MutableList<Char>>
        get() = _guessedCharacters

    private lateinit var puzzleAnswer: String

    init {
        loadPuzzle()
    }

    private fun loadPuzzle() {
        wordAndCategory = wordsAndCategories.random()

        // val foo = currentWordAndCategory.second.replace(Regex("[a-zA-Z0-9]"), "_")

        _currentCategory.value = wordAndCategory.first
        _currentWordPuzzle.value = wordAndCategory.second.uppercase(Locale.getDefault())

        Log.d("GameViewModel", "wordAndCategory = $wordAndCategory")
    }
}
