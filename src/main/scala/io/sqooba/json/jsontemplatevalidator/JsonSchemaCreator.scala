package io.sqooba.json.jsontemplatevalidator

import java.util
import java.util.Iterator

import com.fasterxml.jackson.databind.node.{JsonNodeType, ObjectNode, TextNode}
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import io.sqooba.json.jsontemplatevalidator.JsonValidator.mapper

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

  /*
BINARY,
BOOLEAN,
MISSING,
NULL,
NUMBER,
OBJECT,
POJO,
STRING
  */
  def createSchemaFor(json: JsonNode): JsonNode = {
    json.getNodeType match {
      case JsonNodeType.ARRAY => json
      case JsonNodeType.OBJECT => {
        val on = mapper.createObjectNode
        on.set("type", new TextNode("object"))
        on.set("properties",  mapper.createObjectNode)
        // on.set("properties", recursiveSchema(json, on))
      }
    }
  }

  def recursiveSchema(json: JsonNode, schema: ObjectNode): JsonNode = {
    val fields: util.Iterator[util.Map.Entry[String, JsonNode]] = json.asInstanceOf[ObjectNode].fields()
    val fieldList = fields.asScala.toList
    println(fieldList)
    schema

    /*
    fieldList.foreach(field => {
      val key = field.getKey
      val nodeType = field.getValue.getNodeType
      schema.put(key, nodeType.toString.toLowerCase)
    })

    json.forEach(f => {

      f.getNodeType match {
        case JsonNodeType.ARRAY => json
        case JsonNodeType.OBJECT => {
          val on = mapper.createObjectNode
          on.set("type", new TextNode("object"))
          recursiveSchema(json, on)
        }
        case JsonNodeType.STRING => {

          val f1 = f.get(0)
          println(f1)
          schema.set("nice", new TextNode("nice"))
        }
        case JsonNodeType.NUMBER => {
          val f1 = f.asText()
          println(f1)
          schema.set("nice", new TextNode("nice"))
        }
      }
    })
    schema
    */
  }
}

