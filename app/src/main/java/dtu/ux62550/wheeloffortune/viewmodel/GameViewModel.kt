package dtu.ux62550.wheeloffortune.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dtu.ux62550.wheeloffortune.WheelResultType
import dtu.ux62550.wheeloffortune.WheelResultType.*
import dtu.ux62550.wheeloffortune.wheelResults
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

    private val _currentWheelResult =
        MutableLiveData<Pair<WheelResultType, Int?>>(Pair(UNASSIGNED, null))
    val currentWheelResult: LiveData<Pair<WheelResultType, Int?>>
        get() = _currentWheelResult

    lateinit var puzzleAnswer: String

    private var numberOfCharactersToBeGuessed = -1

    private var numberOfCharactersGuessed = 0

    init {
        _guesses.value = mutableListOf()
        loadPuzzle()
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

    private fun updatePuzzleString() {
        if (hasWordBeenGuessed()) {
            _wordPuzzle.value = puzzleAnswer
            return
        }

        val regexBuilder = StringBuilder("[^ _\\-!,.'\"")
        for (guesses in _guesses.value!!) {
            if (guesses.length == 1)
                regexBuilder.append(guesses)
        }
        regexBuilder.append("]")

        _wordPuzzle.value = puzzleAnswer.replace(Regex(regexBuilder.toString()), "_")
    }

    fun spinTheWheel(): Pair<WheelResultType, Int?> {
        _currentWheelResult.value = wheelResults.random()
        return _currentWheelResult.value!!
    }


    /**
     * Method for guessing either a single character in the puzzle word or the whole word.
     * A guess that has already been previously provided will be rejected.
     * The guess will be compared with the puzzle word, and a number of matches will be returned.
     * This number can be used to calculate the amount of points the user should receive.
     *
     * @param guess String that the user has input as guess to the puzzle word
     * @return If the guess has already been provided previously, a negative number is returned.
     *         If the guess is a single character, the number of matching characters in the puzzle word
     *         is returned.
     *         If the guess is longer than a single character, and the string matches
     *         with the puzzle word, the number of remaining hidden letters in the puzzle word is returned,
     *         otherwise 0 is returned.
     */
    fun guess(guess: String): Int {
        val allCapsString = guess.uppercase(Locale.getDefault())
        if (guesses.value!!.contains(allCapsString))
            return -1

        _guesses.value?.add(0, allCapsString)
        _guesses.value = _guesses.value

        val matches: Int = if (allCapsString.length == 1) {
            val count = countMatches(allCapsString[0])
            numberOfCharactersGuessed += count
            count

        } else {
            if (puzzleAnswer.equals(allCapsString, false)) {
                val numberOfHiddenCharacters =
                    numberOfCharactersToBeGuessed - numberOfCharactersGuessed

                numberOfCharactersGuessed = numberOfCharactersToBeGuessed
                numberOfHiddenCharacters

            } else {
                0
            }
        }

        if (matches > 0)
            updatePuzzleString()

        return matches
    }

    private fun countMatches(guess: Char): Int {
        var count = 0
        for (char in puzzleAnswer) {
            if (char == guess)
                count++
        }

        return count
    }

    fun getPoints(): Int {
        assert(_currentWheelResult.value!!.first == POINTS)

        return _currentWheelResult.value?.second!!
    }

    fun addToScore(points: Int) {
        _score.value = _score.value?.plus(points)
    }

    fun resetScore() {
        _score.value = 0
    }

    fun decrementLives() {
        assert(_lives.value!! > 0)

        _lives.value = _lives.value?.dec()
    }

    fun incrementLives() {
        _lives.value = _lives.value?.inc()
    }

    fun isOutOfLives(): Boolean {
        return _lives.value == 0
    }

    fun hasWordBeenGuessed(): Boolean {
        return numberOfCharactersGuessed == numberOfCharactersToBeGuessed
    }
}
