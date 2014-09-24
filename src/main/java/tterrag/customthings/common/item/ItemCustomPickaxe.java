package tterrag.customthings.common.item;

import tterrag.customthings.common.config.json.ToolType;
import tterrag.customthings.common.config.json.ToolType.ToolClass;
import net.minecraft.item.ItemPickaxe;

public class ItemCustomPickaxe extends ItemPickaxe implements ICustomTool
{
    public ItemCustomPickaxe(ToolType type)
    {
        super(type.getToolMaterial());
        this.setUnlocalizedName(type.getUnlocName(ToolClass.PICKAXE));
        this.setTextureName(type.getIconName(ToolClass.PICKAXE));
    }
}
