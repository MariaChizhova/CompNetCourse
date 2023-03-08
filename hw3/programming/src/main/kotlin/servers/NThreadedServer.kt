package servers

import java.io.IOException
import java.net.ServerSocket
import java.util.concurrent.Executors

class NThreadedServer {
    fun execute(n: Int) {
        val tp = Executors.newFixedThreadPool(n)
        try {
            val port = 8080
            val ss = ServerSocket(port)
            println("Server started at http://0.0.0.0:8080\n")
            while (true) {
                tp.execute(OneThreadServer(ss.accept()))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            tp.shutdown()
        }
    }
}

fun main(args: Array<String>) {
    val threadCount = args[0].toInt()
    NThreadedServer().execute(threadCount)
}