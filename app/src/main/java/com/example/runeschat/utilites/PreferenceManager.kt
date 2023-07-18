package com.example.runeschat.utilites

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager {
    final var sharedPreference : SharedPreferences? = null

    fun PreferenceManger(contex : Context) {
        sharedPreference = contex.getSharedPreferences(Constants.KEY_PREFERENCE_NAME,Context.MODE_PRIVATE)
    }

    fun putBolean(key : String,value : Boolean){
        val editor : SharedPreferences.Editor = sharedPreference!!.edit()
        editor.putBoolean(key,value)
        editor.apply()
    }

    fun getBulean(key: String) : Boolean{
        return sharedPreference!!.getBoolean(key,false)
    }

    fun putString(key : String,value : String){
        val editor : SharedPreferences.Editor = sharedPreference!!.edit()
        editor.putString(key,value)
        editor.apply()
    }

    fun getString(key: String) : String? {
        return sharedPreference!!.getString(key,null)
    }

    fun clear(){
        val editor : SharedPreferences.Editor = sharedPreference!!.edit()
        editor.clear()
        editor.apply()
    }
}