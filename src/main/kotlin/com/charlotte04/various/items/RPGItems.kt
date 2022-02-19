package com.charlotte04.various.items

import com.charlotte04.various.EventListener
import com.charlotte04.various.VariousPlugin
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object RPGItems {

    private val plugin = VariousPlugin.instance

    private fun chat(message: String): String {
        return message.let { ChatColor.translateAlternateColorCodes('&', it) }
    }

    private fun reality(st: String): String {
        when (st) {
            "Heal" -> return chat("&dHealItem")
            "C" -> return chat("Common")
            "R" -> return chat("&aRare")
            "SR" -> return chat("&bSuperRare")
            "SSR" -> return chat("&9SuperSpecialRare")
            "HR" -> return chat("&cHyperRare")
            "UR" -> return chat("&6UltraRare")
            "XR" -> return ""
        }
        return chat("&eCraftingItem")
    }

    fun createTPSword(): ItemStack {
        val targetItem = ItemStack(Material.IRON_SWORD)
        val meta = targetItem.itemMeta
        meta?.setDisplayName(EventListener.chat("&2Teleport Sword"))
        meta?.setCustomModelData(1)
        val lore = ArrayList<String>()
        lore.add(reality("C"))
        lore.add("")
        meta?.lore = lore
        meta?.isUnbreakable
        targetItem.itemMeta = meta

        return targetItem
    }

    fun createnonameItem(): ItemStack {
        val targetItem = ItemStack(Material.APPLE)
        val meta = targetItem.itemMeta

        meta?.setDisplayName(EventListener.chat("&bMed APPLE"))
        meta?.setCustomModelData(1)

        val lore = ArrayList<String>()
        lore.add(reality("Heal"))
        lore.add("")

        meta?.lore = lore
        targetItem.itemMeta = meta

        return targetItem
    }

}