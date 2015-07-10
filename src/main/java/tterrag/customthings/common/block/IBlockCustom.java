package tterrag.customthings.common.block;

import net.minecraft.item.ItemStack;
import tterrag.customthings.common.config.json.BlockType;
import tterrag.customthings.common.config.json.BlockType.BlockData;

public interface IBlockCustom
{
    BlockType getType(int meta);
    
    BlockType getType(ItemStack stack);
    
    void setType(BlockType type, int meta);
    
    BlockType[] getTypes();

    BlockData getData();
    
    int getMaxTypes();
}
