import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileWriter

class FilesManager {

    inline fun <reified T> saveChanges(item: T, filePath: String){
        val jsonString = Json.encodeToString(item)
        val file = File(filePath)
        val writer = FileWriter(file)
        writer.write(jsonString)
        writer.close()
    }

    inline fun <reified T> getData(filePath: String): T{
        val file = File(filePath)
        val content = file.readText()
        return Json.decodeFromString<T>(content)
    }
}