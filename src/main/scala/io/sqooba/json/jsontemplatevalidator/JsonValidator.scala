package io.sqooba.json.jsontemplatevalidator

import scala.collection.JavaConverters._

import com.fasterxml.jackson.databind.node.{JsonNodeType, ObjectNode}
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}

class JsonValidator(templateJson: JsonNode) {

  lazy val mapper: ObjectMapper = new ObjectMapper()

  private def validateJsonAgainstFields(json: JsonNode, subTemplateJson: JsonNode): Boolean = {
    val fields = subTemplateJson.fieldNames().asScala
    fields.forall(field => {
      val nt = subTemplateJson.get(field).getNodeType
      if (nt == JsonNodeType.OBJECT) {
        json.has(field) && json.get(field) != null && validateJsonAgainstFields(json.get(field), subTemplateJson.get(field))
      } else {
        json.has(field) && json.get(field) != null
      }
    })
  }

  def validateJson(json: JsonNode): Boolean = {
    validateJsonAgainstFields(json, templateJson)
  }

  def validateJson(json: String): Boolean = validateJson(mapper.readTree(json))

  def getTemplateJson(): JsonNode = templateJson

  def asJava(): JsonValidatorForJava = new JsonValidatorForJava(this)
}

object JsonValidator {
  lazy val mapper: ObjectMapper = new ObjectMapper()

  def forDotNotation(dotNot: String): JsonValidator = {
    val json = mapper.createObjectNode()
    dotNot.split("\n").foreach(row => {
      val fieldsSplit = row.trim.split('.')
      var tempJson: JsonNode = json
      fieldsSplit.foreach(field => {
        if (!tempJson.has(field)) {
          tempJson.asInstanceOf[ObjectNode].set(field, mapper.createObjectNode())
        }
        tempJson = tempJson.get(field)
      })
    })
    new JsonValidator(json.asInstanceOf[JsonNode])
  }

  def forTemplateJson(json: String): JsonValidator = {
    val asJson = mapper.readTree(json)
    new JsonValidator(asJson)
  }
}
