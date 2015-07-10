package tterrag.customthings.common.block;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import tterrag.customthings.common.config.json.BlockType;
import tterrag.customthings.common.config.json.BlockType.BlockData;
import net.minecraft.creativetab.CreativeTabs;

public class BlockCustomStairs extends BlockStairs implements IBlockCustom
{
    private final BlockProxy<BlockCustomStairs> proxy;
    private final BlockCustom wrapped;
    
    public BlockCustomStairs(BlockData data)
    {
        super(new BlockCustom(data), 0);
        wrapped = ReflectionHelper.getPrivateValue(BlockStairs.class, this, "field_150149_b");
        proxy = new BlockProxy<BlockCustomStairs>(this, data, 1);
        setStepSound(data.getType().sound);
        setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public void registerBlockIcons(IIconRegister register)
    {
        wrapped.registerBlockIcons(register);
    }
    
    @Override
    public boolean canHarvestBlock(EntityPlayer player, int meta)
    {
        return proxy.canHarvestBlock(player, meta);
    }

    @Override
    public boolean isToolEffective(String type, int metadata)
    {
        return proxy.isToolEffective(type, metadata);
    }
    
    @Override
    public BlockType getType(int meta)
    {
        return proxy.getType(meta);
    }

    @Override
    public BlockType getType(ItemStack stack)
    {
        return proxy.getType(stack);
    }

    @Override
    public void setType(BlockType type, int meta)
    {
        proxy.setType(type, meta);
        wrapped.setType(type, meta);
    }

    @Override
    public BlockType[] getTypes()
    {
        return proxy.getTypes();
    }

    @Override
    public BlockData getData()
    {
        return proxy.getData();
    }

    @Override
    public int getMaxTypes()
    {
        return proxy.getMaxTypes();
    }
}
