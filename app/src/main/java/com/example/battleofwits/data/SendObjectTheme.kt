package com.example.battleofwits.data

data class ThemeData (
    val id: Int,
    val start: Int,
    val end: Int?,
    val theme: Themes,
    val gamers: List<Gamers>,
    val moving_gamer: Int,
    val multi: Boolean,
    val rounds: Rounds?
)

data class SendObjectThemeResult (
    val result: Boolean,
    val data: ThemeData,
    val error: String?
)