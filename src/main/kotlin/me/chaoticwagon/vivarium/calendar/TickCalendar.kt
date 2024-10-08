package me.chaoticwagon.vivarium.calendar

import net.minestom.server.event.EventListener
import net.minestom.server.event.instance.InstanceTickEvent
import java.util.*

class TickCalendar(private val dayCycle: DayCycle) : EventListener<InstanceTickEvent> {
    override fun eventType(): Class<InstanceTickEvent> {
        return InstanceTickEvent::class.java
    }

    override fun run(event: InstanceTickEvent): EventListener.Result {

        // Tick calendar
        tickCalendar(dayCycle.calendar, dayCycle.multiplier)

        return EventListener.Result.SUCCESS
    }

    private fun tickCalendar(calendar: Calendar, multiplier: Int) {
        calendar.add(Calendar.MILLISECOND, (50 * multiplier))
    }


}