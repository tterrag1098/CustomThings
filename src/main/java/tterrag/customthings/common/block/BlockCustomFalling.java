package tterrag.customthings.common.block;

import lombok.experimental.Delegate;
import net.minecraft.block.BlockFalling;
import net.minecraft.creativetab.CreativeTabs;
import tterrag.customthings.common.config.json.BlockType;
import tterrag.customthings.common.config.json.BlockType.BlockData;

public class BlockCustomFalling extends BlockFalling implements IBlockCustom
{
    private final BlockProxy<BlockCustomFalling> proxy;
    
    @Delegate
    private BlockProxy<BlockCustomFalling> getProxy()
    {
        return proxy == null ? BlockProxy.<BlockCustomFalling>dummy() : proxy;
    }

    public BlockCustomFalling(BlockData data, BlockType... types)
    {
        super(data.getType().material);
        this.proxy = new BlockProxy<BlockCustomFalling>(this, data, types);
        setSoundType(data.getType().sound);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
}
