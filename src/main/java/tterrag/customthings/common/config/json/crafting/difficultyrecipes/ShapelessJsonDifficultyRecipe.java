//package tterrag.customthings.common.config.json.crafting.difficultyrecipes;
//
//import java.util.EnumMap;
//import java.util.Map.Entry;
//
//import net.minecraftforge.oredict.ShapelessOreRecipe;
//import tterrag.customthings.common.config.json.IJsonType;
//import tterrag.customthings.common.config.json.crafting.ShapelessJsonRecipe;
//import tterrag.difficultyrecipes.recipes.DifficultyRecipe.Builder;
//import tterrag.difficultyrecipes.recipes.ShapelessDifficultyRecipe;
//import tterrag.difficultyrecipes.util.Difficulty;
//import cpw.mods.fml.common.Loader;
//import cpw.mods.fml.common.Optional.Method;
//
//public class ShapelessJsonDifficultyRecipe implements IJsonType
//{
//    private EnumMap<Difficulty, ShapelessJsonRecipe> recipes;
//    private Difficulty defaultDiff = null;
//
//    @Override
//    public void register()
//    {
//        if (Loader.isModLoaded("difficultyrecipes"))
//        {
//            _register();
//        }
//    }
//
//    @Method(modid = "difficultyrecipes")
//    private void _register()
//    {
//        Builder<ShapelessOreRecipe> builder = ShapelessDifficultyRecipe.builder();
//        for (Entry<Difficulty, ShapelessJsonRecipe> e : recipes.entrySet())
//        {
//            builder.addRecipe(e.getKey(), e.getValue().createRecipe());
//        }
//        builder.setDefault(defaultDiff).build();
//    }
//
//    @Override
//    public void postInit()
//    {
//    }
//}
