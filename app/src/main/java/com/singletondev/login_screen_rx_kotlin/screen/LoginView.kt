package com.singletondev.login_screen_rx_kotlin.screen

/**
 * Created by Randy Arba on 11/15/17.
 * This apps contains Login_screen_Rx_Kotlin
 * @email randy.arba@gmail.com
 * @github https://github.com/Leviaran
 */

interface LoginView {
    fun showProgress()
    fun hideProgress()
    fun setUserNameError()
    fun setPasswordError()
}