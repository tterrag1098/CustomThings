package tterrag.customthings.common.config.json.crafting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import tterrag.customthings.common.config.json.IJsonType;

import com.enderio.core.common.util.ItemUtil;

import cpw.mods.fml.common.registry.GameRegistry;

public class ShapelessJsonRecipe implements IJsonType
{
    public String[] input;
    public String output;
    public int outputAmount = 1;

    public ShapelessOreRecipe createRecipe()
    {
        if (this.input == null || this.output == null)
        {
            throw new InvalidShapelessRecipeException((this.input == null ? "Input was null" : "Output was null") + ". You must define this value.");
        }

        List<Object> inputs = new ArrayList<Object>();
        for (String input : this.input)
        {
            inputs.add(ItemUtil.parseStringIntoRecipeItem(input));
        }

        ItemStack output = (ItemStack) ItemUtil.parseStringIntoRecipeItem(this.output, true);

        output.stackSize = this.outputAmount;
        return new ShapelessOreRecipe(output, inputs.toArray());
    }

    @Override
    public void register()
    {
        GameRegistry.addRecipe(createRecipe());
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
