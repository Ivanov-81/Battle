package com.example.battleofwits.network

import com.example.battleofwits.*
import com.example.battleofwits.data.*
import com.example.battleofwits.adapters.AdapterListThemes
import com.example.battleofwits.ui.Auth
import com.example.battleofwits.ui.Login
import com.example.battleofwits.ui.QuestionActivity
import io.reactivex.Observable
import retrofit2.http.Body


import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ServerApiService {

    @GET(URL_START)
    fun search(): Observable<ServerResponse>

    @GET(URL_NEW_GAME)
    fun newGame(
        @Query("contest") contest: Int,
    ): Observable<CreateNewGame>

    @POST(URL_NEW_GAME)
    fun newGameOnTheme(
        @Body company: AdapterListThemes.Obj
    ): Observable<SendObjectThemeResult>

    @GET(URL_GET_QUESTION)
    fun getQuestion(
        @Query("game") game: Int,
    ): Observable<GetQuestion>

    @POST(URL_ANSWER)
    fun sendAnswer(
        @Body answer: QuestionActivity.Object
    ): Observable<SendAnswerResult>

    @POST(URL_LOGIN)
    fun login(
        @Body login: Login.Obj
    ): Observable<SendLoginDataResult>

    @POST(URL_REGISTER)
    fun register(
        @Body login: Auth.Obj
    ): Observable<SendLoginDataResult>

}