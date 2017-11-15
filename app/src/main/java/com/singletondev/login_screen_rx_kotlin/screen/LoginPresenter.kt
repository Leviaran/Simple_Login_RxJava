package com.singletondev.login_screen_rx_kotlin.screen

import android.support.design.widget.TextInputLayout
import android.widget.EditText

/**
 * Created by Randy Arba on 11/15/17.
 * This apps contains Login_screen_Rx_Kotlin
 * @email randy.arba@gmail.com
 * @github https://github.com/Leviaran
 */

interface LoginPresenter{
    fun validateCredentialUserName(usernameWrapper : TextInputLayout, userNameEditText : EditText)
    fun validateCredentialPassword(passwordWrapper : TextInputLayout, passwordEditText : EditText)
    fun onDestroy()
}