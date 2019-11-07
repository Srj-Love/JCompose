package com.example.jcompose.model

import com.google.gson.annotations.SerializedName

class Category (@SerializedName("categoria")
                var name: String = "",
                @SerializedName("livros")
                var books: List<Book> = emptyList())