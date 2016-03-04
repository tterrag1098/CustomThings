package tterrag.customthings.common.block;

import net.minecraft.creativetab.CreativeTabs;

import lombok.experimental.Delegate;
import net.minecraft.block.BlockFence;
import tterrag.customthings.common.config.json.BlockType.BlockData;

public class BlockCustomFence extends BlockFence implements IBlockCustom
{
    private interface Exclusions
    {
        public void isOpaqueCube();
    }

    private final BlockProxy<BlockCustomFence> proxy;

    @Delegate(excludes = Exclusions.class)
    private BlockProxy<BlockCustomFence> getProxy()
    {
        return proxy == null ? BlockProxy.<BlockCustomFence> dummy() : proxy;
    }

    public BlockCustomFence(BlockData data)
    {
        super("unused", data.getType().material);
        setStepSound(data.getType().sound);
        this.proxy = new BlockProxy<BlockCustomFence>(this, data, 16);
        setHardness(0.3f);
        setResistance(0.5f);
        setCreativeTab(CreativeTabs.tabBlock);
    }
}
