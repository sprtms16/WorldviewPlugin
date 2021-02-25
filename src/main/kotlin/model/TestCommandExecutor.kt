package model

import main.Main
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class TestCommandExecutor(plugin: JavaPlugin) : CommandExecutor, TabCompleter {
    private val plugin: JavaPlugin = plugin


    override fun onCommand(commandSender: CommandSender,
                           command: Command, label: String,
                           args: Array<out String>): Boolean {
        if (command.name == "check") {
            if (commandSender is Player) {
                val player: Player = commandSender
                val target = Bukkit.getPlayer(args[0])
                var param = 0
                if (target != null && args.size > 1) {
                    param++
                }
                when (args[param]) {
                    "confirm" -> {
                        if (target != null) {
                            if (player.isOp) {
                                player.sendMessage(target.displayName + "의 돈은 " + (Main.balanceMap[target]
                                        ?: 0) + "원 입니다.")
                            } else {
                                player.sendMessage("권한이 부족합니다.")
                            }

                        } else {
                            player.sendMessage("당신의 돈은 " + (Main.balanceMap[player] ?: 0) + "원 입니다.")
                        }
                    }
                    "publish" -> {
                        if (target != null) {
                            if (player.isOp) {
                                checkGeneration(target, 10000)
                                player.sendMessage(target.displayName + "에게 10000원을 발급하였습니다.")
                                target.sendMessage(player.displayName + "가 당신에게 10000원을 지급하였습니다.")
                            } else {
                                if (Main.balanceMap[player] ?: 0 >= 10000) {
                                    Main.balanceMap[player] = Main.balanceMap[player]?.minus(10000) ?: 0
                                    checkGeneration(target, 10000)
                                    player.sendMessage(target.displayName + "에게 10000원을 발급하였습니다.")
                                    target.sendMessage(player.displayName + "가 당신에게 10000원을 지급하였습니다.")
                                } else {
                                    player.sendMessage("잔액이 부족합니다.")
                                }
                            }
                        } else {
                            if (Main.balanceMap[player] ?: 0 >= 10000 || player.isOp) {
                                if (!player.isOp) {
                                    Main.balanceMap[player] = Main.balanceMap[player]?.minus(10000) ?: 0
                                }
                                checkGeneration(player, 10000)
                                player.sendMessage("당신의 돈은 " + (Main.balanceMap[player] ?: 0) + "원 입니다.")
                            } else {
                                player.sendMessage("잔액이 부족합니다.")
                                player.sendMessage("당신의 돈은 " + (Main.balanceMap[player] ?: 0) + "원 입니다.")
                            }
                        }

                    }
                    "reset" -> {
                        if (target != null) {
                            if (player.isOp) {
                                Main.balanceMap[target] = 0
                                player.sendMessage(target.displayName + "의 돈은 " + (Main.balanceMap[target]
                                        ?: 0) + "원 입니다.")
                            } else {
                                player.sendMessage("권한이 부족합니다.")
                            }
                        } else {
                            Main.balanceMap[player] = 0
                            player.sendMessage("당신의 돈은 " + (Main.balanceMap[player] ?: 0) + "원 입니다.")
                        }

                    }
                    "produce" -> {
                        if (player.isOp) {
                            if (target != null) {
                                Main.balanceMap[target] = Main.balanceMap[target]?.plus(10000) ?: 10000
                                player.sendMessage(target.displayName + "의 돈은 " + (Main.balanceMap[target]
                                        ?: 0) + "원 입니다.")
                            } else {
                                Main.balanceMap[player] = Main.balanceMap[player]?.plus(10000) ?: 10000
                                player.sendMessage("당신의 돈은 " + (Main.balanceMap[player] ?: 0) + "원 입니다.")
                            }
                        } else {
                            player.sendMessage("당신은 오피가 아닙니다.")
                        }

                    }
                    else -> {
                        player.sendMessage("매개변수가 부족합니다.")
                    }
                }
                return true
            }
        }

        return false
    }

    fun checkGeneration(player: Player, amount: Int) {
        val paper = ItemStack(Material.PAPER)
        val meta = paper.itemMeta
        meta?.setDisplayName(amount.toString() + "수표")
        meta?.lore = arrayListOf(amount.toString())
        meta?.addEnchant(Enchantment.LURE, 1, false)
        meta?.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        paper.itemMeta = meta
        player.inventory.addItem(paper)
    }

    override fun onTabComplete(commandSender: CommandSender,
                               command: Command, label: String,
                               args: Array<out String>): MutableList<String> {
        if (command.name == "check") {
            if (args.size == 1) {
                var result = Bukkit.getOnlinePlayers().map { player -> player.displayName }.toMutableList()
                result.add("confirm")
                result.add("publish")
                result.add("reset")
                if (commandSender is Player) {
                    val player: Player = commandSender
                    if (player.isOp) {
                        result.add("produce")
                    }
                } else {
                    result.add("produce")
                }
                return result
            } else if (args[0] != "confirm"
                    && args[0] != "publish"
                    && args[0] != "reset"
                    && args[0] != "produce" && args.size == 2) {
                var result: MutableList<String> = mutableListOf()
                result.add("confirm")
                result.add("publish")
                result.add("reset")
                if (commandSender is Player) {
                    val player: Player = commandSender
                    if (player.isOp) {
                        result.add("produce")
                    }
                } else {
                    result.add("produce")
                }
                return result
            }
        }
        return mutableListOf()

    }
}