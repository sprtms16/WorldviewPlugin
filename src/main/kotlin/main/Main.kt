package main

import Utill.GUI
import model.TestCommandExecutor
import model.TestGUI
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.java.JavaPlugin
import viewmodel.PlayerInteractionListener
import java.util.logging.Level


class Main : JavaPlugin() {
    companion object {
        val balanceMap: MutableMap<Player, Int> = mutableMapOf()
    }

    override fun onEnable() {
        logger.log(Level.INFO, name)

        server.pluginManager.registerEvents(PlayerInteractionListener(),this)

        getCommand("check")?.setExecutor(TestCommandExecutor(this))
        getCommand("check")?.tabCompleter = TestCommandExecutor(this)

        server.pluginManager.registerEvents(object : Listener {
            @EventHandler
            fun guiClick(e: InventoryClickEvent) {
                GUI.getGUI(e.whoClicked as Player)?.onClick(e)
            }

            @EventHandler
            fun guiClose(e: InventoryCloseEvent) {
                GUI.getGUI(e.player as Player)?.closeGUI(e)
            }
        }, this)
    }
}