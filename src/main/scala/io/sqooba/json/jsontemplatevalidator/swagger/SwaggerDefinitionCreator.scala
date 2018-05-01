package io.sqooba.json.jsontemplatevalidator.swagger

import scala.collection.JavaConverters._
import scala.util.Try

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}

object SwaggerDefinitionCreator {

	lazy val mapper: ObjectMapper = new ObjectMapper()

	def createProperties(node: JsonNode): JsonNode = {
		val fields: List[String] = node.fieldNames().asScala.toList
		val properties = mapper.createObjectNode()
		fields.foreach(f => {
			val fieldObject = mapper.createObjectNode()
			val obj = node.get(f)
			if (obj.isObject) {
				fieldObject.put("type", "object")
				fieldObject.set("properties", createProperties(obj))
			} else if (obj.canConvertToLong) {
				fieldObject.put("type:", "number")
			} else if(Try(obj.toString.toBoolean).getOrElse(false)) {
				fieldObject.put("type:", "boolean")
			} else {
				fieldObject.put("type:", "string")
			}
			properties.set(f.toString, fieldObject)
		})
		properties
	}

	def createTypeDef(node: JsonNode): JsonNode = {
		val rootJson = mapper.createObjectNode()
		rootJson.put("type", "object")
		rootJson.set("properties", createProperties(node))
		rootJson.asInstanceOf[JsonNode]
	}

	def createTypeDef(json: String): JsonNode = {
		try {
			val asJson = mapper.readTree(json)
			createTypeDef(asJson)
		} catch {
			case ex: JsonParseException => throw new IllegalArgumentException("Cannot parse given json, invalid format.")
		}
	}
}
