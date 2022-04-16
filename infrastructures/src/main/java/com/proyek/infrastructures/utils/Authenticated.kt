package com.proyek.infrastructures.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.proyek.infrastructures.user.agent.entities.UserAgent

object Authenticated {
    lateinit var context: Context
    private const val KEY_IS_VALID_CACHE_MEMBER = "isValid"
    private const val KEY_USER_CUSTOMER = "user_customer"
    private const val KEY_USER_AGENT = "user_agent"
    private const val KEY_MEMBER = "member"
    private const val KEY_TOKEN = "token"
    private const val PREFS_NAME = "auth_pref"
    private lateinit var preferences : SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val gson = Gson()

    fun init(context: Context){
        this.context = context
        preferences = Authenticated.context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        editor = preferences.edit()
    }

    fun setUserAgent(value: UserAgent) {
        editor.putString(KEY_USER_AGENT, gson.toJson(value))
        editor.putBoolean(KEY_IS_VALID_CACHE_MEMBER, true)
        editor.apply()
    }

    fun getUserAgent(): UserAgent {
            if(preferences.getString(KEY_USER_AGENT, "")!="") {
            return gson.fromJson(preferences.getString(KEY_USER_AGENT, ""), UserAgent::class.java)
        } else {
            return UserAgent()
        }
    }

    fun setAccessToken(value: String) {
        editor.putString(KEY_TOKEN, value.toString())
        editor.apply()
    }

    fun getAccessToken(): String? {
        //return preferences.getString(KEY_TOKEN, null)
        return ""
    }

    fun invalidateCache() {
        val editor = preferences.edit()
        editor.putBoolean(KEY_IS_VALID_CACHE_MEMBER, false)
        editor.apply()
    }

    fun isValidCacheMember(): Boolean {
        return preferences.getBoolean(KEY_IS_VALID_CACHE_MEMBER, false)
    }

    fun destroySession() {
        editor.remove(KEY_USER_CUSTOMER)
        editor.remove(KEY_USER_AGENT)
        editor.remove(KEY_MEMBER)
        editor.remove(KEY_TOKEN)
        editor.putBoolean(KEY_IS_VALID_CACHE_MEMBER, false)
        editor.commit()
    }

    fun isLoggedIn(): Boolean {
        return preferences.getString("token", null) != null
    }
}
