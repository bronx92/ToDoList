package com.bronx92.todolist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bronx92.todolist.R
import com.bronx92.todolist.databinding.ActivityAddTaskBinding
import com.bronx92.todolist.databinding.ItemTaskBinding
import com.bronx92.todolist.model.Task

class TaskListAdapter: ListAdapter<Task, TaskListAdapter.TaskViewHolder>(DiffCallback()) {

    var listenerEdit: (Task) -> Unit = {}
    var listenerDelete: (Task) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class TaskViewHolder(
        private val binding: ItemTaskBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Task) {
            binding.tvTitleTask.text = item.title
            binding.tvDateTask.text = "${item.date} ${item.hour}"
            binding.ivMore.setOnClickListener {
                showmenu(item)
            }
        }

        private fun showmenu(item: Task) {
            val ivMore = binding.ivMore
            val popUp = PopupMenu(ivMore.context, ivMore)
            popUp.menuInflater.inflate(R.menu.popup_menu, popUp.menu)
            popUp.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_edit -> listenerEdit(item)
                    R.id.action_delete -> listenerDelete(item)
                }
                return@setOnMenuItemClickListener true
            }
            popUp.show()
        }
    }

}

class DiffCallback: DiffUtil.ItemCallback<Task>(){
    override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem
}

/*
    public static final DiffUtil.ItemCallback<User> DIFF_CALLBACK =
    new DiffUtil.ItemCallback<User>() {
        @Override
        public boolean areItemsTheSame(
            @NonNull User oldUser,  @NonNull User newUser) {
            // User properties may have changed if reloaded from the DB, but ID is fixed
            return oldUser.getId() == newUser.getId();
        }
        @Override
        public boolean areContentsTheSame(
            @NonNull User oldUser,  @NonNull User newUser) {
            // NOTE: if you use equals, your object must properly override Object#equals()
            // Incorrectly returning false here will result in too many animations.
            return oldUser.equals(newUser);
        }
    }
*/
