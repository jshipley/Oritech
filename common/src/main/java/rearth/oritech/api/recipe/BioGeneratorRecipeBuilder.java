package rearth.oritech.api.recipe;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import rearth.oritech.init.recipes.RecipeContent;

public class BioGeneratorRecipeBuilder extends OritechRecipeBuilder {

    protected BioGeneratorRecipeBuilder(RegistryWrapper.WrapperLookup registryLookup) {
        super(registryLookup, RecipeContent.BIO_GENERATOR, "biogen");
    }

    public static OritechRecipeBuilder build(RegistryWrapper.WrapperLookup registryLookup) {
        return new BioGeneratorRecipeBuilder(registryLookup);
    }

    @Override
    public void validate(Identifier id) throws IllegalStateException {
        if (inputs == null || inputs.isEmpty())
            throw new IllegalStateException("inputs required for recipe " + id + " (type " + type + ")");
    }
}
