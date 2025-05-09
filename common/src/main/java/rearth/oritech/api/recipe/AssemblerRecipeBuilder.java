package rearth.oritech.api.recipe;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import rearth.oritech.init.recipes.RecipeContent;

public class AssemblerRecipeBuilder extends OritechRecipeBuilder {

    protected AssemblerRecipeBuilder(RegistryWrapper.WrapperLookup registryLookup) {
        super(registryLookup, RecipeContent.ASSEMBLER, "assembler");
    }

    public static OritechRecipeBuilder build(RegistryWrapper.WrapperLookup registryLookup) {
        return new AssemblerRecipeBuilder(registryLookup);
    }

    @Override
    public void validate(Identifier id) throws IllegalStateException {
        if (inputs == null || inputs.size() != 4)
            throw new IllegalStateException("Need exactly 4 inputs for recipe " + id + " (type " + type + ")");
        if (results == null || results.size() != 1)
            throw new IllegalStateException("Need exactly 1 result for recipe " + id + " (type " + type + ")");
    }        
}
