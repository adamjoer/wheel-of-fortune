package dtu.ux62550.wheeloffortune.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dtu.ux62550.wheeloffortune.databinding.GuessedCharItemBinding

class GuessedCharAdapter : ListAdapter<Char, GuessedCharAdapter.ViewHolder>(DatasetDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("GuessedCharAdapter", "onCreateViewHolder called")
        return ViewHolder.from(parent)
    }

    class ViewHolder(private val binding: GuessedCharItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Char) {
            Log.d("GuessedCharAdapter", "ViewHolder.bind called")

            binding.character = item.toString()
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = GuessedCharItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class DatasetDiffCallback: DiffUtil.ItemCallback<Char>() {

    override fun areItemsTheSame(oldItem: Char, newItem: Char): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Char, newItem: Char): Boolean {
        return oldItem == newItem
    }
}
