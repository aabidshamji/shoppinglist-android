package hu.aut.android.todorecyclerviewdemo

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import hu.aut.android.todorecyclerviewdemo.data.Todo
import kotlinx.android.synthetic.main.dialog_todo.view.*
import kotlinx.android.synthetic.main.todo_row.*
import kotlinx.android.synthetic.main.todo_row.view.*
import java.lang.RuntimeException

class TodoDialog : DialogFragment() {

    interface TodoHandler {
        fun todoCreated(item: Todo)
        fun todoUpdated(item: Todo)
    }

    private lateinit var todoHandler: TodoHandler

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is TodoHandler) {
            todoHandler = context
        } else {
            throw RuntimeException(
                getString(R.string.does_not_implement))
        }
    }

    private lateinit var etItem: EditText
    private lateinit var etPrice: EditText
    private lateinit var ddType: Spinner
    private lateinit var etDescription: EditText
    private lateinit var cbDone: CheckBox

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(getString(R.string.new_item))

        val rootView = requireActivity().layoutInflater.inflate(
            R.layout.dialog_todo, null
        )

        etItem = rootView.etItem
        etPrice = rootView.etPrice
        ddType = rootView.ddType
        etDescription = rootView.etDescription
        cbDone = rootView.checkBox

        builder.setView(rootView)

        val arguments = this.arguments
        if (arguments != null && arguments.containsKey(
                ScrollingActivity.KEY_ITEM_TO_EDIT)) {
            val dbItem = arguments.getSerializable(
                ScrollingActivity.KEY_ITEM_TO_EDIT
            ) as Todo

            etItem.setText(dbItem.createItem)
            etPrice.setText(dbItem.createPrice)
            cbDone.isChecked = dbItem.setPurchased
            etDescription.setText(dbItem.createDescription)

            when (dbItem.createType) {
                getString(R.string.food) -> ddType.setSelection(0)
                getString(R.string.stationary) -> ddType.setSelection(1)
                getString(R.string.clothing) -> ddType.setSelection(2)
                getString(R.string.electronics) -> ddType.setSelection(3)
                getString(R.string.school) -> ddType.setSelection(4)
                getString(R.string.alcohol) -> ddType.setSelection(5)
                else -> ddType.setSelection(6)
            }

            builder.setTitle(getString(R.string.edit_item))
        }

        builder.setPositiveButton(getString(R.string.save)) {
                dialog, witch -> // empty
        }

        return builder.create()
    }


    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {

            when {
                etItem.text.isEmpty() -> etItem.error = getString(R.string.cannot_be_empty)
                etPrice.text.isEmpty() -> etPrice.error = getString(R.string.cannot_be_empty)
                else -> {
                    val arguments = this.arguments
                    if (arguments != null && arguments.containsKey(ScrollingActivity.KEY_ITEM_TO_EDIT)) {
                        handleTodoEdit()
                    } else {
                        handleTodoCreate()
                    }

                    dialog.dismiss()
                }
            }
        }
    }

    private fun handleTodoCreate() {
        todoHandler.todoCreated(
            Todo(
                null,
                etItem.text.toString(),
                    ddType.selectedItem.toString(),
                    etPrice.text.toString(),
                    etDescription.text.toString(),
                    cbDone.isChecked
            )
        )
    }

    private fun handleTodoEdit() {
        val itemEdit = arguments?.getSerializable(
            ScrollingActivity.KEY_ITEM_TO_EDIT
        ) as Todo
        itemEdit.createItem = etItem.text.toString()
        itemEdit.createPrice = etPrice.text.toString()
        itemEdit.createDescription = etDescription.text.toString()
        itemEdit.createType = ddType.selectedItem.toString()
        itemEdit.setPurchased = cbDone.isChecked

        todoHandler.todoUpdated(itemEdit)
    }

}