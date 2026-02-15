package oj.nuoccam.disableenchant.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import oj.nuoccam.disableenchant.DisableEnchantPlugin;

@CommandAlias("disableenchant|de")
@CommandPermission("disableenchant.admin")
public class MainCommand extends BaseCommand {

    @Subcommand("reload")
    @Description("Làm mới cấu hình hệ thống")
    public void onReload(CommandSender sender) {
        DisableEnchantPlugin.getInstance().reloadPluginConfig();
        
        // Gọi getYamlConfig() thay vì getConfig()
        String prefix = DisableEnchantPlugin.getInstance().getYamlConfig().getString("messages.prefix");
        String msg = DisableEnchantPlugin.getInstance().getYamlConfig().getString("messages.reload");
        
        sender.sendMessage(MiniMessage.miniMessage().deserialize(prefix + msg));
    }
}
