import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.time.Duration
import kotlin.random.Random

@Serializable
class CinemaManager {
    private var movies: MutableList<Movie> = mutableListOf()
    private var sessions: MutableList<Session> = mutableListOf()
    private var soldTickets: MutableList<Ticket> = mutableListOf()

    fun manageMovieData() {
        var input : Int
        while (true) {
            print("Press (1) to edit an existing movie, (2) to add a new one, (3) to delete existing movie: ")
            input = readln().toIntOrNull() ?: 0

            if (input in 1..3) {
                break
            } else {
                println("Invalid input. Please enter either (1) or (2).")
            }
        }
        when (input) {
            1 -> editMovieData()
            2 -> addNewMovieData()
            3 -> deleteMovie()
        }
    }

    private fun addNewMovieData() {
        println("Enter the details for the new movie:")

        print("Name: ")
        val name = readlnOrNull() ?: ""

        print("Duration (in minutes): ")
        val duration = readlnOrNull()?.toUIntOrNull() ?: 0u

        print("Description: ")
        val description = readlnOrNull() ?: ""

        movies.add(Movie(name, duration, description))

        println("New movie added successfully.")
    }

    private fun editMovieData() {
        if (movies.isEmpty()) {
            println("There are no films available for editing at the box office.")
            return
        }
        movies.forEach { print(it.toString() + "\n") }
        var id : Int
        while (true) {
            print("Select the appropriate Id (To exit press 0): ")
            id = readlnOrNull()?.toIntOrNull() ?: -1

            if (id == 0) {
                return
            }

            val foundMovie = movies.find { it.getId() == id }
            if (foundMovie != null) {
                println("Current name: ${foundMovie.getName()}")
                print("New name (type 0 to not edit): ")
                var inputStr : String = readlnOrNull() ?: "0"
                if (inputStr != "0") {
                    foundMovie.setName(inputStr)
                }
                println("Current description: ${foundMovie.getDescription()}")
                print("New description (type 0 to not edit): ")
                inputStr = readlnOrNull() ?: "0"
                if (inputStr != "0") {
                    foundMovie.setDescription(inputStr)
                }
                break
            } else {
                println("Movie with such id is not found. Try again.")
            }
        }
    }

    private fun deleteMovie() {
        if (movies.isEmpty()) {
            println("There is no movie to delete.")
            return
        }
        movies.forEach { print(it.toString() + "\n") }
        var id : Int
        while(true) {
            print("Type the Id of the movie to delete (0 exit): ")
            id = readlnOrNull()?.toIntOrNull() ?: -1

            if (id == 0) {
                return
            }

            val foundMovie = movies.find { it.getId() == id }
            if (foundMovie != null) {
                movies.removeIf { it.getId() == id }
                println("Movie with id=$id has been successfully removed.")
                return
            } else {
                println("Movie with such id is not found. Try again.")
            }
        }
    }


    fun manageSessionData() {
        var input : Int
        while (true) {
            print("Press (1) to edit an existing session, (2) to add a new one, (3) to delete: ")
            input = readln().toIntOrNull() ?: 0

            if (input in 1..3) {
                break
            } else {
                println("Invalid input. Please enter either (1) or (2).")
            }
        }
        when (input) {
            1 -> editSessionData()
            2 -> addNewSessionData()
            3 -> deleteSession()
        }
    }

    private fun addNewSessionData() {
        // в приоритете запрашивать дату, чтобы если вдруг возникнет наложение можно было отказаться от добавления
        // новой сессии как вариант
        // принимаю start date end date - make sure что второе больше чем первое
        val stSession : Date
        val endSession : Date
        val movie : Movie
        while (true) {
            println("Format of the input should be minute/hourTday/month/year for example 00/04T16/04/2006")
            val (start, rs) = getDate("Enter start date of a session (press 0 to exit): ")
            if (rs) {
                return
            }
            val (end, re) = getDate("Enter end date of a session (press 0 to exit): ")
            if (re) {
                return
            }

            if (start >= end) {
                println("Start date can't be >= end date! Try again.")
                continue
            }
            // Доп проверка на пересечение сеанссов
            if (!(sessions.all { start > it.getEnd() || end < it.getStart()})) {
                println("There is already a session for this time!")
                continue
            }
            // а есть ли вообще фильмы duration которых не превосходит end-start?
            val difference = Duration.between(start.toInstant(), end.toInstant())
            val differenceInMinutes = difference.toMinutes().toUInt()
            var mvFound = false
            movies.forEach {
                if (it.getDuration() <= differenceInMinutes) {
                    println(it.toString())
                    mvFound = true
                }
            }
            if (!mvFound) {
                println("There are no movies with a suitable duration!")
                continue
            }
            stSession = start
            endSession = end
            break
        }
        var id : Int
        while (true) {
            print("Enter the ID of the movie you want to add to the session (press 0 to exit): ")
            id = readln().toIntOrNull() ?: -1
            if (id == 0) {
                return
            }
            val movieFound = movies.find { it.getId() == id }
            if (movieFound != null) {
                movie = movieFound
                break
            } else {
                println("Movie with such id is not found. Try again.")
            }
        }
        sessions.add(Session(movie, stSession, endSession))
        println("Movie ${movie.getName()} has been successfully added to the session.")
    }

