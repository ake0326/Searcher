package com.example.searcher.network

import com.example.searcher.models.responses.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Apis {
    @GET("/hotpepper/gourmet/v1/")
    fun getSearch(
        @Query("key") key: String,
        @Query("lat") lat: String,
        @Query("lng") lng: String,
        @Query("range") range: Int,
        @Query("format") format: String = "json"
    ): Call<SearchResponse>
}