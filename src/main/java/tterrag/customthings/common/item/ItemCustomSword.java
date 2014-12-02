package tterrag.customthings.common.item;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import tterrag.customthings.common.config.json.ToolType;
import tterrag.customthings.common.config.json.ToolType.ToolClass;

public class ItemCustomSword extends ItemSword implements ICustomTool
{
    private ToolType type;
    
    public ItemCustomSword(ToolType type)
    {
        super(type.getToolMaterial());
        this.type = type;
        this.setUnlocalizedName(type.getUnlocName(ToolClass.SWORD));
        this.setTextureName(type.getIconName(ToolClass.SWORD));
    }
    
    @Override
    public ToolType getType()
    {
        return type;
    }

    @Override
    public boolean getIsRepairable(ItemStack stack, ItemStack material)
    {
        return ItemCustomPickaxe.repairMatMatchesOredict(stack, material);
    }
}
