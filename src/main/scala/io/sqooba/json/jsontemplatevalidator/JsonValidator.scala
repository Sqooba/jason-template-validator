package io.sqooba.json.jsontemplatevalidator

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.node.{JsonNodeType, ObjectNode}
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}

class JsonValidator(templateJson: JsonNode) {

  lazy val mapper: ObjectMapper = new ObjectMapper()

  private def validateJsonAgainstFields(json: JsonNode, subTemplateJson: JsonNode): Boolean = {

    val fields = subTemplateJson.fieldNames().asScala

    if (subTemplateJson.isArray) {
      if (subTemplateJson.has(0)) {
        val itemTemplate = subTemplateJson.get(0)
        if (json.isArray && json.has(0)) {
          val results = ListBuffer[Boolean]()
          json.forEach(item => {
            val res = validateJsonAgainstFields(item, itemTemplate)
            results.append(res)
          })
          results.forall(x => x)
        } else {
          false
        }
      } else {
        json.isArray
      }
    } else {
      fields.forall(field => {
        val nt = subTemplateJson.get(field).getNodeType
        if (nt == JsonNodeType.OBJECT) {
          json.has(field) && json.get(field) != null && validateJsonAgainstFields(json.get(field), subTemplateJson.get(field))
        } else if (nt == JsonNodeType.ARRAY) {
          json.has(field) && json.get(field).isArray && json.get(field) != null && validateJsonAgainstFields(json.get(field), subTemplateJson.get(field))
        } else {
          json.has(field) && json.get(field) != null
        }
      })
    }
  }

  def validateJson(json: JsonNode): Boolean = {
    validateJsonAgainstFields(json, templateJson)
  }

  def validateJson(json: String): Boolean = {
    try {
      validateJson(mapper.readTree(json))
    } catch {
      case jpe: JsonParseException => false
    }
  }

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
    try {
      val asJson = mapper.readTree(json)
      new JsonValidator(asJson)
    } catch {
      case jpe: JsonParseException => throw new IllegalArgumentException("Invalid json template given, can't be parsed.")
    }
  }
}
