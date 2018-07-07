package at.tugraz.tc.cyfile.logging.impl

import at.tugraz.tc.cyfile.logging.CyFileLogger

class NoOpLogger : CyFileLogger {
    override fun d(tag: String, msg: String) {}

    override fun e(tag: String, msg: String) {}

    override fun i(tag: String, msg: String) {

    }

    override fun w(tag: String, msg: String) {

    }
}
