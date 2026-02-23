package com.shantjap.app

import android.app.Application

class ShantJapApp : Application() {
    val container: AppContainer by lazy { AppContainer(this) }
}
