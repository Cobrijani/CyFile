package at.tugraz.tc.cyfile.logging

interface CyFileLogger {
    fun d(tag: String, msg: String)

    fun e(tag: String, msg: String)

    fun i(tag: String, msg: String)

    fun w(tag: String, msg: String)
}
