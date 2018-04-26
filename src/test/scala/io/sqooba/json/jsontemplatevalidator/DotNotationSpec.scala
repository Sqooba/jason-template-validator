package io.sqooba.json.jsontemplatevalidator

import com.fasterxml.jackson.databind.ObjectMapper
import org.scalatest.{FlatSpec, Matchers}

class DotNotationSpec extends FlatSpec with Matchers {

  val mapper = new ObjectMapper()

  "dot notation creator" should "create validator for 1 line " in {
    val dotNot = "push_antrag.wisi0061.vtrg.pol_nr"
    val validator = JsonValidator.forDotNotation(dotNot)

    val validJson = """ { "push_antrag": { "wisi0061": {"vtrg": {"pol_nr":5 }}}} """
    val invalidJson1 = """ { "push_antrag": { "wisi0061": {"vtrg": {"pol":5 }}}} """
    val invalidJson2 = """ { "push_antrag": { "wisi0061": {"pol_nr":5 }}} """

    validator.validateJson(validJson) shouldBe true
    validator.validateJson(invalidJson1) shouldBe false
    validator.validateJson(invalidJson2) shouldBe false
  }

  "dot notation creator" should "create validator for 2 levels nested" in {
    val dotNot =
      """foo.bar
        foo.pong""".stripMargin

    val validator = JsonValidator.forDotNotation(dotNot)
    val validJson = """ { "foo": { "bar": "perse", "pong": "nice" }} """
    val invalidJson = """ { "foo": { "pong": "nice" }} """
    validator.validateJson(validJson) shouldBe true
    validator.validateJson(invalidJson) shouldBe false
  }

  "dot notation creator" should "create validator for 3 levels nested" in {
    val dotNot =
      """foo.bar.ping
        foo.bar.pong
        foo.pong""".stripMargin

    val validator = JsonValidator.forDotNotation(dotNot)
    val validJson = """ { "foo": { "bar": "perse", "pong": "nice" }} """
    val invalidJson = """ { "foo": { "pong": "nice" }} """

    validator.validateJson(validJson) shouldBe false
    validator.validateJson(invalidJson) shouldBe false
  }

  "dot notation creator" should "create validator" in {
    val dotNot =
      """push_antrag.wisi0061.vtrg.pol_nr
        push_antrag.wisi0061.vtrg.mand_cdu
        push_antrag.wisi0061.vtrg.eingesetzt
        push_antrag.wisi0061.vtrg.v_prod_cdu
        push_antrag.wisi0061.vtrg.vv_g_std.vbsi_id""".stripMargin

    val validator = JsonValidator.forDotNotation(dotNot)
    val validJson = """ { "push_antrag": { "wisi0061": {"vtrg": {"pol_nr":5 }}}} """
  }

}
