package com.lwh.secretdiary

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

    private val numberPicker1: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker1)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val numberPicker2: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker2)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val numberPicker3: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker3)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    // AppCompatButton 은 옛날 OS에서도 똑같이 보이게 해준다
    private val openButton: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.openButton)
    }

    private val changePasswordButton: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.changePasswordButton)
    }

    private var changePasswordMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberPicker1
        numberPicker2
        numberPicker3

        openButton.setOnClickListener {

            if (changePasswordMode) {
                Toast.makeText(this, "비밀번호 변경 중입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)

            val passwordFromUser =
                "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

            if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
                startActivity((Intent(this, DiaryActivity::class.java)))
            } else {
                // 얼럿다이어로그 띄우는 방법
                showErrorAlertDialog()
            }
        }

        changePasswordButton.setOnClickListener {

            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)

            val passwordFromUser =
                "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

            if (changePasswordMode) {
                // 번호를 저장하는 기능
                // sharedpreferences 저장방법 2가지 commit 동기 , apply 비동기
                passwordPreferences.edit(true) {

                    putString("password", passwordFromUser)
                }

                changePasswordMode = false
                changePasswordButton.setBackgroundColor(Color.BLACK)

            } else {
                // ChangePasswordMode 가 활성화 :: 비밀번호가 맞는지를 체크
                if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {

                    changePasswordMode = true
                    Toast.makeText(this, "변경할 패스워드를 이력해 주세요", Toast.LENGTH_SHORT).show()
                    changePasswordButton.setBackgroundColor(Color.RED)

                } else {
                    // 얼럿다이어로그 띄우는 방법
                    showErrorAlertDialog()
                }
            }
        }
    }

    private fun showErrorAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle("실패!!")
            .setMessage("비밀번호가 잘못되었습니다.")
            .setPositiveButton("확인") { _, _ -> }
            .create()
            .show()
    }
}
