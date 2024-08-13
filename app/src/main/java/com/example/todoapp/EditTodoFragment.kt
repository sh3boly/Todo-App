package com.example.todoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.todoapp.database.Todo
import com.example.todoapp.databinding.EditTodoFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditTodoFragment : Fragment() {

    private lateinit var viewModel: TodoViewModel
    private lateinit var binding: EditTodoFragmentBinding
    private val args: EditTodoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EditTodoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        val todoID = args.todoID


        lifecycleScope.launch {
            viewModel.getTodo(todoID).collect() { todo ->


                binding.titleInput.setText(todo.title)
                binding.descriptionInput.setText(todo.description)
                binding.completedStatus.isChecked = todo.status
            }
        }
        view.findViewById<Button>(R.id.editTodoButton).setOnClickListener() {
            val title = view.findViewById<EditText>(R.id.titleInput).text.toString()
            val description = view.findViewById<EditText>(R.id.descriptionInput).text.toString()
            val status = view.findViewById<Switch>(R.id.completedStatus).isChecked
            if (title.isNotBlank() && description.isNotBlank()) {
                viewModel.upsertTodo(Todo(todoID, title, description, status))
            }

        }
    }
}
