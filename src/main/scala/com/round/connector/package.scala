package com.round

import play.api.libs.json._

package object connector {

  trait LocationJson { _: Location.type =>
    implicit val jsonFormat = Json.format[Location]
  }

  implicit val genderFormat: Format[Gender] = new Format[Gender] {
    override def reads(json: JsValue): JsResult[Gender] =
      Gender.fromName(json.as[String]) match {
        case Some(g) => JsSuccess(g)
        case None    => JsError("wrong gender")
      }

    override def writes(g: Gender): JsString = JsString(g.toString)
  }
}