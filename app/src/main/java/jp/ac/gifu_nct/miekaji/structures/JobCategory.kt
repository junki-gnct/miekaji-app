package jp.ac.gifu_nct.miekaji.structures

import kotlin.properties.Delegates

class JobCategory(id: Long, name: String, detail: String, weight: Double) {

    val categoryID = id
    val displayName = name
    val categoryDetail = detail
    val jobWeight = weight

}