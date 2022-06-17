package dev.sefiraat.netheopoiesis.listeners;

import dev.sefiraat.netheopoiesis.managers.MobManager;
import dev.sefiraat.netheopoiesis.utils.Keys;
import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class ManagedMobListener implements Listener {

    @EventHandler
    public void onMobIsNameTagged(@Nonnull PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof LivingEntity livingEntity) {
            final ItemStack stack = event.getPlayer().getInventory().getItem(event.getHand());
            if (stack != null && stack.getType() == Material.NAME_TAG && stack.getItemMeta().hasDisplayName()) {
                MobManager.getInstance().removeMob(livingEntity, false);
            }
        }
    }

    @EventHandler
    public void onMobDies(@Nonnull EntityDeathEvent event) {
        MobManager.getInstance().removeMob(event.getEntity(), false);
    }

    @EventHandler
    public void onChunkUnload(@Nonnull ChunkUnloadEvent event) {
        for (Entity entity : event.getChunk().getEntities()) {
            if (entity instanceof LivingEntity livingEntity
                && MobManager.getInstance().isMobManaged(entity.getUniqueId())
            ) {
                MobManager.getInstance().removeMob(livingEntity, true);
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(@Nonnull PlayerDropItemEvent event) {
        PersistentDataAPI.setString(
            event.getItemDrop(),
            Keys.DROPPED_PLAYER,
            event.getPlayer().getUniqueId().toString()
        );
    }
}
