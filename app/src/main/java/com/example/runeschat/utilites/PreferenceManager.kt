package com.example.runeschat.utilites

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(contex: Context) {
    var sharedPreference : SharedPreferences
    init {
        sharedPreference = contex.getSharedPreferences(Constants.KEY_PREFERENCE_NAME,Context.MODE_PRIVATE)
    }

    fun putBoolean(key : String,value : Boolean){
        val editor : SharedPreferences.Editor = sharedPreference.edit()
        editor.putBoolean(key,value)
        editor.apply()
    }

    fun getBoolean(key: String) : Boolean{
        return sharedPreference.getBoolean(key,false)
    }

    fun putString(key : String,value : String){
        val editor : SharedPreferences.Editor = sharedPreference.edit()
        editor.putString(key,value)
        editor.apply()
    }

    fun getString(key: String) : String {
        return sharedPreference.getString(key,null).toString()
    }

    fun clear(){
        val editor : SharedPreferences.Editor = sharedPreference.edit()
        editor.clear()
        editor.apply()
    }
}