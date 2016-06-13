package tterrag.customthings.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import tterrag.customthings.CustomThings;
import tterrag.customthings.common.block.IBlockCustom;
import tterrag.customthings.common.config.json.BlockType;

public class ItemBlockCustom extends ItemBlock implements ICustomItem<BlockType>
{
    public ItemBlockCustom(Block block)
    {
        super(block);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        BlockType type = getType(stack);
        if (type == null)
        {
            return "error";
        }
        return String.format("tile.%s.%s", CustomThings.MODID, type.name);
    }

    @Override
    public BlockType getType(ItemStack stack)
    {
        return ((IBlockCustom) this.block).getType(stack);
    }
    
    @Override
    public int getItemStackLimit(ItemStack stack)
    {
        return getType(stack).maxStackSize;
    }
    
    @Override
    public int getMetadata(int damage) 
    {
        return damage;
    }
}
