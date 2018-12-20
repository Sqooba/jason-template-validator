# Template based Json validator
Create validator based on a json. Validates further json based on having same fields as the original one. Defined fields are required, others allowed.
```
    val validator = JsonValidator.forTemplateJson(someJsonAsString)
    val isValid: Boolean = validator.validateJson(someJson)
    
    // if you have errors, you can ask validator for latest errors. 
    val errors: Set[String] = validator.getLatestErrors()
```
Errors are just missing fields names.

## Using
Include with sbt:
```
libraryDependencies += "io.sqooba"  %% "json-template-validator" % "0.3.4"
```

With maven:
```
    <dependency>
        <groupId>io.sqooba</groupId>
        <artifactId>json-template-validator_2.11</artifactId>
        <version>0.3.4</version>
    </dependency>    
```