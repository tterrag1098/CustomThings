package tterrag.customthings.common.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import tterrag.customthings.CustomThings;
import tterrag.customthings.common.block.BlockCustom;
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
        return String.format("tile.%s.%s", CustomThings.MODID, type.name);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        for (int i = 0; i < 16; i++)
        {
            if (((BlockCustom) this.field_150939_a).types[i] != null)
            {
                list.add(new ItemStack(item, 1, i));
            }
        }
    }

    @Override
    public BlockType getType(ItemStack stack)
    {
        return ((BlockCustom) this.field_150939_a).getType(stack);
    }
}
