package tterrag.customthings.common.config.json;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import scala.actors.threadpool.Arrays;
import tterrag.core.common.json.JsonUtils;
import tterrag.customthings.common.block.BlockCustom;
import tterrag.customthings.common.item.ItemBlockCustom;

public class BlockType extends JsonType
{
    /* JSON Fields @formatter:off */
    public float     hardness     = 0.3f;
    public float     resistance   = 0.5f;
    public int       harvestLevel = 0;
    public String    toolType     = "";
    public String    soundType    = "cloth";
    public String[]  drops        = {};   // drops itself if empty
    /* End JSON Fields @formatter:on */

    private transient ItemStack[] stackDrops;

    private static int meta = 0, count = 0;
    private static List<BlockCustom> blocks = new ArrayList<BlockCustom>();

    @Override
    public void register()
    {
        makeStackDrops();

        int index = count % 16;

        if (blocks.size() <= index)
        {
            BlockCustom block = new BlockCustom();
            block.setCreativeTab(CreativeTabs.tabBlock);
            GameRegistry.registerBlock(block, ItemBlockCustom.class, "block" + index);
            blocks.add(block);
        }

        blocks.get(index).setType(this, meta);

        meta = count++ % 16;
    }

    private void makeStackDrops()
    {
        stackDrops = new ItemStack[drops.length];
        for (int i = 0; i < drops.length; i++)
        {
            ItemStack stack = (ItemStack) JsonUtils.parseStringIntoItemStack(drops[i]);
            stackDrops[i] = stack;
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<ItemStack> getStackDrops()
    {
        ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();

        if (stackDrops == null)
        {
            return stacks;
        }
        else
        {
            stacks.addAll(Arrays.asList(stackDrops));
            return stacks;
        }
    }

    public static void addAll(List<BlockType> elements)
    {
        for (BlockType type : elements)
        {
            type.register();
        }
    }
}
