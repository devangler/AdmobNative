package com.fcm.firebase.admobnativead

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.CountDownTimer
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.TimeUnit

object InterstitialManager {

    private val TAG = "InterstitialADTag"
    private var interstitialTimeElapsed = 0L
    private var admobInterstitialAd: InterstitialAd? = null
    private var isAdLoading = false
    private val CAPPING_TIME = 1
    private var timer: CountDownTimer? = null


    fun showAdmobInterstitial(activity: Activity) {
        if (isNetworkAvailable(activity) && timeDifference(interstitialTimeElapsed) > CAPPING_TIME) {
            Log.i(TAG, "admob show and load called")

            if (admobInterstitialAd != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    withContext(Dispatchers.Main) {
                        admobInterstitialAd?.show(activity)
                        admobInterstitialAd?.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdImpression() {
                                    super.onAdImpression()
                                    Log.i(TAG, "onAdImpression")
                                    admobInterstitialAd = null
                                    interstitialTimeElapsed = Calendar.getInstance().timeInMillis

                                }

                                override fun onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent()
                                    Log.i(TAG, "onAdDismissedFullScreenContent")



                                }
                            }
                    }
                }
            }
        }
    }

    fun loadAdmobInterstitial(activity: Activity, admobID: String) {
        if (admobInterstitialAd == null) {
            isAdLoading = true
            Log.d(TAG, "Admob: Ad load called.")

            InterstitialAd.load(
                activity,
                admobID,
                AdRequest.Builder().build(),
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Log.d(TAG, adError.message)
                        admobInterstitialAd = null
                        isAdLoading = false
                        timer?.onFinish()

                    }

                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        Log.d(TAG, "admob Ad was loaded.")
                        admobInterstitialAd = interstitialAd
                        isAdLoading = false

                    }
                })
        }
    }

    private fun timeDifference(millis: Long): Int {
        val current = Calendar.getInstance().timeInMillis
        val elapsedTime = current - millis
        return TimeUnit.MILLISECONDS.toSeconds(elapsedTime).toInt()
    }



    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}