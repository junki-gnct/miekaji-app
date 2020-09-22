package jp.ac.gifu_nct.miekaji.utils

import android.content.Context
import jp.ac.gifu_nct.miekaji.utils.http.HTTPClient

object AuthUtil {

    val API_BASE_URL = "https://mie-api.pc-z.net"
    val isDebug = true

    var token: String? = null

    fun fetchToken(context: Context) {
        // TODO: Get login credentials from SharedPreferences
        val sp = context.getSharedPreferences("miekaji-auth", Context.MODE_PRIVATE)
        val id = sp.getString("miekaji-id", "junki") // default value is for debugging.
        val pass = sp.getString("miekaji-pass", "yaharito")

        val args = HashMap<String, Any>()
        args["ID"] = id!!
        args["pass"] = pass!!

        val response = HTTPClient.postRequest("${API_BASE_URL}/auth/login", args)

        token = response["token"] as String
    }

}