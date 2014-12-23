package tterrag.customthings.common.config.json.crafting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import tterrag.core.common.json.JsonUtils;
import tterrag.customthings.common.config.json.IJsonType;
import cpw.mods.fml.common.registry.GameRegistry;

public class ShapelessJsonRecipe implements IJsonType
{
    public String[] input;
    public String output;
    public int outputAmount = 1;

    @Override
    public void register()
    {
        if (this.input == null || this.output == null)
        {
            throw new InvalidShapelessRecipeException((this.input == null ? "Input was null" : "Output was null") + ". You must define this value.");
        }
        
        List<Object> inputs = new ArrayList<Object>();
        for (String input : this.input)
        {
            inputs.add(JsonUtils.parseStringIntoRecipeItem(input));
        }
        
        ItemStack output = (ItemStack) JsonUtils.parseStringIntoRecipeItem(this.output, true);
        
        output.stackSize = this.outputAmount;
        GameRegistry.addRecipe(new ShapelessOreRecipe(output, inputs.toArray()));
    }
    
    @SuppressWarnings("serial")
    private static class InvalidShapelessRecipeException extends RuntimeException
    {
        public InvalidShapelessRecipeException(String text)
        {
            super(text);
        }
    }

    @Override
    public void postInit()
    {
        ;
    }
}
