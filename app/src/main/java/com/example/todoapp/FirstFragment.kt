package com.example.todoapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.FragmentFirstBinding
import com.example.todoapp.model.TodoModel
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private lateinit var newList: ArrayList<TodoModel>
    private lateinit var titles: Array<String>
    private lateinit var descriptions: Array<String>

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titles = arrayOf(
            "First", "Second", "Third", "Fourth", "Fifth",
            "Sixth", "Seventh", "Eighth", "Nineth", "Tenth"
        )
        descriptions = arrayOf(
            "First", "Second", "Third", "Fourth", "Fifth",
            "Sixth", "Seventh", "Eighth", "Nineth", "Tenth"
        )

        val newRecyclerview = binding.mRecyclerView // Access view through binding
        newRecyclerview.layoutManager = LinearLayoutManager(context)
        newRecyclerview.setHasFixedSize(true)

        newList = arrayListOf()
        getData()
        val myAdapter = MyAdapter(newList)

        newRecyclerview.adapter = myAdapter
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

                val deletedTodo: TodoModel = newList.get(position)
                newList.removeAt(position)
                myAdapter.notifyItemChanged(position)
                Snackbar.make(newRecyclerview, "Deleted " + deletedTodo.title, Snackbar.LENGTH_LONG)
                    .setAction(
                        "Undo",
                        View.OnClickListener {
                            // adding on click listener to our action of snack bar.
                            // below line is to add our item to array list with a position.
                            newList.add(position, deletedTodo)

                            // below line is to notify item is
                            // added to our adapter class.
                            myAdapter.notifyItemInserted(position)
                        }).show()

            }

        }).attachToRecyclerView(newRecyclerview)
    }

    private fun getData() {
        for (i in titles.indices) {
            val todo = TodoModel(titles[i], descriptions[i])
            newList.add(todo)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
