package com.pij.utils

import android.text.Editable
import android.text.TextWatcher

/** Just a utility class.
 *
 */
class TextWatcherAdapter(private val afterTextChange: (String) -> Unit) : TextWatcher {
    override fun afterTextChanged(editable: Editable) {
    }

    override fun beforeTextChanged(before: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(newValue: CharSequence?, start: Int, before: Int, count: Int) {
        afterTextChange(newValue.toString())
    }
}