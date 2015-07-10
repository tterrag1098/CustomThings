package tterrag.customthings.common.block;

import lombok.experimental.Delegate;
import net.minecraft.block.BlockFalling;
import net.minecraft.creativetab.CreativeTabs;
import tterrag.customthings.common.config.json.BlockType.BlockData;
import tterrag.customthings.common.block.BlockProxy;

public class BlockCustomFalling extends BlockFalling implements IBlockCustom
{
    private final BlockProxy<BlockCustomFalling> proxy;
    
    @Delegate
    private BlockProxy<BlockCustomFalling> getProxy()
    {
        return proxy == null ? BlockProxy.<BlockCustomFalling>dummy() : proxy;
    }

    public BlockCustomFalling(BlockData data)
    {
        super(data.getType().material);
        proxy = new BlockProxy<BlockCustomFalling>(this, data, 16);
        setStepSound(data.getType().sound);
        setCreativeTab(CreativeTabs.tabBlock);
    }
}
