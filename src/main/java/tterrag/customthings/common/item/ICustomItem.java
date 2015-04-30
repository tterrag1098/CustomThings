package tterrag.customthings.common.item;

import net.minecraft.item.ItemStack;
import tterrag.customthings.common.config.json.IJsonType;

public interface ICustomItem<T extends IJsonType>
{
    public T getType(ItemStack stack);
}
