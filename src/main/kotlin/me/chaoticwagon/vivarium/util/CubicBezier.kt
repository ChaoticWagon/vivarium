package me.chaoticwagon.vivarium.util

import com.google.common.base.Preconditions
import net.minestom.server.coordinate.Vec

/**
 * Class creates a Cubic Bézier curve given a start, end, and 2 control points.
 *
 * @param p0 starting point
 * @param p1 first control point
 * @param p2 second control point
 * @param p3 ending point
 */
class CubicBezier(private val p0: Vec, private val p1: Vec, private val p2: Vec, private val p3: Vec) {

    /**
     * @param t double from 0.0 - 1.0
     * @return a Vec that is the point in the curve at t
     */
    fun getPoint(t: Double): Vec {
        Preconditions.checkArgument(t <= 1.0, "t must be <= 1")
        Preconditions.checkArgument(t >= 0.0, "t must be >= 0")

        val a = p0.lerp(p1, t)
        val b = p1.lerp(p2, t)
        val c = p2.lerp(p3, t)
        val d = a.lerp(b, t)
        val e = b.lerp(c, t)

        return d.lerp(e, t)
    }

}