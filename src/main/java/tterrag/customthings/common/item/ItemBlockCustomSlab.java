package tterrag.customthings.common.item;

import tterrag.customthings.common.block.BlockCustomSlab;
import tterrag.customthings.common.config.json.BlockType;
import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;

public class ItemBlockCustomSlab extends ItemSlab implements ICustomItem<BlockType>
{
    public ItemBlockCustomSlab(Block slab, Boolean doubleslab)
    {
        super(slab, (BlockCustomSlab) slab, ((BlockCustomSlab)slab).doubleslab);
    }

    @Override
    public BlockType getType(ItemStack stack)
    {
        return ((BlockCustomSlab)block).getType(stack);
    }
    
    @Override
    public int getItemStackLimit(ItemStack stack)
    {
        return getType(stack).maxStackSize;
    }
}
