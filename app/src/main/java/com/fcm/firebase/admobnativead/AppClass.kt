package com.fcm.firebase.admobnativead

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics


/**
 * @Author: Kamran Khan
 * @Date: 24,January,2023
 * @Accounts
 *      -> https://stackoverflow.com/users/17921670/kamran-khan
 */
class AppClass : Application() {
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    override fun onCreate() {
        super.onCreate()
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

    }

}