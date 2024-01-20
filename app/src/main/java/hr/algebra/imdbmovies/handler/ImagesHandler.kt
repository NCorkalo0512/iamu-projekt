package hr.algebra.imdbmovies.handler

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import hr.algebra.imdbmovies.factory.createGetHttpURLConnection
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths

fun downloadImageAndStore(context: Context, url: String): String? {
    val filename = url.substring(url.lastIndexOf("/") + 1)
    val file = createFile(context, filename)

    if (file.exists()) {
        // File already exists, no need to download
        return file.absolutePath
    }

    try {
        val con: HttpURLConnection = createGetHttpURLConnection(url)
        val inputStream = con.inputStream
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.close()
        return file.absolutePath
    } catch (e: Exception) {
        Log.e("IMAGESHANDLER", e.toString(), e)
    }

    return null
}


private fun createFile(context: Context, filename: String): File {
    val dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    var counter = 0
    var uniqueFilename = filename

    while (File(dir, uniqueFilename).exists()) {
        val extension = filename.substringAfterLast('.')
        val nameWithoutExtension = filename.substringBeforeLast('.')
        uniqueFilename = "${nameWithoutExtension}_$counter.$extension"
        counter++
    }

    return File(dir, uniqueFilename)
}
