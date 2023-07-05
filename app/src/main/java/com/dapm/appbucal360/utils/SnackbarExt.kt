package com.dapm.appbucal360.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showErrorSnackbar(message: String) {
    Snackbar.make(
        this,
        message,
        Snackbar.LENGTH_LONG
    ).show()
}

fun View.showSuccessSnackbar(message: String) {
    Snackbar.make(
        this,
        message,
        Snackbar.LENGTH_LONG
    ).show()
}
