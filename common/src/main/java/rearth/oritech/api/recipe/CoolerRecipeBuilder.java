package rearth.oritech.api.recipe;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import rearth.oritech.init.recipes.RecipeContent;

public class CoolerRecipeBuilder extends OritechRecipeBuilder {

    protected CoolerRecipeBuilder(RegistryWrapper.WrapperLookup registryLookup) {
        super(registryLookup, RecipeContent.COOLER, "cooler");
    }

    public static OritechRecipeBuilder build(RegistryWrapper.WrapperLookup registryLookup) {
        return new CoolerRecipeBuilder(registryLookup);
    }

    @Override
    public void validate(Identifier id) throws IllegalStateException {
        if (results == null || results.isEmpty())
            throw new IllegalStateException("Results required for recipe " + id + " (type " + type + ")");
        if (fluidInput == null || fluidInput.isEmpty())
            throw new IllegalStateException("Fluid input required for recipe " + id + " (type " + type + ")");
    }
    
}
