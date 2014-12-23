package tterrag.customthings.common.block;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tterrag.customthings.common.config.json.BlockType;
import tterrag.customthings.common.config.json.BlockType.BlockData;

public class BlockCustom extends Block
{
    public final BlockType[] types = new BlockType[16];
    private static final Random rand = new Random();
    
    public BlockCustom(BlockData type)
    {
        super(type.material);
        setStepSound(type.sound);
        setHardness(0.3f);
        setResistance(0.5f);
        setCreativeTab(CreativeTabs.tabBlock);
    }
    
    public void setType(BlockType type, int meta)
    {
        types[meta % types.length] = type;
    }
    
    @Override
    public boolean isToolEffective(String tool, int metadata)
    {
        BlockType type = types[metadata];
        return type.toolType.isEmpty() ? super.isToolEffective(tool, metadata) : tool.equals(type.toolType);
    }
    
    @Override
    public float getBlockHardness(World world, int x, int y, int z)
    {
        BlockType type = getType(world, x, y, z);
        return type == null ? super.getBlockHardness(world, x, y, z) : type.hardness;
    }
    
    @Override
    public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ)
    {
        BlockType type = getType(world, x, y, z);
        return type == null ? super.getExplosionResistance(par1Entity) : type.resistance;
    }
    
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        BlockType type = getType(world, x, y, z);
        return type == null || type.drops.length == 0 ? super.getDrops(world, x, y, z, metadata, fortune) : type.getStackDrops();
    }
    
    @Override
    public int damageDropped(int metadata)
    {
        return metadata;
    }
    
    @Override
    public int getExpDrop(IBlockAccess world, int metadata, int fortune)
    {
        BlockType type = types[metadata];
        return type == null ? 0 : rand.nextInt(type.maxXp - type.minXp + 1) + type.minXp;
    }
    
    private BlockType getType(World world, int x, int y, int z)
    {
        int meta = world.getBlockMetadata(x, y, z);
        return types[meta];
    }

    public BlockType getType(ItemStack stack)
    {
        return types[stack.getItemDamage() % types.length];
    }
}
