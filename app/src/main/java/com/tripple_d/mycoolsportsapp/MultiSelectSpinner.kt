/*
 * Copyright (C) 2012 Kris Wong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tripple_d.mycoolsportsapp

import android.R
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnMultiChoiceClickListener
import android.util.AttributeSet
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.Toast
import java.util.*

/**
 * A Spinner view that does not dismiss the dialog displayed when the control is "dropped down"
 * and the user presses it. This allows for the selection of more than one option.
 */
class MultiSelectSpinner : androidx.appcompat.widget.AppCompatSpinner, OnMultiChoiceClickListener {
    private var _items: Array<String>? = null
    private var _maxItems: Int? = null
    private var _selection: BooleanArray? = null
    private lateinit var _itemSelectedCallback: (Any?)->Unit
    private lateinit var _showState: AlertDialog
    private var _proxyAdapter: ArrayAdapter<String>

    /**
     * Constructor for use when instantiating directly.
     * @param context
     */
    constructor(context: Context?) : super(context!!) {
        _proxyAdapter = ArrayAdapter(context!!, R.layout.simple_spinner_item)
        super.setAdapter(_proxyAdapter)
    }

    /**
     * Constructor used by the layout inflater.
     * @param context
     * @param attrs
     */
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        _proxyAdapter = ArrayAdapter(context!!, R.layout.simple_spinner_item)
        super.setAdapter(_proxyAdapter)
    }

    /**
     * {@inheritDoc}
     */
    override fun onClick(dialog: DialogInterface, which: Int, isChecked: Boolean) {
        if(_maxItems != null && selectedIndicies?.size > _maxItems!!) {
            _selection!![which] = false
            _showState.dismiss()
            Toast.makeText(context, "Μέγιστο πλήθος διαγωνιζόμενων: $_maxItems", Toast.LENGTH_LONG).show()
        }
        else if (_selection != null && which < _selection!!.size) {
            _selection!![which] = isChecked
        }


        _proxyAdapter.clear()
        _proxyAdapter.add(buildSelectedItemString())
        setSelection(0)
        _itemSelectedCallback(selectedIndicies)
    }

    /**
     * {@inheritDoc}
     */
    override fun performClick(): Boolean {
        val builder = AlertDialog.Builder(context)
        builder.setMultiChoiceItems(_items, _selection,this)
        _showState = builder.show()

        return true
    }

    /**
     * MultiSelectSpinner does not support setting an adapter. This will throw an exception.
     * @param adapter
     */
    override fun setAdapter(adapter: SpinnerAdapter) {
        throw RuntimeException("setAdapter is not supported by MultiSelectSpinner.")
    }

    /**
     * Sets the options for this spinner.
     * @param items
     */
    fun setItems(items: Array<String>?) {
        _items = items
        _proxyAdapter.add("${_items?.size} Επιλογές")
        _selection = BooleanArray(_items!!.size)
        Arrays.fill(_selection, false)
    }

    fun setMaxItems(max:Int) {
        _maxItems = max
    }

    fun setItemSelectedCallback(callback: (Any?) -> Unit) {
        _itemSelectedCallback = callback
    }

    /**
     * Sets the options for this spinner.
     * @param items
     */
    fun setItems(items: List<String>) {
        _items = items.toTypedArray()
        _proxyAdapter.add("${_items?.size} Επιλογές")
        _selection = BooleanArray(_items!!.size)
        Arrays.fill(_selection, false)
    }

    /**
     * Sets the selected options based on an array of string.
     * @param selection
     */
    fun setSelection(selection: Array<String>) {
        for (sel in selection) {
            for (j in _items!!.indices) {
                if (_items!![j] == sel) {
                    _selection!![j] = true
                }
            }
        }
    }

    /**
     * Sets the selected options based on a list of string.
     * @param selection
     */
    fun setSelection(selection: List<String>) {
        for (sel in selection) {
            for (j in _items!!.indices) {
                if (_items!![j] == sel) {
                    _selection!![j] = true
                }
            }
        }
    }

    /**
     * Sets the selected options based on an array of positions.
     * @param selectedIndicies
     */
    fun setSelection(selectedIndicies: IntArray) {
        for (index in selectedIndicies) {
            if (index >= 0 && index < _selection!!.size) {
                _selection!![index] = true
            } else {
                throw IllegalArgumentException("Index $index is out of bounds.")
            }
        }
    }

    /**
     * Returns a list of strings, one for each selected item.
     * @return
     */
    val selectedStrings: List<String>
        get() {
            val selection: MutableList<String> = LinkedList()
            for (i in _items!!.indices) {
                if (_selection!![i]) {
                    selection.add(_items!![i])
                }
            }
            return selection
        }

    /**
     * Returns a list of positions, one for each selected item.
     * @return
     */
    val selectedIndicies: List<Int>
        get() {
            val selection: MutableList<Int> = LinkedList()
            for (i in _items!!.indices) {
                if (_selection!![i]) {
                    selection.add(i)
                }
            }
            return selection
        }

    /**
     * Builds the string for display in the spinner.
     * @return comma-separated list of selected items
     */
    private fun buildSelectedItemString(): String {
        val sb = StringBuilder()
        var foundOne = false
        for (i in _items!!.indices) {
            if (_selection!![i]) {
                if (foundOne) {
                    sb.append(",\n ")
                }
                foundOne = true
                sb.append(_items!![i])
            }
        }

        if(!foundOne){
            sb.append("${_items!!.size} Επιλογές")
        }
        return sb.toString()
    }
}
