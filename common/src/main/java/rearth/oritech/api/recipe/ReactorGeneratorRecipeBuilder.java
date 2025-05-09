package rearth.oritech.api.recipe;

import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import rearth.oritech.init.recipes.RecipeContent;

public class ReactorGeneratorRecipeBuilder extends OritechRecipeBuilder {

    protected ReactorGeneratorRecipeBuilder(RegistryWrapper.WrapperLookup registryLookup) {
        super(registryLookup, RecipeContent.REACTOR, "reactorgen");
    }

    public static OritechRecipeBuilder build(RegistryWrapper.WrapperLookup registryLookup) {
        return new ReactorGeneratorRecipeBuilder(registryLookup);
    }

    @Override
    public void validate(Identifier id) throws IllegalStateException {
        if (inputs == null || inputs.isEmpty())
            throw new IllegalStateException("Input required for recipe " + id + " (type " + type + ")");
    }
}
