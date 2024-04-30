package ru.kpfu.itis.debugmenu.feature.network.api

import retrofit2.http.GET

interface TestApi {
    @GET("/first")
    suspend fun test()
}