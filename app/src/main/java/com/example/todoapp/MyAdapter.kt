package com.example.todoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.database.Todo

class MyAdapter(
    private var todoList: List<Todo>,
    private val onEditClick: (Int) -> Unit
) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_row, parent, false)
        return MyViewHolder(itemView)
    }

    fun updateDate(newTodos: List<Todo>){
        todoList = newTodos
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = todoList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = todoList[position]
        holder.title.text = currentItem.title
        holder.description.text = currentItem.description
        holder.status.isChecked = currentItem.status
        holder.editButton.setOnClickListener {
            onEditClick(currentItem.id)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.card_title)
        val description: TextView = itemView.findViewById(R.id.card_description)
        val status: CheckBox = itemView.findViewById(R.id.card_status)
        val editButton: ImageButton = itemView.findViewById(R.id.editBtn)
    }
}
