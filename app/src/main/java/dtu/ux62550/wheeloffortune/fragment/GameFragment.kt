package dtu.ux62550.wheeloffortune.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dtu.ux62550.wheeloffortune.R
import dtu.ux62550.wheeloffortune.adapter.GuessAdapter
import dtu.ux62550.wheeloffortune.databinding.FragmentGameBinding
import dtu.ux62550.wheeloffortune.viewmodel.GameViewModel

private const val TAG = "GameFragment"

/**
 * Fragment for 'Wheel of Fortune' game logic
 */
class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding
    private lateinit var viewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Change back-button behaviour on this screen
        // so it exits the app instead of potentially going to the game-ended fragment
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            activity?.finish()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        // Inflate the layout for this fragment
        binding = FragmentGameBinding.inflate(inflater)

        // Initialise and assign adapter to the RecyclerView
        val adapter = GuessAdapter()
        binding.guesses.adapter = adapter

        // Add observer to the guesses-list, so the RecyclerView gets updated
        viewModel.guesses.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)

                // Check if an item has been added to the beginning of the list
                if (it.size > 0) {

                    // Notify that an item has been added to the start of the dataset
                    adapter.notifyItemInserted(0)

                    // Scroll the RecyclerView to where the new item has been inserted
                    binding.guesses.layoutManager?.scrollToPosition(0)

                } else { // List has been cleared, so the whole dataset has changed
                    adapter.notifyDataSetChanged()
                }
            }
        })

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
        Log.d(TAG, "onSpinWheel called")
    }

    private fun onSubmitGuess() {
        Log.d(TAG, "onSubmitGuess called")

        val input = binding.guessInput.text.toString()

        if (input.isEmpty()) {
            setErrorTextField(true, R.string.error_no_input)
            return
        }

        if (input.length == 1) {

            if (!(input[0] in 'a'..'z' || input[0] in 'A'..'Z')) {
                setErrorTextField(true, R.string.error_invalid_input)
                binding.guessInput.text?.clear()
                return
            }

            val matches = viewModel.guessChar(input[0])
            if (matches < 0) {
                setErrorTextField(true, R.string.error_char_already_guessed)

            } else {
                setErrorTextField(false)

                Log.d(TAG, "Matches = $matches")
                viewModel.incrementScore(matches)

                if (viewModel.isOutOfLives())
                    navigateToGameEndedDest(false)
                else if (viewModel.hasWordBeenGuessed())
                    navigateToGameEndedDest(true)
            }

        } else {
            setErrorTextField(false)

            if (viewModel.guessString(input))
                navigateToGameEndedDest(true)
        }

        binding.guessInput.text?.clear()
    }

    private fun navigateToGameEndedDest(hasWon: Boolean) {
        val action = GameFragmentDirections.actionGameDestToGameEndedDest(
            hasWon,
            viewModel.puzzleAnswer,
            viewModel.score.value!!
        )

        findNavController().navigate(action)
    }

    private fun setErrorTextField(error: Boolean, errorMessage: Int? = null) {
        binding.guessInputLayout.isErrorEnabled = error

        binding.guessInputLayout.error =
            if (error && errorMessage != null) getString(errorMessage) else null
    }
}
