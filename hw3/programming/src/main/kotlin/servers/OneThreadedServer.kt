package servers

import java.io.*
import java.net.ServerSocket
import java.net.Socket
import java.util.*

class OneThreadServer(private val client: Socket?) : Runnable {
    override fun run() {
        try {
            val bufferReader = BufferedReader(InputStreamReader(client!!.getInputStream()))
            val printWriter = PrintWriter(client.getOutputStream())
            val input = bufferReader.readLine()
            val stringTokenizer = StringTokenizer(input)
            stringTokenizer.nextToken()
            val localFile: String = stringTokenizer.nextToken()
            val file = File(localFile.substring(1))
            if (file.exists()) {
                val fileLength = file.length().toInt()
                println("Requested file ${file.path}")
                printWriter.println("HTTP/1.1 200 OK")
                printWriter.println("Date: " + Date())
                printWriter.println("Content-type: ${if (file.name.endsWith(".html")) "text/html" else "text/plain"}")
                printWriter.println("Content-length: ${fileLength}\n")
                printWriter.write(file.readText())
            } else {
                val data = "<HTML><HEAD><TITLE>Not Found</TITLE></HEAD><BODY>Not Found</BODY></HTML>"
                val content = "text/html"
                printWriter.println("HTTP/1.1 404 Not Found")
                printWriter.println("Date: " + Date())
                printWriter.println("Content-type: $content")
                printWriter.println("Content-length: ${data.length}\n")
                printWriter.write(data, 0, data.length)
            }
            printWriter.flush()
            bufferReader.close()
            printWriter.close()
            client.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

fun main() {
    try {
        val port = 8080
        val ss = ServerSocket(port)
        println("Server started at http://0.0.0.0:8080\n")
        while (true) {
            OneThreadServer(ss.accept()).run()
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}