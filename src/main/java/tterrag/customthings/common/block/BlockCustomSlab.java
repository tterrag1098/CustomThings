package tterrag.customthings.common.block;

import java.util.ArrayList;

import lombok.experimental.Delegate;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import tterrag.customthings.common.config.json.BlockType;
import tterrag.customthings.common.config.json.BlockType.BlockData;
import tterrag.customthings.common.item.ItemBlockCustomSlab;

@MaxTypes(8)
public class BlockCustomSlab extends BlockSlab implements IBlockCustom
{
    private interface Exclusions
    {
        boolean isOpaqueCube(IBlockState state);

        ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune);

        int damageDropped(int metadata);
    }

    private final BlockProxy<BlockCustomSlab> proxy;
    
    public final BlockCustomSlab doubleslab;

    @Delegate(excludes = Exclusions.class)
    private BlockProxy<BlockCustomSlab> getProxy()
    {
        return proxy == null ? BlockProxy.<BlockCustomSlab> dummy() : proxy;
    }

    private static int doubleslab_num = 0;
    
    public BlockCustomSlab(BlockData data, BlockType... types)
    {
        super(data.getType().material);
        this.proxy = new BlockProxy<BlockCustomSlab>(this, data, types);
        doubleslab = new BlockCustomSlab(this);
        GameRegistry.register(doubleslab.setRegistryName("doubleslab" + doubleslab_num++));
        GameRegistry.register(new ItemBlockCustomSlab(doubleslab, true).setRegistryName(doubleslab.getRegistryName()));
        setSoundType(data.getType().sound);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        useNeighborBrightness = true;
    }
    
    private BlockCustomSlab(BlockCustomSlab slab)
    {
        super(slab.getData().getType().material);
        this.proxy = new BlockProxy<BlockCustomSlab>(this, slab.getData(), slab.getProperty().getAllowedValues().toArray(new BlockType[0]));
        doubleslab = this;
        setSoundType(slab.getData().getType().sound);
    }
    
    @Override
    public String getUnlocalizedName(int p_150002_1_)
    {
        String name = getStateFromMeta(p_150002_1_).getValue(getProperty()).name;
        if (doubleslab == this)
        {
            name += ".doubleslab";
        }
        return name;
    }

    @Override
    @Deprecated
    public boolean isFullBlock(IBlockState state)
    {
        return isOpaqueCube(state);
    }
    
    @Override
    public boolean isDouble() 
    {
        return doubleslab == this;
    }

    @Override
    public IProperty<?> getVariantProperty() 
    {
        return getProperty();
    }

    @Override
    public Comparable<?> getTypeForItem(ItemStack stack) 
    {
        return stack.getItemDamage();
    }
}
