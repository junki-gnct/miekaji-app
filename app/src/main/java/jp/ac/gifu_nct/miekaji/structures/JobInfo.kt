package jp.ac.gifu_nct.miekaji.structures

class JobInfo(id: Long, category: JobCategory, motion: Double, time: Double, value: Double) {

    val jobID = id
    val jobCategory = category
    val jobMotion = motion
    val jobTime = time
    val jobValue = value

}