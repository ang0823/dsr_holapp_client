package com.example.holapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_user_profile.*

class user_profile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        cancelBtn.setOnClickListener {
            val intent: Intent = Intent(this, FeedFotos::class.java)
            startActivity(intent)
            finish()
        }

    }


}
