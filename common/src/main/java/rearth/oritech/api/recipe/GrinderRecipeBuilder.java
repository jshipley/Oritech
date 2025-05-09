package rearth.oritech.api.recipe;

import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import rearth.oritech.init.recipes.RecipeContent;

public class GrinderRecipeBuilder extends OritechRecipeBuilder {

    protected GrinderRecipeBuilder(RegistryWrapper.WrapperLookup registryLookup) {
        super(registryLookup, RecipeContent.GRINDER, "grinder");
    }

    public static OritechRecipeBuilder build(RegistryWrapper.WrapperLookup registryLookup) {
        return new GrinderRecipeBuilder(registryLookup);
    }

    @Override
    public void validate(Identifier id) throws IllegalStateException {
        if ((inputs == null || inputs.isEmpty()) || (results == null || results.isEmpty()))
            throw new IllegalStateException("inputs and results required for recipe " + id + " (type " + type + ")");
    }
    
}
