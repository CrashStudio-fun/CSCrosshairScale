package fun.crashstudio.cscrosshairscale.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import fun.crashstudio.cscrosshairscale.client.gui.CrosshairScaleConfigScreen;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return CrosshairScaleConfigScreen::new;
    }
}
