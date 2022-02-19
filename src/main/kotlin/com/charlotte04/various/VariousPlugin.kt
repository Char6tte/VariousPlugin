package com.charlotte04.various

import com.charlotte04.various.items.TradeItem
import org.bukkit.command.CommandExecutor
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.transaction


class VariousPlugin : JavaPlugin() {

    companion object {
        lateinit var instance: Plugin
            private set
    }

    override fun onEnable() {

        instance = this

        saveDefaultConfig() //Default ConfigFile Export
        reloadConfig()

        val hostUrl = config["Mysql.hostUrl"]
        val user = config["Mysql.user"]
        val password = config["Mysql.password"]

        Database.connect(
            "jdbc:mysql://$hostUrl",
            driver = "com.mysql.jdbc.Driver",
            user = "$user",
            password = "$password"
        )
        transaction {
            // データを作成
            SchemaUtils.create(User)
        }

        registerCommand("various", CentralManager)
        registerCommand("effect_item", EffectItem)
        registerCommand("trade", TradeItem)

        server.pluginManager.registerEvents(EventListener, this)
        server.pluginManager.registerEvents(CentralManager, this)
        server.pluginManager.registerEvents(EffectItem, this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    object User : Table() {
        val mcId = varchar("mcId", 16)
        val uuid = uuid("uuid")
        val login = datetime("login")
    }

    private fun registerCommand(name: String, executor: CommandExecutor) {
        getCommand(name)?.run {
            setExecutor(executor)
            logger.info("/$name を登録しました")
        } ?: logger.severe("/$name を登録できませんでした")
    }
}
