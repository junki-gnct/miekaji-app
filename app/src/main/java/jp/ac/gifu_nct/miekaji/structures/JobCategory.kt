package jp.ac.gifu_nct.miekaji.structures

import java.util.*
import kotlin.properties.Delegates

class JobCategory(id: Long, name: String, detail: String, weight: Double) {

    val categoryID = id
    val displayName = name
    val categoryDetail = detail
    val jobWeight = weight

    override fun equals(other: Any?): Boolean {
        if (other is JobCategory) {
            return categoryID == other.categoryID
        }
        return false
    }

    override fun hashCode(): Int {
        return Objects.hashCode(categoryID)
    }
}