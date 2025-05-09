package rearth.oritech.api.recipe;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import rearth.oritech.init.recipes.RecipeContent;

public class FoundryRecipeBuilder extends OritechRecipeBuilder {
    private static final String resourcePath = "foundry/alloy";
    
    private FoundryRecipeBuilder(RegistryWrapper.WrapperLookup registryLookup) {
        super(registryLookup, RecipeContent.FOUNDRY, resourcePath);
    }

    public static OritechRecipeBuilder build(RegistryWrapper.WrapperLookup registryLookup) {
        return new FoundryRecipeBuilder(registryLookup);
    }

    @Override
    public void validate(Identifier id) throws IllegalStateException {
        if ((inputs == null || inputs.size() < 2) || (results == null || results.isEmpty()))
            throw new IllegalStateException("wrong number of inputs and results for recipe " + id + " (type " + type + ")");
    }
}
