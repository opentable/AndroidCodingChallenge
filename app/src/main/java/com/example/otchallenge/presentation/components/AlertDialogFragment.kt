package com.example.otchallenge.presentation.components

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat.ID_NULL
import androidx.fragment.app.DialogFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class AlertDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(resolveString(ARG_TITLE))
            .setMessage(resolveString(ARG_MESSAGE))
            .setButtonIfSet(ARG_POSITIVE_TEXT) { text ->
                setPositiveButton(text) { _, _ ->
                    _eventBus.onNext(
                        Event.ButtonClick(
                            dialogTag = this@AlertDialogFragment.tag,
                            buttonType = ButtonType.Positive
                        )
                    )
                }
            }
            .setButtonIfSet(ARG_NEGATIVE_TEXT) { text ->
                setNegativeButton(text) { _, _ ->
                    _eventBus.onNext(
                        Event.ButtonClick(
                            dialogTag = this@AlertDialogFragment.tag,
                            buttonType = ButtonType.Negative
                        )
                    )
                }
            }
            .create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        _eventBus.onNext(
            Event.Dismiss(
                dialogTag = this.tag
            )
        )
    }

    private fun AlertDialog.Builder.setButtonIfSet(
        argument: String,
        setter: AlertDialog.Builder.(String) -> Unit
    ): AlertDialog.Builder = apply {
        val text = resolveString(argument)
        if (text != null) {
            setter(text)
        }
    }

    private fun resolveString(argument: String): String? {
        return arguments?.getString(argument)
            ?: arguments?.getInt(argument, ID_NULL)
                .takeIf { it != ID_NULL }
                ?.let { getString(it) }
    }

    class Builder {

        var title: String? = null
        var titleId: Int? = null
        var message: String? = null
        var messageId: Int? = null
        var positiveButtonText: String? = null
        var positiveButtonTextId: Int? = null
        var negativeButtonText: String? = null
        var negativeButtonTextId: Int? = null

        fun build(): Bundle {
            return Bundle().apply {
                putEither(ARG_TITLE, title, titleId)
                putEither(ARG_MESSAGE, message, messageId)
                putEither(ARG_POSITIVE_TEXT, positiveButtonText, positiveButtonTextId)
                putEither(ARG_NEGATIVE_TEXT, negativeButtonText, negativeButtonTextId)
            }
        }

        private fun Bundle.putEither(
            argument: String,
            stringValue: String?,
            idValue: Int?
        ) {
            when {
                stringValue != null -> putString(argument, stringValue)
                idValue != null -> putInt(argument, idValue)
            }
        }
    }

    enum class ButtonType {
        Positive,
        Negative
    }

    sealed interface Event {

        val dialogTag: String?

        data class ButtonClick(
            override val dialogTag: String?,
            val buttonType: ButtonType
        ): Event

        data class Dismiss(
            override val dialogTag: String?
        ): Event
    }

    companion object {
        private const val ARG_TITLE = "arg_title"
        private const val ARG_MESSAGE = "arg_message"
        private const val ARG_POSITIVE_TEXT = "arg_positive"
        private const val ARG_NEGATIVE_TEXT = "arg_negative"

        private val _eventBus = PublishSubject.create<Event>()
        val eventBus: Observable<Event> = _eventBus

        operator fun invoke(block: Builder.() -> Unit): AlertDialogFragment {
            return AlertDialogFragment()
                .apply {
                    arguments = Builder().apply(block).build()
                }
        }
    }

}