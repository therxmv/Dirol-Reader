package com.therxmv.dirolreader.domain.usecase.message

import android.content.Context
import android.text.format.DateUtils.HOUR_IN_MILLIS
import android.text.format.DateUtils.MINUTE_IN_MILLIS
import android.text.format.DateUtils.getRelativeTimeSpanString
import com.therxmv.common.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Returns time difference between now and past in human readable format.
 *
 * - If more than 24 hours ago, then it returns "06.08 17:00".
 * - If more than 1 hour ago, then it returns "2 hours ago".
 * - If more than 1 minute ago, then it returns "20 minutes ago".
 * - Else it returns "Just now".
 */
class GetReadablePostTimeUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    companion object {
        private const val PAST_DAY_FORMAT = "dd.MM, HH:mm"
    }

    operator fun invoke(timestamp: Int): String {
        val date = Date(timestamp * 1000L)
        val calendar = Calendar.getInstance().apply {
            timeZone = TimeZone.getDefault()
            time = date
        }

        val postMillis = calendar.timeInMillis
        val currentMillis = System.currentTimeMillis()

        val difference = currentMillis - postMillis
        val hours = TimeUnit.MILLISECONDS.toHours(difference)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(difference)

        return when {
            hours >= 24 -> {
                SimpleDateFormat(PAST_DAY_FORMAT, Locale.getDefault()).run {
                    timeZone = TimeZone.getDefault()
                    format(date)
                }
            }

            hours >= 1 -> {
                getRelativeTimeSpanString(postMillis, currentMillis, HOUR_IN_MILLIS).toString()
            }

            minutes >= 1 -> {
                getRelativeTimeSpanString(postMillis, currentMillis, MINUTE_IN_MILLIS).toString()
            }

            else -> {
                context.getString(R.string.news_just_now)
            }
        }
    }
}