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
    @Getter
    private YamlDocument config;
    private PaperCommandManager commandManager;

    @Override
    public void onEnable() {
        instance = this;

        // 1. Khởi tạo Config (BoostedYAML)
        try {
            config = YamlDocument.create(new File(getDataFolder(), "config.yml"),
                    getResource("config.yml"),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")).build()
            );
        } catch (IOException e) {
            getLogger().severe("Không thể tải config.yml!");
            e.printStackTrace();
        }

        // 2. Đăng ký lệnh (ACF)
        commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new MainCommand());

        // 3. Đăng ký Listener
        getServer().getPluginManager().registerEvents(new EnchantListener(), this);

        getLogger().info("Plugin đã khởi động thành công trên phiên bản 1.21.x!");
    }

    @Override
    public void onDisable() {
        if (commandManager != null) {
            commandManager.unregisterCommands();
        }
    }

    public void reloadPluginConfig() {
        try {
            config.reload();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}