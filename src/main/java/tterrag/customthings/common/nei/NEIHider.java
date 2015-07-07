package tterrag.customthings.common.nei;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import net.minecraft.item.ItemStack;
import tterrag.customthings.common.config.ConfigHandler;
import codechicken.nei.api.API;

import com.enderio.core.common.compat.ICompat;
import com.enderio.core.common.util.ItemUtil;

public class NEIHider implements ICompat
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
            ItemStack stack = (ItemStack) ItemUtil.parseStringIntoRecipeItem(s, true);
            API.hideItem(stack);
        }
    }
}
