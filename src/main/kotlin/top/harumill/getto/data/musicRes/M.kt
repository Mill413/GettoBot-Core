package top.harumill.getto.data.musicRes

import kotlinx.serialization.Serializable

@Serializable
data class M(
    val br: Int,
    val fid: Int,
    val size: Int,
    val vd: Double
)