package hr.algebra.imdbmovies.factory

import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

private const val TIMEOUT = 10000
private const val METHOD_GET = "GET"
private const val USER_AGENT = "User-Agent"
private const val MOZILLA = "Mozilla/5.0"

fun createGetHttpURLConnection(path: String): HttpsURLConnection {
    val url = URL(path)
    val connection = url.openConnection() as HttpsURLConnection
    connection.connectTimeout = TIMEOUT
    connection.readTimeout = TIMEOUT
    connection.requestMethod = METHOD_GET
    connection.setRequestProperty(USER_AGENT, MOZILLA)
    return connection
    }
