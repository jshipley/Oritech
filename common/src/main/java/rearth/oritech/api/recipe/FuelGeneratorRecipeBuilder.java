package rearth.oritech.api.recipe;

import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import rearth.oritech.init.recipes.RecipeContent;

public class FuelGeneratorRecipeBuilder extends OritechRecipeBuilder {
    private FuelGeneratorRecipeBuilder(RegistryWrapper.WrapperLookup registryLookup) {
        super(registryLookup, RecipeContent.FUEL_GENERATOR, "fuelgen");
    }

    public static OritechRecipeBuilder build(RegistryWrapper.WrapperLookup registryLookup) {
        return new FuelGeneratorRecipeBuilder(registryLookup);
    }

    @Override
    public void validate(Identifier id) throws IllegalStateException {
        if (fluidInput == null || fluidInput.isEmpty())
            throw new IllegalStateException("fluid input required for recipe " + id + " (type " + type + ")");
    }
}
