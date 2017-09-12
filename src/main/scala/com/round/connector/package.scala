package com.round

import play.api.libs.json._

import com.sksamuel.elastic4s.ElasticApi._

package object connector {

  trait LocationJson { _: Location.type =>
    implicit val jsonFormat: Format[Location] = Json.format[Location]

    val inMapping = objectField("location").fields(
      Address.inMapping,
      doubleField("latitude"),
      doubleField("longtitude"))
  }

  trait AddressJson { _: Address.type =>
    implicit val jsonFormat: Format[Address] = Json.format[Address]

    val inMapping = objectField("address").fields(
      textField("line1"),
      textField("line2"),
      textField("ward"),
      textField("district"),
      textField("city"),
      textField("province"),
      textField("country"))
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
