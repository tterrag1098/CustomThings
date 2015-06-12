package tterrag.customthings.common.compat;

import java.lang.reflect.Type;

import lombok.experimental.UtilityClass;
import tterrag.difficultyrecipes.util.Difficulty;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

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

    public static void registerDifficultyAdapter(GsonBuilder builder)
    {
        builder.registerTypeAdapter(Difficulty.class, new JsonDeserializer<Difficulty>()
        {
            @Override
            public Difficulty deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
            {
                if (json.isJsonPrimitive())
                {
                    String name = json.getAsString();
                    Difficulty diff = Difficulty.valueOf(name);
                    return diff;
                }
                return null;
            }
        });
    }
}
