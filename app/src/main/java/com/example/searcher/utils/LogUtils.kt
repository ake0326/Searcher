@file:Suppress("NOTHING_TO_INLINE")
package com.example.searcher.utils

import android.util.Log
import com.example.searcher.BuildConfig

inline fun logV(tag: String, msg: String) { if (BuildConfig.DEBUG) Log.v(tag, msg) }

inline fun logV(tag: String, msg: String, tr: Throwable) { if (BuildConfig.DEBUG) Log.v(tag, msg, tr) }

inline fun logD(tag: String, msg: String) { if (BuildConfig.DEBUG) Log.d(tag, msg) }

inline fun logD(tag: String, msg: String, tr: Throwable) { if (BuildConfig.DEBUG) Log.d(tag, msg, tr) }

inline fun logI(tag: String, msg: String) { if (BuildConfig.DEBUG) Log.i(tag, msg) }

inline fun logI(tag: String, msg: String, tr: Throwable) { if (BuildConfig.DEBUG) Log.i(tag, msg, tr) }

inline fun logW(tag: String, msg: String) { if (BuildConfig.DEBUG) Log.w(tag, msg) }

inline fun logW(tag: String, msg: String, tr: Throwable) { if (BuildConfig.DEBUG) Log.w(tag, msg, tr) }

inline fun logE(tag: String, msg: String) { if (BuildConfig.DEBUG) Log.e(tag, msg) }

inline fun logE(tag: String, msg: String, tr: Throwable) { if (BuildConfig.DEBUG)Log.e(tag, msg, tr) }