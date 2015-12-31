package tterrag.customthings.common.item;

import lombok.experimental.Delegate;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import tterrag.customthings.common.config.json.items.ToolType;
import tterrag.customthings.common.config.json.items.ToolType.ToolClass;

public class ItemCustomAxe extends ItemAxe implements ICustomRepair<ToolType>
{
    private ToolType type;
    
    @Delegate
    private final ItemProxy<ToolType, ItemCustomAxe> proxy = new ItemProxy<ToolType, ItemCustomAxe>(this);
    
    public ItemCustomAxe(ToolType type)
    {
        super(type.getToolMaterial());
        this.type = type;
        this.setUnlocalizedName(type.getUnlocName(ToolClass.AXE));
        this.setTextureName(type.getIconName(ToolClass.AXE));
    }
    
    @Override
    public ToolType getType(ItemStack stack)
    {
        return type;
    }

    @Override
    public boolean getIsRepairable(ItemStack stack, ItemStack material)
    {
        return ItemCustomPickaxe.repairMatMatchesOredict(stack, material);
    }
}
