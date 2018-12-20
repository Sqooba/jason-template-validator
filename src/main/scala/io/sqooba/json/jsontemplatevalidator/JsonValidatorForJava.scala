package io.sqooba.json.jsontemplatevalidator

import java.util

import com.fasterxml.jackson.databind.JsonNode

class JsonValidatorForJava(validator: JsonValidator) {

  def validateJson(json: JsonNode): java.lang.Boolean = validator.validateJson(json)
  def validateJson(json: String): java.lang.Boolean = validator.validateJson(json)
  def getLatestErrors(): java.util.Set[String] = {
    val set: java.util.Set[String] = new util.HashSet[String]()
    validator.getLatestErrors().foreach(erField => set.add(erField))
    set
  }

}
