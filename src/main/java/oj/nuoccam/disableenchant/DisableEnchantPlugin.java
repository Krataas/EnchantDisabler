package oj.nuoccam.disableenchant;

import co.aikar.commands.PaperCommandManager;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import oj.nuoccam.disableenchant.commands.MainCommand;
import oj.nuoccam.disableenchant.listeners.EnchantListener;

import java.io.File;
import java.io.IOException;

public class DisableEnchantPlugin extends JavaPlugin {

    @Getter
    private static DisableEnchantPlugin instance;
    
    // Đổi tên từ config thành yamlConfig để tránh trùng với hàm mặc định của Bukkit
    @Getter
    private YamlDocument yamlConfig; 
    
    private PaperCommandManager commandManager;

    @Override
    public void onEnable() {
        instance = this;

        try {
            yamlConfig = YamlDocument.create(new File(getDataFolder(), "config.yml"),
                    getResource("config.yml"),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")).build()
            );
        } catch (IOException e) {
            getLogger().severe("Không thể khởi tạo tệp cấu hình!");
            e.printStackTrace();
        }

        commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new MainCommand());

        getServer().getPluginManager().registerEvents(new EnchantListener(), this);

        getLogger().info("Plugin DisableEnchant đã sẵn sàng!");
    }

    public void reloadPluginConfig() {
        try {
            yamlConfig.reload();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
