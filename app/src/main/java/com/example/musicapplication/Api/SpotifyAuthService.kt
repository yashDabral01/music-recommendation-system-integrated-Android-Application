package com.example.musicapplication.Api

import com.example.musicapplication.Models.AuthToken
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

//interface SpotifyAuthService {
//    @FormUrlEncoded
//    @POST("https://accounts.spotify.com/api/token")
//    suspend fun getAuthToken(
//        @Header("Authorization") authorization: String,
//        @Field("grant_type") grantType: String = "client_credentials"
//    ): Response<AuthToken>
//}
interface SpotifyAuthService {
    @POST("token")
    @FormUrlEncoded
    suspend fun getAccessToken(
        @Field("grant_type") grantType: String,
        @Header("Authorization") authHeader: String
    ): Response<AuthToken>
}


