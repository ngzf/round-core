package com.round

import java.time._

import com.trueaccord.scalapb.TypeMapper

trait TimeMapper {
  implicit val yearMonthTypeMapper = TypeMapper[String, YearMonth](YearMonth.parse)(_.toString)
  implicit val localDateTypeMapper = TypeMapper[String, LocalDate](LocalDate.parse)(_.toString)
  implicit val localTimeTypeMapper = TypeMapper(LocalTime.ofNanoOfDay)(_.toNanoOfDay())
}
