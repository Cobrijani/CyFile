package at.tugraz.tc.cyfile.file.impl

import android.content.Context
import at.tugraz.tc.cyfile.file.FileHandler
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.io.OutputStream

class FileHandlerImpl(private var context: Context,
                      private var fileName: String) : FileHandler {

    init {
        createFile(context, fileName)
    }

    private fun createFile(context: Context, fileName: String): File{
        val file = File(context.filesDir, fileName)

        if(!file.exists()){
            file.createNewFile()
        }

        return file
    }


    override fun getOutputStream(): OutputStream {
        return try{
            context.openFileOutput(fileName, Context.MODE_PRIVATE)
        }catch (e: FileNotFoundException){
            e.printStackTrace()
            createFile(context, fileName).outputStream()
        }
    }

    override fun getInputStream(): InputStream {
        return try{
            context.openFileInput(fileName)
        }catch (e: FileNotFoundException){
            e.printStackTrace()
            createFile(context, fileName).inputStream()
        }
    }
}