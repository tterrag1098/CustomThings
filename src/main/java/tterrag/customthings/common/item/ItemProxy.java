package tterrag.customthings.common.item;

import lombok.RequiredArgsConstructor;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tterrag.customthings.common.config.json.items.ItemType;

@RequiredArgsConstructor
public class ItemProxy<J extends ItemType, T extends Item & ICustomItem<J>>
{
    public static <J extends ItemType, T extends Item & ICustomItem<J>> ItemProxy<J, T> dummy()
    {
        return new ItemProxy<J, T>(null);
    }
    
    private final T item;

    private J getType(ItemStack stack)
    {
        return item.getType(stack);
    }

    public int getItemStackLimit(ItemStack stack)
    {
        return getType(stack).maxStackSize;
    }

    public boolean hasContainerItem(ItemStack stack)
    {
        return getType(stack).getContainerItem() != null;
    }

    public ItemStack getContainerItem(ItemStack stack)
    {
        ItemStack container = getType(stack).getContainerItem();
        if (container != null)
        {
            return container.copy();
        }
        return null;
    }
    
    public EnumRarity getRarity(ItemStack stack)
    {
        return getType(stack).getEnumRarity();
    }
    
    public boolean hasEffect(ItemStack stack)
    {
        return getType(stack).enchanted;
    }
}
