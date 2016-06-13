package tterrag.customthings.common.handlers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;
import tterrag.customthings.common.config.json.BlockType;
import tterrag.customthings.common.config.json.IJsonType;
import tterrag.customthings.common.config.json.items.ItemType;
import tterrag.customthings.common.item.ICustomItem;

public class FuelHandler implements IFuelHandler
{
    @Override
    public int getBurnTime(ItemStack fuel)
    {
        Item item = fuel.getItem();
        if (item instanceof ICustomItem)
        {
            IJsonType type = ((ICustomItem<?>) item).getType(fuel);
            if (type instanceof ItemType)
            {
                return ((ItemType) type).burnTime;
            }
            if (type instanceof BlockType)
            {
                return ((BlockType) type).burnTime;
            }
        }
        return 0;
    }

}
