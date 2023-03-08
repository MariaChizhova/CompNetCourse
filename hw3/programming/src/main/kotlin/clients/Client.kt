import java.io.*
import java.net.*

class Client(host: String, port: Int, private val file: String) {
    private val socket = Socket(host, port)
    private var input = BufferedReader(InputStreamReader(socket.getInputStream()))
    private var output = DataOutputStream(socket.getOutputStream())

    fun execute() {
        val data = "GET /$file HTTP/1.1\n"
        output.writeUTF(data)
        output.flush()
        input.lines().forEach { println(it) }
    }
}

fun main(args: Array<String>) {
    val client = Client(args[0], args[1].toInt(), args[2])
    client.execute()
}