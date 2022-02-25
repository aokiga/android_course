package com.oboringleb.androidcourse.ui.emailconfirmation

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import com.oboringleb.androidcourse.R
import com.oboringleb.androidcourse.databinding.ViewVerificationCodeEditTextBinding
import kotlin.math.min

class VerificationCodeEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val viewBinding =
        ViewVerificationCodeEditTextBinding.inflate(LayoutInflater.from(context), this)

    val digitsNumber: Int = context
        .theme
        .obtainStyledAttributes(
            attrs,
            R.styleable.VerificationCodeEditText,
            defStyleAttr,
            defStyleRes
        ).getInt(R.styleable.VerificationCodeEditText_vcet_slotsCount, 6)


    private val slotViews: List<VerificationCodeSlotView>

    private val slotValues: Array<CharSequence?> = Array(digitsNumber) { null }

    var onVerificationCodeFilledListener: (String) -> Unit = {}

    var onVerificationCodeFilledChangeListener: (Boolean) -> Unit = {}

    init {
        if (id == NO_ID) {
            id = View.generateViewId()
        }
        viewBinding.realVerificationCodeEditText.filters += InputFilter.LengthFilter(digitsNumber)
        val views = mutableListOf<VerificationCodeSlotView>()
        for (i in 0 until digitsNumber) {
            val slot = VerificationCodeSlotView(context).apply {
                id = View.generateViewId()
            }
            views += slot
        }
        val constraints = ConstraintSet()
        constraints.clone(this)
        for (i in 0 until digitsNumber) {
            constraints.connect(
                views[i].id,
                ConstraintSet.BOTTOM,
                viewBinding.realVerificationCodeEditText.id,
                ConstraintSet.BOTTOM
            )
            if (i > 0 && i + 1 < digitsNumber) {
                constraints.addToHorizontalChain(views[i].id, views[i - 1].id, views[i + 1].id)
                constraints.setHorizontalChainStyle(views[i].id, ConstraintSet.CHAIN_PACKED)
            }
            if (i == 0) {
                constraints.connect(views[i].id, ConstraintSet.LEFT, id, ConstraintSet.LEFT)
            } else {
                constraints.connect(
                    views[i].id,
                    ConstraintSet.LEFT,
                    views[i - 1].id,
                    ConstraintSet.RIGHT,
                    8
                )
            }
            if (i + 1 < digitsNumber) {
                constraints.connect(
                    views[i].id,
                    ConstraintSet.RIGHT,
                    views[i + 1].id,
                    ConstraintSet.LEFT
                )
            } else {
                constraints.connect(views[i].id, ConstraintSet.RIGHT, id, ConstraintSet.RIGHT)
            }
            constraints.connect(
                views[i].id,
                ConstraintSet.TOP,
                viewBinding.realVerificationCodeEditText.id,
                ConstraintSet.TOP
            )
            views[i].layoutParams = LayoutParams(
                R.dimen.verification_code_slot_width,
                R.dimen.verification_code_slot_height
            )
            views[i].background =
                getDrawable(context, R.drawable.selector_bg_verification_code_slot)
            addView(views[i])
        }
        constraints.applyTo(this)
        slotViews = views


        viewBinding.realVerificationCodeEditText.addTextChangedListener(

            object : TextWatcher {

                private var wasClearedLastSlot = false

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    wasClearedLastSlot = !wasClearedLastSlot && start + before == slotViews.size && count == 0
                }

                override fun afterTextChanged(s: Editable) {
                    slotValues.fillWith(s)
                    slotViews.render(slotValues)
                    slotViews.moveCursorToFirstEmptySlot(slotValues)
                    val filled = slotValues.isFilled()
                    onVerificationCodeFilledChangeListener(filled)
                    if (filled) onVerificationCodeFilledListener(slotValues.toCodeString())
                    // Uncomment if we need to clear the whole field on backspace.
                    // if (wasClearedLastSlot) viewBinding.realVerificationCodeEditText.setText("")
                }
            }
        )
        viewBinding.realVerificationCodeEditText.setOnFocusChangeListener { _, focused ->
            if (focused) {
                slotViews.moveCursorToFirstEmptySlot(slotValues)
            }
        }
        slotViews.forEach {
            (it.viewBinding.cursorView.background as AnimationDrawable).start()
        }
        slotValues.fillWith(viewBinding.realVerificationCodeEditText.text)
        slotViews.render(slotValues)
    }

    fun getCode(): String {
        return slotValues.toCodeString()
    }

    fun clear() {
        viewBinding.realVerificationCodeEditText.setText("")
    }

    fun isFilled(): Boolean =
        slotValues.isFilled()

    private fun List<VerificationCodeSlotView>.render(values: Array<CharSequence?>) {
        values.forEachIndexed { index, value ->
            val slotView = this[index]
            slotView.viewBinding.slotValueTextView.text = value
            slotView.viewBinding.root.isActivated = (value != null)
        }
    }

    private fun List<VerificationCodeSlotView>.moveCursorToFirstEmptySlot(values: Array<CharSequence?>) {
        val indexOfFirstEmptySlot = values.indexOfFirst { it == null }
        forEachIndexed { index, slotView ->
            slotView.viewBinding.cursorView.isVisible = (index == indexOfFirstEmptySlot)
        }
    }

    private fun Array<CharSequence?>.fillWith(s: Editable?): Array<CharSequence?> {
        if (s.isNullOrEmpty()) {
            fill(null)
            return this
        }
        val lowestSize = min(size, s.length)
        var i = 0
        while (i < lowestSize) {
            this[i] = s.subSequence(i, i + 1)
            ++i
        }
        if (i < size) {
            fill(null, i, size)
        }
        return this
    }


    private fun Array<CharSequence?>.isFilled(): Boolean =
        all { it != null }

    private fun Array<CharSequence?>.toCodeString(): String =
        joinToString(separator = "", prefix = "", postfix = "", limit = -1, truncated = "") { it ?: "" }
}