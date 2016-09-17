package tterrag.customthings.common.config.json.items;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.StringUtils;

import tterrag.customthings.common.config.json.JsonType;
import tterrag.customthings.common.item.ItemCustom;

import com.enderio.core.common.util.ItemUtil;

import cpw.mods.fml.common.registry.GameRegistry;

public class ItemType extends JsonType
{
    /* JSON Fields @formatter:off */
    public String   container       = null;
    public String[] oreDictNames    = null;
    public int      maxStackSize    = 64;
    public int      burnTime        = 0;
    public String   rarity          = EnumRarity.common.name();
    public boolean  enchanted       = false;
    /* End JSON Fields @formatter:on */

    @Getter
    private transient ItemStack containerItem = null;
    
    @Getter
    private transient EnumRarity enumRarity = null;

    private static Item theItem;

    public String getUnlocName()
    {
        return "item." + name;
    }

    private static ItemType dummy = new ItemType();
    static
    {
        dummy.name = "broken";
    }

    public static final List<ItemType> types = new ArrayList<ItemType>();

    @Override
    public void register()
    {
        maxStackSize = MathHelper.clamp_int(maxStackSize, 1, 64);
        burnTime = Math.max(0, burnTime);
        enumRarity = EnumRarity.valueOf(rarity);

        if (getClass() == ItemType.class)
        {
            if (getItem() == null)
            {
                theItem = new ItemCustom();
                GameRegistry.registerItem(getItem(), "item");
            }
            types.add(this);

            ItemStack stack = getStack();
            OreDictionary.registerOre("item" + StringUtils.capitalize(name), stack);
            addOreDictNames(stack);
        }
    }

    protected void addOreDictNames(ItemStack stack)
    {
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
            containerItem = "this".equals(container) ? getStack() : ItemUtil.parseStringIntoItemStack(container);
        }
    }

    public ItemStack getStack()
    {
        return new ItemStack(getItem(), 1, types.indexOf(this));
    }

    public static ItemType getType(int damage)
    {
        return damage >= types.size() ? dummy : types.get(damage);
    }

    public static Item getItem()
    {
        return theItem;
    }
}
