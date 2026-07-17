package com.example.ui

import android.content.Context
import android.text.InputType
import android.view.ActionMode
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.BaseInputConnection
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection

class TerminalInputView(
    context: Context,
    var onProcessIncomingText: (String) -> Unit = {},
    var onHandlePaste: () -> Unit = {},
    var onDeleteSurroundingText: (Int, Int) -> Unit = { _, _ -> },
    var onSendKeyEvent: (KeyEvent, Boolean) -> Boolean = { _, _ -> false }
) : View(context) {

    var shouldRequestFocusOnWindowFocus = true

    
    init {
        isFocusable = true
        isFocusableInTouchMode = true
        isClickable = true
    }

    
    override fun onCheckIsTextEditor(): Boolean = true

    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection {
        outAttrs.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        outAttrs.imeOptions = EditorInfo.IME_ACTION_NONE or EditorInfo.IME_FLAG_NO_EXTRACT_UI or EditorInfo.IME_FLAG_NO_FULLSCREEN
        outAttrs.initialSelStart = 0
        outAttrs.initialSelEnd = 0

        return object : BaseInputConnection(this, true) {
            private var composingText = ""

            override fun setComposingText(text: CharSequence?, newCursorPosition: Int): Boolean {
                composingText = text?.toString() ?: ""
                return true
            }

            override fun finishComposingText(): Boolean {
                if (composingText.isNotEmpty()) {
                    onProcessIncomingText(composingText)
                    composingText = ""
                }
                return true
            }

            override fun commitText(text: CharSequence?, newCursorPosition: Int): Boolean {
                if (composingText.isNotEmpty()) {
                    onProcessIncomingText(composingText)
                    composingText = ""
                }
                text?.let { onProcessIncomingText(it.toString()) }
                return true
            }

            override fun deleteSurroundingText(beforeLength: Int, afterLength: Int): Boolean {
                onDeleteSurroundingText(beforeLength, afterLength)
                return true
            }

            override fun sendKeyEvent(event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN) {
                }
                val handled = onSendKeyEvent(event, true)
                return if (handled) true else super.sendKeyEvent(event)
            }

            override fun performContextMenuAction(id: Int): Boolean {
                if (id == android.R.id.paste) {
                    onHandlePaste()
                    return true
                }
                return super.performContextMenuAction(id)
            }
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        val handled = onSendKeyEvent(event, false)
        return if (handled) true else super.dispatchKeyEvent(event)
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus && shouldRequestFocusOnWindowFocus) {
            post {
                val result = requestFocus()
                
                if (result) {
                    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                        imm.invalidateInput(this@TerminalInputView)
                    } else {
                        imm.restartInput(this@TerminalInputView)
                    }
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                        windowInsetsController?.show(android.view.WindowInsets.Type.ime())
                    } else {
                        imm.showSoftInput(this@TerminalInputView, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT)
                    }
                }
            }
        }
    }
}
