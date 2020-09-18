package jp.ac.gifu_nct.miekaji.structures

class User (id: Long, name: String, icon_id: String, icon_loaded: Boolean, job_sum: Double) {

    val userID = id
    val userName = name
    val iconID = icon_id
    var iconLoaded = icon_loaded
    val jobSum = job_sum

}