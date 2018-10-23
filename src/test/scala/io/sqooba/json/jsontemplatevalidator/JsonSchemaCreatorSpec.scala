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
    // println(s"schema: ${mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonSchema)}")
    jsonSchema.get("type").asText() shouldBe "object"
    // val propList = jsonSchema.get("properties").fieldNames().asScala.toList
    // propList.length shouldBe 1
  }

}
