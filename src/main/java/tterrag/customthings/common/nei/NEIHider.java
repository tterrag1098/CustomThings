package tterrag.customthings.common.nei;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import net.minecraft.item.ItemStack;
import tterrag.core.common.compat.ICompatability;
import tterrag.core.common.json.JsonUtils;
import tterrag.customthings.common.config.ConfigHandler;
import codechicken.nei.api.API;

public class NEIHider implements ICompatability
{
    private static List<String> readStrings = new ArrayList<String>();
    public static void load()
    {
        File file = new File(ConfigHandler.baseDir.getAbsolutePath() + "/hiddenItems.txt");

        try
        {
            if (!file.exists())
            {
                file.createNewFile();
            }
            
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine())
                readStrings.add(scanner.nextLine());
            
            scanner.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        
        for (String s : readStrings)
        {
            ItemStack stack = (ItemStack) JsonUtils.parseStringIntoRecipeItem(s, true);
            API.hideItem(stack);
        }
    }
}
