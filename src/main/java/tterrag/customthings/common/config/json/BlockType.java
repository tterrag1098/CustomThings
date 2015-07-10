package tterrag.customthings.common.config.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Random;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;
import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.StringUtils;

import tterrag.customthings.common.block.BlockCustom;
import tterrag.customthings.common.block.BlockCustomFalling;
import tterrag.customthings.common.block.BlockCustomSlab;
import tterrag.customthings.common.block.BlockCustomStairs;
import tterrag.customthings.common.block.IBlockCustom;
import tterrag.customthings.common.item.ItemBlockCustom;
import tterrag.customthings.common.item.ItemBlockCustomSlab;

import com.enderio.core.common.util.ItemUtil;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import cpw.mods.fml.common.registry.GameRegistry;

public class BlockType extends JsonType
{
    // we use this since there are no context-sensitive methods for material/sound
    // so we must have a completely separate block for each "type"
    @AllArgsConstructor
    public enum MaterialSound
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

    @AllArgsConstructor
    public enum BlockShape
    {
        NORMAL(BlockCustom.class),
        SLAB(BlockCustomSlab.class) {
            
            @Override
            public void registerBlock(IBlockCustom block, int index)
            {
                BlockCustomSlab slab = (BlockCustomSlab) block;
                GameRegistry.registerBlock(slab, ItemBlockCustomSlab.class, "block" + index, false);
            }
        },
        STAIR(BlockCustomStairs.class),
        FALLING(BlockCustomFalling.class);

        public final Class<? extends IBlockCustom> clazz;
        
        @SuppressWarnings("unchecked")
        @SneakyThrows
        public <T extends IBlockCustom> T create(BlockData data)
        {
            return (T) clazz.getConstructor(BlockData.class).newInstance(data);
        }
        
        public void registerBlock(IBlockCustom block, int index)
        {
            GameRegistry.registerBlock((Block) block, ItemBlockCustom.class, "block" + index);
        }
    }

    @Value
    public static class BlockData
    {
        MaterialSound type;
        BlockShape shape;
        boolean isOpaque;
    }

    @Value
    private static class DropData
    {
        private static final Random rand = new Random();

        ItemStack stack;
        int min, max;

        public int getRandomStackSize()
        {
            return rand.nextInt(max - min + 1) + min;
        }
    }

    /* JSON Fields @formatter:off */
    public String   type            = "rock";
    public float    hardness        = 0.3f;
    public float    resistance      = 0.5f;
    public int      harvestLevel    = 0;
    public String   toolType        = "";
    public String[] drops           = {};   // drops itself if empty
    public int      minXp           = 0;
    public int      maxXp           = 0;
    public String[] textureMap      = null; // handled appropriately
    public String[] oreDictNames    = null;
    public boolean  isOpaque        = true;
    public String   shape           = "normal";
    public int      lightLevel      = 0;
    public int      maxStackSize    = 64;
    /* End JSON Fields @formatter:on */

    private transient DropData[] stackDrops;

    private transient BlockData data;

    private static Multimap<BlockData, BlockType> blockTypes = LinkedHashMultimap.create();

    @Override
    public void register()
    {
        initData();
        blockTypes.get(data).add(this);
    }

    private void initData()
    {
        MaterialSound ms = null;
        BlockShape bs = null;
        try
        {
            ms = MaterialSound.valueOf(type.toUpperCase(Locale.ENGLISH));
            bs = BlockShape.valueOf(shape.toUpperCase(Locale.ENGLISH));
            this.data = new BlockData(ms, bs, isOpaque);
        }
        catch (IllegalArgumentException e)
        {
            if (ms == null)
            {
                throw new RuntimeException(type + " is not a valid block type. Valid types are: " + Arrays.toString(MaterialSound.values()), e);
            }
            else
            {
                throw new RuntimeException(shape + " is not a valid shape. Value shapes are: " + Arrays.toString(BlockShape.values()), e);
            }
        }
    }

    @Override
    public void postInit()
    {
        stackDrops = new DropData[drops.length];
        for (int i = 0; i < drops.length; i++)
        {
            String drop = drops[i];
            if (drop.indexOf('#') == -1 || drop.indexOf('-') == -1)
            {
                ItemStack stack = (ItemStack) ItemUtil.parseStringIntoItemStack(drops[i]);
                stackDrops[i] = new DropData(stack, stack.stackSize, stack.stackSize);
            }
            else
            {
                String range = drops[i].substring(drop.indexOf('#') + 1, drop.length());
                String[] minmax = range.split("\\-");
                try
                {
                    ItemStack stack = ItemUtil.parseStringIntoItemStack(drops[i].substring(0, drop.indexOf('#')));
                    stackDrops[i] = new DropData(stack, Integer.valueOf(minmax[0]), Integer.valueOf(minmax[1]));
                }
                catch (NumberFormatException e)
                {
                    throw new RuntimeException("Could not parse drop amount range for block " + name, e);
                }
            }
        }
    }

    private static int meta = 0;

    public static void registerBlocks()
    {
        int blockNum = 0;
        for (BlockData d : blockTypes.keySet())
        {
            Collection<BlockType> blocks = blockTypes.get(d);
            if (!blocks.isEmpty())
            {
                IBlockCustom realBlock = d.getShape().create(d);
                for (BlockType block : blocks)
                {
                    realBlock.setType(block, meta++);
                    if (meta > realBlock.getMaxTypes())
                    {
                        registerBlock(d, realBlock, blockNum++);
                        realBlock = d.getShape().create(d);
                        meta = 0;
                    }
                }
                if (meta != 0)
                {
                    registerBlock(d, realBlock, blockNum++);
                }
                meta = 0;
            }
        }
    }

    private static void registerBlock(BlockData data, IBlockCustom block, int index)
    {
        data.getShape().registerBlock(block, index);
        
        for (int i = 0; i < block.getTypes().length; i++)
        {
            BlockType type = block.getTypes()[i];
            if (type != null)
            {
                ItemStack stack = new ItemStack((Block) block, 1, i);
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
            for (DropData data : stackDrops)
            {
                ItemStack stack = data.stack.copy();
                stack.stackSize = data.getRandomStackSize();
                stacks.add(stack);
            }
            return stacks;
        }
    }
}
