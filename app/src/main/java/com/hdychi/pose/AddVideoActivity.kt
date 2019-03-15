package com.hdychi.pose

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.MediaController
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_video.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.File


class AddVideoActivity : AppCompatActivity() {
    private var videoPath : String? = null
    private val estimationApi : EstimationApi = RetrofitProvider.create(EstimationApi::class.java)

    companion object {
        const val VIDEO_PATH_KEY = "video_path"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_video)
        videoPath = intent.extras.getString(VIDEO_PATH_KEY)
        videoPath?.apply {
            val file = File(videoPath)
            if (!file.exists()) {
                return
            }
            video_add_video.setVideoPath(videoPath)
            val controller = MediaController(this@AddVideoActivity)
            controller.setAnchorView(video_add_video)
            video_add_video.setMediaController(controller)
            /*video_add_video.setOnPreparedListener{ video_add_video.start() }
            video_add_video.setOnCompletionListener{ video_add_video.start() }*/
            btn_add_video.setOnClickListener {
                uploadVideo(file)
                btn_add_video.isEnabled = false
                btn_add_video.text = "已上传视频，等待服务器处理..."
                btn_add_video.setBackgroundResource(R.drawable.shape_disable_button)
            }
            // video_add_video.setVideoPath("http://172.23.242.149/video/7c73b451bfb00dd237f88ebb89b1fd94.mp4")
        }
    }

    private fun uploadVideo(file: File) {
        val videoBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val list = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("video", file.name, videoBody)
            .build()
            .parts()
        launch(UI) {
            try {
                val response : ResponseBody = estimationApi.uploadVideo(list).await()
                val url = response.string()
                btn_add_video.text = "处理完成"
                video_add_video.setVideoPath(url)
                System.out.println("video url: $url \n")

            } catch (e : Exception) {
                e.printStackTrace()
                Toast.makeText(this@AddVideoActivity, "网络错误: " + e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}