package tterrag.customthings.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import tterrag.customthings.CustomThings;
import tterrag.customthings.common.block.BlockCustom;
import tterrag.customthings.common.config.json.BlockType;

public class ItemBlockCustom extends ItemBlock
{
    public ItemBlockCustom(Block block)
    {
        super(block);
    }
    
    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        BlockType type = ((BlockCustom)this.field_150939_a).getType(stack);
        return String.format("tile.%s.%s", CustomThings.MODID, type.name);
    }
    
    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }
}
