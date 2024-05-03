package com.example.myapplication

import retrofit2.http.GET
import retrofit2.http.QueryMap

// HashMap형태(요청변수: 서비스키, 데이터방식 등등)의 데이터를 받으면 Dust 형태의 데이터로 받아옴
interface NetworkInterface {
    @GET("getMinuDustFrcstDspth")
    suspend fun getDust(@QueryMap param: HashMap<String, String>): Dust
}