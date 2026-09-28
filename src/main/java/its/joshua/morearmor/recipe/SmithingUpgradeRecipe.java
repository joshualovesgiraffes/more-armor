package its.joshua.morearmor.recipe;

import com.mojang.serialization.MapCodec;
import its.joshua.morearmor.item.moditem;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleSmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public final class SmithingUpgradeRecipe extends SimpleSmithingRecipe {
    private static final String UPGRADE_TAG = "more_armor_upgrade";
    private static final SmithingUpgradeRecipe INSTANCE = new SmithingUpgradeRecipe();

    public static final RecipeSerializer<SmithingUpgradeRecipe> SERIALIZER = new RecipeSerializer<>(
            MapCodec.unit(INSTANCE),
            StreamCodec.unit(INSTANCE)
    );

    private static final Ingredient SHARDS = Ingredient.of(
            moditem.UPGRADESHARD_SPD,
            moditem.UPGRADESHARD_DMG,
            moditem.UPGRADESHARD_HST,
            moditem.UPGRADESHARD_REG
    );

    private SmithingUpgradeRecipe() {
        super(new Recipe.CommonInfo(false));
    }

    @Override
    public boolean matches(SmithingRecipeInput input, Level level) {
        return isValidInput(input);
    }

    private static boolean isValidInput(SmithingRecipeInput input) {
        return input.template().isEmpty()
                && isArmor(input.base())
                && SHARDS.test(input.addition())
                && !alreadyUpgraded(input.base());
    }

    @Override
    public ItemStack assemble(SmithingRecipeInput input) {
        if (!isValidInput(input)) {
            return ItemStack.EMPTY;
        }

        ItemStack upgradedArmor = input.base().copy();
        String upgradeId = getUpgradeId(input.addition());
        CustomData.update(DataComponents.CUSTOM_DATA, upgradedArmor, tag ->
                tag.putString(UPGRADE_TAG, upgradeId));
        improveDurability(upgradedArmor);
        improveToughness(upgradedArmor);
        return upgradedArmor;
    }

    @Override
    public Optional<Ingredient> templateIngredient() {
        return Optional.empty();
    }

    @Override
    public Ingredient baseIngredient() {
        return Ingredient.of(StreamSupport.stream(
                BuiltInRegistries.ITEM.getTagOrEmpty(ItemTags.TRIMMABLE_ARMOR).spliterator(),
                false
        ).map(Holder::value));
    }

    @Override
    public Optional<Ingredient> additionIngredient() {
        return Optional.of(SHARDS);
    }

    @Override
    protected PlacementInfo createPlacementInfo() {
        return PlacementInfo.createFromOptionals(List.of(
                Optional.empty(),
                Optional.of(baseIngredient()),
                Optional.of(SHARDS)
        ));
    }

    @Override
    public RecipeSerializer<SmithingUpgradeRecipe> getSerializer() {
        return SERIALIZER;
    }

    private static boolean isArmor(ItemStack stack) {
        Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
        if (equippable == null) {
            return false;
        }

        return switch (equippable.slot()) {
            case HEAD, CHEST, LEGS, FEET -> true;
            default -> false;
        };
    }

    private static boolean alreadyUpgraded(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        return customData != null && customData.copyTag().contains(UPGRADE_TAG);
    }

    private static void improveDurability(ItemStack armor) {
        int currentMaxDamage = armor.getMaxDamage();
        armor.set(DataComponents.MAX_DAMAGE, (currentMaxDamage * 3 + 1) / 2);
    }

    private static void improveToughness(ItemStack armor) {
        ItemAttributeModifiers currentModifiers = armor.get(DataComponents.ATTRIBUTE_MODIFIERS);
        if (currentModifiers == null) {
            return;
        }

        List<ItemAttributeModifiers.Entry> improvedModifiers = new ArrayList<>();
        for (ItemAttributeModifiers.Entry entry : currentModifiers.modifiers()) {
            AttributeModifier modifier = entry.modifier();
            if (entry.attribute().equals(Attributes.ARMOR_TOUGHNESS)) {
                modifier = new AttributeModifier(
                        modifier.id(),
                        modifier.amount() * 1.5,
                        modifier.operation()
                );
            }
            improvedModifiers.add(new ItemAttributeModifiers.Entry(
                    entry.attribute(),
                    modifier,
                    entry.slot(),
                    entry.display()
            ));
        }
        armor.set(DataComponents.ATTRIBUTE_MODIFIERS, new ItemAttributeModifiers(improvedModifiers));
    }

    public static String getStoredUpgrade(ItemStack armor) {
        CustomData customData = armor.get(DataComponents.CUSTOM_DATA);
        return customData == null ? "" : customData.copyTag().getString(UPGRADE_TAG).orElse("");
    }

    private static String getUpgradeId(ItemStack shard) {
        if (shard.is(moditem.UPGRADESHARD_SPD)) {
            return "speed";
        }
        if (shard.is(moditem.UPGRADESHARD_DMG)) {
            return "damage";
        }
        if (shard.is(moditem.UPGRADESHARD_HST)) {
            return "haste";
        }
        if (shard.is(moditem.UPGRADESHARD_REG)) {
            return "regeneration";
        }
        throw new IllegalArgumentException("Not an upgrade shard: " + shard);
    }
}
