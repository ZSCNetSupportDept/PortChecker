package love.sola.portcheck

import tornadofx.App
import tornadofx.launch

/**
 * @author Sola
 */
class PortCheckApp : App(MainView::class) {

}

fun main(args: Array<String>) {
    launch<PortCheckApp>(args)
}
