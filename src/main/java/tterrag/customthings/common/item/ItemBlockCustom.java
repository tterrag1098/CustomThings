package tterrag.customthings.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import tterrag.customthings.CustomThings;
import tterrag.customthings.common.block.IBlockCustom;
import tterrag.customthings.common.config.json.BlockType;

public class ItemBlockCustom extends ItemBlockWithMetadata implements ICustomItem<BlockType>
{
    public ItemBlockCustom(Block block)
    {
        super(block, block);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        BlockType type = getType(stack);
        if (type == null) {
            System.out.println(stack.getUnlocalizedName());
            return "error";
        }
        return String.format("tile.%s.%s", CustomThings.MODID, type.name);
    }

    @Override
    public BlockType getType(ItemStack stack)
    {
        return ((IBlockCustom) this.field_150939_a).getType(stack);
    }
    
    @Override
    public int getItemStackLimit(ItemStack stack)
    {
        return getType(stack).maxStackSize;
    }
}
