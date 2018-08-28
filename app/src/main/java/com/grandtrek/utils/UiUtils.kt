package com.grandtrek.utils

import android.content.Context
import android.support.v7.app.AlertDialog
import com.grandtrek.R

class UiUtils {

    companion object {

        fun showDialog(context: Context, messageId: Int, onAction: () -> Unit) {
            AlertDialog
                    .Builder(context)
                    .setTitle(R.string.app_name)
                    .setMessage(messageId)
                    .setPositiveButton(R.string.ok, { _, b -> onAction() })
                    .show()
        }
    }
}