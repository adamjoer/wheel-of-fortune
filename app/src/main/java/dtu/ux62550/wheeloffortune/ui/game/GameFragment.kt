package dtu.ux62550.wheeloffortune.ui.game

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import dtu.ux62550.wheeloffortune.R
import dtu.ux62550.wheeloffortune.databinding.FragmentGameBinding

/**
 * Fragment for 'Wheel of Fortune' game logic
 */
class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding
    private lateinit var viewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        // Inflate the layout for this fragment
        binding = FragmentGameBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.gameViewModel = viewModel

        // Setup click listeners for buttons
        binding.spinWheel.setOnClickListener { onSpinWheel() }
        binding.submitGuess.setOnClickListener { onSubmitGuess() }
    }

    private fun onSpinWheel() {
        Log.d("GameFragment", "onSpinWheel called")
    }

    private fun onSubmitGuess() {
        Log.d("GameFragment", "onSubmitGuess called")

        val input = binding.guessInput.text.toString()

        if (input.isEmpty()) {
            setErrorTextField(true, R.string.error_no_input)
            return
        }

        if (input.length == 1) {

            val regex = Regex("[a-zA-z0-9]")
            if (!regex.matches(input)) {
                setErrorTextField(true, R.string.error_invalid_input)
                return
            }

            if (!viewModel.addGuessedChar(input[0])) {
                setErrorTextField(true, R.string.error_char_already_guessed)

            } else {
                setErrorTextField(false)
            }

        } else {
            if (viewModel.isGuessRight(input)) {
                Log.d("GameFragment", "OMG, the guess was right!")
            }
        }

        binding.guessInput.text?.clear()
    }

    private fun setErrorTextField(error: Boolean, errorMessage: Int = -1) {
        binding.guessInputLayout.isErrorEnabled = error

        binding.guessInputLayout.error = if (error) getString(errorMessage) else null
    }
}
