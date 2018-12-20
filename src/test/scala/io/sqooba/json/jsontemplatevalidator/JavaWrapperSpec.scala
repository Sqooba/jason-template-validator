package io.sqooba.json.jsontemplatevalidator

import org.scalatest.{FlatSpec, Matchers}

class JavaWrapperSpec extends FlatSpec with Matchers {

  val json1 = """{ "id": 5, "desc":"someval" }"""
  val json2 = """{ "id": 5, "desc":"someval", "other":"optional" }"""

  "JsonValidatorAsJava" should "validate legal json in java boolean" in {
    val validator = JsonValidator.forTemplateJson(json1).asJava()
    val res1 = validator.validateJson(json1)
    res1 shouldBe a [java.lang.Boolean]
    res1 shouldBe true

    val res2 = validator.validateJson(json2)
    res2 shouldBe a [java.lang.Boolean]
    res2 shouldBe true
  }

  "JsonValidatorAsJava" should "procudes set of errors" in {
    val validator = JsonValidator.forTemplateJson(json2).asJava()
    val res1 = validator.validateJson(json1)
    res1 shouldBe a [java.lang.Boolean]
    res1 shouldBe false

    val errors = validator.getLatestErrors()
    errors.size() shouldBe 1
    errors.contains("other") shouldBe true
  }
}
