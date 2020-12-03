package tz.co.asoft

import kotlinx.serialization.Serializable

/**
 * @param D the data type itself
 * @param pageSize maximum number the page can have
 * @param data [List] of data available on the page
 * @param pageNo the number of the page starting from pageNo=1
 */
@Serializable
class Page<D>(
    val pageSize: Int,
    val data: List<D>,
    val pageNo: Int
) {
    init {
        require(pageNo > 0) { "Page number must not be negative" }
    }
}