package fun.crashstudio.cscrosshairscale.client;

import fun.crashstudio.cscrosshairscale.client.gui.CrosshairScaleConfigScreen;
import fun.crashstudio.cscrosshairscale.config.ModConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

public class CscrosshairscaleKeybinds {
    public static final KeyBinding.Category CATEGORY = KeyBinding.Category.create(Identifier.of("cscrosshairscale", "cscrosshairscale"));

    public static final KeyBinding OPEN_MENU_KEY = new KeyBinding(
            "key.cscrosshairscale.open_menu",
            InputUtil.Type.KEYSYM,
            InputUtil.UNKNOWN_KEY.getCode(),
            CATEGORY
    );

    public static final KeyBinding INCREASE_KEY = new KeyBinding(
            "key.cscrosshairscale.increase",
            InputUtil.Type.KEYSYM,
            InputUtil.UNKNOWN_KEY.getCode(),
            CATEGORY
    );

    public static final KeyBinding DECREASE_KEY = new KeyBinding(
            "key.cscrosshairscale.decrease",
            InputUtil.Type.KEYSYM,
            InputUtil.UNKNOWN_KEY.getCode(),
            CATEGORY
    );

    public static void register() {
        KeyBindingHelper.registerKeyBinding(OPEN_MENU_KEY);
        KeyBindingHelper.registerKeyBinding(INCREASE_KEY);
        KeyBindingHelper.registerKeyBinding(DECREASE_KEY);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.currentScreen != null) {
                return;
            }

            while (OPEN_MENU_KEY.wasPressed()) {
                client.setScreen(new CrosshairScaleConfigScreen(null));
            }

            while (INCREASE_KEY.wasPressed()) {
                ModConfig.get().changeScale(ModConfig.SCALE_STEP);
            }

            while (DECREASE_KEY.wasPressed()) {
                ModConfig.get().changeScale(-ModConfig.SCALE_STEP);
            }
        });
    }
}
