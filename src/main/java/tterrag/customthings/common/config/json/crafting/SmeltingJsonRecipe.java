package tterrag.customthings.common.config.json.crafting;

import net.minecraft.item.ItemStack;
import tterrag.core.common.json.JsonUtils;
import tterrag.customthings.common.config.json.IJsonType;
import cpw.mods.fml.common.registry.GameRegistry;

public class SmeltingJsonRecipe implements IJsonType
{
    public String input, output;
    public int outputAmount = 1;
    public float xp = 0;

    @Override
    public void register()
    {
        if (this.input == null || this.output == null)
        {
            throw new InvalidSmeltingRecipeException((this.input == null ? "Input was null" : "Output was null") + ". You must define this value.");
        }
        
        ItemStack input = (ItemStack) JsonUtils.parseStringIntoRecipeItem(this.input, true);
        ItemStack output = (ItemStack) JsonUtils.parseStringIntoRecipeItem(this.output, true);

        output.stackSize = this.outputAmount;
        
        if (input != null && output != null)
        {
            GameRegistry.addSmelting(input, output, this.xp);
        }
    }

    @SuppressWarnings("serial")
    private static class InvalidSmeltingRecipeException extends RuntimeException
    {
        public InvalidSmeltingRecipeException(String text)
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
