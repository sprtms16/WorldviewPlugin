package viewmodel.eventlistener

import model.PlayerEconomy
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import java.util.logging.Level

class PlayerInteractionListener(private val economy: PlayerEconomy?) : Listener {
    @EventHandler
    fun playerInteractionEvent(event: PlayerInteractEvent) {

        when (event.item?.itemMeta?.lore?.get(0)) {
            "10000.0" -> {
                Bukkit.getLogger().log(Level.INFO, "paper")
                Bukkit.getLogger().log(Level.INFO, event.item?.itemMeta?.lore?.get(0))
                economy?.add(event.player, 10000.0)
                event.item?.amount = event.item?.amount?.minus(1) ?: 0
                economy?.showBalance(player = event.player)
            }
        }

    }
}