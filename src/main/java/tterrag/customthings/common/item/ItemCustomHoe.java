package tterrag.customthings.common.item;

import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import tterrag.customthings.common.config.json.IHasMaterial;
import tterrag.customthings.common.config.json.items.ToolType;
import tterrag.customthings.common.config.json.items.ToolType.ToolClass;

public class ItemCustomHoe extends ItemHoe implements ICustomRepair
{
    private ToolType type;
    
    public ItemCustomHoe(ToolType type)
    {
        super(type.getToolMaterial());
        this.type = type;
        this.setUnlocalizedName(type.getUnlocName(ToolClass.HOE));
        this.setTextureName(type.getIconName(ToolClass.HOE));
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
