package jp.ac.gifu_nct.miekaji.utils.http

import android.util.Log
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

object HTTPClient {

    fun getRequest(url: String, parameters: HashMap<String, String>?): JSONObject {
        var result: String = ""
        var arguments = ""
        if (parameters != null) {
            parameters!!.keys.forEach{
                if (arguments.equals("", true)) {
                    arguments = "?${URLEncoder.encode(it, "UTF-8")}=${parameters[URLEncoder.encode(parameters[it], "UTF-8")]}"
                } else {
                    arguments += "&${URLEncoder.encode(it, "UTF-8")}=${parameters[URLEncoder.encode(parameters[it], "UTF-8")]}"
                }
            }
        }
        var requestedUrl = url + arguments

        Log.d("URL", requestedUrl)

        val url = URL(requestedUrl);
        val con = url.openConnection() as HttpURLConnection
        try {
            con.requestMethod = "GET"
            con.useCaches = false
            con.doOutput = true
            con.doInput = true
            con.connect()

            val br = BufferedReader(InputStreamReader(con.inputStream))

            var line: String? = null
            val sb = StringBuilder()

            for (line in br.readLines()) {
                line?.let { sb.append(line) }
            }

            br.close()

            result = sb.toString()
        } catch(e: FileNotFoundException) {
            val br = BufferedReader(InputStreamReader(con.errorStream))

            var line: String? = null
            val sb = StringBuilder()

            for (line in br.readLines()) {
                line?.let { sb.append(line) }
            }

            br.close()

            result = sb.toString()
        } catch(e: IOException) {
            e.printStackTrace()
        }

        return JSONObject(result)
    }

    fun postRequest(url: String, parameters: HashMap<String, String>?): JSONObject {
        var result = ""
        val url = URL(url);
        val con = url.openConnection() as HttpURLConnection
        try {
            con.requestMethod = "POST"
            con.doOutput = true
            con.doInput = true
            con.useCaches = false
            con.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            con.connect()

            val bw = BufferedWriter(OutputStreamWriter(con.outputStream))
            bw.write(JSONObject(parameters as Map<String, String>).toString())
            bw.close()

            val br = BufferedReader(InputStreamReader(con.inputStream))

            var line: String? = null
            val sb = StringBuilder()

            for (line in br.readLines()) {
                line?.let { sb.append(line) }
            }

            br.close()

            result = sb.toString()
        } catch(e: FileNotFoundException) {
            val br = BufferedReader(InputStreamReader(con.errorStream))

            var line: String? = null
            val sb = StringBuilder()

            for (line in br.readLines()) {
                line?.let { sb.append(line) }
            }

            br.close()

            result = sb.toString()
        } catch(e: IOException) {
            e.printStackTrace()
        }

        return JSONObject(result)
    }
}