package com.example.ml_aws

import android.app.Application
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.predictions.aws.AWSPredictionsPlugin

class AwsMlApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        try {
            // Spraudnis autentifikācijas kategorijai
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            // Spraudnis mākslīgā intelekta kategorijai
            Amplify.addPlugin(AWSPredictionsPlugin())

            // Amplify inicializēšana
            Amplify.configure(applicationContext)
            Log.i("MyAmplifyApp", "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error)
        }
    }
}