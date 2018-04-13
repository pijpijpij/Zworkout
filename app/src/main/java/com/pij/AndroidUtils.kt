/*
 * Copyright (c) 2018, Chiswick Forest
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

package com.pij

import android.support.annotation.StringRes
import android.support.v4.util.ObjectsCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView

/** Just a utility class.
 *
 */
class TextWatcherAdapter(private val onTextChange: (String) -> Unit) : TextWatcher {
    override fun afterTextChanged(editable: Editable) {
    }

    override fun beforeTextChanged(before: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(newValue: CharSequence?, start: Int, before: Int, count: Int) {
        onTextChange(newValue?.toString() ?: "")
    }
}

/**
 * Only applies the new text if it is different from the current text.
 */
fun TextView.updateText(newValue: String?) {
    if (!ObjectsCompat.equals(this.text.toString(), newValue)) {
        this.text = newValue
    }
}

/**
 * Only applies the new text if it is different from the current text.
 */
fun TextView.updateText(@StringRes newValue: Int) {
    updateText(resources.getString(newValue))
}

/**
 * Only applies the new text if it is different from the current text.
 */
fun TextView.updateError(newValue: String?) {
    if (error != newValue) {
        error = newValue
    }
}

/**
 * Only applies the new enabled if it is different from the current text.
 */
fun View.updateEnabled(newValue: Boolean) {
    if (this.isEnabled != newValue) {
        this.isEnabled = newValue
    }
}