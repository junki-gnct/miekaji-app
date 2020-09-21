package jp.ac.gifu_nct.miekaji.utils

import android.util.Log
import jp.ac.gifu_nct.miekaji.structures.JobCategory
import jp.ac.gifu_nct.miekaji.structures.JobInfo
import jp.ac.gifu_nct.miekaji.structures.User
import jp.ac.gifu_nct.miekaji.utils.http.HTTPClient
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object DataUtil {

    fun fetchData(endpoint: String, arguments: String): JSONObject {
        val req_url = "${AuthUtil.API_BASE_URL}${endpoint}?token=${AuthUtil.token}${arguments}"

        return HTTPClient.getRequest(req_url, null)
    }

    fun fetchTodayJob(): List<JobInfo> {
        val bufferList = ArrayList<JobInfo>()
        try {
            val jobs = fetchData("/job/today", "").getJSONArray("histories")
            for(i in 0 until jobs.length()) {
                val element = jobs.getJSONObject(i)
                val category = element.getJSONObject("category")
                bufferList.add(
                    JobInfo(
                        element.getLong("ID"),
                        JobCategory(
                            category.getLong("ID"),
                            category.getString("name"),
                            category.getString("detail"),
                            category.getDouble("weight")
                        ),
                        element.getDouble("motion"),
                        element.getDouble("time"),
                        element.getDouble("value")
                    )
                )
            }

            return bufferList
        } catch (e: JSONException) {
            return bufferList
        }
    }

    fun fetchTodayJobTimeValueByEach(): HashMap<JobCategory, Pair<Double, Double>> {
        return fetchTodayJobTimeValueByEach(fetchTodayJob())
    }

    fun fetchTodayJobTimeValueByEach(jobs: List<JobInfo>): HashMap<JobCategory, Pair<Double, Double>> {
        val map = HashMap<JobCategory, Pair<Double, Double>>()
        val categories = fetchCategories()
        categories.forEach {
            map[it] = Pair<Double, Double>(0.0, 0.0)
        }
        jobs.forEach {
            val value = map[it.jobCategory]!!
            map[it.jobCategory] = Pair<Double, Double>(value.first + it.jobValue, value.second + it.jobTime)
        }
        return map
    }

    fun fetchCategories(): List<JobCategory> {
        val bufferList = ArrayList<JobCategory>()
        try {
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
        } catch(e: JSONException) {
            return bufferList
        }
    }

    fun fetchFriends(): List<User> {
        val bufferList = ArrayList<User>()
        try {
            val categories = fetchData("/friends/list", "").getJSONArray("users")
            for(i in 0 until categories.length()) {
                val element = categories.getJSONObject(i) ?: continue
                bufferList.add(
                    User(
                        element.getLong("ID"),
                        element.getString("name"),
                        element.getString("icon_id"),
                        false,
                        element.getDouble("sum"),
                        element.getDouble("today")
                    )
                )
            }
            return bufferList
        } catch(e: JSONException) {
            return bufferList
        }
    }

    fun fetchGroupMembers(): List<User> {
        val bufferList = ArrayList<User>()
        try {
            val categories = fetchData("/fun/info", "").getJSONArray("members")
            for(i in 0 until categories.length()) {
                val element = categories.getJSONObject(i)
                bufferList.add(
                    User(
                        element.getLong("ID"),
                        element.getString("name"),
                        element.getString("icon_id"),
                        false,
                        element.getDouble("sum"),
                        element.getDouble("today")
                    )
                )
            }
            return bufferList
        } catch(e: JSONException) {
            return bufferList
        }
    }

}