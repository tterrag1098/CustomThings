package tterrag.customthings.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.ArrayUtils;

import tterrag.customthings.common.config.json.items.ToolType;
import tterrag.customthings.common.config.json.items.ToolType.ToolClass;

public class ItemCustomPickaxe extends ItemPickaxe implements ICustomTool
{
    private ToolType type;

    public ItemCustomPickaxe(ToolType type)
    {
        super(type.getToolMaterial());
        this.type = type;
        this.setUnlocalizedName(type.getUnlocName(ToolClass.PICKAXE));
        this.setTextureName(type.getIconName(ToolClass.PICKAXE));
    }

    @Override
    public ToolType getType()
    {
        return type;
    }

    @Override
    public boolean getIsRepairable(ItemStack stack, ItemStack material)
    {
        return repairMatMatchesOredict(stack, material);
    }

    public static boolean repairMatMatchesOredict(ItemStack stack, ItemStack material)
    {
        Item item = stack.getItem();
        if (item instanceof ICustomTool)
        {
            ToolType tool = ((ICustomTool) item).getType();
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
