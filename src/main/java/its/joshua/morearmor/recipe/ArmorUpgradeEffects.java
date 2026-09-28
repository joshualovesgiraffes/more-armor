package its.joshua.morearmor.recipe;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

public final class ArmorUpgradeEffects {
    private static final int EFFECT_DURATION_TICKS = 40;
    private static final int EFFECT_REFRESH_INTERVAL_TICKS = 20;

    private ArmorUpgradeEffects() {
    }

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (server.getTickCount() % EFFECT_REFRESH_INTERVAL_TICKS != 0) {
                return;
            }

            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                applyEquippedUpgrades(player);
            }
        });
    }

    private static void applyEquippedUpgrades(ServerPlayer player) {
        Set<String> upgrades = new HashSet<>();
        for (EquipmentSlot slot : new EquipmentSlot[]{
                EquipmentSlot.HEAD,
                EquipmentSlot.CHEST,
                EquipmentSlot.LEGS,
                EquipmentSlot.FEET
        }) {
            ItemStack armor = player.getItemBySlot(slot);
            String upgrade = SmithingUpgradeRecipe.getStoredUpgrade(armor);
            if (!upgrade.isEmpty()) {
                upgrades.add(upgrade);
            }
        }

        for (String upgrade : upgrades) {
            switch (upgrade) {
                case "speed" -> player.addEffect(new MobEffectInstance(MobEffects.SPEED, EFFECT_DURATION_TICKS));
                case "damage" -> player.addEffect(new MobEffectInstance(MobEffects.STRENGTH, EFFECT_DURATION_TICKS));
                case "haste" -> player.addEffect(new MobEffectInstance(MobEffects.HASTE, EFFECT_DURATION_TICKS));
                case "regeneration" -> player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, EFFECT_DURATION_TICKS));
                default -> {
                }
            }
        }
    }
}
