package com.hdychi.pose

import kotlinx.coroutines.experimental.Deferred
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface EstimationApi {
    @POST("img")
    @Multipart
    fun uploadImg(@Part list : List<MultipartBody.Part>): Deferred<ResponseBody>

    @POST("video")
    @Multipart
    fun uploadVideo(@Part list : List<MultipartBody.Part>): Deferred<ResponseBody>
}