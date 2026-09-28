package its.joshua.morearmor.recipe;

import its.joshua.morearmor.MoreArmor;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public final class ModRecipes {
    private ModRecipes() {
    }

    public static void register() {
        Registry.register(
                BuiltInRegistries.RECIPE_SERIALIZER,
                MoreArmor.id("smithing_upgrade"),
                SmithingUpgradeRecipe.SERIALIZER
        );
    }
}
