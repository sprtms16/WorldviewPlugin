package model

import Utill.GUI
import main.Main
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class TestGUI(p: Player) : GUI(p, "test", 9) {
    private val player: Player = p
    override fun init() {
        setItem("test", listOf("test"), Material.STICK, 1, 0, "test", true)
        setItem("Plus", listOf("Plus"), Material.BEACON, 1, 1, "Plus", true)
        setItem("Minus", listOf("Minus"), Material.IRON_INGOT, 1, 2, "Minus", true)
        setItem("10000", listOf("10000"), Material.PAPER, 1, 3, "10000", true)
        setItem("수표 발행(10000)", listOf("10000"), Material.PAPER, 1, 4, "수표", true)
    }

    operator fun plus(other: Int): Int {
        return plus(other)
    }

    override fun onClick(e: InventoryClickEvent) {

        when (getValue(e.rawSlot)) {
            "test" -> Main.balanceMap[player] = 0
            "Plus" -> Main.balanceMap[player] = Main.balanceMap[player]?.plus(1) ?: 0
            "Minus" -> Main.balanceMap[player] = Main.balanceMap[player]?.minus(1) ?: 0
            "10000" -> e.whoClicked.inventory.addItem(e.currentItem)
            "수표" -> {
                if (Main.balanceMap[player] ?: 0 >= 10000) {
                    e.whoClicked.inventory.addItem(e.currentItem)
                    Main.balanceMap[player] = Main.balanceMap[player]?.minus(10000) ?: 0
                }
            }
        }
        player.sendMessage(player.displayName + "의 금액은 :" + Main.balanceMap[player])
        e.isCancelled = true
        return
    }
}