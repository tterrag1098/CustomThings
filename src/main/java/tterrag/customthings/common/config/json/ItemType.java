package tterrag.customthings.common.config.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.item.Item;
import tterrag.customthings.common.item.ItemCustom;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemType extends JsonType
{
    private static Item item;

    public String getUnlocName()
    {
        return "item." +  name;
    }

    private static final List<ItemType> types = new ArrayList<ItemType>();

    public static void addType(ItemType type)
    {
        types.add(type);
    }

    public static void addAll(Collection<? extends ItemType> col)
    {
        for (ItemType type : col)
        {
            addType(type);
        }
    }

    @Override
    public void register()
    {
        if (item == null)
        {
            item = new ItemCustom();
            GameRegistry.registerItem(item, "customthings.item");
        }
        else
        {
            throw new IllegalStateException("ItemType can only be registered once.");
        }
    }

    public static ItemType getType(int damage)
    {
        return damage >= types.size() ? null : types.get(damage);
    }

    public static List<ItemType> getTypes()
    {
        return types;
    }
}
