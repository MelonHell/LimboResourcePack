package ru.melonhell.limboresourcepack

import com.velocitypowered.api.command.SimpleCommand
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor

class LimboResourcePackCommand(private val plugin: LimboResourcePackPlugin) : SimpleCommand {
    override fun execute(invocation: SimpleCommand.Invocation?) {
        val executor = invocation?.source() ?: return
        if(executor.hasPermission("limboresourcepack.reload")) {
            executor.sendMessage(Component.text().color(TextColor.color(0, 255, 0)).content("Плагин перезагружается"))
            plugin.reloadConfig()
            executor.sendMessage(Component.text().color(TextColor.color(0, 255, 0)).content("Плагин перезагружен"))
        }
    }

}