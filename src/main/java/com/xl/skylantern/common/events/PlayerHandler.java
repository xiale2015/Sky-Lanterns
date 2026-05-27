package com.xl.skylantern.common.events;

import com.xl.skylantern.common.entities.SkyLanternEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber
public class PlayerHandler {
    @SubscribeEvent
    public static void onPlayerInteractEntity(PlayerInteractEvent.EntityInteract event) {
        final Entity target = event.getTarget();
        if (!(target instanceof SkyLanternEntity)) {
            return;
        }
        final Player player = event.getEntity();
        if (!player.isCrouching()) {
            return;
        }
        final SkyLanternEntity lantern = (SkyLanternEntity) target;
        if (lantern.isLatched()) {
            lantern.setUnlatched();
        }
    }
}
