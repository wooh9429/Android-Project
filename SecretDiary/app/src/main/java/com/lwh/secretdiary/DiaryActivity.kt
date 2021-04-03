package com.lwh.secretdiary

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener

class DiaryActivity : AppCompatActivity() {

    //    private val diaryEditText: EditText by lazy {
//        findViewById<EditText>(R.id.diaryEditText)
//    }

    // 메인쓰레드에 연결된 핸들러(핸들러는 쓰레드간 통신 시켜주는 역할)
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        // onCreate밖으로 나갈일이 없을 경우 안에서 초기화
        val diaryEditText = findViewById<EditText>(R.id.diaryEditText)
        val detailPreferences = getSharedPreferences("diary", Context.MODE_PRIVATE)

        diaryEditText.setText(detailPreferences.getString("detail", ""))

        // 글 작성중 멈칫 할때 저장되도록 (쓰레드 사용, 비동기 방식으로 계속 저장 commit = false)
        val runnable = Runnable {
            getSharedPreferences("diary", Context.MODE_PRIVATE).edit {
                putString("detail", diaryEditText.text.toString())
            }
            Log.d("DiaryActivity", "SAVE!!")
        }

        // 쓰레드 작업 예제
//        val t = Thread(Runnable {
//            // 네트워크 작업
//
//            handler.post {
//
//            }
//        }).start()

        // 내용이 수정될때마다 자동저장
        diaryEditText.addTextChangedListener {

            Log.d("DiaryActivity", "TextChange:: $it")
            // 0.5초안에 입력이 되면 runnable이 삭제되고 0.5초 지났을 경우 텍스트를 저장하는 runnable 실행
            handler.removeCallbacks(runnable)
            handler.postDelayed(runnable, 500)
        }
    }
}