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
    }

    override fun onClick(e: InventoryClickEvent) {
        when (getValue(e.rawSlot)) {
            "test" -> Main.balanceMap[player] = 0
            "Plus" -> Main.balanceMap[player] = Main.balanceMap[player]!! + 1
            "Minus" -> Main.balanceMap[player] = Main.balanceMap[player]!! - 1
        }
        player.sendMessage(player.displayName + "의 금액은 :" + Main.balanceMap[player])
        e.isCancelled = true
        return
    }
}