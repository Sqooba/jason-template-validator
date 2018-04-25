package io.sqooba.json.jsontemplatevalidator

import com.fasterxml.jackson.databind.JsonNode

class JsonValidatorForJava(validator: JsonValidator) {

  def validateJson(json: JsonNode): java.lang.Boolean = validator.validateJson(json)
  def validateJson(json: String): java.lang.Boolean = validator.validateJson(json)

}
