package com.example.battleofwits.data

data class AnswersQuest (
    val id: Int,
    val text: String,
    val img_url: String?,
    val correct: Boolean,
)

data class Answer (
    val id: Int,
)

data class Quest (
    val theme: String,
    val text: String,
    val img_url: String?,
    val timer: Int,
    val difficulty: Int,
)

data class QuestionData (
    val id: Int,
    val round_num: Int,
    val question: Quest,
    val answer: Answer?,
    val timer_left: Int,
    val score: Int,
    val answers: List<AnswersQuest>,
)

data class Question (
    val result: Boolean,
    val data: QuestionData,
    val error: String?
)