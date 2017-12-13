package love.sola.portcheck

/**
 * @author Sola
 */
private val runtime = Runtime.getRuntime()

fun check651(): Boolean {
    val proc = runtime.exec("rasdial aaa a@a.a aaa")
    val stdout = proc.inputStream.bufferedReader().readLines().joinToString(separator = "\n")
    println(stdout)
    return stdout.contains("651")
}