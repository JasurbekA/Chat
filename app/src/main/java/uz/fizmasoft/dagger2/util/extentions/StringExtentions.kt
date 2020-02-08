package uz.fizmasoft.dagger2.util.extentions

import android.text.Editable

fun String.toEditable() = Editable.Factory.getInstance().newEditable(this)
