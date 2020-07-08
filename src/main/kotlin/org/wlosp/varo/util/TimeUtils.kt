package org.wlosp.varo.util

import java.time.Duration


fun Duration.toHoursPart(): Int = (toHours() % 24).toInt()

fun Duration.toMinutesPart(): Int = (toMinutes() % 60).toInt()