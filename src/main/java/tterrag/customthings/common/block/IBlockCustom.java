package tterrag.customthings.common.block;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.properties.IProperty;
import net.minecraft.item.ItemStack;
import tterrag.customthings.common.config.json.BlockType;
import tterrag.customthings.common.config.json.BlockType.BlockData;

@ParametersAreNonnullByDefault
public interface IBlockCustom
{   
    IProperty<BlockType> getProperty();
    
    BlockType getType(ItemStack stack);
        
    BlockData getData();
}
