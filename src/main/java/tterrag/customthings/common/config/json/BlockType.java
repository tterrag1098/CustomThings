package tterrag.customthings.common.config.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import tterrag.core.common.json.JsonUtils;
import tterrag.customthings.common.block.BlockCustom;
import tterrag.customthings.common.item.ItemBlockCustom;

import com.google.common.collect.Maps;

import cpw.mods.fml.common.registry.GameRegistry;

public class BlockType extends JsonType
{
    // we use this since there are no context-sensitive methods for material/sound
    // so we must have a completely separate block for each "type"
    @AllArgsConstructor
    public enum BlockData
    {
        ROCK(Material.rock, Block.soundTypeStone),
        DIRT(Material.ground, Block.soundTypeGravel),
        WOOD(Material.wood, Block.soundTypeWood),
        METAL(Material.iron, Block.soundTypeMetal),
        GRASS(Material.grass, Block.soundTypeGrass),
        GLASS(Material.glass, Block.soundTypeGlass),
        WOOL(Material.cloth, Block.soundTypeCloth),
        LEAF(Material.leaves, Block.soundTypeGrass),
        SNOW(Material.snow, Block.soundTypeSnow);

        public final Material material;
        public final SoundType sound;
    }

    /* JSON Fields @formatter:off */
    public String    type         = "rock";
    public float     hardness     = 0.3f;
    public float     resistance   = 0.5f;
    public int       harvestLevel = 0;
    public String    toolType     = "";
    public String[]  drops        = {};   // drops itself if empty
    public int       minXp        = 0;
    public int       maxXp        = 0;
    public int[]     textureMap   = null; // handled appropriately
    public String[]  oreDictNames = null;
    /* End JSON Fields @formatter:on */

    private transient ItemStack[] stackDrops;

    private transient BlockData data;

    private static Map<BlockData, List<BlockType>> blockTypes = Maps.newHashMap();

    @Override
    public void register()
    {
        initData();

        List<BlockType> list = blockTypes.get(data);
        if (list == null)
        {
            list = new ArrayList<BlockType>();
            blockTypes.put(data, list);
        }

        list.add(this);
    }

    private void initData()
    {
        try
        {
            data = BlockData.valueOf(type.toUpperCase(Locale.ENGLISH));
        }
        catch (IllegalArgumentException e)
        {
            throw new RuntimeException(type + " is not a valid block type. Valid types are: " + Arrays.toString(BlockData.values()), e);
        }
    }

    @Override
    public void postInit()
    {
        stackDrops = new ItemStack[drops.length];
        for (int i = 0; i < drops.length; i++)
        {
            ItemStack stack = (ItemStack) JsonUtils.parseStringIntoItemStack(drops[i]);
            stackDrops[i] = stack;
        }
    }

    private static int meta = 0;

    public static void registerBlocks()
    {
        int blockNum = 0;
        for (BlockData type : BlockData.values())
        {
            List<BlockType> blocks = blockTypes.get(type);
            if (blocks != null)
            {
                BlockCustom realBlock = new BlockCustom(type);
                for (BlockType block : blocks)
                {
                    realBlock.setType(block, meta++);
                    if (meta > 15)
                    {
                        registerBlock(realBlock, blockNum++);
                        realBlock = new BlockCustom(type);
                        meta = 0;
                    }
                }
                if (meta != 0)
                {
                    registerBlock(realBlock, blockNum++);
                }
                meta = 0;
            }
        }
    }

    private static void registerBlock(BlockCustom block, int index)
    {
        GameRegistry.registerBlock(block, ItemBlockCustom.class, "block" + index);
        
        for (int i = 0; i < block.types.length; i++)
        {
            BlockType type = block.types[i];
            if (type != null)
            {
                ItemStack stack = new ItemStack(block, 1, i);
                OreDictionary.registerOre("block" + StringUtils.capitalize(type.name), stack);
                if (type.oreDictNames != null)
                {
                    for (String s : type.oreDictNames)
                    {
                        OreDictionary.registerOre(s, stack);
                    }
                }
            }
        }
    }

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
}
