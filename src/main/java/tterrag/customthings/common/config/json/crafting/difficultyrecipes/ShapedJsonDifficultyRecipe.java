package tterrag.customthings.common.config.json.crafting.difficultyrecipes;

import java.util.EnumMap;
import java.util.Map.Entry;

import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.oredict.ShapedOreRecipe;
import tterrag.customthings.common.config.json.IJsonType;
import tterrag.customthings.common.config.json.crafting.ShapedJsonRecipe;
import tterrag.difficultyrecipes.recipes.DifficultyRecipe.Builder;
import tterrag.difficultyrecipes.recipes.ShapedDifficultyRecipe;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional.Method;

public class ShapedJsonDifficultyRecipe implements IJsonType
{
    private EnumMap<EnumDifficulty, ShapedJsonRecipe> recipes;
    private EnumDifficulty defaultDiff = null;

    @Override
    public void register()
    {
        if (Loader.isModLoaded("difficultyrecipes"))
        {
            _register();
        }
    }

    @Method(modid = "difficultyrecipes")
    private void _register()
    {
        Builder<ShapedOreRecipe> builder = ShapedDifficultyRecipe.builder();
        for (Entry<EnumDifficulty, ShapedJsonRecipe> e : recipes.entrySet())
        {
            builder.addRecipe(e.getKey(), e.getValue().createRecipe());
        }
        builder.setDefault(defaultDiff).build();
    }

    @Override
    public void postInit()
    {
    }
}
