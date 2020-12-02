package tz.co.asoft

import kotlinx.serialization.Serializable

@Serializable
class Page<K, V>(
    val key: K?,
    var prev: Page<K, V>?,
    val data: List<V>,
    var pageSize: Int,
    var nextKey: K?
)