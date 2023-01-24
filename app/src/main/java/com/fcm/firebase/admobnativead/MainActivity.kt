package com.fcm.firebase.admobnativead

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fcm.firebase.admobnativead.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private var adsManager: AdsManager? = null
    lateinit var binding: ActivityMainBinding
    private var adFrame: FrameLayout? = null
    private lateinit var billing: BillingUtilsIAP
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        billing = BillingUtilsIAP(this@MainActivity)
        binding.showAd.setOnClickListener {
            val intent = Intent(this@MainActivity, NextActivity::class.java)
            startActivity(intent)
            if (Constants.splash_inter_config == "1") {
                InterstitialManager.showAdmobInterstitial(this@MainActivity)
            }else{
                Toast.makeText(this, "Ads Off", Toast.LENGTH_SHORT).show()
            }
        }
        if (isInternetConnected(this)) {
            callingNative()
        }
        binding.premium.setOnClickListener {
            billing.purchase(this, "android.test.purchased")

        }

    }

    private fun callingNative() {
        if (adsManager == null) {
            adFrame = binding.adFrame
            adsManager = AdsManager(object : AdsManager.InterstitialControllerListener {
                override fun onAdLoaded(isAdShown: Boolean) {
                    binding.animationView.visibility = View.GONE
                    binding.adsTxt.visibility = View.GONE
                }

                override fun onAdManhos(isAdShown: Boolean) {
                    binding.adWhole.visibility = View.GONE
                }

            }, this@MainActivity)
            adsManager?.loadNativeAdWithPriority(
                binding.adFrame,
                "ca-app-pub-3940256099942544/2247696110",
                false
            )!!
        } else {
            if (adFrame != null)
                binding.adFrame.visibility = View.VISIBLE
        }
    }

    private fun isInternetConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            networkInfo != null && networkInfo.isConnected
        }
    }
}