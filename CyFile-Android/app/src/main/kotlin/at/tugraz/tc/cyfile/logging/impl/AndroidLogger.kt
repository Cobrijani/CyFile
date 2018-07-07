package at.tugraz.tc.cyfile.logging.impl

import android.util.Log

import at.tugraz.tc.cyfile.logging.CyFileLogger

class AndroidLogger : CyFileLogger {

    override fun d(tag: String, msg: String) {
        Log.d(tag, msg)
    }

    override fun e(tag: String, msg: String) {
        Log.e(tag, msg)
    }

    override fun i(tag: String, msg: String) {
        Log.i(tag, msg)
    }

    override fun w(tag: String, msg: String) {
        Log.w(tag, msg)
    }
}
