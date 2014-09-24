package tterrag.customthings.common.item;

import tterrag.customthings.common.config.json.ToolType;
import tterrag.customthings.common.config.json.ToolType.ToolClass;
import net.minecraft.item.ItemAxe;

public class ItemCustomAxe extends ItemAxe implements ICustomTool
{
    public ItemCustomAxe(ToolType type)
    {
        super(type.getToolMaterial());
        this.setUnlocalizedName(type.getUnlocName(ToolClass.AXE));
        this.setTextureName(type.getIconName(ToolClass.AXE));
    }
}
