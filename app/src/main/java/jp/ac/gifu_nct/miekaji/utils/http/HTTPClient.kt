package jp.ac.gifu_nct.miekaji.utils.http

import android.util.Log
import org.json.JSONObject
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

object HTTPClient {

    fun getRequest(url: String, parameters: HashMap<String, String>?): JSONObject {
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
        con.requestMethod = "GET"
        con.useCaches = false
        con.connect()

        val br = BufferedReader(InputStreamReader(con.inputStream))

        var line: String? = null
        val sb = StringBuilder()

        for (line in br.readLines()) {
            line?.let { sb.append(line) }
        }

        br.close()

        return JSONObject(sb.toString())
    }

    fun postRequest(url: String, parameters: HashMap<String, String>?): JSONObject {
        val url = URL(url);
        val con = url.openConnection() as HttpURLConnection
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

        return JSONObject(sb.toString())
    }
}