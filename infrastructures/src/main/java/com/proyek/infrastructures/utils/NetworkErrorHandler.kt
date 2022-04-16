package com.proyek.infrastructures.utils

import android.accounts.NetworkErrorException
import android.content.Context
import android.util.Log

import java.io.IOException

object NetworkErrorHandler {
    fun checkResponse(code: Int, context: Context?=null) {
      if(code in 300..400) {

      }  else if(code in 400..500) {

      } else if(code > 500) {

      }
    }
    fun checkFailure(t: Throwable, context: Context){
        Log.d("error", context.packageName+" :"+t.message)

        if(t is IOException){

        } else if( t is NetworkErrorException) {

        } else {

        }
    }
}