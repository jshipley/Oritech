package rearth.oritech;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRenderEvents;
import net.minecraft.entity.EquipmentSlot;
import rearth.oritech.client.init.ModRenderers;
import rearth.oritech.client.init.ModScreens;
import rearth.oritech.item.tools.armor.BaseJetpackItem;
import rearth.oritech.item.tools.util.Helpers;

public final class OritechClient {
    
    public static void initialize() {

        Oritech.LOGGER.info("Oritech client initialization");
        ModScreens.assignScreens();
        
        ClientTickEvents.START_CLIENT_TICK.register(Helpers::onClientTickEvent);
        LivingEntityFeatureRenderEvents.ALLOW_CAPE_RENDER.register(player -> !(player.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof BaseJetpackItem));
    }
    
    public static void registerRenderers() {
        
        Oritech.LOGGER.info("Registering oritech renderers");
        ModRenderers.registerRenderers();
    }
}