package rearth.oritech.api.recipe;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import rearth.oritech.init.recipes.RecipeContent;

public class CentrifugeRecipeBuilder extends OritechRecipeBuilder {

    protected CentrifugeRecipeBuilder(RegistryWrapper.WrapperLookup registryLookup) {
        super(registryLookup, RecipeContent.CENTRIFUGE, "centrifuge");
    }

    public static OritechRecipeBuilder build(RegistryWrapper.WrapperLookup registryLookup) {
        return new CentrifugeRecipeBuilder(registryLookup);
    }

    @Override
    public void validate(Identifier id) throws IllegalStateException {
        if ((inputs == null || inputs.isEmpty()) || (results == null || results.isEmpty()))
            throw new IllegalStateException("inputs and results are required for recipe " + id + " (type " + type + ")");
        if (inputs.size() > 1)
            throw new IllegalStateException("too many inputs for recipe " + id + " (type " + type + ")");
    }
}