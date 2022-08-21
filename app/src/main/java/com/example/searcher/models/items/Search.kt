package com.example.searcher.models.items

data class Search(
    val api_version : String,
    val results_available : Int,
    val results_returned : Int,
    val results_start : Int,
    val shop : List<Shop>
)
