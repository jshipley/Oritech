package rearth.oritech.api.recipe;

import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import rearth.oritech.init.recipes.RecipeContent;

public class LavaGeneratorRecipeBuilder extends OritechRecipeBuilder {

    protected LavaGeneratorRecipeBuilder(RegistryWrapper.WrapperLookup registryLookup) {
        super(registryLookup, RecipeContent.LAVA_GENERATOR, "lavagen");
    }

    public static OritechRecipeBuilder build(RegistryWrapper.WrapperLookup registryLookup) {
        return new LavaGeneratorRecipeBuilder(registryLookup);
    }

    @Override
    public void validate(Identifier id) throws IllegalStateException {
        if (fluidInput == null || fluidInput.isEmpty())
            throw new IllegalStateException("fluid input required for recipe " + id + " (type " + type + ")");
    }
}
