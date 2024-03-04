package com.example.tomasdemoapp

import android.os.AsyncTask
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class HttpPostTask(private val callback: (String) -> Unit) : AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg params: String?): String {
        val url = URL(params[0])
        val postData = params[1]?.toByteArray()

        return try {
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            connection.doOutput = true

            // Write the POST data to the output stream
            val os: OutputStream = connection.outputStream
            os.write(postData)
            os.close()

            // Get the response from the server
            val br = BufferedReader(InputStreamReader(connection.inputStream))
            val response = br.use { it.readText() }

            connection.disconnect()

            response
        } catch (e: Exception) {
            e.printStackTrace()
            "Error: ${e.message}"
        }
    }

    override fun onPostExecute(result: String?) {
        // Handle the result by calling the callback function
        callback.invoke(result ?: "")
    }
}