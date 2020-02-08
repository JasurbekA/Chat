package uz.fizmasoft.dagger2.util.extentions

import android.text.Editable

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