    private fun editSessionData() {
        if (sessions.isEmpty()) {
            println("There are no sessions available for editing.")
            return
        }
        sessions.forEach { println(it.toString()) }
        var id : Int
        while (true) {
            print("Select the appropriate Id (To exit press 0): ")
            id = readlnOrNull()?.toIntOrNull() ?: -1

            if (id == 0) {
                return
            }

            val foundSession = sessions.find { it.getId() == id }
            if (foundSession != null) {
                println("Current movie: ${foundSession.getMovie()}")
                println("Movies available in the box office: ")
                movies.forEach {  println(it.toString()) }
                println("(if you select the Id of the current movie, nothing will be changed)")
                var inputId : Int
                while (true) {
                    print("Select the appropriate Id of the movie to change the current one of the session (To exit press 0): ")
                    inputId = readln().toIntOrNull() ?: -1
                    if (inputId == 0) {
                        break
                    }
                    val movieFound = movies.find { it.getId() == inputId}
                    if (movieFound != null) {
                        foundSession.setMovie(movieFound)
                        break
                    } else {
                        println("Movie with such id is not found. Try again.")
                    }
                }
                var userInput: String
                while(true) {
                    print("Would you like to edit the Session start/end date ('Y'/'N'): ")
                    userInput = readlnOrNull()?.trim() ?: ""

                    if (userInput.equals("Y", ignoreCase = true)) {
                        break
                    } else if (userInput.equals("N", ignoreCase = true)) {
                        println("Session editing is finished!")
                        return
                    } else {
                        println("Wrong input. It should be 'Y'/'y' или 'N'/'n'.")
                        continue
                    }
                }
                while (true) {
                    println("Important Note: if you want to change session's start date without changing end date, " +
                            "you should enter the same end date (and vise versa). if 0 is pressed, dates remain unchanged.")
                    println("Format of the input should be minute/hourTday/month/year for example 00/04T16/04/2006")
                    val (start, rs) = getDate("Enter start date of a session (press 0 to exit): ")
                    if (rs) {
                        return
                    }
                    val (end, re) = getDate("Enter end date of a session (press 0 to exit): ")
                    if (re) {
                        return
                    }

                    if (start >= end) {
                        println("Start date can't be >= end date! Try again.")
                        continue
                    }
                    // Доп проверка на пересечение сеанссов
                    sessions.filter { it != foundSession }.all {
                        start > it.getEnd() || end < it.getStart()
                    }
                    if (!(sessions.filter { it != foundSession }.all { start > it.getEnd() || end < it.getStart() })) {
                        println("There is already a session for this time!")
                        continue
                    }
                    // а есть ли вообще фильмы duration которых не превосходит end-start?
                    val difference = Duration.between(start.toInstant(), end.toInstant())
                    val differenceInMinutes = difference.toMinutes().toUInt()
                    if (foundSession.getMovie().getDuration() > differenceInMinutes) {
                        println("Session' movie has a duration (${foundSession.getMovie().getDuration()} minutes) " +
                                "can't be greater that session duration ($differenceInMinutes minutes). Try again!")
                        continue
                    }
                    foundSession.setStart(start)
                    foundSession.setEnd(end)
                    break
                }
            } else {
                println("Session with such id is not found. Try again.")
            }
        }
    }

    private fun deleteSession() {
        if (sessions.isEmpty()) {
            println("There is no session to delete.")
            return
        }
        sessions.forEach { println(it.toString()) }
        var id : Int
        while(true) {
            print("Type the Id of the session to delete (0 to exit): ")
            id = readlnOrNull()?.toIntOrNull() ?: -1

            if (id == 0) {
                return
            }

            val foundSession = sessions.find { it.getId() == id }
            if (foundSession != null) {
                sessions.removeIf { it.getId() == id }
                println("Session with id=$id has been successfully removed.")
                return
            } else {
                println("Session with such id is not found. Try again.")
            }
        }
    }


    fun getMovies() {
        movies.forEach { println("Movie info (id = ${it.getId()})\nName: ${it.getName()}\n" +
                "Duration: ${it.getDuration()}\nDescription: ${it.getDescription()}\n")}
    }

    fun getSession() {
        sessions.forEach { println("Session info (id = ${it.getId()})\nMovie name: ${it.getMovie().getName()}\n" +
                "Start date: ${it.getStart()}\nEnd date: ${it.getEnd()}\n") }
    }


