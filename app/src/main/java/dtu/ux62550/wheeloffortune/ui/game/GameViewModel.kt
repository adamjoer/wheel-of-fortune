package dtu.ux62550.wheeloffortune.ui.game

import android.text.Spannable
import android.text.SpannableString
import android.text.style.TtsSpan
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

const val START_LIVES = 5

class GameViewModel : ViewModel() {
    private val _lives = MutableLiveData(START_LIVES)
    val lives: LiveData<Int>
        get() = _lives

    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    private val _currentGuessWord = MutableLiveData<String>()
    val currentGuessWord: LiveData<Spannable> = Transformations.map(_currentGuessWord) {

        if (it == null) {
            Log.d("GameViewModel", "\"it\" is null!")
            SpannableString("")

        } else {
            Log.d("GameViewModel", "\"it\" is not null")
            val currentWord = it.toString()
            val spannable: Spannable = SpannableString(currentWord)
            spannable.setSpan(
                TtsSpan.VerbatimBuilder(currentWord).build(),
                0,
                currentWord.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannable
        }
    }
}