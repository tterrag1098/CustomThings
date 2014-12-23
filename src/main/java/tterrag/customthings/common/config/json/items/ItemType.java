package tterrag.customthings.common.config.json.items;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tterrag.core.common.json.JsonUtils;
import tterrag.customthings.common.config.json.JsonType;
import tterrag.customthings.common.item.ItemCustom;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemType extends JsonType
{
    /* JSON Fields @formatter:off */
    private String container = null; // handled appropriately
    /* End JSON Fields @formatter:on */
    
    @Getter
    private transient ItemStack containerItem = null;
    
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

        if (container != null)
        {
            containerItem = "this".equals(container) ? new ItemStack(item, 1, types.indexOf(this)) : JsonUtils.parseStringIntoItemStack(container);
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
