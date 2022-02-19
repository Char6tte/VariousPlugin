package com.charlotte04.various

import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object EventListener : Listener {

    private val plugin = VariousPlugin.instance

    fun chat(message: String?): String? {
        return message?.let { ChatColor.translateAlternateColorCodes('&', it) }
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player

        event.joinMessage = chat(plugin.config["Message.joinMessage"] as String?)?.replace("%player%", player.name)

        transaction {
            val user = VariousPlugin.User.select { VariousPlugin.User.uuid eq player.uniqueId }
            // ユーザーデータがあればアップデート＆前回のログインを通知
            if (user.count() > 0L) {
                player.sendMessage(
                    "ようこそ！${user.single()[VariousPlugin.User.mcId]}さん！前回のログインは${ChatColor.GREEN}${
                        user.single()[VariousPlugin.User.login].format(
                            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
                        )
                    }${ChatColor.WHITE}です！"
                )
                VariousPlugin.User.update({ VariousPlugin.User.uuid eq player.uniqueId }) {
                    it[login] = LocalDateTime.now()
                }
                VariousPlugin.User.update({ VariousPlugin.User.uuid eq player.uniqueId }) { it[mcId] = player.name }
                return@transaction
            }

            // なければ追加＆初めまして
            VariousPlugin.User.insert {
                it[mcId] = player.name
                it[uuid] = player.uniqueId
                it[login] = LocalDateTime.now()
            }
            player.sendMessage("初めまして${player.name}さん！")

        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
    }
}