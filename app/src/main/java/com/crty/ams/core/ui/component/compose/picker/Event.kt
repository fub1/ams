package com.crty.ams.core.ui.compose.picker
// Event.kt
// git fork : https://github.com/whynotcompose/whynotcompose.git
// package org.imaginativeworld.whynotcompose.base.models

data class Event<out T>(
    val value: T,
    private val id: Int = if (lastId == Int.MAX_VALUE) {
        lastId = Int.MIN_VALUE
        Int.MAX_VALUE
    } else {
        lastId++
    }
) {
    companion object {
        private var lastId = Int.MAX_VALUE
    }

    private var valueSent = false

    /**
     * Get the [value] only once.
     */
    fun getValueOnce(): T? = if (!valueSent) {
        valueSent = true

        value
    } else {
        null
    }
}
