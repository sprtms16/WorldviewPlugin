package main

import Utill.GUI
import model.TestCommandExecutor
import net.milkbowl.vault.chat.Chat
import net.milkbowl.vault.economy.AbstractEconomy
import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.economy.EconomyResponse
import net.milkbowl.vault.permission.Permission
import org.bukkit.OfflinePlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.plugin.java.JavaPlugin
import viewmodel.PlayerInteractionListener
import java.util.logging.Level
import java.util.logging.Logger
import org.bukkit.plugin.ServicePriority

import org.bukkit.plugin.ServicesManager


class Main : JavaPlugin(), CommandExecutor {
    companion object {
        val balanceMap: MutableMap<Player, Int> = mutableMapOf()
        private val log: Logger = Logger.getLogger("Minecraft")
        private var econ: Economy? = null
        private var perms: Permission? = null
        private var chat: Chat? = null
    }

    override fun onDisable() {
        log.info(String.format("[%s] Disabled Version %s", description.name, description.version))
    }

    override fun onEnable() {
        log.info(String.format("[%s] Enabled Version %s", description.name, description.version))
        if (!setupEconomy()) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", description.name))
            server.pluginManager.disablePlugin(this)
            return
        }
        setupPermissions()
        setupChat()
        logger.log(Level.INFO, name)

        server.pluginManager.registerEvents(PlayerInteractionListener(), this)

        getCommand("check")?.setExecutor(TestCommandExecutor(this))
        getCommand("test-economy")?.setExecutor(this)
        getCommand("test-permission")?.setExecutor(this)
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

    private fun setupEconomy(): Boolean {
        if (server.pluginManager.getPlugin("Vault") == null) {
            log.severe(String.format("[%s] - Vault is null", description.name))
            return false
        }
        val rsp = server.servicesManager.getRegistration(Economy::class.java) ?: return false
        econ = rsp.provider
        return econ != null
    }

    private fun setupChat(): Boolean {
        val rsp = server.servicesManager.getRegistration(Chat::class.java)
        chat = rsp?.provider
        return chat != null
    }

    private fun setupPermissions(): Boolean {
        val rsp = server.servicesManager.getRegistration(
            Permission::class.java
        )
        perms = rsp?.provider
        return perms != null
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            log.info("Only players are supported for this Example Plugin, but you should not do this!!!")
            return true
        }
        return when (command.label) {
            "test-economy" -> {
                // Lets give the player 1.05 currency (note that SOME economic plugins require rounding!)
                sender.sendMessage(String.format("You have %s", econ?.getBalance(sender)))
                val r = econ?.depositPlayer(sender, 1.0)?: EconomyResponse(0.0,0.0,EconomyResponse.ResponseType.FAILURE,"econ is null")
                if (r.transactionSuccess()) {
                    log.log(Level.INFO, r.amount.toString())
                    log.log(Level.INFO, r.balance.toString())
                    log.log(Level.INFO, (econ?.isEnabled?:false).toString())
                    sender.sendMessage(
                        String.format(
                            "You were given %s and now have %s",
                            econ?.format(r.amount),
                            econ?.format(r.balance)
                        )
                    )
                } else {
                    sender.sendMessage(String.format("An error occured: %s", r.errorMessage))
                }
                true
            }
            "test-permission" -> {
                // Lets test if user has the node "example.plugin.awesome" to determine if they are awesome or just suck
                if (perms?.has(sender, "example.plugin.awesome") == true) {
                    sender.sendMessage("You are awesome!")
                } else {
                    sender.sendMessage("You suck!")
                }
                true
            }
            else -> {
                false
            }
        }
    }

    fun getEconomy(): Economy? {
        return econ
    }

    fun getPermissions(): Permission? {
        return perms
    }

    fun getChat(): Chat? {
        return chat
    }
}