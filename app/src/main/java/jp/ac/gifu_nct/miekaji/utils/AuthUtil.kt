package jp.ac.gifu_nct.miekaji.utils

import android.content.Context
import jp.ac.gifu_nct.miekaji.utils.http.HTTPClient

object AuthUtil {

    val API_BASE_URL = "https://mie-api.pc-z.net"
    val isDebug = false

    var token: String? = null

    fun fetchToken(context: Context) {
        val sp = context.getSharedPreferences("miekaji-auth", Context.MODE_PRIVATE)
        val id = sp.getString("miekaji-id", if(isDebug) "junki" else null) // default value is for debugging.
        val pass = sp.getString("miekaji-pass",  if(isDebug) "yaharito" else null)

        val args = HashMap<String, Any>()
        args["ID"] = id!!
        args["pass"] = pass!!

        val response = HTTPClient.postRequest("${API_BASE_URL}/auth/login", args)

        token = response["token"] as String
    }

}