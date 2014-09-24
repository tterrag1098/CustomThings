package tterrag.customthings.common.item;

import tterrag.customthings.common.config.json.ToolType;
import tterrag.customthings.common.config.json.ToolType.ToolClass;
import net.minecraft.item.ItemSpade;

public class ItemCustomShovel extends ItemSpade implements ICustomTool
{
    public ItemCustomShovel(ToolType type)
    {
        super(type.getToolMaterial());
        this.setUnlocalizedName(type.getUnlocName(ToolClass.SHOVEL));
        this.setTextureName(type.getIconName(ToolClass.SHOVEL));
    }
}
