package tterrag.customthings.common.block;

import lombok.experimental.Delegate;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.BlockRenderLayer;
import tterrag.customthings.common.config.json.BlockType;
import tterrag.customthings.common.config.json.BlockType.BlockData;

public class BlockCustom extends Block implements IBlockCustom
{    
    private final BlockProxy<BlockCustom> proxy;
    
    @Delegate
    private BlockProxy<BlockCustom> getProxy()
    {
        return proxy == null ? BlockProxy.<BlockCustom>dummy() : proxy;
    }

    public BlockCustom(BlockData data, BlockType... types)
    {
        super(data.getType().material);
        setSoundType(data.getType().sound);
        this.proxy = new BlockProxy<BlockCustom>(this, data, types);
        setHardness(0.3f);
        setResistance(0.5f);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
    
    @Override
    @Deprecated
    public boolean isFullBlock(IBlockState state)
    {
        return isOpaqueCube(state);
    }
}
