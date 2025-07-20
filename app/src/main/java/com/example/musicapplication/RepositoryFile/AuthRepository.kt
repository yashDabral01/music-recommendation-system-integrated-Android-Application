package com.example.musicapplication.RepositoryFile

import android.util.Base64
import android.util.Log
import com.example.musicapplication.Api.SpotifyAuthService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.HttpException
import java.io.IOException
import kotlin.math.exp

//class AuthRepository {
//    private val authService: SpotifyAuthService by lazy {
//        Retrofit.Builder()
//            .baseUrl("https://accounts.spotify.com/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(SpotifyAuthService::class.java)
//    }
//
//suspend fun getAccessToken(clientId: String, clientSecret: String): Response<Pair<String, Int?>> {
//    val authHeader = "Basic " + Base64.encodeToString("$clientId:$clientSecret".toByteArray(), Base64.NO_WRAP)
//    return try {
//        val retrofitResponse = authService.getAuthToken(authHeader)
//
//        // Log the raw response for debugging
//        Log.d("AuthRepository", "Raw Response: ${retrofitResponse.raw()}")
//
//        if (retrofitResponse.isSuccessful) {
//            val accessToken = retrofitResponse.body()?.accessToken
//            val expires = retrofitResponse.body()?.expiresIn
//
//            if (accessToken != null) {
//                Response.Success(Pair(accessToken, expires))
//            } else {
//                Log.e("AuthRepository", "Error: Access token not found in response body.")
//                Response.Error("Access token not found")
//            }
//        } else {
//            Log.e("AuthRepository", "Failed to authenticate: ${retrofitResponse.message()}")
//            Response.Error("Failed to authenticate: ${retrofitResponse.message()}")
//        }
//    } catch (e: HttpException) {
//        Response.Error("HttpException: ${e.message}")
//    } catch (e: IOException) {
//        Response.Error("IOException: ${e.message}")
//    } catch (e: Exception) {
//        Response.Error("Unexpected error: ${e.message}")
//    }
//}
//}
class AuthRepository(
    private val clientId: String,
    private val clientSecret: String
) {
    private var token: String? = null
    private var tokenExpiry: Long = 0

    suspend fun getToken(): String {
        return if (token == null || isTokenExpired()) {
            fetchNewToken()
        } else {
            token!!
        }
    }

    private fun isTokenExpired(): Boolean {
        return System.currentTimeMillis() > tokenExpiry
    }

    private suspend fun fetchNewToken(): String {
        // Basic auth header using client ID and client secret
        val authHeader = "Basic " + android.util.Base64.encodeToString(
            "$clientId:$clientSecret".toByteArray(),
            android.util.Base64.NO_WRAP
        )

        // Make API call to fetch the token
        val response = Retrofit.Builder()
            .baseUrl("https://accounts.spotify.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpotifyAuthService::class.java)
            .getAccessToken("client_credentials", authHeader)

        if (response.isSuccessful) {
            token = response.body()?.accessToken
            tokenExpiry = System.currentTimeMillis() + (response.body()?.expiresIn ?: 0) * 1000
            return token!!
        } else {
            throw Exception("Failed to fetch token")
        }
    }
}
