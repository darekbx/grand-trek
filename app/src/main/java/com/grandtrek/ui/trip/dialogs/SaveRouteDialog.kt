package com.grandtrek.ui.trip.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ToggleButton
import com.grandtrek.R
import kotlinx.android.synthetic.main.dialog_save_route.*

class SaveRouteDialog(
        context: Context,
        val onSave: (name: String, color: Int) -> Unit,
        val onDiscard: () -> Unit): Dialog(context) {

    private lateinit var buttons: Array<ToggleButton>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_save_route)

        buttons = arrayOf(color_violet, color_red, color_orange, color_green, color_blue)
        buttons.forEach {
            it.setOnClickListener { button -> handleClick(button) }
        }

        save_button.setOnClickListener {
            onSave(getRouteName(), getSelectedColor())
            dismiss()
        }
        discard_button.setOnClickListener {
            onDiscard()
            dismiss()
        }
    }

    private fun getRouteName() = route_name.text.toString()

    private fun getSelectedColor(): Int {
        val tagColor = buttons.first { it.isChecked }.tag as String
        return Color.parseColor(tagColor)
    }

    private fun handleClick(view: View) {
        buttons.forEach { it.isChecked = false }
        (view as ToggleButton).isChecked = true
    }
}