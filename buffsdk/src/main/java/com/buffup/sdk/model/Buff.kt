package com.buffup.sdk.model

import com.buffup.sdk.network.BuffError

data class Buff(val buffResult: BuffResult? = null, val buffError: BuffError? = null)

val Buff.succeeded
    get() = buffResult != null