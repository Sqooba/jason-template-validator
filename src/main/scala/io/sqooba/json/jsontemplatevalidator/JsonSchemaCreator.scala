package io.sqooba.json.jsontemplatevalidator

import java.util

import com.fasterxml.jackson.databind.node._
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import scala.collection.JavaConverters._

object JsonSchemaCreator extends App {
  println(s"perse: ${args.length}")
}


class JsonSchemaCreator() {

  lazy val mapper: ObjectMapper = new ObjectMapper()

  def createSchemaFor(jsonFile: String): JsonNode = {
    val json = mapper.readTree(jsonFile)
    createSchemaFor(json)
  }

  def createSchemaFor(json: JsonNode, isRoot: Boolean = true): JsonNode = {
    json.getNodeType match {
      case JsonNodeType.ARRAY => json
      case JsonNodeType.OBJECT => {
        val on = mapper.createObjectNode
        if (isRoot) {
          on.set("$schema", new TextNode("http://json-schema.org/draft-07/schema#"))
        }
        on.set("type", new TextNode("object"))
        on.set("properties", parseProperties(json))
      }
      //case JsonNodeType.STRING => new TextNode("string")
      //case JsonNodeType.NUMBER => new TextNode("number")
      case _ => new TextNode(json.getNodeType.toString.toLowerCase)
    }
  }

  def createTypeNode(typeString: String): ObjectNode = {
    val on = mapper.createObjectNode()
    on.set("type", new TextNode(typeString))
    on
  }

  def parseProperties(json: JsonNode): JsonNode = {
    val schema = mapper.createObjectNode()
    val fields: util.Iterator[util.Map.Entry[String, JsonNode]] = json.asInstanceOf[ObjectNode].fields()
    val fieldList = fields.asScala.toList
    fieldList.foreach(field => {
      val key = field.getKey
      val nodeType = field.getValue.getNodeType
      val typeNode = createTypeNode(nodeType.toString.toLowerCase)
      if (field.getValue.getNodeType == JsonNodeType.OBJECT) {
        typeNode.set("properties", parseProperties(field.getValue))
      } else if (field.getValue.getNodeType == JsonNodeType.ARRAY) {
        typeNode.set("items", parseItemsFromList(field.getValue.asInstanceOf[ArrayNode]))
      }
      schema.set(key, typeNode)
    })
    schema
  }

  def parseItemsFromList(jsonArray: ArrayNode): JsonNode = {
    val item = jsonArray.get(0)
    createSchemaFor(item, false)
  }

}

