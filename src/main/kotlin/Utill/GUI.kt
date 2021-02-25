package Utill

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import java.util.*

abstract class GUI protected constructor(p: Player, name: String?, size: Int) {
    private val inv: Inventory = Bukkit.createInventory(null, size, name!!)
    private var slotMap: MutableMap<Int, String?>?
    protected abstract fun init()
    abstract fun onClick(e: InventoryClickEvent)

    protected fun setItem(
            name: String?,
            lore: List<String?>?,
            m: Material?,
            amount: Int,
            slot: Int,
            value: String?,
            glow: Boolean
    ) {
        val item = ItemStack(m ?: Material.AIR, amount)
        val meta = item.itemMeta
        meta?.setDisplayName(name)
        if (lore != null) meta?.lore = lore
        if (glow) {
            meta?.addEnchant(Enchantment.LURE, 1, false)
            meta?.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }
        item.itemMeta = meta
        slotMap?.set(slot, value)
        inv.setItem(slot, item)
    }

    protected fun getValue(slot: Int): String? {
        return slotMap!!.getOrDefault(slot, null)
    }

    fun closeGUI(e: InventoryCloseEvent) {
        slotMap = null
        guiMap.remove(e.player as Player)
    }

    companion object {
        var WHITE: Short = 0
        var ORANGE: Short = 1
        var MAGENTA: Short = 2
        var LIGHT_BLUE: Short = 3
        var YELLOW: Short = 4
        var LIME: Short = 5
        var PINK: Short = 6
        var GRAY: Short = 7
        var LIGHT_GRAY: Short = 8
        var CYAN: Short = 9
        var PURPLE: Short = 10
        var BLUE: Short = 11
        var BROWN: Short = 12
        var GREEN: Short = 13
        var RED: Short = 14
        var BLACK: Short = 15
        private val guiMap: MutableMap<Player, GUI?> = HashMap()
        fun getGUI(p: Player): GUI? {
            return guiMap.getOrDefault(p, null)
        }
    }

    init {
        slotMap = HashMap()
        init()
        p.openInventory(inv)
        guiMap[p] = this
    }
}