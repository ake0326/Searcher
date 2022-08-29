package com.example.searcher.network

import com.example.searcher.models.responses.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface Apis {
    @GET("/hotpepper/gourmet/v1/")
    fun getSearch(
        @QueryMap option: Map<String,String?>
    ): Call<SearchResponse>
}