package rearth.oritech.init.recipes;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import rearth.oritech.init.ItemContent;
import rearth.oritech.util.SizedIngredient;

import java.util.List;

public class AugmentRecipe implements Recipe<RecipeInput> {
    
    private final AugmentRecipeType type;
    private final List<SizedIngredient> researchCost;
    private final List<SizedIngredient> applyCost;
    private final List<Identifier> requirements;
    private final Identifier requiredStation;
    private final int uiX;
    private final int uiY;
    private final int time;
    private final long rfCost;

    public static final AugmentRecipe DUMMY = new AugmentRecipe(null, RecipeContent.AUGMENT, List.of(new SizedIngredient(1, Ingredient.ofItems(ItemContent.NICKEL_DUST))), List.of(new SizedIngredient(1, Ingredient.ofItems(ItemContent.NICKEL_DUST))), List.of(), Identifier.of(""), -1, -1, -1, -1);
    
    public AugmentRecipe(RegistryWrapper.WrapperLookup registryLookup, AugmentRecipeType type, List<SizedIngredient> inputs, List<SizedIngredient> applyCost, List<Identifier> requirements, Identifier requiredStation, int uiX, int uiY, int time, long rfCost) {
        this.type = type;
        this.researchCost = inputs;
        this.applyCost = applyCost;
        this.requirements = requirements;
        this.requiredStation = requiredStation;
        this.uiX = uiX;
        this.uiY = uiY;
        this.time = time;
        this.rfCost = rfCost;
    }
    
    @Override
    public boolean matches(RecipeInput input, World world) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ItemStack craft(RecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return ItemStack.EMPTY;
    }
    
    @Override
    public boolean fits(int width, int height) {
        return false;
    }
    
    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return ItemStack.EMPTY;
    }
    
    @Override
    public RecipeSerializer<? extends Recipe<RecipeInput>> getSerializer() {
        return type;
    }
    
    @Override
    public RecipeType<? extends Recipe<RecipeInput>> getType() {
        return type;
    }
    
    public AugmentRecipeType getOriType() {
        return type;
    }
    
    public List<SizedIngredient> getResearchCost() {
        return researchCost;
    }
    
    public List<SizedIngredient> getApplyCost() {
        return applyCost;
    }
    
    public long getRfCost() {
        return rfCost;
    }
    
    public int getTime() {
        return time;
    }
    
    public Identifier getRequiredStation() {
        return requiredStation;
    }
    
    public List<Identifier> getRequirements() {
        return requirements;
    }
    
    public int getUiX() {
        return uiX;
    }
    
    public int getUiY() {
        return uiY;
    }
    
    @Override
    public String toString() {
        return "AugmentRecipe{" +
                 "type=" + type +
                 ", inputs=" + researchCost +
                 ", time=" + time +
                 '}';
    }
}
