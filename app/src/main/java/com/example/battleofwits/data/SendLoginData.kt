package com.example.battleofwits.data

data class SendLoginDataResult (
    val result: Boolean,
    val data: String?,
    val error: String?
)

data class SendLoginData (
    var login: String,
    var password: String,
)