package com.therxmv.dirolreader.ui.auth.viewmodel.utils

/**
 * - [START] - initial state;
 * - [PHONE], [CODE], [PASSWORD] - log in states;
 * - [READY] - successfully logged in;
 * - [ERROR] - problems with TdApi;
 * - [PROCESSING] - imitate loading when data was sent for verification.
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