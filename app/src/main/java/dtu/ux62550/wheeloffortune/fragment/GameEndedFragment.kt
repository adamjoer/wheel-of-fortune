package dtu.ux62550.wheeloffortune.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import dtu.ux62550.wheeloffortune.R

private const val ARG_HAS_WON = "hasWon"
private const val ARG_PUZZLE_ANSWER = "puzzleAnswer"
private const val ARG_SCORE = "score"

/**
 * Fragment for the end game screen
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

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            navigateToGameDest()
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

        view.findViewById<TextView>(R.id.status_text).text =
            getString(if (hasWon!!) R.string.you_won else R.string.you_lost)
        view.findViewById<TextView>(R.id.puzzle_answer).text = puzzleAnswer
        view.findViewById<TextView>(R.id.end_score).text = getString(R.string.score, score!!)

        view.findViewById<Button>(R.id.play_again).setOnClickListener {
            navigateToGameDest()
        }

        view.findViewById<Button>(R.id.exit).setOnClickListener {
            activity?.finish()
        }
    }

    private fun navigateToGameDest() {
        val action = GameEndedFragmentDirections.actionGameEndedDestToGameDest()
        findNavController().navigate(action)
    }
}
