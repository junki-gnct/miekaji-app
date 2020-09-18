package jp.ac.gifu_nct.miekaji.utils

import android.util.Log
import jp.ac.gifu_nct.miekaji.structures.JobCategory
import jp.ac.gifu_nct.miekaji.structures.User
import jp.ac.gifu_nct.miekaji.utils.http.HTTPClient
import org.json.JSONArray
import org.json.JSONObject

object DataUtil {

    fun fetchData(endpoint: String, arguments: String): JSONObject {
        val req_url = "${AuthUtil.API_BASE_URL}${endpoint}?token=${AuthUtil.token}${arguments}"

        return HTTPClient.getRequest(req_url, null)
    }

    fun fetchCategories(): List<JobCategory> {
        val bufferList = ArrayList<JobCategory>()
        val categories = fetchData("/job/list", "").getJSONArray("categories")
        for(i in 0 until categories.length()) {
            val element = categories.getJSONObject(i)
            bufferList.add(
                JobCategory(
                    element.getLong("ID"),
                    element.getString("name"),
                    element.getString("detail"),
                    element.getDouble("weight")
                )
            )
        }
        return bufferList
    }

    fun fetchFriends(): List<User> {
        val bufferList = ArrayList<User>()
        val categories = fetchData("/friends/list", "").getJSONArray("users")
        for(i in 0 until categories.length()) {
            val element = categories.getJSONObject(i) ?: continue
            bufferList.add(
                User(
                    element.getLong("ID"),
                    element.getString("name"),
                    element.getString("icon_id"),
                    false,
                    element.getDouble("sum")
                )
            )
        }
        return bufferList
    }

    fun fetchGroupMembers(): List<User> {
        val bufferList = ArrayList<User>()
        for (i in 0..15) {
            bufferList.add(
                User(
                    123456789,
                    "ユーザー${i}",
                    "",
                    false,
                    0.1
                )
            )
        }
        /*val categories = fetchData("/friends/list", "").getJSONArray("users")
        for(i in 0 until categories.length()) {
            val element = categories.getJSONObject(i)
            bufferList.add(
                User(
                    element.getLong("ID"),
                    element.getString("name"),
                    element.getString("icon_id"),
                    false,
                    element.getDouble("sum")
                )
            )
        }*/
        return bufferList
    }

}