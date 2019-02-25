import com.fasterxml.jackson.core.JsonFactory
import jsonDescription.readJsonItemDescription
import jsonDescription.toJsonString
import java.io.File

fun main(args: Array<String>) {
    for (arg in args) {
        val inFile = File(arg)
        val description = readJsonItemDescription(JsonFactory().createParser(inFile))
        val outFile = File(inFile.parentFile, "${inFile.nameWithoutExtension}.description.json")
        println(description)
        outFile.writeText(description.toJsonString())
    }
}
