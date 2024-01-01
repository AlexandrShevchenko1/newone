import kotlinx.serialization.Serializable

@Serializable
class Movie (private var name : String, private var duration : UInt, private var description : String) {
    private var id: Int = 0

    fun getId() : Int {
        return id
    }

    fun getName() : String {
        return name
    }

    fun setName(newName : String) {
        name = newName
    }

    fun getDuration() : UInt {
        return duration
    }

    fun setDuration(newDuration : UInt) {
        duration = newDuration
    }

    fun getDescription() : String {
        return description
    }

    fun setDescription(newDescription : String) {
        description = newDescription
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
        return "Id: $id Name: $name"
    }
}
