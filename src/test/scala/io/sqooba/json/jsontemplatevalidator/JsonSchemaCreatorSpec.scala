package io.sqooba.json.jsontemplatevalidator

import org.scalatest.{FlatSpec, Matchers}
import scala.collection.JavaConverters._

import com.fasterxml.jackson.databind.ObjectMapper

class JsonSchemaCreatorSpec extends FlatSpec with Matchers {

  val creator = new JsonSchemaCreator
  val mapper: ObjectMapper = new ObjectMapper()

  "JsonSchemaCreator" should "create schema" in {
    val simpleJson = """{ "id": 1 }"""
    val jsonSchema = creator.createSchemaFor(simpleJson)
    jsonSchema.get("type").asText() shouldBe "object"
    val propList = jsonSchema.get("properties").fieldNames().asScala.toList
    propList.length shouldBe 1
  }

  it should "create schema for few values" in {
    val simpleJson = """{ "id": 1, "name":"johny" }"""
    val jsonSchema = creator.createSchemaFor(simpleJson)
    jsonSchema.get("type").asText() shouldBe "object"
    val propList = jsonSchema.get("properties").fieldNames().asScala.toList
    propList.length shouldBe 2
  }

  it should "create schema for nested values" in {
    val simpleJson = """{ "id": 1, "name": {"firstName":"johny", "lastName":"mcenroe"} }"""
    val jsonSchema = creator.createSchemaFor(simpleJson)
    jsonSchema.get("type").asText() shouldBe "object"
    val propList = jsonSchema.get("properties").fieldNames().asScala.toList
    propList.length shouldBe 2
  }

  it should "create schema for array values" in {
    val simpleJson = """{ "id": 1, "names": [{"firstName":"johny", "lastName":"mcenroe"}, {"firstName":"mark", "lastName":"marky"}] }"""
    val jsonSchema = creator.createSchemaFor(simpleJson)
    jsonSchema.get("type").asText() shouldBe "object"
    val names = jsonSchema.get("properties").get("names").get("items").get("properties")
    names.has("firstName") shouldBe true
    names.has("lastName") shouldBe true
  }

  it should "create more complex example" in {
    val src = """
      {
        "productId": 1,
        "productName": "A green door",
        "price": 12.50,
        "tags": [ "home", "green" ]
      }
    """.stripMargin

    val jsonSchema = creator.createSchemaFor(src)
    val propList = jsonSchema.get("properties").fieldNames().asScala.toList
    propList.length shouldBe 4
    val tags = jsonSchema.get("properties").get("tags")
    tags.get("type").asText() shouldBe "array"
    tags.get("items").asText() shouldBe "string"

  }

  it should "create more complex example with number array" in {
    val src = """
      {
        "productId": 1,
        "productName": "A green door",
        "tags": [ 4, 5, 10 ]
      }
    """.stripMargin

    val jsonSchema = creator.createSchemaFor(src)
    val propList = jsonSchema.get("properties").fieldNames().asScala.toList
    propList.length shouldBe 3
    val tags = jsonSchema.get("properties").get("tags")
    tags.get("type").asText() shouldBe "array"
    tags.get("items").asText() shouldBe "number"

  }
}
