package tterrag.customthings.common.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import tterrag.customthings.CustomThings;
import tterrag.customthings.common.config.json.BlockType;
import tterrag.customthings.common.config.json.BlockType.BlockData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockProxy<T extends Block & IBlockCustom> implements IBlockCustom
{
    public final BlockType[] types;

    private final T block;
    private final BlockData data;
    private final int maxTypes;
    private final Random rand = new Random();

    @SideOnly(Side.CLIENT)
    private IIcon[][] icons;

    public BlockProxy(T block, BlockData data, int maxTypes)
    {
        this.block = block;
        this.data = data;
        this.maxTypes = maxTypes;
        this.types = new BlockType[maxTypes];
    }
    
    public static <T extends Block & IBlockCustom> BlockProxy<T> dummy()
    {
        return new BlockProxy<T>(null, null, 1);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
        for (int i = 0; i < getMaxTypes(); i++)
        {
            if (getTypes()[i] != null)
            {
                list.add(new ItemStack(item, 1, i));
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register)
    {
        icons = new IIcon[getMaxTypes()][6];
        for (int i = 0; i < types.length; i++)
        {
            BlockType type = types[i];
            if (type != null)
            {
                if (type.textureMap == null)
                {
                    icons[i][0] = register.registerIcon(CustomThings.MODID.toLowerCase() + ":" + type.name);
                }
                else
                {
                    for (int j = 0; j < type.textureMap.length; j++)
                    {
                        String tex = type.textureMap[j];
                        icons[i][j] = register.registerIcon(CustomThings.MODID.toLowerCase() + ":" + tex);
                    }
                }
            }
        }
    }

    public IIcon getIcon(int side, int meta)
    {
        return getType(meta).textureMap == null ? icons[meta % getMaxTypes()][0] : icons[meta % getMaxTypes()][side];
    }

    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        return getIcon(side, world.getBlockMetadata(x, y, z));
    }

    public boolean isOpaqueCube()
    {
        return block == null ? true : getData().isOpaque();
    }

    public boolean isToolEffective(String tool, int metadata)
    {
        BlockType type = getType(metadata);
        return type.toolType.isEmpty() ? false : tool.equals(type.toolType);
    }

    public String getHarvestTool(int metadata)
    {
        BlockType type = getType(metadata);
        return type.toolType.isEmpty() ? null : type.toolType;
    }

    public boolean canHarvestBlock(EntityPlayer player, int meta)
    {
        BlockType type = getType(meta);
        ItemStack held = player.getHeldItem();
        int harvestLevel = getHarvestLevel(meta);
        if (type.toolType.isEmpty() || held == null)
        {
            return ForgeHooks.canHarvestBlock(block, player, meta);
        }
        return held.getItem().getHarvestLevel(held, getHarvestTool(meta)) >= harvestLevel
                && held.getItem().getToolClasses(held).contains(type.toolType);
    }

    public int getHarvestLevel(int metadata)
    {
        BlockType type = getType(metadata);
        return type == null ? 0 : type.harvestLevel;
    }

    public float getBlockHardness(World world, int x, int y, int z)
    {
        BlockType type = getType(world, x, y, z);
        return type == null ? block.getBlockHardness(world, x, y, z) : type.hardness;
    }

    public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ)
    {
        BlockType type = getType(world, x, y, z);
        return type == null ? block.getExplosionResistance(par1Entity) : type.resistance;
    }

    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        BlockType type = getType(metadata);
        return type == null || type.drops.length == 0 ? Lists.newArrayList(new ItemStack(block, 1, metadata)) : type.getStackDrops();
    }

    public int damageDropped(int metadata)
    {
        return metadata;
    }

    public int getExpDrop(IBlockAccess world, int metadata, int fortune)
    {
        BlockType type = types[metadata];
        return type == null ? 0 : rand.nextInt(type.maxXp - type.minXp + 1) + type.minXp;
    }
    
    public int getLightValue(IBlockAccess world, int x, int y, int z)
    {
        BlockType type = getType(world, x, y, z);
        return type.lightLevel;
    }

    private BlockType getType(IBlockAccess world, int x, int y, int z)
    {
        int meta = world.getBlockMetadata(x, y, z);
        return getType(meta);
    }

    @Override
    public void setType(BlockType type, int meta)
    {
        types[meta % types.length] = type;
    }

    @Override
    public BlockType getType(int meta)
    {
        return types[meta % types.length];
    }

    @Override
    public BlockType getType(ItemStack stack)
    {
        return types[stack.getItemDamage() % types.length];
    }

    @Override
    public BlockType[] getTypes()
    {
        return types;
    }

    @Override
    public BlockData getData()
    {
        return data;
    }

    @Override
    public int getMaxTypes()
    {
        return maxTypes;
    }
}
