package tterrag.customthings.common.block;

import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import tterrag.customthings.common.config.json.BlockType;
import tterrag.customthings.common.config.json.BlockType.BlockData;

import com.google.common.collect.Lists;

public class BlockProxy<T extends Block & IBlockCustom> implements IBlockCustom
{
    public final IProperty<BlockType> types;

    private final T block;
    private final BlockData data;
    private final Random rand = new Random();
    
    private final BlockStateContainer states;

    public BlockProxy(T block, BlockData data, BlockType... types)
    {
        this.block = block;
        this.data = data;
        this.types = new PropertyBlockType("type", types);
        if (block != null) {
            this.states = new BlockStateContainer(block, ArrayUtils.add(block.blockState.getProperties().toArray(new IProperty[0]), this.types));
            this.block.setDefaultState(states.getBaseState());
        } else {
            this.states = null;
        }
    }
    
    public static <T extends Block & IBlockCustom> BlockProxy<T> dummy()
    {
        return new BlockProxy<T>(null, null);
    }
    
    public BlockStateContainer getBlockState() 
    {
        return states;
    }
    
    public IBlockState getStateFromMeta(int meta) 
    {
        return block.getDefaultState().withProperty(types, BlockType.fromMeta(block, meta));
    }
    
    public int getMetaFromState(IBlockState state) 
    {
        return state.getValue(types).getVariant();
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
        for (BlockType type : types.getAllowedValues())
        {
            list.add(new ItemStack(item, 1, getMetaFromState(block.getDefaultState().withProperty(getProperty(), type))));
        }
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return block == null ? true : getData().isOpaque();
    }
    
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) 
    {
        return state.getValue(getProperty()).layer == layer.ordinal();
    }

    public boolean isToolEffective(String tool, IBlockState state)
    {
        BlockType type = state.getValue(getProperty());
        return type.toolType.isEmpty() ? false : tool.equals(type.toolType);
    }

    public String getHarvestTool(IBlockState state)
    {
        BlockType type = state.getValue(getProperty());
        return type == null || type.toolType.isEmpty() ? null : type.toolType;
    }

    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
    {
        IBlockState state = world.getBlockState(pos);
        BlockType type = state.getValue(getProperty());
        ItemStack held = player.getHeldItemMainhand();
        int harvestLevel = getHarvestLevel(state);
        if (type.toolType.isEmpty() || held == null)
        {
            return ForgeHooks.canToolHarvestBlock(world, pos, held);
        }
        return held.getItem().getHarvestLevel(held, getHarvestTool(state)) >= harvestLevel
                && held.getItem().getToolClasses(held).contains(type.toolType);
    }

    public int getHarvestLevel(IBlockState state)
    {
        BlockType type = state.getValue(getProperty());
        return type == null ? 0 : type.harvestLevel;
    }

    @Deprecated
    public float getBlockHardness(IBlockState state, World world, BlockPos pos)
    {
        BlockType type = state.getValue(getProperty());
        return type == null ? block.getBlockHardness(state, world, pos) : type.hardness;
    }

    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
    {
        BlockType type = world.getBlockState(pos).getValue(getProperty());
        return type == null ? block.getExplosionResistance(exploder) : type.resistance;
    }

    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        BlockType type = state.getValue(getProperty());
        return type == null || type.drops.length == 0 ? Lists.newArrayList(new ItemStack(block, 1, block.getMetaFromState(state))) : type.getStackDrops();
    }

    public int damageDropped(IBlockState state)
    {
        return block.getMetaFromState(state);
    }

    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune)
    {
        BlockType type = state.getValue(getProperty());
        return type == null ? 0 : rand.nextInt(type.maxXp - type.minXp + 1) + type.minXp;
    }
    
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        BlockType type = state.getValue(getProperty());
        return type.lightLevel;
    }

    @Override
    public BlockType getType(ItemStack stack)
    {
        return BlockType.fromMeta(block, stack.getItemDamage());
    }

    @Override
    public BlockData getData()
    {
        return data;
    }
    
    @Override
    public IProperty<BlockType> getProperty() 
    {
        return types;
    }
}
