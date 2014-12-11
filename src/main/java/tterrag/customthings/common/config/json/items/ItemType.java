package tterrag.customthings.common.config.json.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import tterrag.customthings.common.config.json.JsonType;
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

    @Override
    public void register()
    {
        if (item == null)
        {
            item = new ItemCustom();
            GameRegistry.registerItem(item, "customthings.item");
        }
        
        types.add(this);
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
