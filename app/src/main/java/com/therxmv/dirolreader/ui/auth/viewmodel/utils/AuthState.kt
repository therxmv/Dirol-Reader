package com.therxmv.dirolreader.ui.auth.viewmodel.utils

/**
 * [START] - initial state.
 *
 * [READY] - successfully logged in.
 *
 * [PROCESSING] - imitate loading when data was sent for verification.
 *
 * [PHONE], [CODE], [PASSWORD] - log in states.
 */
enum class AuthState {
    START,
    PHONE,
    CODE,
    PASSWORD,
    READY,
    ERROR,
    PROCESSING,
}