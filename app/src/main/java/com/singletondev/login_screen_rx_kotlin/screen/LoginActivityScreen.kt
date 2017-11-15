package com.singletondev.login_screen_rx_kotlin.screen

import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.ProgressBar
import com.singletondev.login_screen_rx_kotlin.R

/**
 * Created by Randy Arba on 11/15/17.
 * This apps contains Login_screen_Rx_Kotlin
 * @email randy.arba@gmail.com
 * @github https://github.com/Leviaran
 */

class LoginActivityScreen : AppCompatActivity(), LoginView{

    private lateinit var progressBar : ProgressBar
    private lateinit var userNameWrapper : TextInputLayout
    private lateinit var userNameEditText : EditText
    private lateinit var passwordWrapper : TextInputLayout
    private lateinit var passwordEditText : EditText

    private lateinit var loginPresenter : LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        progressBar = findViewById(R.id.login_progress) as ProgressBar
        userNameWrapper = findViewById(R.id.username_wrapper) as TextInputLayout
        userNameEditText = findViewById(R.id.email) as EditText
        passwordWrapper = findViewById(R.id.password_wrapper) as TextInputLayout
        passwordEditText = findViewById(R.id.password) as EditText

        loginPresenter = LoginPresenterImp(this)

        loginPresenter.validateCredentialUserName(userNameWrapper,userNameEditText)
        loginPresenter.validateCredentialPassword(passwordWrapper,passwordEditText)


    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }

    override fun setUserNameError() {

    }

    override fun setPasswordError() {

    }
}