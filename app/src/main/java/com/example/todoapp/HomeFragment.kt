package com.example.todoapp

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.database.Todo
import com.example.todoapp.databinding.HomeFragmentBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {
    private lateinit var viewModel: TodoViewModel
    private lateinit var newList: List<Todo>

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.setTitle("Todo App")

        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        binding.fab.setOnClickListener { _ ->
            findNavController().navigate(R.id.AddTodoFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        val newRecyclerview = binding.mRecyclerView
        newRecyclerview.layoutManager = LinearLayoutManager(context)
        newRecyclerview.setHasFixedSize(true)
        val myAdapter = MyAdapter(emptyList(), onEditClick = { todoID ->
            val action = HomeFragmentDirections.actionFirstFragmentToEditTodoFragment(todoID)
            findNavController().navigate(action)
        })
        newRecyclerview.adapter = myAdapter

        lifecycleScope.launch {
            viewModel.getTodos().collect { todos ->
                newList = todos
                myAdapter.updateDate(todos)
            }
        }

        val swipeCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                if (position != RecyclerView.NO_POSITION) {
                    val deletedTodo: Todo = newList[position]
                    viewModel.deleteTodo(deletedTodo)
                    myAdapter.notifyItemRemoved(position)
                    Snackbar.make(newRecyclerview, "Deleted " + deletedTodo.title, Snackbar.LENGTH_LONG)
                        .setAction(
                            "Undo",
                            View.OnClickListener {
                                viewModel.upsertTodo(deletedTodo)
                                myAdapter.notifyItemInserted(position)
                            }).show()
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete)
                val background = ColorDrawable(Color.RED)

                if (dX < 0) { // Swiping left
                    background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                    icon?.let {
                        val iconMargin = (itemView.height - it.intrinsicHeight) / 2
                        val iconTop = itemView.top + iconMargin
                        val iconBottom = iconTop + it.intrinsicHeight
                        val iconLeft = itemView.right - iconMargin - it.intrinsicWidth
                        val iconRight = itemView.right - iconMargin
                        it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    }

                } else {
                    background.setBounds(0, 0, 0, 0)
                    icon?.setBounds(0, 0, 0, 0)
                }

                background.draw(c)
                icon?.draw(c)
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(newRecyclerview)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

