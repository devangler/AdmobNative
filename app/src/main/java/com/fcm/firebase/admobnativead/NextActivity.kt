package com.fcm.firebase.admobnativead

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import com.fcm.firebase.admobnativead.databinding.ActivityNextBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

class NextActivity : AppCompatActivity() {
    lateinit var binding:ActivityNextBinding
    private var adView: AdView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityNextBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        loadAdmobBanner()

    }

    private val adSize: AdSize get() {
            val display = this.windowManager.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)
            val density = outMetrics.density
            var adWidthPixels = binding.flAdViewBannerClock.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }
            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this@NextActivity, adWidth)
        }

    private fun loadAdmobBanner() {
        adView = AdView(this@NextActivity)
        binding.flAdViewBannerClock.removeAllViews()
        binding.flAdViewBannerClock.addView(adView)

        adView?.adUnitId = resources.getString(R.string.admob_banner_clock_id)
        adView?.setAdSize(adSize)
        val adRequest = AdRequest.Builder().build()
        adView?.loadAd(adRequest)
    }

    override fun onPause() {
        adView?.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        adView?.resume()
    }

    override fun onDestroy() {
        adView?.destroy()
        super.onDestroy()
    }
}