package rearth.oritech.api.recipe.util;

import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import rearth.oritech.Oritech;
import rearth.oritech.api.recipe.AtomicForgeRecipeBuilder;
import rearth.oritech.api.recipe.CentrifugeFluidRecipeBuilder;
import rearth.oritech.api.recipe.CentrifugeRecipeBuilder;
import rearth.oritech.api.recipe.FoundryRecipeBuilder;
import rearth.oritech.api.recipe.GrinderRecipeBuilder;
import rearth.oritech.api.recipe.PulverizerRecipeBuilder;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

import static rearth.oritech.api.recipe.util.RecipeHelpers.of;

public class MetalProcessingChainBuilder {
    private String metalName;
    private String resourcePath = "";
    // ingredient should generally be used for recipe inputs and item for recipe output
    // wherever possible, use ConventionalItemTags (Fabric) or Tags.Items (Neoforge) for ingredients
    private Ingredient ore;
    private Ingredient rawOreIngredient;
    private Item rawOreItem;
    // should be a raw ore, secondary raw ore given when grinding ore blocks
    private Item rawOreByproduct;
    private Ingredient ingotIngredient;
    private Item ingotItem;
    private Ingredient nuggetIngredient;
    private Item nuggetItem;
    private Ingredient clumpIngredient;
    private Item clumpItem;
    private Item smallClumpItem;
    private Item dustItem;
    private Item smallDustItem;
    private Item centrifugeResult;
    private int centrifugeAmount;
    // usually a small dust (or nugget) given as a byproduct from the grinder or centrifuge
    private Item dustByproduct;
    private Item clumpByproduct;
    private int byproductAmount = 3;
    private Ingredient gemIngredient;
    private Item gemItem;
    private Ingredient gemCatalyst;
    private float timeMultiplier = 1f;
    // for compat use. no need to add vanilla processing for other mods' ores
    private boolean vanillaProcessing = false;
    private final RegistryWrapper.WrapperLookup registryLookup;

    private MetalProcessingChainBuilder(RegistryWrapper.WrapperLookup registryLookup, String metalName) {
        this.registryLookup = registryLookup;
        this.metalName = metalName;
    }

    public static MetalProcessingChainBuilder build(RegistryWrapper.WrapperLookup registryLookup, String metalName) {
        return new MetalProcessingChainBuilder(registryLookup, metalName);
    }

