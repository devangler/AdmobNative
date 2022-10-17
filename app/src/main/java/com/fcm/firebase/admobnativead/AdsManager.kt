package com.fcm.firebase.admobnativead

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView


class AdsManager(
    private var callback: InterstitialControllerListener,
    private var mContext: Context?,
) {

    private var mAdmobNativeId: String? = null
    private var mIsForSmall = false
    private lateinit var layoutInflater: LayoutInflater

    fun loadNativeAdWithPriority(
        frameLayout: FrameLayout?, admobNativeId: String,
        isForSmall: Boolean,
    ) {
        if (frameLayout == null) {
            frameLayout?.removeAllViews()
            frameLayout?.visibility = View.GONE
            return
        }
        if (admobNativeId == "") {
            frameLayout.removeAllViews()
            frameLayout.visibility = View.GONE
            return
        }
        mAdmobNativeId = admobNativeId
        mIsForSmall = isForSmall


        loadAdmobNativeAd(frameLayout)

    }


    private fun loadAdmobNativeAd(adFrame: FrameLayout) {

        try {
            layoutInflater = LayoutInflater.from(mContext)
        } catch (ex: java.lang.Exception) {
        }
        try {
            val builder = mContext?.let { mAdmobNativeId?.let { it1 -> AdLoader.Builder(it, it1) } }
            builder?.forNativeAd { nativeAd ->
                try {

                    val adView: NativeAdView = if (mIsForSmall) {
                        layoutInflater.inflate(R.layout.layout_native_full_item,
                            null) as NativeAdView
                    } else {

                        layoutInflater.inflate(
                            R.layout.layout_native_full_item,
                            null
                        ) as NativeAdView
                    }
                    populateNativeAdView(nativeAd, adView, mIsForSmall)

                    adFrame.removeAllViews()
                    adFrame.addView(adView)


                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }
            val videoOptions = VideoOptions.Builder()
                .setStartMuted(true)
                .build()
            val adOptions = NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build()
            builder?.withNativeAdOptions(adOptions)
            val adLoader = builder?.withAdListener(object : AdListener() {

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    adFrame.removeAllViews()
                    adFrame.visibility = View.GONE
                    callback.onAdManhos(false)

                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    callback.onAdLoaded(true)


                }

                override fun onAdImpression() {
                    super.onAdImpression()


                }
            })?.build()
            adLoader?.loadAd(AdRequest.Builder().build())
        } catch (ignored: Exception) {

        }

    }

    private fun populateNativeAdView(
        nativeAd: NativeAd,
        adView: NativeAdView,
        isForSmall: Boolean,
    ) {
        if (!isForSmall) {
            val mediaView: com.google.android.gms.ads.nativead.MediaView =
                adView.findViewById(R.id.ad_media)
            adView.mediaView = mediaView
        }
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        adView.priceView = adView.findViewById(R.id.ad_price)
        adView.starRatingView = adView.findViewById(R.id.ad_stars)
        adView.storeView = adView.findViewById(R.id.ad_store)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)
        (adView.headlineView as TextView).text = nativeAd.headline
        if (nativeAd.body == null) {
            (adView.bodyView as TextView).visibility = View.GONE
        } else {
            (adView.bodyView as TextView).visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }
        if (nativeAd.callToAction == null) {
            (adView.callToActionView as TextView).visibility = View.GONE
        } else {
            (adView.callToActionView as TextView).visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }
        if (nativeAd.icon == null) {
            (adView.iconView as ImageView).visibility = View.GONE
        } else {
            (adView.iconView as ImageView).setImageDrawable(nativeAd.icon?.drawable)
            (adView.iconView as ImageView).visibility = View.VISIBLE
        }
        if (nativeAd.price == null) {
            (adView.priceView as TextView).visibility = View.GONE
        } else {
            (adView.priceView as TextView).visibility = View.VISIBLE
            (adView.priceView as TextView).text = nativeAd.price
        }
        if (nativeAd.store == null) {
            (adView.storeView as TextView).visibility = View.GONE
        } else {
            (adView.storeView as TextView).visibility = View.VISIBLE
            (adView.storeView as TextView).text = nativeAd.store
        }
        if (nativeAd.starRating == null) {
            (adView.starRatingView as RatingBar).visibility = View.GONE
        } else {
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating?.toFloat()!!
            (adView.starRatingView as RatingBar).visibility = View.VISIBLE
        }
        if (nativeAd.advertiser == null) {
            (adView.advertiserView as TextView).visibility = View.GONE
        } else {
            (adView.advertiserView as TextView).text = nativeAd.advertiser
            (adView.advertiserView as TextView).visibility = View.VISIBLE
        }
        adView.setNativeAd(nativeAd)

    }


    interface InterstitialControllerListener {
        fun onAdLoaded(isAdShown: Boolean)
        fun onAdManhos(isAdShown: Boolean)
    }
}