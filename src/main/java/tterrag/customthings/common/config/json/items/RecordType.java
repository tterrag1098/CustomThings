package tterrag.customthings.common.config.json.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.StringUtils;

import tterrag.customthings.common.item.ItemCustomRecord;
import cpw.mods.fml.common.registry.GameRegistry;

public class RecordType extends ItemType
{
    /* JSON Fields @formatter:off */
    public int color = 0xFF70FF;
    public boolean isCreeperLoot = true;
    /* End JSON Fields @formatter:on */
    
    private transient ItemRecord item;
    
    @Override
    public void register()
    {
    	// Max stack size must be 1 or we get a dupe bug, so ignore json value
    	maxStackSize = 1;
    	
        super.register();
        item = new ItemCustomRecord(this);
        GameRegistry.registerItem(item, name);
        types.add(this);
        OreDictionary.registerOre("record", item);
        OreDictionary.registerOre("record" + StringUtils.capitalize(name), item);
        addOreDictNames(new ItemStack(item));
    }

    public static final List<RecordType> types = new ArrayList<RecordType>();
    
    public static int getColor(Item item)
    {
        for (RecordType type : types)
        {
            if (type.item == item)
            {
                return type.color;
            }
        }
        
        return 0xFFFFFF;
    }

    public static int getNumRecords()
    {
        return types.size();
    }
    
    public static int getLootRecordCount()
    {
        int count = 0;
        for (RecordType type : types)
        {
            count += type.isCreeperLoot ? 1 : 0;
        }
        return count;
    }

    public static Item getItem(int index)
    {
        return types.get(index % types.size()).item;
    }

    public static RecordType getType(int index)
    {
        return types.get(index);
    }
}
