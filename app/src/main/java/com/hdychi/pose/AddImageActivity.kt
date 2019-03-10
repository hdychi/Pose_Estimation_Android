package com.hdychi.pose

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_add_image.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.io.DataOutputStream


class AddImageActivity : AppCompatActivity() {
    private var imagePath : String? = null
    private val estimationApi : EstimationApi = RetrofitProvider.create(EstimationApi::class.java)

    companion object {
        const val IMAGE_PATH_KEY = "image"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_image)
        imagePath = intent.extras.getString(IMAGE_PATH_KEY)
        imagePath?.apply {
            val file = File(imagePath)
            if (!file.exists()) {
                return
            }
            Glide.with(this@AddImageActivity)
                .load(Uri.fromFile(file))
                .into(iv_add_image)
            btn_add_image.setOnClickListener {
                uploadImage(file)
                btn_add_image.text = "已上传图片，服务器处理中...."
                btn_add_image.isEnabled = false
            }
        }
    }

    private fun uploadImage(file: File) {

        val picBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val list = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("img", file.name, picBody)
            .build()
            .parts()
        launch(UI) {
            try {
                val response : ResponseBody = estimationApi.uploadImg(list).await()
                val url = response.string()
                btn_add_image.text = "处理完成"
                Glide.with(this@AddImageActivity)
                    .load(url)
                    .into(iv_add_image)
            } catch (e : Exception) {
                e.printStackTrace()
                btn_add_image.text = "网络错误 ${e.message}"
            }
        }
    }
}