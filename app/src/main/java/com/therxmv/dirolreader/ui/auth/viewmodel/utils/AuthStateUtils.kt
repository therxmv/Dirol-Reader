package com.therxmv.dirolreader.ui.auth.viewmodel.utils

import android.content.Context
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.therxmv.common.R

fun AuthState.getTitleString(context: Context): String {
    return when (this) {
        AuthState.PHONE -> context.getString(R.string.auth_title_phone)
        AuthState.CODE -> context.getString(R.string.auth_title_code)
        AuthState.PASSWORD -> context.getString(R.string.auth_title_password)
        AuthState.ERROR -> context.getString(R.string.auth_title_error)
        else -> ""
    }
}

fun AuthState.getKeyboardOptions(): KeyboardOptions =
    when (this) {
        AuthState.PHONE -> KeyboardOptions(
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Done
        )

        AuthState.CODE -> KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
        )

        AuthState.PASSWORD -> KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        )

        else -> KeyboardOptions.Default
    }