package com.charlotte04.various.items

import com.charlotte04.various.EventListener
import com.charlotte04.various.VariousPlugin
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object TradeItem : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val player = sender as Player
        val inv = mainManu(player, 9)

        player.openInventory(inv)
        return true
    }

    private val plugin = VariousPlugin.instance

    private fun mainManu(player: Player?, size: Int): Inventory {
        val iName = EventListener.chat(plugin.config["GUIName.MainMenu"].toString())
        val inventory = Bukkit.createInventory(player, size, iName.toString())
        val item1 = ItemStack(Material.BLACK_STAINED_GLASS_PANE)
        for (i in 0..8) {
            if (i != 4) {
                inventory.setItem(i, item1)
            }
        }
        return inventory
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {

        val mainMenu = plugin.config["GUIName.MainMenu"].toString()
        //val clickName = event.view.title.equals(mainMenu)
        //plugin.logger.info("ConfigGUIName : $mainMenu , ClickGUIName : $clickName")
        //plugin.logger.info(event.view.type.toString())
        plugin.logger.info("SlotId?: ${event.slot.toString()} ")
        if (event.view.title != mainMenu) {
        } else {
            plugin.logger.info("SlotId?: ${event.rawSlot.toString()} ")
            plugin.logger.info("Cancelll!!!!!!!!")
            event.isCancelled = true
        }
    }

}