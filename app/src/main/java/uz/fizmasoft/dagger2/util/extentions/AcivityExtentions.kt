package uz.fizmasoft.dagger2.util.extentions

import android.widget.Toast
import dagger.android.support.DaggerAppCompatActivity

fun DaggerAppCompatActivity.toast(message : String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
