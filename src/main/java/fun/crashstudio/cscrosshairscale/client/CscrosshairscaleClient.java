package fun.crashstudio.cscrosshairscale.client;

import fun.crashstudio.cscrosshairscale.config.ModConfig;
import net.fabricmc.api.ClientModInitializer;

public class CscrosshairscaleClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModConfig.load();
        CscrosshairscaleKeybinds.register();
    }
}
