package rearth.oritech.api.recipe.util;

import net.minecraft.data.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.data.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import org.jetbrains.annotations.Nullable;
import rearth.oritech.Oritech;
import rearth.oritech.api.recipe.GrinderRecipeBuilder;
import rearth.oritech.api.recipe.PulverizerRecipeBuilder;

import java.util.List;

public class RecipeHelpers {
    
    public static void addDustRecipe(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter, Ingredient ingot, Item dust, String suffix) {
        addDustRecipe(registryLookup, exporter, ingot, dust, null, suffix);
    }
    
    public static void addDustRecipe(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter, Ingredient ingot, Item dust, @Nullable Item ingotSmelted, String suffix) {
        PulverizerRecipeBuilder.build(registryLookup).input(ingot).result(dust).export(exporter, suffix);
        GrinderRecipeBuilder.build(registryLookup).input(ingot).result(dust).time(140).export(exporter, suffix);
        if (ingotSmelted != null) {
            RecipeGenerator.offerSmelting(List.of(dust), RecipeCategory.MISC, ingotSmelted, 1f, 200, Oritech.MOD_ID);
            RecipeGenerator.offerBlasting(List.of(dust), RecipeCategory.MISC, ingotSmelted, 1f, 100, Oritech.MOD_ID);
        }
    }
    
    public static CraftingRecipeJsonBuilder createInsulatedCableRecipe(RegistryWrapper.WrapperLookup registryLookup, RecipeCategory category, Item output, int count, Ingredient input, Ingredient insulation) {
        return ShapedRecipeJsonBuilder.create(registryLookup.getOrThrow(RegistryKeys.ITEM), category, output, count).input('c', input).input('i', insulation).pattern("iii").pattern("ccc").pattern("iii");
    }
    
    public static Ingredient of(ItemConvertible item) {
        return Ingredient.ofItems(item);
    }
    
    public static Ingredient of(RegistryWrapper.WrapperLookup registryLookup, TagKey<Item> itemTag) {
        return Ingredient.fromTag(registryLookup.getOrThrow(RegistryKeys.ITEM).getOrThrow(itemTag));
    }

    public static RegistryKey<Recipe<?>> recipeKey(Identifier id) {
        return RegistryKey.of(RegistryKeys.RECIPE, id);
    }
}