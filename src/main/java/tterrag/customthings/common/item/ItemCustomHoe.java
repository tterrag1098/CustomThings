package tterrag.customthings.common.item;

import net.minecraft.item.ItemHoe;
import tterrag.customthings.common.config.json.ToolType;
import tterrag.customthings.common.config.json.ToolType.ToolClass;

public class ItemCustomHoe extends ItemHoe implements ICustomTool
{
    public ItemCustomHoe(ToolType type)
    {
        super(type.getToolMaterial());
        this.setUnlocalizedName(type.getUnlocName(ToolClass.HOE));
        this.setTextureName(type.getIconName(ToolClass.HOE));
    }
}
