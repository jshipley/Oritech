package rearth.oritech.api.recipe;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import rearth.oritech.init.recipes.RecipeContent;

public class AtomicForgeRecipeBuilder extends OritechRecipeBuilder {
    private AtomicForgeRecipeBuilder(RegistryWrapper.WrapperLookup registryLookup) {
        super(registryLookup, RecipeContent.ATOMIC_FORGE, "atomicforge");
    }

    public static OritechRecipeBuilder build(RegistryWrapper.WrapperLookup registryLookup) {
        return new AtomicForgeRecipeBuilder(registryLookup);
    }

    public void validate(Identifier id) throws IllegalStateException {

    }
}