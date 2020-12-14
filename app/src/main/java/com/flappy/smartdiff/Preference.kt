package com.flappy.smartdiff

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.flappy.smartdiff.constant.Constant
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * SharedPreferences operation, you need application to invoke setContext
 * @param name sp key name
 * @param default default value
 */
class Preference<T>(private val name: String, private val default: T) : ReadWriteProperty<Any?, T> {

    companion object {
        lateinit var preferences: SharedPreferences
        /**
         * init Context
         * @param context Context
         */
        fun setContext(context: Context) {
            preferences = context.getSharedPreferences(
                context.packageName + Constant.SHARED_NAME,
                Context.MODE_PRIVATE
            )
        }

        fun clear() {
            preferences.edit().clear().apply()
        }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = findPreference(name, default)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = putPreference(name, value)

    private fun findPreference(key: String,defValue: T): T = when (defValue) {
        is Int -> preferences.getInt(key, defValue)
        is Long -> preferences.getLong(key, defValue)
        is Float -> preferences.getFloat(key, defValue)
        is Boolean -> preferences.getBoolean(key, defValue)
        is String -> preferences.getString(key, defValue)
        else -> throw IllegalArgumentException("Unsupported type.")
    } as T

    @SuppressLint("CommitPrefEdits")
    private fun <U> putPreference(name: String, value: U) = with(preferences.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalArgumentException("This type can be saved into Preferences")
        }.apply()
    }
}