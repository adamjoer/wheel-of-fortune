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
        _guessedCharacters.value = mutableListOf()
        loadPuzzle()
    }

    private fun updatePuzzleString() {

        val builder = java.lang.StringBuilder("[^ _\\-!,.")
        _guessedCharacters.value?.forEach { char ->
            builder.append(char)
        }
        builder.append("]")

        val regexString = builder.toString()

        Log.d("GameViewModel", "generatePuzzle: current regex = \"$regexString\"")

        _wordPuzzle.value = puzzleAnswer.replace(Regex(regexString), "_")
    }

    private fun loadPuzzle() {
        val wordAndCategory = wordsAndCategories.random()

        puzzleAnswer = wordAndCategory.second.uppercase(Locale.getDefault())

        _category.value = wordAndCategory.first

        updatePuzzleString()

        Log.d("GameViewModel", "Category = \"${_category.value} Answer = \"$puzzleAnswer\"")
    }

    fun addGuessedChar(char: Char) {
        val upperCaseChar = char.uppercaseChar()

        assert(!_guessedCharacters.value!!.contains(upperCaseChar))

        _guessedCharacters.value?.add(upperCaseChar)

        updatePuzzleString()
    }

    fun isGuessRight(string: String): Boolean {
        return puzzleAnswer.equals(string, true)
    }
}