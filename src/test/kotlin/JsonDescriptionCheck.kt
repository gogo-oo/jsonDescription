import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.ObjectMapper
import jsonDescription.readJsonItemDescription
import org.junit.Test

//    testImplementation 'com.fasterxml.jackson.core:jackson-core:2.9.8'
//
//        https://github.com/FasterXML/jackson-core#general
//        val factory = JsonFactory()
//        // configure, if necessary:
//        factory.enable(JsonParser.Feature.ALLOW_COMMENTS)
class JsonDescriptionCheck {

    val jsonStr01 = """
{
"rootObj":{
  "strVal01": "val01",
  "obj01":{
    "f01":"val015",
    "f02":1025
  },
  "obj02":{
    "f01":"val027",
    "f02":2027
  },
  "strArr01": [
    120,
    110,
    130
  ],
  "objArr01": [
      {
        "f01":"oval012",
        "f02":100012
      },
      {
        "f01":"oval022",
        "f02":200022
      }
  ],
  "arrArr01": [
      [
        "oval012",
        "1100012"
      ],
      [
        "oval022",
        "1200022"
      ]
  ],
  "strVal02": "val02",
  "intVal01": 1001
 }
}"""

    val jsonStr02 = """{
  "intVal01": 10,
  "strVal01": "val10",
  "objVal02": {
      "intVal01": 210,
      "strVal01": "val210"
  },
  "objVal01": {
      "intVal010": 110,
      "strVal010": "val110"
  },
  "objVal03": {
      "intVal012": 310,
      "strVal012": "val310",
      "objVal012": {
          "intVal014": 510,
          "strVal014": "val510"
      }
  },
  "arrObjVal":[
    {
      "intVal301": 31210,
      "strVal301": "val301"
    },
    {
      "intVal301": 41210,
      "strVal301": "val401"
    }
  ],
  "arrStrVal":[
    "valStr31",
    "valStr41",
    -1,
    true,
    -0.0
  ]
}
"""

    @Test
    fun `check 01 Json Description`() {
        println("JsonDescriptionCheck.`check 01 Json Description` ${KotlinVersion.CURRENT}")

        val descNull = readJsonItemDescription(JsonFactory().createParser("null"))
        println(descNull)

        val descJsonStr01 = readJsonItemDescription(JsonFactory().createParser(jsonStr01))
        println(descJsonStr01)
        println(toJsonPrettyStr(descJsonStr01.toString()))

        val descJsonStr02 = readJsonItemDescription(JsonFactory().createParser(jsonStr02))
        println(descJsonStr02)
        println(toJsonPrettyStr(descJsonStr02.toString()))

    }

    private fun toJsonPrettyStr(str: String): String {
        val mapper = ObjectMapper()
        val result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapper.readValue(str, Any::class.java))
        return result ?: ""
    }

}
