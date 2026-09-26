package its.joshua.morearmor.item;

import its.joshua.morearmor.MoreArmor;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class moditem {
    public static final Item UPGRADESHARD_SPD = registerItem("upgrade_shard_spd", Item::new);

    private static Item registerItem(String name, Function<Item.Properties, Item> function) {
        return Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(MoreArmor.MOD_ID, name),
                function.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MoreArmor.MOD_ID, name)))));
    }

    public static void registerModItems() {
        MoreArmor.LOGGER.info("Registering Mod Items for " + MoreArmor.MOD_ID);

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(output ->
            {output.accept(UPGRADESHARD_SPD);
        });
    }
}
