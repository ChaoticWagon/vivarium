package me.chaoticwagon.vivarium.util

import net.kyori.adventure.bossbar.BossBar

object BossBarUtil {

    fun getSeasonColor(month: Int): BossBar.Color {
        return when (month) {
            11, 0, 1 -> BossBar.Color.BLUE
            in 2..4 -> BossBar.Color.GREEN
            in 5..7 -> BossBar.Color.YELLOW
            in 8..10 -> BossBar.Color.RED
            else -> {
                BossBar.Color.PINK
            }
        }
    }

    fun dateToSeconds(month: Int, day: Int, hour: Int, minute: Int, second: Int): Float {
        return (month * 2592000 + day * 86400 + hour * 3600 + minute * 60 + second).toFloat()
    }
}