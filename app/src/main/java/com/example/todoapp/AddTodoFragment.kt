package com.example.todoapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.database.Todo
import com.example.todoapp.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AddTodoFragment : Fragment() {
    private lateinit var viewModel: TodoViewModel

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        view.findViewById<Button>(R.id.addBtn).setOnClickListener() {
            val title = view.findViewById<EditText>(R.id.titleInput).text.toString()
            val description = view.findViewById<EditText>(R.id.descriptionInput).text.toString()
            val status = view.findViewById<Switch>(R.id.completedStatus).isChecked
            if (title.isNotBlank() && description.isNotBlank()) {
                addTodo(
                    title = title,
                    description = description,
                    status = status,
                    viewModel = viewModel
                )
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addTodo(
        title: String,
        description: String,
        status: Boolean,
        viewModel: TodoViewModel
    ) {
        val newTodo = Todo(title = title, description = description, status = status)
        viewModel.upsertTodo(newTodo)
    }
}