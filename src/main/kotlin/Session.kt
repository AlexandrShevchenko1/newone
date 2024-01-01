import kotlinx.serialization.Serializable
import java.util.*

import kotlinx.serialization.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat

@Serializer(forClass = Date::class)
object DateSerializer : KSerializer<Date> {
    private val dateFormat = SimpleDateFormat("mm/HH'T'dd/MM/yyyy")

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(dateFormat.format(value))
    }

    override fun deserialize(decoder: Decoder): Date {
        return dateFormat.parse(decoder.decodeString()) ?: Date()
    }
}


const val TOTAL_SEATS = 10

@Serializable
class Session (
    private var movie : Movie,
    @Serializable(with = DateSerializer::class)
    private var start : Date,
    @Serializable(with = DateSerializer::class)
    private var end : Date,
    private var seats : Array<Boolean> = Array(TOTAL_SEATS) { false })
{
    private var id: Int = 0

    fun getId() : Int {
        return id
    }

    fun getMovie() : Movie {
        return movie
    }

    fun setMovie(newMovie : Movie) {
        movie = newMovie
    }

    fun getStart() : Date {
        return start
    }

    fun setStart(newStart : Date) {
        start = newStart
    }

    fun getEnd() : Date {
        return end
    }

    fun setEnd(newEnd : Date) {
        end = newEnd
    }

    fun getSeats() : Array<Boolean> {
        return seats
    }

    fun setSeats(newSeats : Array<Boolean>) {
        seats = newSeats
    }

    // Статическая переменная для отслеживания текущего значения id
    companion object {
        private var currentId: Int = 1
    }
    init {
        // Присваиваем уникальный id при создании объекта
        this.id = currentId++
    }

    override fun toString() : String {
        return "Id=$id  Movie name: ${movie.getName()}  Session starts: $start  Session ends: $end"
    }
}
