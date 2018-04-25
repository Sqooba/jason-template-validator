# Template based Json validator
Create validator based on a json. Validates further json based on having same fields as the original one. Defined fields are required, others allowed.
```
    val validator = JsonValidator.forTemplateJson(someJsonAsString)
    val isValid: Boolean = validator.validateJson(someJson)
```