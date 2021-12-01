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

    lateinit var puzzleAnswer: String

    private var charactersToBeGuessed = -1

    private var charactersGuessed = 0

    init {
        _guessedCharacters.value = mutableListOf()
        loadPuzzle()
    }

    private fun updatePuzzleString() {
        val regexBuilder = StringBuilder("[^ _\\-!,.")
        for (char in _guessedCharacters.value!!) {
            regexBuilder.append(char)
        }
        regexBuilder.append("]")

        _wordPuzzle.value = puzzleAnswer.replace(Regex(regexBuilder.toString()), "_")
    }

    private fun loadPuzzle() {
        val wordAndCategory = wordsAndCategories.random()

        puzzleAnswer = wordAndCategory.second.uppercase(Locale.getDefault())
        _category.value = wordAndCategory.first

        charactersToBeGuessed = puzzleAnswer.count {
            it in 'a'..'z' || it in 'A'..'Z'
        }

        updatePuzzleString()

        Log.d(
            "GameViewModel",
            "Category = \"${_category.value}\" Answer = \"$puzzleAnswer\" ($charactersToBeGuessed)"
        )
    }

    fun addGuessedChar(char: Char): Int {
        val upperCaseChar = char.uppercaseChar()

        if (_guessedCharacters.value!!.contains(upperCaseChar))
            return -1

        _guessedCharacters.value?.add(0, upperCaseChar)
        _guessedCharacters.value = _guessedCharacters.value

        updatePuzzleString()

        val matches = countMatches(upperCaseChar)

        charactersGuessed = charactersGuessed.plus(matches)

        Log.d("GameViewModel", "charactersGuessed = $charactersGuessed")

        return matches
    }

    fun isGuessRight(string: String): Boolean {
        return puzzleAnswer.equals(string, true)
    }

    private fun countMatches(char: Char): Int {
        var count = 0
        for (answerChar in puzzleAnswer) {
            if (answerChar == char)
                count = count.inc()
        }

        return count
    }

    fun incrementScore(matches: Int) {
        _score.value = _score.value?.plus(matches)
    }

    fun isOutOfLives(): Boolean {
        return _lives.value == 0
    }

    fun hasWordBeenGuessed(): Boolean {
        return charactersGuessed == charactersToBeGuessed
    }
}
