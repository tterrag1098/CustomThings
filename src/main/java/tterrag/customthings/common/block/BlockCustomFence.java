package tterrag.customthings.common.block;

import lombok.experimental.Delegate;
import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import tterrag.customthings.common.config.json.BlockType;
import tterrag.customthings.common.config.json.BlockType.BlockData;

public class BlockCustomFence extends BlockFence implements IBlockCustom
{
    private interface Exclusions
    {
        public void isOpaqueCube(IBlockState state);
    }

    private final BlockProxy<BlockCustomFence> proxy;

    @Delegate(excludes = Exclusions.class)
    private BlockProxy<BlockCustomFence> getProxy()
    {
        return proxy == null ? BlockProxy.<BlockCustomFence> dummy() : proxy;
    }

    public BlockCustomFence(BlockData data, BlockType... types)
    {
        super(data.getType().material, data.getType().material.getMaterialMapColor());
        setSoundType(data.getType().sound);
        this.proxy = new BlockProxy<BlockCustomFence>(this, data, types);
        setHardness(0.3f);
        setResistance(0.5f);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
}
