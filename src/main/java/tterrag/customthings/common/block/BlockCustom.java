package tterrag.customthings.common.block;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tterrag.customthings.CustomThings;
import tterrag.customthings.common.config.json.BlockType;
import tterrag.customthings.common.config.json.BlockType.BlockData;

public class BlockCustom extends Block
{
    public final BlockType[] types = new BlockType[16];

    @SideOnly(Side.CLIENT)
    private IIcon[][] icons;

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
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register)
    {
        icons = new IIcon[16][6];
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

    @Override
    public IIcon getIcon(int side, int meta)
    {
        return types[meta].textureMap == null ? icons[meta][0] : icons[meta][side];
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        return getIcon(side, world.getBlockMetadata(x, y, z));
    }

    @Override
    public boolean isToolEffective(String tool, int metadata)
    {
        BlockType type = types[metadata];
        return type.toolType.isEmpty() ? super.isToolEffective(tool, metadata) : tool.equals(type.toolType);
    }

    @Override
    public String getHarvestTool(int metadata)
    {
        BlockType type = types[metadata];
        return type.toolType.isEmpty() ? super.getHarvestTool(metadata) : type.toolType;
    }

    @Override
    public boolean canHarvestBlock(EntityPlayer player, int meta)
    {
        BlockType type = getType(meta);
        ItemStack held = player.getHeldItem();
        int harvestLevel = getHarvestLevel(meta);
        if (type.toolType.isEmpty() || held == null)
        {
            return super.canHarvestBlock(player, meta);
        }
        return held.getItem().getHarvestLevel(held, getHarvestTool(meta)) >= harvestLevel &&
               held.getItem().getToolClasses(held).contains(type.toolType);
    }
    
    @Override
    public int getHarvestLevel(int metadata)
    {
        return getType(metadata).harvestLevel;
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
        BlockType type = getType(metadata);
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
        return getType(meta);
    }

    public BlockType getType(int meta)
    {
        return types[meta % types.length];
    }

    public BlockType getType(ItemStack stack)
    {
        return types[stack.getItemDamage() % types.length];
    }
}
