package oj.nuoccam.disableenchant.listeners;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import oj.nuoccam.disableenchant.DisableEnchantPlugin;

import java.util.List;
import java.util.Map;

public class EnchantListener implements Listener {

    private boolean isBanned(Enchantment enchantment) {
        // Gọi getYamlConfig() ở đây
        List<String> bannedList = DisableEnchantPlugin.getInstance().getYamlConfig().getStringList("disabled-enchants");
        String key = enchantment.getKey().getKey().toUpperCase();
        return bannedList.contains(key);
    }

    @EventHandler
    public void onEnchantTable(PrepareItemEnchantEvent event) {
        if (event.getEnchanter().hasPermission("disableenchant.bypass")) return;

        for (int i = 0; i < event.getOffers().length; i++) {
            if (event.getOffers()[i] != null) {
                if (isBanned(event.getOffers()[i].getEnchantment())) {
                    event.getOffers()[i] = null;
                }
            }
        }
    }

    @EventHandler
    public void onAnvilPrepare(PrepareAnvilEvent event) {
        ItemStack result = event.getResult();
        if (result == null || result.getType() == Material.AIR) return;

        if (event.getView().getPlayer() instanceof Player player) {
             if (player.hasPermission("disableenchant.bypass")) return;
        }

        boolean shouldBlock = false;
        if (result.getItemMeta() instanceof EnchantmentStorageMeta meta) {
            for (Enchantment en : meta.getStoredEnchants().keySet()) {
                if (isBanned(en)) { shouldBlock = true; break; }
            }
        } else {
            for (Enchantment en : result.getEnchantments().keySet()) {
                if (isBanned(en)) { shouldBlock = true; break; }
            }
        }

        if (shouldBlock) {
            event.setResult(null);
        }
    }
}
