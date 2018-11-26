package hu.aut.android.todorecyclerviewdemo.touch

interface TodoTochHelperAdapter {
    fun onDismissed(position: Int)
    fun onItemMoved(fromPosition: Int, toPosition: Int)
}