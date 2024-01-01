import kotlinx.serialization.Serializable

@Serializable
class Visitor(private val nickName: String, private val cardNumber: UInt) {
    // Getter for nickName (read-only)
    fun getNickName(): String {
        return nickName
    }
    // Getter for cardNumber (read-only)
    fun getCardNumber(): UInt {
        return cardNumber
    }
}
