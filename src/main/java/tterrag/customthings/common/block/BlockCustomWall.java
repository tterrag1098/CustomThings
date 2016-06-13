package tterrag.customthings.common.block;

import lombok.experimental.Delegate;
import net.minecraft.block.BlockWall;
import net.minecraft.creativetab.CreativeTabs;
import tterrag.customthings.common.config.json.BlockType;
import tterrag.customthings.common.config.json.BlockType.BlockData;

public class BlockCustomWall extends BlockWall implements IBlockCustom
{
    private interface Exclusions
    {
        public void isOpaqueCube();
    }

    private final BlockProxy<BlockCustomWall> proxy;

    @Delegate(excludes = Exclusions.class)
    private BlockProxy<BlockCustomWall> getProxy()
    {
        return proxy == null ? BlockProxy.<BlockCustomWall> dummy() : proxy;
    }

    public BlockCustomWall(BlockData data, BlockType... types)
    {
        super(new BlockCustom(data, types));
        setSoundType(data.getType().sound);
        this.proxy = new BlockProxy<BlockCustomWall>(this, data, types);
        setHardness(0.3f);
        setResistance(0.5f);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
}
