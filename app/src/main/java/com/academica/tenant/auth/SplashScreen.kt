package com.academica.tenant.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.academica.tenant.MainActivity
import com.academica.tenant.R
import com.academica.tenant.core.helper.SessionManager

class SplashScreen : AppCompatActivity() {
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        sessionManager = SessionManager(this)
        Handler().postDelayed({
            if(sessionManager.getIsLogin()){
                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
            }else{
                startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
            }
        }, 3000)

    }
}