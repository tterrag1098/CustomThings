package tterrag.customthings.common.compat;

import lombok.experimental.UtilityClass;
import cpw.mods.fml.common.Loader;

@UtilityClass
public class CompatUtil
{
    private static boolean difficultyRecipesChecked, difficultyRecipesLoaded;

    public static boolean isDifficultyRecipesLoaded()
    {
        if (!difficultyRecipesChecked)
        {
            difficultyRecipesLoaded = Loader.isModLoaded("difficultyrecipes");
            difficultyRecipesChecked = true;
        }
        return difficultyRecipesLoaded;
    }
}
