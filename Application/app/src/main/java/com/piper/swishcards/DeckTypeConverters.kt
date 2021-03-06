package com.piper.swishcards

import androidx.room.TypeConverter
import java.util.*

/////////////////////////////////
//  REQUIRED FOR THE DATABASE  //
////////////////////////////////
class DeckTypeConverters {
    @TypeConverter
    fun toUUI(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }

    @TypeConverter
    fun fromUUID(uuid: UUID): String? {
        return uuid.toString()
    }

    @TypeConverter
    fun toCalender(time: Long): Calendar {
        val c = Calendar.getInstance()
        c.timeInMillis = time
        return c
    }

    @TypeConverter
    fun fromCalendar(calendar: Calendar): Long {
        return calendar.timeInMillis
    }

    @TypeConverter
    fun toCardType(card: String): CardType? {
        return CardType.valueOf(card)
    }

    @TypeConverter
    fun fromCardType(card: CardType): String? {
        return card.toString()
    }

}