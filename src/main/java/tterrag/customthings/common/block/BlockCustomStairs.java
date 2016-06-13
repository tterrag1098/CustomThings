package tterrag.customthings.common.block;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import tterrag.customthings.common.config.json.BlockType;
import tterrag.customthings.common.config.json.BlockType.BlockData;

@MaxTypes(1)
public class BlockCustomStairs extends BlockStairs implements IBlockCustom
{
    private final BlockProxy<BlockCustomStairs> proxy;
    private final BlockCustom wrapped;
    
    public BlockCustomStairs(BlockData data, BlockType... types)
    {
        super(new BlockCustom(data).getDefaultState());
        wrapped = ReflectionHelper.getPrivateValue(BlockStairs.class, this, "field_150149_b");
        proxy = new BlockProxy<BlockCustomStairs>(this, data, types);
        setSoundType(data.getType().sound);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
    
    @Override
    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
    {
        return proxy.canHarvestBlock(world, pos, player);
    }

    @Override
    public boolean isToolEffective(String type, IBlockState state)
    {
        return proxy.isToolEffective(type, state);
    }

    @Override
    public BlockType getType(ItemStack stack)
    {
        return proxy.getType(stack);
    }

    @Override
    public BlockData getData()
    {
        return proxy.getData();
    }

    @Override
    public IProperty<BlockType> getProperty() 
    {
        return proxy.getProperty();
    }
}
