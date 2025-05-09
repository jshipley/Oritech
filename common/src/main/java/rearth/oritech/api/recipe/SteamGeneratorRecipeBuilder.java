package rearth.oritech.api.recipe;

import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import rearth.oritech.init.recipes.RecipeContent;

public class SteamGeneratorRecipeBuilder extends OritechRecipeBuilder {

    protected SteamGeneratorRecipeBuilder(RegistryWrapper.WrapperLookup registryLookup) {
        super(registryLookup, RecipeContent.STEAM_ENGINE, "steamgen");
    }

    public static OritechRecipeBuilder build(RegistryWrapper.WrapperLookup registryLookup) {
        return new SteamGeneratorRecipeBuilder(registryLookup);
    }

    @Override
    public void validate(Identifier id) throws IllegalStateException {
        if (fluidInput == null || fluidInput.isEmpty())
            throw new IllegalStateException("fluid input required for recipe " + id + " (type " + type + ")");
    }
}
