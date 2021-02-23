package main

import Utill.GUI
import model.TestGUI
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Level


class Main : JavaPlugin() {
    companion object {
        val balanceMap: MutableMap<Player, Int> = mutableMapOf()
    }


    override fun onEnable() {
        logger.log(Level.INFO, name)

        server.pluginManager.registerEvents(object : Listener {
            @EventHandler
            fun guiClick(e: InventoryClickEvent) {
                GUI.getGUI(e.whoClicked as Player)?.onClick(e)
            }

            @EventHandler
            fun guiClose(e: InventoryCloseEvent) {
                GUI.getGUI(e.player as Player)?.closeGUI(e)
            }

            @EventHandler
            fun test(e: PlayerInteractEvent) {
                if (e.isBlockInHand) {
                    TestGUI(e.player)
                }
            }
        }, this)
    }
}