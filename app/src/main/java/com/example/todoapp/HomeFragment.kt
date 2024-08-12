package com.example.todoapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.database.Todo
import com.example.todoapp.databinding.FragmentFirstBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {
    private lateinit var viewModel: TodoViewModel
    private lateinit var newList: List<Todo>
    private lateinit var titles: Array<String>
    private lateinit var descriptions: Array<String>

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        binding.fab.setOnClickListener { _ ->
            findNavController().navigate(R.id.SecondFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        val newRecyclerview = binding.mRecyclerView // Access view through binding
        newRecyclerview.layoutManager = LinearLayoutManager(context)
        newRecyclerview.setHasFixedSize(true)
        val myAdapter = MyAdapter(emptyList())
        newRecyclerview.adapter = myAdapter

        lifecycleScope.launch {
            viewModel.getTodos().collect { todos ->
                myAdapter.updateDate(todos)
            }
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                val deletedTodo: Todo = newList[position]
                viewModel.deleteTodo(deletedTodo)
                myAdapter.notifyItemChanged(position)
                Snackbar.make(newRecyclerview, "Deleted " + deletedTodo.title, Snackbar.LENGTH_LONG)
                    .setAction(
                        "Undo",
                        View.OnClickListener {
                            // adding on click listener to our action of snack bar.
                            // below line is to add our item to array list with a position.
                            viewModel.upsertTodo(deletedTodo)

                            // below line is to notify item is
                            // added to our adapter class.
                            myAdapter.notifyItemInserted(position)
                        }).show()

            }

        }).attachToRecyclerView(newRecyclerview)
    }

    private fun getData(viewModel: TodoViewModel): Flow<List<Todo>> {
        return viewModel.getTodos()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
