package com.example.jcompose.model

import com.google.gson.annotations.SerializedName

class Book (
    @SerializedName("titulo")
    var title: String = "",
    var category: String = "",
    @SerializedName("autor")
    var author: String = "",
    @SerializedName("ano")
    var year: Int = 0,
    @SerializedName("paginas")
    var pages: Int = 0,
    @SerializedName("capa")
    var coverUrl: String = ""
)