package tterrag.customthings.common.item;

import lombok.experimental.Delegate;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.ArrayUtils;

import tterrag.customthings.common.config.json.IHasMaterial;
import tterrag.customthings.common.config.json.items.ToolType;
import tterrag.customthings.common.config.json.items.ToolType.ToolClass;

public class ItemCustomPickaxe extends ItemPickaxe implements ICustomRepair<ToolType>
{
    private ToolType type;
    
    @Delegate
    private final ItemProxy<ToolType, ItemCustomPickaxe> proxy = new ItemProxy<ToolType, ItemCustomPickaxe>(this);
    
    public ItemCustomPickaxe(ToolType type)
    {
        super(type.getToolMaterial());
        this.type = type;
        this.setUnlocalizedName(type.getUnlocName(ToolClass.PICKAXE));
    }

    @Override
    public ToolType getType(ItemStack stack)
    {
        return type;
    }

    @Override
    public boolean getIsRepairable(ItemStack stack, ItemStack material)
    {
        return repairMatMatchesOredict(stack, material);
    }

    @SuppressWarnings("unchecked")
    public static boolean repairMatMatchesOredict(ItemStack stack, ItemStack material)
    {
        Item item = stack.getItem();
        
        if (item instanceof ICustomRepair<?>)
        {
            IHasMaterial tool = ((ICustomRepair<ToolType>) item).getType(stack);
            ItemStack repairMat = tool.getRepairMat();

            int[] oreIds = OreDictionary.getOreIDs(repairMat);
            int[] oreIdsToMatch = OreDictionary.getOreIDs(material);

            for (int i : oreIdsToMatch)
            {
                if (ArrayUtils.contains(oreIds, i))
                {
                    return true;
                }
            }

            return OreDictionary.itemMatches(repairMat, material, false);
        }
        return false;
    }
}
