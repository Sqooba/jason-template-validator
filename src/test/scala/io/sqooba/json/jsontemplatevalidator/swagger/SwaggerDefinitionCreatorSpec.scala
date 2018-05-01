package io.sqooba.json.jsontemplatevalidator.swagger

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import org.scalatest.{FlatSpec, Matchers}

class SwaggerDefinitionCreatorSpec extends FlatSpec with Matchers {

	val json =
		"""{
  			"id": "5",
    		"desc": "this is desc"
  	}""".stripMargin

	val jsonWithNumber =
		"""{
  			"id": 5,
    		"desc": "this is desc"
  	}""".stripMargin

	val jsonWithBoolean =
		"""{
  			"id": 5,
    		"desc": "this is desc",
      	"valid": true
  	}""".stripMargin

	val nestedJson =
		s"""{
  			"id": "5",
    		"desc": "this is desc",
      	"nested": $json
  	}""".stripMargin

	"createTypeDef" should "create type def json string" in {
		val res: JsonNode = SwaggerDefinitionCreator.createTypeDef(json)
		res shouldBe a [JsonNode]
		res.toString shouldBe "{\"type\":\"object\",\"properties\":{\"id\":{\"type\":\"string\"},\"desc\":{\"type\":\"string\"}}}"
	}

	it should "create type def jsonNode" in {
		val mapper = new ObjectMapper()
		val res: JsonNode = SwaggerDefinitionCreator.createTypeDef(mapper.readTree(json))
		res shouldBe a [JsonNode]
		res.toString shouldBe "{\"type\":\"object\",\"properties\":{\"id\":{\"type\":\"string\"},\"desc\":{\"type\":\"string\"}}}"
	}

	it should "create number for number type" in {
		val res: JsonNode = SwaggerDefinitionCreator.createTypeDef(jsonWithNumber)
		res shouldBe a [JsonNode]
		res.toString shouldBe "{\"type\":\"object\",\"properties\":{\"id\":{\"type\":\"number\"},\"desc\":{\"type\":\"string\"}}}"
	}

	it should "create boolean for boolean type" in {
		val res: JsonNode = SwaggerDefinitionCreator.createTypeDef(jsonWithBoolean)
		res shouldBe a [JsonNode]
		res.toString shouldBe "{\"type\":\"object\",\"properties\":{\"id\":{\"type\":\"number\"},\"desc\":{\"type\":\"string\"},\"valid\":{\"type\":\"boolean\"}}}"
	}

	it should "throw illegal argument if invalid json" in {
		val invalidJsonString = "{this is not json: blaa}"
		assertThrows[IllegalArgumentException] {
			SwaggerDefinitionCreator.createTypeDef(invalidJsonString)
		}
	}

	it should "parse also nested json" in {
		val res: JsonNode = SwaggerDefinitionCreator.createTypeDef(nestedJson)
		res shouldBe a [JsonNode]
	}
}
