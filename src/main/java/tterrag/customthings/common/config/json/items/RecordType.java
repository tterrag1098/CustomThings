package tterrag.customthings.common.config.json.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import tterrag.customthings.common.config.json.JsonType;
import tterrag.customthings.common.item.ItemCustomRecord;
import cpw.mods.fml.common.registry.GameRegistry;

public class RecordType extends JsonType
{
    /* JSON Fields @formatter:off */
    public int color = 0xFF70FF;
    public boolean isCreeperLoot = true;
    /* End JSON Fields @formatter:on */
    
    private transient ItemRecord item;
    
    @Override
    public void register()
    {
        item = new ItemCustomRecord(name);
        GameRegistry.registerItem(item, name);
        types.add(this);
    }

    private static final List<RecordType> types = new ArrayList<RecordType>();
    
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

    public static Item getItem(int index)
    {
        return types.get(index % types.size()).item;
    }

    public static RecordType getType(int index)
    {
        return types.get(index);
    }
}
