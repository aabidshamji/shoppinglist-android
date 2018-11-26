package hu.aut.android.todorecyclerviewdemo.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import hu.aut.android.todorecyclerviewdemo.R
import hu.aut.android.todorecyclerviewdemo.ScrollingActivity
import hu.aut.android.todorecyclerviewdemo.data.AppDatabase
import hu.aut.android.todorecyclerviewdemo.data.Todo
import hu.aut.android.todorecyclerviewdemo.touch.TodoTochHelperAdapter
import kotlinx.android.synthetic.main.dialog_todo.view.*
import kotlinx.android.synthetic.main.todo_row.view.*
import java.util.*

class TodoAdapter : RecyclerView.Adapter<TodoAdapter.ViewHolder>, TodoTochHelperAdapter {


    var itemsList = mutableListOf<Todo>()

    val context : Context

    constructor(context: Context, todoList: List<Todo>) : super() {
        this.context = context
        this.itemsList.addAll(todoList)
    }

    constructor(context: Context) : super() {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.todo_row, parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemsList[position]

        holder.tvItem.text = item.createItem
        holder.tvPrice.text = "$ ${item.createPrice}"
        holder.cbDone.isChecked = item.setPurchased

        when (item.createType) {
            context.getString(R.string.food) -> holder.imgItem.setImageResource(R.drawable.food)
            context.getString(R.string.stationary) -> holder.imgItem.setImageResource(R.drawable.stationary)
            context.getString(R.string.clothing) -> holder.imgItem.setImageResource(R.drawable.clothing)
            context.getString(R.string.electronics) -> holder.imgItem.setImageResource(R.drawable.electronics)
            context.getString(R.string.school) -> holder.imgItem.setImageResource(R.drawable.school)
            context.getString(R.string.alcohol) -> holder.imgItem.setImageResource(R.drawable.alcohol)
            else -> holder.imgItem.setImageResource(R.drawable.other)
        }

        holder.btnDelete.setOnClickListener {
            deleteTodo(holder.adapterPosition)
        }

        holder.btnEdit.setOnClickListener {
            (context as ScrollingActivity).showEditTodoDialog(
                item, holder.adapterPosition
            )
        }

        holder.cbDone.setOnClickListener {
            item.setPurchased = holder.cbDone.isChecked
            Thread {
                AppDatabase.getInstance(context).todoDao().updateTodo(item)
            }.start()
        }
    }

    private fun deleteTodo(adapterPosition: Int) {
        Thread {
            AppDatabase.getInstance(
                context).todoDao().deleteTodo(itemsList[adapterPosition])

            itemsList.removeAt(adapterPosition)

            (context as ScrollingActivity).runOnUiThread {
                notifyItemRemoved(adapterPosition)
            }
        }.start()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val tvItem = itemView.tvItem
        val cbDone = itemView.cbDone
        val btnDelete = itemView.btnDelete
        val btnEdit = itemView.btnEdit
        val tvPrice = itemView.tvPrice
        val ddType = itemView.ddType
        val imgItem = itemView.imgItem

    }


    fun addTodo(todo: Todo) {
        itemsList.add(0, todo)
        notifyItemInserted(0)
    }

    override fun onDismissed(position: Int) {
        deleteTodo(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(itemsList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    fun updateTodo(item: Todo, editIndex: Int) {
        itemsList[editIndex] = item
        notifyItemChanged(editIndex)
    }

    fun deleteAllItems() {
        itemsList.clear()
        notifyDataSetChanged()
    }

}