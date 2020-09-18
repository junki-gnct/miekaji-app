package jp.ac.gifu_nct.miekaji.utils

import jp.ac.gifu_nct.miekaji.utils.http.HTTPClient

object AuthUtil {

    val API_BASE_URL = "https://mie-api.pc-z.net"

    var token: String? = null

    fun fetchToken() {
        // TODO: Get login credentials from SharedPreferences
        val id = "junki"
        val pass = "yaharito"

        val args = HashMap<String, String>()
        args["ID"] = id
        args["pass"] = pass

        val response = HTTPClient.postRequest("${API_BASE_URL}/auth/login", args)

        token = response["token"] as String
    }

}