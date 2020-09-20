package jp.ac.gifu_nct.miekaji.structures

import java.util.*

class User (id: Long, name: String, icon_id: String, icon_loaded: Boolean, job_sum: Double) {

    val userID = id
    val userName = name
    val iconID = icon_id
    var iconLoaded = icon_loaded
    val jobSum = job_sum

    override fun equals(other: Any?): Boolean {
        if (other is User) {
            return userID == other.userID
        }
        return false
    }

    override fun hashCode(): Int {
        return Objects.hashCode(userID)
    }

}