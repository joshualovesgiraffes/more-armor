package its.joshua.morearmor.creativemodetab;

import its.joshua.morearmor.MoreArmor;
import its.joshua.morearmor.item.moditem;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTabs {
    public static final CreativeModeTab MOD_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            Identifier.fromNamespaceAndPath(MoreArmor.MOD_ID, "tab_more_armor"),
            FabricCreativeModeTab.builder().icon(() -> new ItemStack(moditem.UPGRADESHARD_SPD))
                    .title(Component.translatable("creativemodetab.MoreArmor.tab_more_armor"))
                    .displayItems((parameters, output) -> {
                        output.accept(moditem.UPGRADESHARD_SPD);
                    }).build());
    public static void registerModCreativeModeTabs() {
        MoreArmor.LOGGER.info("Registering Creative Mode Tabs for " + MoreArmor.MOD_ID);
    }
}
