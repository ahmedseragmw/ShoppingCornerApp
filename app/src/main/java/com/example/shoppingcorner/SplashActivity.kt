package com.example.shoppingcorner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.WindowManager

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFullScreen()
        setContentView(R.layout.activity_splash)

        initActionBar()

        Handler().postDelayed(
            {
                    val prefs=getSharedPreferences("prefs", MODE_PRIVATE)
                    if(prefs.getBoolean("loggedIn",false)){
                        startActivity(Intent(this,MainActivity::class.java))
                    }
                    else{
                        startActivity(Intent(this,LoginActivity::class.java))
                    }
                    finish()


            },
            2000
        )
    }

    private fun initActionBar() {
        if(supportActionBar!=null){
            supportActionBar!!.hide()
        }
    }

    private fun initFullScreen() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }
}