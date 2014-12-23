package tterrag.customthings.common.item;

import tterrag.customthings.common.config.json.IHasMaterial;
import tterrag.customthings.common.config.json.items.ToolType;
import tterrag.customthings.common.config.json.items.ToolType.ToolClass;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;

public class ItemCustomAxe extends ItemAxe implements ICustomRepair
{
    private ToolType type;
    
    public ItemCustomAxe(ToolType type)
    {
        super(type.getToolMaterial());
        this.type = type;
        this.setUnlocalizedName(type.getUnlocName(ToolClass.AXE));
        this.setTextureName(type.getIconName(ToolClass.AXE));
    }
    
    @Override
    public IHasMaterial getType()
    {
        return type;
    }

    @Override
    public boolean getIsRepairable(ItemStack stack, ItemStack material)
    {
        return ItemCustomPickaxe.repairMatMatchesOredict(stack, material);
    }
}
