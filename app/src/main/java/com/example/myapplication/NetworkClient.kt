package com.example.myapplication

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// Gson을 사용한 Retrofit 부분, 복붙해서 사용
object NetWorkClient {
    // 오픈 API 상세정보에서 서비스 URL 넣으면 됨
    private const val DUST_BASE_URL = "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/"
    private fun createOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()

        // BulidConfig가 import가 안되면, buildFeaturesd에 추가
        // 클린 프로젝트, 리빌드프로젝트, 재시작 <- 이렇게 하면 보통 됨
        if (BuildConfig.DEBUG)
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        else
            interceptor.level = HttpLoggingInterceptor.Level.NONE

        return OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addNetworkInterceptor(interceptor)
            .build()
    }

    // URL을 갖고 Gson을 생성하는 팩토리메서드
    private val dustRetrofit = Retrofit.Builder()
        .baseUrl(DUST_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(createOkHttpClient())
        .build()

    // 인터페이스를 파라미터로 Retrofit 생성
    val dustNetWork: NetworkInterface = dustRetrofit.create(NetworkInterface::class.java)

}
