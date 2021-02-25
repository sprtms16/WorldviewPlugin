package viewmodel

import main.Main
import model.TestGUI
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import java.util.logging.Level

class PlayerInteractionListener : Listener {
    @EventHandler
    fun playerInteractionEvent(event: PlayerInteractEvent) {
        Bukkit.getLogger().log(Level.INFO, "paper")
        Bukkit.getLogger().log(Level.INFO, event.item?.itemMeta?.lore?.get(0))
        when (event.item?.itemMeta?.lore?.get(0)) {
            "10000" -> {
                Main.balanceMap[event.player] = Main.balanceMap[event.player]?.plus(10000) ?: 10000
                event.item?.amount = event.item?.amount?.minus(1) ?: 0
            }
            else -> {
                TestGUI(event.player)
            }
        }
        event.player.sendMessage(Main.balanceMap[event.player].toString())
    }
}