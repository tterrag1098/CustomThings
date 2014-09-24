package tterrag.customthings.common.item;

import tterrag.customthings.common.config.json.ToolType;
import tterrag.customthings.common.config.json.ToolType.ToolClass;
import net.minecraft.item.ItemSword;

public class ItemCustomSword extends ItemSword implements ICustomTool
{
    public ItemCustomSword(ToolType type)
    {
        super(type.getToolMaterial());
        this.setUnlocalizedName(type.getUnlocName(ToolClass.SWORD));
        this.setTextureName(type.getIconName(ToolClass.SWORD));
    }
}
