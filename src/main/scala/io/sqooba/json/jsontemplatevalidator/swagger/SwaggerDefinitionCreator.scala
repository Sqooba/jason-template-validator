package io.sqooba.json.jsontemplatevalidator.swagger

import scala.collection.JavaConverters._
import scala.util.Try

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}

object SwaggerDefinitionCreator {

	lazy val mapper: ObjectMapper = new ObjectMapper()

	def createTypeDef(node: JsonNode): JsonNode = {

		val fields: List[String] = node.fieldNames().asScala.toList
		val rootJson = mapper.createObjectNode()
		val properties = mapper.createObjectNode()
		fields.foreach(f => {
			val fieldObject = mapper.createObjectNode()
			val obj = node.get(f)
			if (obj.canConvertToLong) {
				fieldObject.put("type:", "number")
			} else if(Try(obj.toString.toBoolean).getOrElse(false)) {
				fieldObject.put("type:", "boolean")
			} else {
				fieldObject.put("type:", "string")
			}
			properties.set(f.toString, fieldObject)
		})
		rootJson.put("type", "object")
		rootJson.set("properties", properties)
		rootJson.asInstanceOf[JsonNode]
	}

	def createTypeDef(json: String): JsonNode = {
		val asJson = mapper.readTree(json)
		createTypeDef(asJson)
	}

}
/*
{
  "type":"object",
  "properties":{
    "id":{
      "type":"integer"
    },
    "desc":{
      "type":"string"
    },
    "contractType":{
      "type":"string"
    }
  }
}
 */
