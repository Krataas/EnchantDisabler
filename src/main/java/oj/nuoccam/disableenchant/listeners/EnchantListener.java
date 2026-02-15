package oj.nuoccam.disableenchant.listeners;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
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

    private final MiniMessage mm = MiniMessage.miniMessage();

    // Helper: Kiểm tra xem Enchantment có bị cấm không
    private boolean isBanned(Enchantment enchantment) {
        List<String> bannedList = DisableEnchantPlugin.getInstance().getConfig().getStringList("disabled-enchants");
        String key = enchantment.getKey().getKey().toUpperCase(); // Lấy tên key (ví dụ: mending)
        
        // Kiểm tra trong list config (đổi sang UpperCase để so sánh không phân biệt hoa thường)
        return bannedList.contains(key);
    }

    // 1. Chặn tại Bàn Phù Phép (Enchanting Table)
    @EventHandler
    public void onEnchantTable(PrepareItemEnchantEvent event) {
        if (event.getEnchanter().hasPermission("disableenchant.bypass")) return;

        // Duyệt qua các offer enchant mà bàn phù phép đưa ra
        // Lưu ý: Offers là mảng cố định size 3.
        for (int i = 0; i < event.getOffers().length; i++) {
            if (event.getOffers()[i] != null) {
                Enchantment offered = event.getOffers()[i].getEnchantment();
                if (isBanned(offered)) {
                    event.getOffers()[i] = null; // Xóa offer này đi
                }
            }
        }
    }

    // 2. Chặn tại Cái Đe (Anvil) - Khi ghép sách hoặc ghép đồ
    @EventHandler
    public void onAnvilPrepare(PrepareAnvilEvent event) {
        ItemStack result = event.getResult();
        
        // Nếu không có kết quả, bỏ qua
        if (result == null || result.getType() == Material.AIR) return;

        // Lấy người xem (Viewer) để check quyền bypass
        // Anvil Inventory có thể có nhiều viewer, ta lấy người đầu tiên (thường là người đang dùng)
        if (event.getView().getPlayer() instanceof Player player) {
             if (player.hasPermission("disableenchant.bypass")) return;
        }

        boolean shouldBlock = false;

        // Trường hợp 1: Sách Phù Phép (Enchanted Book)
        if (result.getItemMeta() instanceof EnchantmentStorageMeta meta) {
            for (Map.Entry<Enchantment, Integer> entry : meta.getStoredEnchants().entrySet()) {
                if (isBanned(entry.getKey())) {
                    shouldBlock = true;
                    break;
                }
            }
        } 
        // Trường hợp 2: Item thường (Kiếm, Giáp...)
        else {
            for (Map.Entry<Enchantment, Integer> entry : result.getEnchantments().entrySet()) {
                if (isBanned(entry.getKey())) {
                    shouldBlock = true;
                    break;
                }
            }
        }

        if (shouldBlock) {
            // Set kết quả về null để không cho phép lấy ra
            event.setResult(null);
            
            // (Optional) Gửi tin nhắn cảnh báo? 
            // Lưu ý: PrepareAnvilEvent trigger liên tục khi gõ tên, nên gửi tin nhắn ở đây sẽ bị spam.
            // Tốt nhất là chỉ chặn kết quả.
        }
    }
}