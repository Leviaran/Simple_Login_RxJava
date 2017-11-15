package com.singletondev.login_screen_rx_kotlin.screen

import android.support.design.widget.TextInputLayout
import android.util.Patterns
import android.widget.EditText
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

/**
 * Created by Randy Arba on 11/15/17.
 * This apps contains Login_screen_Rx_Kotlin
 * @email randy.arba@gmail.com
 * @github https://github.com/Leviaran
 */

class LoginPresenterImp(var loginView: LoginView) : LoginPresenter {

    private val verifikasiUpperCase = ObservableTransformer<String,String> {
        observable -> observable.flatMap {
        Observable.just(it).map { it.trim() }
                .filter { Pattern.compile("[A-Z ]").matcher(it).find() }
                .singleOrError()
                .onErrorResumeNext {
                    when {
                        it is NoSuchElementException -> Single.error(Exception("Password harus terdapat minimal satu huruf besar"))
                        else -> Single.error(it)
                    }
                }
                .toObservable()
        }
    }

    private val verifikasiAngka = ObservableTransformer<String,String> {
        observable -> observable.flatMap {
        Observable.just(it).map { it.trim() }
                .filter { Pattern.compile("[0-9 ]").matcher(it).find() }
                .singleOrError()
                .onErrorResumeNext {
                    when {
                        it is NoSuchElementException -> Single.error(Exception("Password harus terdapat minimal satu angka"))
                        else -> Single.error(it)
                    }
                }
                .toObservable()
        }
    }

    private val panjangLebihDariEnam = ObservableTransformer<String,String> {
        observable -> observable.flatMap {     //Membuat flatmap berisi Observable Baru
        Observable.just(it).map { it.trim() } // String kata di proses dan di-Trim() artinya spasi kosong depan dan belakang dihilangkan
                .filter { it.length > 6 } //memfilter artinya kata akan diproses kembali jika panjang kata lebih dari enam
                .singleOrError() //jika ternyata kata kurang dari maka akan terjadi Error dan diatur dalam Single Observable
                .onErrorResumeNext { //Error ditangkap kemudian di alihkan untuk dilanjutkan kembali
                    when{
                        it is NoSuchElementException -> Single.error(Exception("Panjang Huruf harus lebih dari enam"))
                        else -> Single.error(it)
                    }
                }.toObservable()
        }
    }

    private val lengthGreaterThan = ObservableTransformer<String,String>{
        observable -> observable.flatMap {
        Observable.just(it).map { it.trim() }
                .filter { it.length > 6 }
                .singleOrError()
                .onErrorResumeNext {
                    when {
                        it is NoSuchElementException -> Single.error(Exception("Panjang Huruf harus lebih dari enam"))
                        else -> Single.error(it)
                    }
                }
                .toObservable()
        }
    }

    private val verifikasiPolaEmail = ObservableTransformer<String,String> {
        observable -> observable.flatMap {
        Observable.just(it).map { it.trim() }
                .filter { Patterns.EMAIL_ADDRESS.matcher(it).matches() }
                .singleOrError()
                .onErrorResumeNext {
                    when {
                        it is NoSuchElementException -> Single.error(Exception("Email Anda tidak valid"))
                        else -> Single.error(it)
                    }
                }
                .toObservable()
        }
    }

    private val verifyEmailPattern = ObservableTransformer<String, String>{
        observable -> observable.flatMap {
        Observable.just(it).map { it.trim() }
                .filter { Patterns.EMAIL_ADDRESS.matcher(it).matches() }
                .singleOrError()
                .onErrorResumeNext {
                    when {
                        it is NoSuchElementException -> Single.error(Exception("Email tidak valid"))
                        else -> Single.error(it)
                    }
                }
                .toObservable()
        }
    }

    private inline fun retryWhenError(crossinline onError: (ex: Throwable) -> Unit) : ObservableTransformer<String, String> = ObservableTransformer { observable ->
        observable.retryWhen { errors ->
            errors.flatMap {
                onError(it)
                Observable.just(it)
            }
        }
    }

    override fun validateCredentialUserName(usernameWrapper: TextInputLayout, userNameEditText: EditText) {
        RxTextView.afterTextChangeEvents(userNameEditText)
                .skipInitialValue()
                .map {
                    usernameWrapper.error = null
                    it.view().text.toString()
                }
                .debounce(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                .compose(panjangLebihDariEnam)
                .compose(verifyEmailPattern)
                .compose(retryWhenError {
                    usernameWrapper.error = it.message
                })
                .subscribe()
    }

    override fun validateCredentialPassword(passwordWrapper: TextInputLayout, passwordEditText: EditText) {
        RxTextView.afterTextChangeEvents(passwordEditText)
                .skipInitialValue()
                .map {
                    passwordWrapper.error = null
                    it.view().text.toString()
                }
                .debounce(1,TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                .compose(panjangLebihDariEnam)
                .compose(verifikasiAngka)
                .compose(verifikasiUpperCase)
                .compose(retryWhenError {
                    passwordWrapper.error = it.message
                })
                .subscribe()
    }

    override fun onDestroy() {

    }

}