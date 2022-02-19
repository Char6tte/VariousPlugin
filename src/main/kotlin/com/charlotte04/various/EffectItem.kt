package com.charlotte04.various

import com.charlotte04.various.items.RPGItems.createTPSword
import com.charlotte04.various.items.RPGItems.createnonameItem
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

object EffectItem : CommandExecutor, Listener {

    private val plugin = VariousPlugin.instance

    private fun eItems(name: String): ItemStack {
        when (name) {
            "TPSword" -> return createTPSword()
            "MedApple" -> return createnonameItem()
        }
        return ItemStack(Material.STICK)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val player = sender as Player
        if (args.isNotEmpty())// サブコマンドの解析
            return when (args[0]) {
                // effectItem TPSwordコマンドの処理
                "TPSword" -> {
                    player.inventory.addItem(eItems("TPSword"))
                    true
                }
                "med" -> {
                    player.inventory.addItem(eItems("MedApple"))
                    true
                }
                else -> {
                    // effectItemのパラメータが意図しないものであれば復帰値にfalseを返す
                    sender.sendMessage(args[0])
                    true
                }
            }
        return true
    }

    @EventHandler
    fun onPlayerDamage(event: EntityDamageByEntityEvent) {
        if (event.damager.type == EntityType.PLAYER && event.entity.type == EntityType.PLAYER) {
            val player = event.entity as Player
            val plDamage = event.damager as Player
            val itemName = plDamage.inventory.itemInMainHand.itemMeta?.displayName.toString()

            player.sendMessage("$plDamage から")

            if (itemName == EventListener.chat("&2Teleport Sword")) {
                randomTeleport(player, 10)
                player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, (5.0).toFloat(), (5.0).toFloat())
                player.playSound(plDamage.location, Sound.ENTITY_PLAYER_LEVELUP, (5.0).toFloat(), (5.0).toFloat())
            }

            plugin.logger.info("Player to Player damage")


        }
    }

    private fun getGroundPos(loc: Location): Int {

        // 最も高い位置にある非空気ブロックを取得
        var loc: Location = loc
        loc = loc.world?.getHighestBlockAt(loc)?.location!!

        // 最後に見つかった地上の高さ
        var ground: Int = loc.blockY

        // 下に向かって探索
        for (y in loc.blockY downTo 1) {
            // 座標をセット
            loc.y = y.toDouble()

            // そこは太陽光が一定以上届く場所で、非固体ブロックで、ひとつ上も非固体ブロックか
            if (loc.block.lightFromSky >= 8 && !loc.block.type.isSolid
                && !loc.clone().add(0.0, 1.0, 0.0).block.type.isSolid
            ) {
                // 地上の高さとして記憶しておく
                ground = y
            }
        }

        // 地上の高さを返す
        return ground
    }

    private fun randomTeleport(p: Player, width: Int) {
        // 乱数ジェネレーター
        val rand: Random = Random

        // 無限ループにならないよう５回上限で繰り返し安全な場所を探す
        for (n in 0..4) {

            // 現在のプレイヤーの座標を取得
            val loc: Location = p.location

            // X,Z方向に-width～widthメートルの範囲でランダムに移動
            loc.add(
                (rand.nextInt(width * 2) - width).toDouble(), 0.0, ((rand.nextInt(width * 2) - width).toDouble())
            )

            // その場所の地上の座標を取得
            val y: Int = getGroundPos(loc)
            loc.y = y.toDouble()

            // その場所は液体か、または下が葉ブロックか
            val under: Location = loc.clone().add(0.0, -1.0, 0.0)
            if ((loc.block.isLiquid
                        || (under.block.type == Material.JUNGLE_LEAVES))
            ) {

                // 水上・マグマ上・樹の上へのテレポートは安全ではないので、別の場所を探す
                continue
            }

            // テレポートして処理終了
            p.teleport(loc)
            return
        }

        // テレポートできずにループが終了した場合メッセージを送る
        p.sendMessage("§e[Teleport] 安全な場所が見つからなかった。")
    }
}
