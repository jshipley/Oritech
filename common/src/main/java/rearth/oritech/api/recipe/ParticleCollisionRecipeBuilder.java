package rearth.oritech.api.recipe;

import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import rearth.oritech.init.recipes.RecipeContent;

public class ParticleCollisionRecipeBuilder extends OritechRecipeBuilder {

    protected ParticleCollisionRecipeBuilder(RegistryWrapper.WrapperLookup registryLookup) {
        super(registryLookup, RecipeContent.PARTICLE_COLLISION, "particle");
    }

    public static OritechRecipeBuilder build(RegistryWrapper.WrapperLookup registryLookup) {
        return new ParticleCollisionRecipeBuilder(registryLookup);
    }

    @Override
    public void validate(Identifier id) throws IllegalStateException {
        if (inputs == null || inputs.size() != 2)
            throw new IllegalStateException("Exactly 2 inputs required for recipe " + id + " (type " + type + ")");
        if (results == null || results.size() != 1)
            throw new IllegalStateException("Exactly 1 result required for recipe " + id + " (type " + type + ")");
    }
}
