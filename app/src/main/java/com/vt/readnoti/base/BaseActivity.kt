package com.vt.readnoti.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity: AppCompatActivity() {

    fun start(clazz: Class<*>, bundle: Bundle? = null) {
        val intent = Intent(this, clazz)
        bundle?.let {
            intent.putExtras(it)
        }
        startActivity(intent)
    }
}