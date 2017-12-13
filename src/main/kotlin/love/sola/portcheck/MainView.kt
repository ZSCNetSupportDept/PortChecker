package love.sola.portcheck

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.ToggleGroup
import javafx.scene.paint.Color
import tornadofx.*
import java.io.File
import kotlin.math.min

/**
 * @author Sola
 */
class MainView : View("帮移动擦屁股") {

    private val PORTS = 24
    private val status = (1..PORTS).associate { it to SimpleObjectProperty<Boolean>() }
    private val checkingProperty = SimpleIntegerProperty(1)
    private var checking by checkingProperty
    private val serial = SimpleStringProperty("4857443")

    override val root = vbox(spacing = 20, alignment = Pos.CENTER) {
        paddingAll = 20
        val toggleGroup = ToggleGroup()
        hbox(spacing = 10) {
            status.filter { it.key % 2 == 0 }.forEach { (index, property) ->
                portRadioButton(toggleGroup, index, property)
            }
        }
        hbox(spacing = 5, alignment = Pos.CENTER) {
            label("SN")
            textfield { bind(serial) }
        }
        hbox(spacing = 10) {
            status.filter { it.key % 2 != 0 }.forEach { (index, property) ->
                portRadioButton(toggleGroup, index, property)
            }
        }
        toggleGroup.bind(checkingProperty)
        hbox(10, Pos.CENTER) {
            button("check") {
                action {
                    val rs = check651()
                    status[checking]!!.value = !rs
                    checking = min(PORTS, checking + 1)
                }
            }
            button("export") {
                action {
                    status.map {
                        val stat = when (it.value.value) {
                            null -> "--"
                            true -> "OK"
                            false -> "NG"
                        }
                        "${it.key} -> $stat"
                    }.let {
                        File("${serial.value}.txt").writeText(it.joinToString(separator = "\n"))
                    }
                }
            }
            button("reconnect") { }
        }
    }

    private fun Node.portRadioButton(group: ToggleGroup, index: Int, property: SimpleObjectProperty<Boolean>) {
        radiobutton(index.toString(), value = index, group = group) {
            style {
                backgroundColor = multi(Color.WHITE)
            }
            property.onChange {
                val color = when (it) {
                    null -> Color.WHITE
                    true -> Color.GREEN
                    false -> Color.RED
                }
                style {
                    backgroundColor = multi(color)
                }
            }
        }
    }

}