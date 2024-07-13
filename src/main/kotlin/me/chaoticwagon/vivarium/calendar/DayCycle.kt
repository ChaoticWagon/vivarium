package me.chaoticwagon.vivarium.calendar

import me.chaoticwagon.vivarium.util.BossBarUtil
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import net.minestom.server.MinecraftServer
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.timer.TaskSchedule
import java.util.*

class DayCycle(private val instance: InstanceContainer) {

    val calendar: Calendar = Calendar.Builder()
        .setCalendarType("gregorian")
        .setDate(
            1900,
            0,
            1
        )
        .setTimeOfDay(
            0,
            0,
            0
        )
        .build()

    val locale: Locale = Locale.getDefault()

    private val secsSeason: Int = 7_776_000
    private val hoursPerSeason = 3
    val multiplier = secsSeason / (hoursPerSeason * 3600)

    private var bossBar = BossBar.bossBar(
        Component.empty(),
        0f,
        BossBar.Color.WHITE,
        BossBar.Overlay.PROGRESS
    )

    init {
        val scheduler = MinecraftServer.getSchedulerManager()
        scheduler.scheduleTask({

            updateBossBar()

            /** TODO
             * Listen to AsyncPlayerConfigurationEvent and add the boss bar to the player when they join
             * pull out the calendar and boss bar from this listener and add them to a class you pass in
             */
            instance.players.forEach {
                it.showBossBar(bossBar)
            }

        }, TaskSchedule.immediate(), TaskSchedule.nextTick())
    }

    private fun updateBossBar() {
        bossBar.name(
            Component.text(
                "${calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG_STANDALONE, locale)}, " +
                        "${calendar.getDisplayName(Calendar.MONTH, Calendar.LONG_STANDALONE, locale)} " +
                        "${calendar.get(Calendar.DAY_OF_MONTH)}, " +
                        "${calendar.get(Calendar.YEAR)}"
            )
        )

        bossBar.color(BossBarUtil.getSeasonColor(calendar.get(Calendar.MONTH)))

        bossBar.progress(
            BossBarUtil.dateToSeconds(
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND)
            ) / BossBarUtil.dateToSeconds(
                calendar.getActualMaximum(Calendar.MONTH),
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH),
                23,
                59,
                59
            )
        )

        bossBar.overlay(BossBar.Overlay.PROGRESS)
    }

}
