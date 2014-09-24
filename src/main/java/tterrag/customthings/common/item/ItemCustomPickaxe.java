package tterrag.customthings.common.item;

import tterrag.customthings.common.config.json.ToolType;
import net.minecraft.item.ItemPickaxe;

public class ItemCustomPickaxe extends ItemPickaxe implements ICustomTool
{
    public ItemCustomPickaxe(ToolType type)
    {
        super(type.getToolMaterial());
        this.setUnlocalizedName(type.getUnlocName());
        this.setTextureName(type.getIconName());
    }
}
