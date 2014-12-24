package tterrag.customthings.common.config.json.items;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import tterrag.core.common.json.JsonUtils;
import tterrag.customthings.common.config.json.JsonType;
import tterrag.customthings.common.item.ItemCustom;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemType extends JsonType
{
    /* JSON Fields @formatter:off */
    private String      container       = null;
    private String[]    oreDictNames    = null;
    /* End JSON Fields @formatter:on */

    @Getter
    private transient ItemStack containerItem = null;

    private static Item item;

    public String getUnlocName()
    {
        return "item." + name;
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

        ItemStack stack = getStack();
        OreDictionary.registerOre("item" + StringUtils.capitalize(name), stack);
        if (oreDictNames != null)
        {
            for (String s : oreDictNames)
            {
                OreDictionary.registerOre(s, stack);
            }
        }
    }

    @Override
    public void postInit()
    {
        if (container != null)
        {
            containerItem = "this".equals(container) ? getStack() : JsonUtils.parseStringIntoItemStack(container);
        }
    }

    public ItemStack getStack()
    {
        return new ItemStack(item, 1, types.indexOf(this));
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
