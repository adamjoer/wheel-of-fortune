package dtu.ux62550.wheeloffortune.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dtu.ux62550.wheeloffortune.wordsAndCategories
import java.util.*

private const val TAG = "GameViewModel"

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

    private val _guesses = MutableLiveData<MutableList<String>>()
    val guesses: LiveData<MutableList<String>>
        get() = _guesses

    lateinit var puzzleAnswer: String

    private var numberOfCharactersToBeGuessed = -1

    private var numberOfCharactersGuessed = 0

    init {
        _guesses.value = mutableListOf()
        loadPuzzle()
    }

    private fun updatePuzzleString() {
        val regexBuilder = StringBuilder("[^ _\\-!,.")
        for (string in _guesses.value!!) {

            if (string.length == 1)
                regexBuilder.append(string)
        }
        regexBuilder.append("]")

        _wordPuzzle.value = puzzleAnswer.replace(Regex(regexBuilder.toString()), "_")
    }

    private fun loadPuzzle() {
        val wordAndCategory = wordsAndCategories.random()

        puzzleAnswer = wordAndCategory.second.uppercase(Locale.getDefault())
        _category.value = wordAndCategory.first

        numberOfCharactersToBeGuessed = puzzleAnswer.count {
            it in 'a'..'z' || it in 'A'..'Z'
        }

        updatePuzzleString()

        Log.d(
            TAG,
            "Category = \"${_category.value}\" Answer = \"$puzzleAnswer\" ($numberOfCharactersToBeGuessed)"
        )
    }

    fun guessChar(char: Char): Int {
        val upperCaseChar = char.uppercaseChar()
        val upperCaseCharString = upperCaseChar.toString()

        if (_guesses.value!!.contains(upperCaseCharString))
            return -1

        _guesses.value?.add(0, upperCaseCharString)
        _guesses.value = _guesses.value

        updatePuzzleString()

        val matches = countMatches(upperCaseChar)

        numberOfCharactersGuessed = numberOfCharactersGuessed.plus(matches)

        Log.d(TAG, "numberOfCharactersGuessed = $numberOfCharactersGuessed")

        return matches
    }

    fun guessString(string: String): Boolean {
        val allCapsString = string.uppercase(Locale.getDefault())

        _guesses.value?.add(0, allCapsString)
        _guesses.value = _guesses.value

        return puzzleAnswer.equals(allCapsString, false)
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
        return numberOfCharactersGuessed == numberOfCharactersToBeGuessed
    }
}
