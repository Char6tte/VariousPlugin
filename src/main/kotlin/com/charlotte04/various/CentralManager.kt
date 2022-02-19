package com.charlotte04.various

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object CentralManager : CommandExecutor, Listener {

    private val plugin = VariousPlugin.instance

    fun mainManu(player: Player?, size: Int): Inventory {
        val iName = EventListener.chat(plugin.config["GUIName.MainMenu"].toString())
        val inventory = Bukkit.createInventory(player, size, iName.toString())
        val item1 = ItemStack(Material.BLACK_STAINED_GLASS_PANE)
        for (i in 0..26) {
            inventory.setItem(i, item1)
        }

        return inventory
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        sender.sendMessage("Run command")

        val player = sender as Player

        val inv = mainManu(player, 27)
        player.openInventory(inv)
        return true
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {

        val mainMenu = plugin.config["GUIName.MainMenu"].toString()
        //val clickName = event.view.title.equals(mainMenu)
        //plugin.logger.info("ConfigGUIName : $mainMenu , ClickGUIName : $clickName")
        //plugin.logger.info(event.view.type.toString())
        if (event.view.title != mainMenu) {
        } else {
            plugin.logger.info("Cancelll!!!!!!!!")
            event.isCancelled = true
        }
    }

}