    fun sellTicket() {
        if (sessions.isEmpty()) {
            println("There is no session to buy ticket for.")
            return
        }
        // есть массив, индекс это свободное место в случае если его значение false (то есть не куплено)
        // choose session
        println("Available sessions in the cinema: ")
        getSession()
        var id : Int
        val session : Session
        while(true) {
            print("Enter the Id of the session you want to attend (0 to exit): ")
            id = readlnOrNull()?.toIntOrNull() ?: -1

            if (id == 0) {
                return
            }

            val foundSession = sessions.find { it.getId() == id }
            if (foundSession != null) {
                val currentDateAndTime: Date = Date()
                if (currentDateAndTime >= foundSession.getStart()) {
                    println("Unfortunately, you can't by ticket for this session " +
                            "because it has already started/finished.")
                    continue
                }
                session = foundSession
                break
            } else {
                println("Session with such id is not found. Try again.")
            }
        }
        // ask visitor to choose the seat val
        print("Free seats (total $TOTAL_SEATS): ")
        var freeSeats = false
        for (index in session.getSeats().indices) {
            if (!session.getSeats()[index]) {
                print("${index + 1} ")
                freeSeats = true
            }
        }
        print('\n')
        // get input
        if (!freeSeats) {
            println("Unfortunately, this session is fully booked.")
            return
        }
        var seatNumber : Int
        while(true) {
            print("Choose free seat by typing it's number from the list above (0 to exit): ")
            seatNumber = readlnOrNull()?.toIntOrNull() ?: (TOTAL_SEATS+1)
            if (seatNumber == 0) {
                println("Booking was canceled!")
                return
            }
            if (seatNumber < 1 || seatNumber > TOTAL_SEATS) {
                println("Input isn't correct. Try again.")
                continue
            }
            if (session.getSeats()[seatNumber-1]) {
                println("This seat is already booked!")
                continue
            }
            break
        }
        // (get visitor data) providing data for Visitor creation in Ticket constructor
        print("Enter you nickname: ")
        val nickName = readlnOrNull() ?: "-"
        var cardNumber : UInt
        while(true) {
            print("Enter card number (6-digit positive number) (0 to exit): ")
            cardNumber = readlnOrNull()?.toUIntOrNull() ?: 1u
            if (cardNumber == 0u) {
                println("Booking was canceled!")
                return
            }
            if (cardNumber !in 100000u..999999u) {
                println("Error! The value entered isn't 6-digit positive number. Try again.")
                continue
            }
            break
        }
        // book the seat for the visitor
        session.getSeats()[seatNumber-1] = true
        println("Congratulations! $nickName you have just bought ticket for ${session.getMovie().getName()}")
        val randomDigit: Int = Random.nextInt(10000, 100000)
        println("If you want to return your ticket, remember this unique number (don't tell it anyone): $randomDigit")
        soldTickets.add(Ticket(session, seatNumber, nickName, cardNumber, randomDigit))
    }

    fun returnTicket() {
        if (soldTickets.isEmpty()) {
            println("You can't return ticket for now.")
            return
        }

        var uniqueNumber : Int
        while (true) {
            print("Enter unique number of your ticket (0 to exit): ")
            uniqueNumber = readlnOrNull()?.toIntOrNull() ?: -1
            if (uniqueNumber == 0) {
                println("Return ticket procedure was canceled.")
                return
            }
            val foundTicket = soldTickets.find { it.getId() == uniqueNumber }
            if (foundTicket != null) {
                // < then ok
                val currentDateAndTime: Date = Date()
                if (currentDateAndTime >= foundTicket.getSession().getStart()) {
                    println("Unfortunately, you can't return you ticket because the session has already started.")
                    return
                }
                foundTicket.getSession().getSeats()[foundTicket.getSeatNumber() - 1] = false
                soldTickets.removeIf { it.getId() == uniqueNumber }
                println("Congratulations! You have successfully returned your ticket.")
                return
            } else {
                println("Wrong unique number! Try again.")
            }
        }
    }

    fun displaySeatsStatus() {
        if (sessions.isEmpty()){
            println("No available sessions yet. Come later.")
            return
        }
        getSession()
        var id : Int
        while (true) {
            print("Enter unique number of your session (0 to exit): ")
            id = readlnOrNull()?.toIntOrNull() ?: -1
            if (id == 0) {
                println("No free/booked seats were displayed.")
                return
            }
            val foundSession = sessions.find { it.getId() == id }
            if (foundSession != null) {
                print("Booked seats: ")
                for (index in foundSession.getSeats().indices) {
                    if (foundSession.getSeats()[index]) {
                        print("${index + 1} ")
                    }
                }
                print("\nFree seats: ")
                for (index in foundSession.getSeats().indices) {
                    if (!foundSession.getSeats()[index]) {
                        print("${index + 1} ")
                    }
                }
                print("\n")
                return
            } else {
                println("Wrong unique number! Try again.")
            }
        }
    }
}

fun isValidDate(date : String, dateFormat : SimpleDateFormat) : Boolean {
    dateFormat.isLenient = false

    try {
        dateFormat.parse(date)
    } catch (e : Exception) {
        return false
    }
    return true
}

fun getDate(prompt : String) : Pair<Date, Boolean> {
    var resDate : Date = Date()
    while (true) {
        print(prompt)
        val endDate : String = readlnOrNull() ?: "0"
        if (endDate == "0") {
            return Pair(resDate, true)
        }
        val formatter = SimpleDateFormat("mm/HH'T'dd/MM/yyyy")
        if (isValidDate(endDate, formatter)) {
            resDate = formatter.parse(endDate)
            break
        }
        println("The date is in the incorrect format. Please try to enter it again.")
    }
    return Pair(resDate, false)
}