package com.dev.util

fun <T, R, P> safeLeft(t: T?, r: R?, block: (T, R) -> P?): P? {
    return if (t != null && r != null) block(t, r) else null
}
