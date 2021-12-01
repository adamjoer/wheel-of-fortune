package dtu.ux62550.wheeloffortune.ui.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import dtu.ux62550.wheeloffortune.R

private const val TAG = "GameEndedFragment"

private const val ARG_HAS_WON = "hasWon"
private const val ARG_PUZZLE_ANSWER = "puzzleAnswer"
private const val ARG_SCORE = "score"

/**
 * A simple [Fragment] subclass.
 * Use the [GameEndedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameEndedFragment : Fragment() {

    private var hasWon: Boolean? = null
    private var puzzleAnswer: String? = null
    private var score: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            hasWon = it.getBoolean(ARG_HAS_WON)
            puzzleAnswer = it.getString(ARG_PUZZLE_ANSWER)
            score = it.getInt(ARG_SCORE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_ended, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireView().findViewById<TextView>(R.id.status_text).text =
            getString(if (hasWon!!) R.string.you_won else R.string.you_lost)
        requireView().findViewById<TextView>(R.id.puzzle_answer).text = getString(R.string.puzzle_answer, puzzleAnswer!!)
        requireView().findViewById<TextView>(R.id.end_score).text = getString(R.string.score, score!!)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param hasWon Whether the user has won
         * @param puzzleAnswer The answer to the word puzzle
         * @param score The end score
         * @return A new instance of fragment GameEndedFragment.
         */
        @JvmStatic
        fun newInstance(hasWon: Boolean, puzzleAnswer: String, score: Int) =
            GameEndedFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_HAS_WON, hasWon)
                    putString(ARG_PUZZLE_ANSWER, puzzleAnswer)
                    putInt(ARG_SCORE, score)
                }
            }
    }
}
