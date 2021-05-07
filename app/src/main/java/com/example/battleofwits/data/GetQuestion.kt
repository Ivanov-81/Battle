package com.example.battleofwits.data

data class AnsQuest (
    val num: Int,
    val answer: String,
    val answer_img: String?,
    val correct: Boolean,
)

data class QuestionsData (
    val id: Int,
    val theme: String,
    val question: String,
    val question_img: String?,
    val timer: Int,
    val complexity: Int,
    val answers: List<AnsQuest>
)

data class QuestionsData2 (
    val round_num: Int,
    val questions: List<QuestionsData>,
    val answers: List<AnsQuest>,
)

data class GetQuestion (
    val result: Boolean,
    val data: List<QuestionsData2>,
    val error: String?
)