    public MetalProcessingChainBuilder resourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
        return this;
    }

    public MetalProcessingChainBuilder ore(Ingredient ore) {
        this.ore = ore;
        return this;
    }

    public MetalProcessingChainBuilder ore(TagKey<Item> oreTag) {
        return ore(of(registryLookup, oreTag));
    }

    public MetalProcessingChainBuilder ore(ItemConvertible ore) {
        return ore(of(ore));
    }

    public MetalProcessingChainBuilder rawOre(Ingredient rawOreIngredient, Item rawOre) {
        this.rawOreIngredient = rawOreIngredient;
        this.rawOreItem = rawOre;
        return this;
    }

    public MetalProcessingChainBuilder rawOre(TagKey<Item> rawOreTag, Item rawOre) {
        return rawOre(of(registryLookup, rawOreTag), rawOre);
    }

    public MetalProcessingChainBuilder rawOre(Item rawOre) {
        return rawOre(of(rawOre), rawOre);
    }

    public MetalProcessingChainBuilder rawOreByproduct(Item byproduct) {
        this.rawOreByproduct = byproduct;
        return this;
    }

    public MetalProcessingChainBuilder ingot(Ingredient ingotIngredient, Item ingot) {
        this.ingotIngredient = ingotIngredient;
        this.ingotItem = ingot;
        return this;
    }

    public MetalProcessingChainBuilder ingot(TagKey<Item> ingotTag, Item ingot) {
        return ingot(of(registryLookup, ingotTag), ingot);
    }

    public MetalProcessingChainBuilder ingot(Item ingot) {
        return ingot(of(ingot), ingot);
    }

    public MetalProcessingChainBuilder nugget(Ingredient nuggetIngredient, Item nugget) {
        this.nuggetIngredient = nuggetIngredient;
        this.nuggetItem = nugget;
        return this;
    }

    public MetalProcessingChainBuilder nugget(TagKey<Item> nuggetTag, Item nugget) {
        return nugget(of(registryLookup, nuggetTag), nugget);
    }

    public MetalProcessingChainBuilder nugget(Item nugget) {
        return nugget(of(nugget), nugget);
    }

    public MetalProcessingChainBuilder clump(Ingredient clumpIngredient, Item clump) {
        this.clumpIngredient = clumpIngredient;
        this.clumpItem = clump;
        return this;
    }

    public MetalProcessingChainBuilder clump(TagKey<Item> clumpTag, Item clump) {
        return clump(of(registryLookup, clumpTag), clump);
    }

    public MetalProcessingChainBuilder clump(Item clump) {
        return clump(of(clump), clump);
    }

    public MetalProcessingChainBuilder smallClump(Item smallClump) {
        this.smallClumpItem = smallClump;
        return this;
    }

    public MetalProcessingChainBuilder centrifugeResult(Item result, int amount) {
        this.centrifugeResult = result;
        this.centrifugeAmount = amount;
        return this;
    }

    public MetalProcessingChainBuilder centrifugeResult(Item result) {
        return centrifugeResult(result, 1);
    }

    public MetalProcessingChainBuilder clumpByproduct(Item byproduct) {
        this.clumpByproduct = byproduct;
        return this;
    }

    public MetalProcessingChainBuilder dustByproduct(Item byproduct) {
        this.dustByproduct = byproduct;
        return this;
    }
    
    public MetalProcessingChainBuilder byproductAmount(int amount) {
        this.byproductAmount = amount;
        return this;
    }

    public MetalProcessingChainBuilder dust(Item dust) {
        this.dustItem = dust;
        return this;
    }

    public MetalProcessingChainBuilder smallDust(Item smallDust) {
        this.smallDustItem = smallDust;
        return this;
    }

    public MetalProcessingChainBuilder gem(Ingredient gemIngredient, Item gem) {
        this.gemIngredient = gemIngredient;
        this.gemItem = gem;
        return this;
    }

    public MetalProcessingChainBuilder gem(TagKey<Item> gemTag, Item gem) {
        return gem(of(registryLookup, gemTag), gem);
    }

    public MetalProcessingChainBuilder gem(Item gem) {
        return gem(of(gem), gem);
    }

    public MetalProcessingChainBuilder gemCatalyst(Ingredient gemCatalyst) {
        this.gemCatalyst = gemCatalyst;
        return this;
    }

    public MetalProcessingChainBuilder gemCatalyst(TagKey<Item> gemCatalyst) {
        return gemCatalyst(of(registryLookup, gemCatalyst));
    }

    public MetalProcessingChainBuilder gemCatalyst(Item gemCatalyst) {
        return gemCatalyst(of(gemCatalyst));
    }

    public MetalProcessingChainBuilder timeMultiplier(float timeMultiplier) {
        this.timeMultiplier = timeMultiplier;
        return this;
    }

    public MetalProcessingChainBuilder vanillaProcessing() {
        this.vanillaProcessing = true;
        return this;
    }

    private void validate(String path) throws IllegalStateException {
        if (ore == null)
            throw new IllegalStateException("ore is required for metal processing chain " + path);
        if (rawOreItem == null)
            throw new IllegalStateException("raw ore is required for metal processing chain " + path);
        if ((dustItem != null || vanillaProcessing == true) && ingotItem == null)
            throw new IllegalStateException("ingot is required if dust is provided or vanilla processing is required for metal processing chain " + path);
        if ((smallClumpItem != null || smallDustItem != null) && nuggetItem == null)
            throw new IllegalStateException("nugget item is required if small clump or small dust are provided for metal processing chain " + path);
        if (centrifugeResult != null && centrifugeAmount < 1)
            throw new IllegalStateException("centrifugeAmount must be >= 1 if centrifugeOutput is provided for metal processing chain " + path);
        if (clumpItem != null && (centrifugeResult == null && gemItem == null))
            throw new IllegalStateException("either centrifugeResult or gemItem is required if clump is provided for metal processing chain " + path);
    }

    public void export(RecipeExporter exporter) {
        validate(resourcePath + "ore/" + metalName);

        // ore block -> raw ores
        PulverizerRecipeBuilder.build(registryLookup).input(ore).result(rawOreItem, 2).timeMultiplier(timeMultiplier).export(exporter, resourcePath + "ore/" + metalName);
        var grinderOreRecipe = GrinderRecipeBuilder.build(registryLookup).input(ore).result(rawOreItem, 2).time(140).timeMultiplier(timeMultiplier);
        if (rawOreByproduct != null)
            grinderOreRecipe.result(rawOreByproduct);
        grinderOreRecipe.export(exporter, resourcePath + "ore/" + metalName);

        // raw ores -> dusts in pulverizer
        if (dustItem != null) {
            PulverizerRecipeBuilder.build(registryLookup)
                .input(rawOreIngredient)
                .result(dustItem)
                .result(firstNonNullOptional(smallDustItem, nuggetItem), 3)
                .timeMultiplier(timeMultiplier)
                .export(exporter, resourcePath + "raw/" + metalName);
        }

        // raw ores -> clumps (falling back to dusts) in grinder
        if (clumpItem != null || dustItem != null) {
            GrinderRecipeBuilder.build(registryLookup)
                .input(rawOreIngredient)
                .result(firstNonNull(clumpItem, dustItem))
                .result(firstNonNullOptional(smallClumpItem, smallDustItem, nuggetItem), 3)
                .result(Optional.fromNullable(clumpByproduct), byproductAmount)
                .time(140).timeMultiplier(timeMultiplier)
                .export(exporter, resourcePath + "raw/" + metalName);
        }

        // clump processing into gems in centrifuge
        if (clumpItem != null) {
            CentrifugeRecipeBuilder.build(registryLookup)
                .input(clumpIngredient)
                .result(firstNonNull(centrifugeResult, gemItem))
                .result(Optional.fromNullable(dustByproduct), byproductAmount)
                .timeMultiplier(timeMultiplier)
                .export(exporter, resourcePath + "clump/" + metalName);
            CentrifugeFluidRecipeBuilder.build(registryLookup)
                .input(clumpIngredient)
                .fluidInput(Fluids.WATER)
                .result(firstNonNull(centrifugeResult, gemItem), 2)
                .time(300).timeMultiplier(timeMultiplier)
                .export(exporter, resourcePath + "clump/" + metalName);
        }

        // gems to dust (doubling)
        if (gemIngredient != null) {
            // atomic forge: 1 gem -> 2 ingots
            AtomicForgeRecipeBuilder.build(registryLookup).input(gemIngredient).input(gemCatalyst).input(gemCatalyst).result(dustItem, 2).time(20).export(exporter, resourcePath + "dust/" + metalName);

            // foundry alternative: 2 gems -> 3 ingots
            FoundryRecipeBuilder.build(registryLookup).input(gemIngredient).input(gemIngredient).result(ingotItem, 3).export(exporter, resourcePath + "gem/" + metalName);
        }

        // ingots/nuggets to dust
        if (dustItem != null)
            RecipeHelpers.addDustRecipe(exporter, ingotIngredient, dustItem, resourcePath + "dust/" + metalName);
        if (smallDustItem != null)
            RecipeHelpers.addDustRecipe(exporter, nuggetIngredient, smallDustItem, resourcePath + "smalldust/" + metalName);
        
        // smelting/compacting
        // Using item instead of ingredient for recipe inputs, as that's what the offerSmelting/offerBlasting methods accept
        // This should be fine, because any mod that adds ores, dusts, etc. will provide their own smelting/blasting recipes
        if (vanillaProcessing) {
            if (dustItem != null) {
                RecipeGenerator.offerSmelting(List.of(dustItem), RecipeCategory.MISC, ingotItem, 1f, 200, Oritech.MOD_ID);
                RecipeGenerator.offerBlasting(List.of(dustItem), RecipeCategory.MISC, ingotItem, 1f, 100, Oritech.MOD_ID);
                RecipeGenerator.offerCompactingRecipe(RecipeCategory.MISC, dustItem, smallDustItem);
            }
            if (smallDustItem != null) {
                RecipeGenerator.offerSmelting(List.of(smallDustItem), RecipeCategory.MISC, nuggetItem, 0.5f, 50, Oritech.MOD_ID);
                RecipeGenerator.offerBlasting(List.of(smallDustItem), RecipeCategory.MISC, nuggetItem, 0.5f, 25, Oritech.MOD_ID);
            }
            if (gemItem != null) {
                RecipeGenerator.offerSmelting(List.of(gemItem), RecipeCategory.MISC, ingotItem, 1f, 200, Oritech.MOD_ID);
                RecipeGenerator.offerBlasting(List.of(gemItem), RecipeCategory.MISC, ingotItem, 1f, 100, Oritech.MOD_ID);
            }
            if (clumpItem != null && smallClumpItem != null)
                RecipeGenerator.offerCompactingRecipe(RecipeCategory.MISC, clumpItem, smallClumpItem);
            if (nuggetItem != null)
                RecipeGenerator.offerCompactingRecipe(RecipeCategory.MISC, ingotItem, nuggetItem);
        }
    }

    private Item firstNonNull(Item... items) {
        return Iterables.find(Arrays.asList(items), Predicates.notNull());
    }

    private Optional<Item> firstNonNullOptional(Item... items) {
        return Iterables.tryFind(Arrays.asList(items), Predicates.notNull());
    }
}
