package com.charlotte04.various

import org.bukkit.plugin.java.JavaPlugin

class VariousPlugin : JavaPlugin() {

    override fun onEnable() {
        saveDefaultConfig() //Default ConfigFile Export

        reloadConfig()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    @Override
    override fun reloadConfig() {
        super.reloadConfig()
        saveDefaultConfig()
        config.options().copyDefaults(true)
        saveConfig()
    }
}