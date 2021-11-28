package dtu.ux62550.wheeloffortune.ui.game

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dtu.ux62550.wheeloffortune.databinding.FragmentGameBinding

const val TAG = "GameFragment"

/**
 * Fragment for 'Wheel of Fortune' game logic
 */
class GameFragment : Fragment() {

    private lateinit var _binding: FragmentGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        _binding = FragmentGameBinding.inflate(inflater)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup click listeners for buttons
        _binding.spinWheel.setOnClickListener { onSpinWheel() }
        _binding.submitGuess.setOnClickListener { onSubmitGuess() }
    }

    private fun onSpinWheel() {
        Log.d(TAG, "onSpinWheel called")
    }

    private fun onSubmitGuess() {
        Log.d(TAG, "onSubmitGuess called")
    }
}