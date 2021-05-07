package com.example.battleofwits.data

data class MyQuestions(
    val total: Int,
    val moderated: Int
)

data class QuestionsAnswers(
    val num: Int,
    val answer: String,
    val answer_img: String,
    val correct: Boolean,
)

data class Questions(
    val id: Int,
    val theme: Int,
    val question: String,
    val question_img: String,
    val timer: Int,
    val complexity: Int,
    val answers: List<QuestionsAnswers>,
)

data class Answers(
    val id: Int,
)

data class Rounds(
    val round_num: Int,
    val questions: List<Questions>,
    val answers: List<Answers>,
)

data class Gamers(
    val id: Int,
    val name: String
)

data class Games(
    val id: Int,
    val start: Long,
    val end: Long?,
    val theme: Themes?,
    val gamers: List<Gamers>,
    val moving_gamer: Int,
    val multi: Boolean,
    val rounds: List<Rounds>?,
)

data class Themes(
    val id: Int,
    val name: String,
    val path_to_img: String?,
    val lang: String
)

data class Data(
    val games: List<Games>,
    val themes: List<Themes>,
    val my_questions: MyQuestions
)

data class ListData(
    val games: List<Games>,
)

data class ServerResponse (
    val result: Boolean,
    val data: Data,
    val error: String?
)