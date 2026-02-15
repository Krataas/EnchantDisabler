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
    @Description("Tải lại file cấu hình")
    public void onReload(CommandSender sender) {
        DisableEnchantPlugin.getInstance().reloadPluginConfig();
        
        String prefix = DisableEnchantPlugin.getInstance().getConfig().getString("messages.prefix");
        String msg = DisableEnchantPlugin.getInstance().getConfig().getString("messages.reload");
        
        // Sử dụng MiniMessage (Paper API 1.21) để hỗ trợ màu RGB/Gradient
        sender.sendMessage(MiniMessage.miniMessage().deserialize(prefix + msg));
    }
}