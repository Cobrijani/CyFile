package at.tugraz.tc.cyfile.file

import java.io.InputStream
import java.io.OutputStream

interface FileHandler {

    fun getOutputStream(): OutputStream

    fun getInputStream() : InputStream

}