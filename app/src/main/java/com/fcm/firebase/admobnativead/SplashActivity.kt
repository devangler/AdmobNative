package com.fcm.firebase.admobnativead

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings


class SplashActivity : AppCompatActivity() {
    private var mFirebaseRemoteConfig: FirebaseRemoteConfig? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        InterstitialManager.loadAdmobInterstitial(this, getString(R.string.splash_interstitial))

        fetchConfig()
        move(this)


        mFirebaseRemoteConfig!!.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    Toast.makeText(this@SplashActivity, "Fetch and activate succeeded",
                        Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@SplashActivity, "Fetch failed",
                        Toast.LENGTH_SHORT).show()
                }
                Constants.splash_inter_config=
                    mFirebaseRemoteConfig?.getString("splash_inter_value").toString()
                Log.d("TAG", "onCreate: ${Constants.splash_inter_config}")
            }


    }

    private fun fetchConfig() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(1)
            .build()
        mFirebaseRemoteConfig?.setConfigSettingsAsync(configSettings)
    }

    private fun move(context: Context) {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}