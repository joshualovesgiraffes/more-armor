package its.joshua.morearmor;

import its.joshua.morearmor.item.moditem;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ConstantValue;

import java.util.Set;

public final class ModLootInjector {
    private static final float DROP_CHANCE = 0.01F;
    private static final Set<ResourceKey<LootTable>> TARGET_LOOT_TABLES = Set.of(
            BuiltInLootTables.END_CITY_TREASURE,
            BuiltInLootTables.ANCIENT_CITY,
            BuiltInLootTables.BASTION_TREASURE,
            BuiltInLootTables.WOODLAND_MANSION,
            BuiltInLootTables.STRONGHOLD_CORRIDOR,
            BuiltInLootTables.STRONGHOLD_CROSSING,
            BuiltInLootTables.STRONGHOLD_LIBRARY,
            BuiltInLootTables.NETHER_BRIDGE,
            BuiltInLootTables.TRIAL_CHAMBERS_REWARD_RARE,
            BuiltInLootTables.TRIAL_CHAMBERS_REWARD_UNIQUE,
            BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS_RARE,
            BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS_UNIQUE,
            BuiltInLootTables.DESERT_PYRAMID,
            BuiltInLootTables.JUNGLE_TEMPLE,
            BuiltInLootTables.BURIED_TREASURE
    );

    public static void register() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (source.isBuiltin() && TARGET_LOOT_TABLES.contains(key)) {
                addUpgradeShardPool(tableBuilder);
            }
        });
    }

    private static void addUpgradeShardPool(LootTable.Builder tableBuilder) {
        tableBuilder.withPool(LootPool.lootPool()
                .setRolls(Holder.<ContextIntProvider>direct(new ConstantValue(1)))
                .when(LootItemRandomChanceCondition.randomChance(DROP_CHANCE))
                .add(LootItem.lootTableItem(moditem.UPGRADESHARD_SPD))
                .add(LootItem.lootTableItem(moditem.UPGRADESHARD_DMG))
                .add(LootItem.lootTableItem(moditem.UPGRADESHARD_HST))
                .add(LootItem.lootTableItem(moditem.UPGRADESHARD_REG)));
    }
}
