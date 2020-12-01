package tz.co.asoft

import kotlinx.serialization.Serializable

@Serializable
class Page<K, V>(
    val key: K?,
    var prev: Page<K, V>?,
    @Deprecated("Do not use this, it will be removed soon")
    var next: Page<K, V>?,
    val data: List<V>,
    var pageSize: Int,
    var nextKey: K?
)