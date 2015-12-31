package tterrag.customthings.common.item;

import lombok.experimental.Delegate;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import tterrag.customthings.common.config.json.items.ToolType;
import tterrag.customthings.common.config.json.items.ToolType.ToolClass;

public class ItemCustomShovel extends ItemSpade implements ICustomRepair<ToolType>
{
    @Delegate
    private final ItemProxy<ToolType, ItemCustomShovel> proxy = new ItemProxy<ToolType, ItemCustomShovel>(this);
    
    private ToolType type;
    
    public ItemCustomShovel(ToolType type)
    {
        super(type.getToolMaterial());
        this.type = type;
        this.setUnlocalizedName(type.getUnlocName(ToolClass.SHOVEL));
        this.setTextureName(type.getIconName(ToolClass.SHOVEL));
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
