package me.chaoticwagon.vivarium.util

import net.minestom.server.coordinate.BlockVec
import net.minestom.server.coordinate.Point
import net.minestom.server.coordinate.Pos
import net.minestom.server.coordinate.Vec
import kotlin.math.abs
import kotlin.math.floor

fun Point.offsetByChunks(): Point {
    val x = wrapNumber(this.x(), -2.0 * 16, 2.0 * 16)
    val z = wrapNumber(this.z(), -2.0 * 16, 2.0 * 16)
    return when (this) {
        is Pos -> Pos(x, this.y(), z, this.yaw, this.pitch)
        is Vec -> Vec(z, this.y(), z)
        is BlockVec -> BlockVec(z, this.y(), z)
        else -> throw IllegalArgumentException("Unknown point type")
    }
}

fun Point.negOffsetByChunks(): Point {
    return offsetByChunks()
}

fun wrapNumber(value: Int, min: Int, max: Int): Int {
    if (min <= value && value <= max) return value
    val range_length = max - min + 1
    val normal = value - min
    val adjusted_val = abs(normal % range_length)
    return min + adjusted_val
}
fun wrapNumber(value: Double, min: Double, max: Double): Double {
    if (min <= value && value <= max) return value
    val range_length = max - min + 1
    val normal = value - min
    val adjusted_val = abs(normal % range_length)
    return min + adjusted_val
}
