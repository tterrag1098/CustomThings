package tterrag.customthings.common.item;

import java.util.List;

import lombok.experimental.Delegate;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tterrag.customthings.common.config.json.items.ItemType;

public class ItemCustom extends Item implements ICustomItem<ItemType>
{    
    @Delegate
    private final ItemProxy<ItemType, ItemCustom> proxy = new ItemProxy<ItemType, ItemCustom>(this);

    public ItemCustom()
    {
        super();
        setCreativeTab(CreativeTabs.MISC);
        setHasSubtypes(true);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        List<ItemType> types = ItemType.types;
        for (int i = 0; i < types.size(); i++)
        {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return getType(stack).getUnlocName();
    }

    @Override
    public ItemType getType(ItemStack stack)
    {
        return ItemType.getType(stack.getItemDamage() % ItemType.types.size());
    }
}
