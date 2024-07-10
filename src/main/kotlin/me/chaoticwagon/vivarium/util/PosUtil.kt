package me.chaoticwagon.vivarium.util

import net.minestom.server.coordinate.Point
import net.minestom.server.coordinate.Pos

fun Point.offsetByChunks(chunkCount: Int): Pos {
    return Pos(this.x() + (16 * chunkCount), this.y(), this.z() + (16 * chunkCount))
}
fun Point.negOffsetByChunks(chunkCount: Int): Pos {
    return Pos(this.x() - (16 * chunkCount), this.y(), this.z() - (16 * chunkCount))
}
