package com.example.guru2_10

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class CameraActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
        const val REQUEST_CAMERA_PERMISSION = 2
    }

    private lateinit var imageView2: ImageView
    private var isPhotoCaptured = false // 사진 촬영 여부 확인 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        val btnCapture: Button = findViewById(R.id.btnCapture)
        val button4: Button = findViewById(R.id.button4)
        imageView2 = findViewById(R.id.imageView2)

        // 카메라 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        }

        // 카메라 실행 버튼
        btnCapture.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            }
        }

        // 다음 화면 이동 버튼 (사진 촬영 후 이동 가능)
        button4.setOnClickListener {
            if (isPhotoCaptured) {
                val intent = Intent(this, RecycleActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "사진 촬영 후 이동할 수 있습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 카메라 실행 함수
    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } else {
            Toast.makeText(this, "카메라를 실행할 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // 권한 요청 결과 처리
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera() // 권한이 허용되었을 때 카메라 실행
            } else {
                Toast.makeText(this, "카메라 권한이 필요합니다. 설정에서 권한을 허용해주세요.", Toast.LENGTH_LONG).show()
            }
        }
    }


    // 사진 촬영 후 결과 처리
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            if (imageBitmap != null) {
                imageView2.setImageBitmap(imageBitmap) // 촬영한 사진을 ImageView에 설정
                isPhotoCaptured = true // 사진 촬영 완료 상태 변경
            } else {
                Toast.makeText(this, "이미지를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
