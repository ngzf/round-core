package com.round

import org.joda.time._

import com.trueaccord.scalapb.TypeMapper

trait TimeMapper {
  implicit val yearMonthTypeMapper = TypeMapper(YearMonth.parse)(_.toString)
  implicit val dateTimeTypeMapper = TypeMapper[Long, DateTime](new DateTime(_))(_.getMillis)
  implicit val localDateTypeMapper = TypeMapper(LocalDate.parse)(_.toString)
  implicit val localTimeTypeMapper = TypeMapper(LocalTime.fromMillisOfDay)(_.getMillisOfDay)
}
