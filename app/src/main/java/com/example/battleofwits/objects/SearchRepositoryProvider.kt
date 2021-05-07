package com.example.battleofwits.objects

object SearchRepositoryProvider {
    fun provideSearchRepository(): SearchRepository {
        return SearchRepository()
    }
}