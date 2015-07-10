package tterrag.customthings.common.item;

import tterrag.customthings.common.block.BlockCustomSlab;
import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;

public class ItemBlockCustomSlab extends ItemSlab
{
    public ItemBlockCustomSlab(Block slab, Boolean doubleslab)
    {
        super(slab, (BlockCustomSlab) slab, ((BlockCustomSlab)slab).doubleslab, doubleslab);
    }
}
