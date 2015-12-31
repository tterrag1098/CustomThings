package tterrag.customthings.common.item;

import lombok.experimental.Delegate;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import tterrag.customthings.common.config.json.items.ToolType;
import tterrag.customthings.common.config.json.items.ToolType.ToolClass;

public class ItemCustomSword extends ItemSword implements ICustomRepair<ToolType>
{
    private ToolType type;
    
    @Delegate
    private final ItemProxy<ToolType, ItemCustomSword> proxy = new ItemProxy<ToolType, ItemCustomSword>(this);
    
    public ItemCustomSword(ToolType type)
    {
        super(type.getToolMaterial());
        this.type = type;
        this.setUnlocalizedName(type.getUnlocName(ToolClass.SWORD));
        this.setTextureName(type.getIconName(ToolClass.SWORD));
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
