package com.example.battleofwits.data

data class AnswerData (
    val game_over: Boolean
)
data class SendAnswerResult (
    val result: Boolean,
    val data: AnswerData,
    val error: String?
)
