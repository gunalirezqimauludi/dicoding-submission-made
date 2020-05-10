package com.gunalirezqi.moviecatalogue.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class ApiRepository {

    suspend fun doRequest(url: String): String =
        withContext(Dispatchers.Default) {
            URL(url).readText()
        }
}