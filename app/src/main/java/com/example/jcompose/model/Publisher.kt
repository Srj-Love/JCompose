package com.example.jcompose.model

import com.google.gson.annotations.SerializedName

data class Publisher(
    @SerializedName("novatec")
    var categories: List<Category> = emptyList()
)