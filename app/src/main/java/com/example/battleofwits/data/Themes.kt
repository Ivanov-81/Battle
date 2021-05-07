package com.example.battleofwits.data

data class DataTheme(
    val theme: List<Themes>,
)

data class CreateNewGame (
    val result: Boolean,
    val data: DataTheme,
    val error: String?
)