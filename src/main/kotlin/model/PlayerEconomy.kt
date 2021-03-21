package model


import net.milkbowl.vault.economy.Economy
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Level

class PlayerEconomy(plugin: JavaPlugin, private val econ: Economy) {

    private val logger =  plugin.logger

    fun add(player: Player, amount: Double) {
        val r = econ.depositPlayer(player, amount)
        if (r?.transactionSuccess() == true) {
            player.sendMessage(
                String.format(
                    "You were given %s and now have %s",
                    econ.format(r.amount),
                    econ.format(r.balance)
                )
            )
            logger.log(Level.FINE, player.displayName+"에게 "+r.amount+" 만큼 지급하였습니다.")
        } else {
            player.sendMessage(String.format("An error occured: %s", r?.errorMessage))
            logger.log(Level.WARNING,
                player.displayName+"에게 "+r.amount+" 만큼 지급시도 하였으나 실패 하였습니다. : "+ r?.errorMessage)
        }
    }

    fun deposit(giver: Player, recipient: Player, amount: Double): Boolean {
        return if (subtraction(recipient, amount)) {
            add(giver, amount)
            true
        } else {
            false
        }
    }

    fun subtraction(player: Player, amount: Double): Boolean {
        val r = econ.withdrawPlayer(player, amount)
        return if (r?.transactionSuccess() == true) {
            player.sendMessage(
                String.format(
                    "You were given %s and now have %s",
                    econ.format(r.amount),
                    econ.format(r.balance)
                )
            )
            logger.log(Level.FINE, player.displayName+"에게 "+r.amount+" 만큼 출금하였습니다.")
            true
        } else {
            player.sendMessage(String.format("An error occured: %s", r?.errorMessage))
            logger.log(Level.WARNING,
                player.displayName+"에게 "+r.amount+" 만큼 출금시도 하였으나 실패 하였습니다. : "+ r?.errorMessage)
            false
        }
    }

    fun getBalance(player: Player): Double {
        return econ.getBalance(player)
    }

    fun checkBalance(player: Player, amount: Double): Boolean {
        return econ.has(player, amount)
    }

    fun showBalance(player: Player){
        player.sendMessage(player.displayName + "님의 소지하고 있는 금액은 "+econ.getBalance(player)+ "입니다.")
    }

    fun checkGeneration(player: Player, amount: Double) {
        val paper = ItemStack(Material.PAPER)
        val meta = paper.itemMeta
        meta?.setDisplayName(amount.toString() + "수표")
        meta?.lore = arrayListOf(amount.toString())
        meta?.addEnchant(Enchantment.LURE, 1, false)
        meta?.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        paper.itemMeta = meta
        player.inventory.addItem(paper)
    }


}