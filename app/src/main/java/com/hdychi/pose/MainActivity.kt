package com.hdychi.pose

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.File

class MainActivity : AppCompatActivity() {

    private val CHOOSE_PHOTO = 0
    private val CHOOSE_VIDEO = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PermissionUtil.requestPower(this)

        btn_upload_img.setOnClickListener {
           Matisse.from(this)
               .choose(MimeType.ofImage())
               .maxSelectable(1)
               .countable(false)
               .forResult(CHOOSE_PHOTO)
        }

        btn_upload_video.setOnClickListener {
            Matisse.from(this)
                .choose(MimeType.ofVideo())
                .maxSelectable(1)
                .countable(false)
                .forResult(CHOOSE_VIDEO)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val path = Matisse.obtainPathResult(data)[0]
            if (requestCode == CHOOSE_PHOTO) {
                val intent = Intent(this, AddImageActivity::class.java)
                intent.putExtra(AddImageActivity.IMAGE_PATH_KEY, path)
                startActivity(intent)

            } else if (requestCode == CHOOSE_VIDEO) {
                val intent = Intent(this, AddVideoActivity::class.java)
                intent.putExtra(AddVideoActivity.VIDEO_PATH_KEY, path)
                startActivity(intent)

            }
        }
    }


}
