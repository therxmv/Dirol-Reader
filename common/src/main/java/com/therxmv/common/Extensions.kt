package com.therxmv.common

fun String.extractVersion() = this.filter { it.isDigit() }.toInt()