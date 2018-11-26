package hu.aut.android.todorecyclerviewdemo

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import hu.aut.android.todorecyclerviewdemo.adapter.TodoAdapter
import hu.aut.android.todorecyclerviewdemo.data.AppDatabase
import hu.aut.android.todorecyclerviewdemo.data.Todo
import hu.aut.android.todorecyclerviewdemo.touch.TodoTouchHelperCallback
import kotlinx.android.synthetic.main.activity_scrolling.*
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import java.util.Date

class ScrollingActivity : AppCompatActivity(), TodoDialog.TodoHandler {
    private lateinit var todoAdapter: TodoAdapter

    companion object {
        val KEY_ITEM_TO_EDIT = "KEY_ITEM_TO_EDIT"
    }
    private var editIndex: Int = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)

        setSupportActionBar(toolbar)

        fabAddTodo.setOnClickListener { view ->
            /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()*/

            showAddTodoDialog()
        }

        initRecyclerView()

        if (isFirstStart()) {
            MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.fabAddTodo)
                .setPrimaryText(getString(R.string.shopping))
                .setSecondaryText(getString(R.string.add_to_list))
                .show()

            saveStart()
        }

    }

    private val KEY_FIRST = "KEY_FIRST"

    fun isFirstStart() : Boolean {
        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        return sp.getBoolean(KEY_FIRST, true)
    }

    fun saveStart() {
        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = sp.edit()
        editor.putBoolean(KEY_FIRST, false)
        editor.apply()
    }



    private fun initRecyclerView() {
        Thread {
            val todoList = AppDatabase.getInstance(
                this@ScrollingActivity
            ).todoDao().findAllTodos()

            todoAdapter = TodoAdapter(
                this@ScrollingActivity,
                todoList
            )

            runOnUiThread {
                recyclerTodo.adapter = todoAdapter

                val callback = TodoTouchHelperCallback(todoAdapter)
                val touchHelper = ItemTouchHelper(callback)
                touchHelper.attachToRecyclerView(recyclerTodo)
            }
        }.start()
    }

    private fun showAddTodoDialog() {
        TodoDialog().show(supportFragmentManager,
            "TAG_CREATE")
    }

    public fun showEditTodoDialog(todoToEdit: Todo, idx: Int) {
        editIndex = idx
        val editItemDialog = TodoDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEM_TO_EDIT, todoToEdit)
        editItemDialog.arguments = bundle

        editItemDialog.show(supportFragmentManager,
            "EDITITEMDIALOG")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {
                Thread {
                    AppDatabase.getInstance(this).todoDao().deleteAll()
                    runOnUiThread{
                        todoAdapter.deleteAllItems()
                    }
                }.start()
            }
        }
        return true
    }

    override fun todoCreated(item: Todo) {
        Thread {
            val todoId = AppDatabase.getInstance(
                this@ScrollingActivity).todoDao().insertTodo(item)

            item.todoId = todoId

            runOnUiThread {
                todoAdapter.addTodo(item)
            }
        }.start()
    }

    override fun todoUpdated(item: Todo) {
        Thread {
            AppDatabase.getInstance(
                this@ScrollingActivity).todoDao().updateTodo(item)

            runOnUiThread{
                todoAdapter.updateTodo(item, editIndex)
            }
        }.start()
    }
}
