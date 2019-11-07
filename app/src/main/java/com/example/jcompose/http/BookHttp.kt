package com.example.jcompose.http

import com.example.jcompose.model.Book
import com.example.jcompose.model.Publisher
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object  BookHttp {

    private const val BOOK_JSON_URL = "https://raw.githubusercontent.com/nglauber/dominando_android2/master/livros_novatec.json"

    fun loadBooksGson(): List<Book>? {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()

        val request = Request.Builder()
            .url(BOOK_JSON_URL)
            .build()

        try {
            val response = client.newCall(request).execute()
            val json = response.body()?.string()
            val gson = Gson()
            val publisher = gson.fromJson<Publisher>(json, Publisher::class.java)
            return publisher.categories
                .flatMap { category ->
                    category.books.forEach { book ->
                        book.category = category.name
                    }
                    category.books
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


}