package tterrag.customthings.common.block;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tterrag.customthings.common.config.json.BlockType;

public class BlockCustom extends Block
{
    private BlockType[] types = new BlockType[16];
    
    public BlockCustom()
    {
        super(Material.cloth);
        setHardness(0.3f);
        setResistance(0.5f);
    }
    
    public void setType(BlockType type, int meta)
    {
        types[meta % types.length] = type;
    }
    
    @Override
    public boolean isToolEffective(String tool, int metadata)
    {
        BlockType type = types[metadata];
        return type == null ? super.isToolEffective(tool, metadata) : type.toolType.isEmpty() ? true : tool.equals(type.toolType);
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
