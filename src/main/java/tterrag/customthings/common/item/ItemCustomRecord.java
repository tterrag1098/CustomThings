package tterrag.customthings.common.item;

import lombok.experimental.Delegate;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import tterrag.customthings.CustomThings;
import tterrag.customthings.common.config.json.items.RecordType;

public class ItemCustomRecord extends ItemRecord implements ICustomItem<RecordType>
{
    @Delegate
    private final ItemProxy<RecordType, ItemCustomRecord> proxy = new ItemProxy<RecordType, ItemCustomRecord>(this);
    
    private RecordType type;
    
    public ItemCustomRecord(RecordType recordType)
    {
        super(CustomThings.MODID + "." + recordType.name, null);
    	this.type = recordType;

        setUnlocalizedName(CustomThings.MODID + ".record." + recordType.name);
    }

//    @Override
//    public boolean requiresMultipleRenderPasses()
//    {
//        return true;
//    }
//    
//    @Override
//    public int getRenderPasses(int metadata)
//    {
//        return 2;
//    }
//
//    @Override
//    public int getColorFromItemStack(ItemStack stack, int pass)
//    {
//        return pass == 0 ? 0xFFFFFF : RecordType.getColor(stack.getItem());
//    }

    @Override
    public RecordType getType(ItemStack stack)
    {
        return type;
    }
}
