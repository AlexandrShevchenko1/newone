import kotlinx.serialization.Serializable

@Serializable
class Ticket private constructor (private val session : Session, private val seatNumber : Int,
              private val id : Int, private val visitor: Visitor) {
    constructor(session : Session, seatNumber : Int, nickName : String, cardNumber : UInt, id : Int) : this(
        session = session, seatNumber = seatNumber, id = id, visitor = Visitor(nickName, cardNumber))

    fun getSession() : Session {
        return session
    }

    fun getVisitor() : Visitor {
        return visitor
    }

    fun getSeatNumber() : Int {
        return seatNumber
    }

    fun getId() : Int {
        return id
    }
}
