const val FILE_PATH = "file.txt"

class Program {
    fun runProgram() {
        val fileManager = FilesManager()
        var manager : CinemaManager
        try  {
            manager = fileManager.getData<CinemaManager>(FILE_PATH)
        } catch (e: Exception) {
            manager = CinemaManager()
        }
        var input : String?
        displayMenu()
        while(true) {
            print("Your choice (press 8 to display menu again): ")
            input = readlnOrNull() ?: "0"
            when(input) {
                "0" -> break
                "1" -> manager.manageMovieData()
                "2" -> manager.manageSessionData()
                "3" -> manager.getMovies()
                "4" -> manager.getSession()
                "5" -> manager.sellTicket()
                "6" -> manager.returnTicket()
                "7" -> manager.displaySeatsStatus()
                "8" -> displayMenu()
                else -> continue
            }
        }
        fileManager.saveChanges(manager, FILE_PATH)
    }

    private fun displayMenu() {
        print("Menu of the application\n" +
                "To exit application press 0\n" +
                "To manage movie data press 1\n" +
                "To manage session data press 2\n" +
                "To get all movies data press 3\n" +
                "To get all session data press 4\n" +
                "To sell ticket press 5\n" +
                "To return ticket press 6\n" +
                "To display seats status press 7\n")
    }
}