package tterrag.customthings.common.config.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.Value;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.StringUtils;

import tterrag.customthings.common.block.BlockCustom;
import tterrag.customthings.common.block.BlockCustomFalling;
import tterrag.customthings.common.block.BlockCustomFence;
import tterrag.customthings.common.block.BlockCustomSlab;
import tterrag.customthings.common.block.BlockCustomStairs;
import tterrag.customthings.common.block.BlockCustomWall;
import tterrag.customthings.common.block.IBlockCustom;
import tterrag.customthings.common.block.MaxTypes;
import tterrag.customthings.common.item.ItemBlockCustom;
import tterrag.customthings.common.item.ItemBlockCustomSlab;

import com.enderio.core.common.util.ItemUtil;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;

public class BlockType extends JsonType implements Comparable<BlockType>
{
    // we use this since there are no context-sensitive methods for material/sound
    // so we must have a completely separate block for each "type"
    @AllArgsConstructor
    public enum MaterialSound
    {
        ROCK(Material.ROCK, SoundType.STONE),
        DIRT(Material.GROUND, SoundType.GROUND),
        WOOD(Material.WOOD, SoundType.WOOD),
        METAL(Material.IRON, SoundType.METAL),
        GRASS(Material.GRASS, SoundType.PLANT),
        GLASS(Material.GLASS, SoundType.GLASS),
        WOOL(Material.CLOTH, SoundType.CLOTH),
        LEAF(Material.LEAVES, SoundType.PLANT),
        SNOW(Material.SNOW, SoundType.SNOW);

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
                GameRegistry.register(slab.setRegistryName("block" + index));
                GameRegistry.register(new ItemBlockCustomSlab(slab, false).setRegistryName(slab.getRegistryName()));
            }
        },
        STAIR(BlockCustomStairs.class),
        FALLING(BlockCustomFalling.class),
        FENCE(BlockCustomFence.class),
        WALL(BlockCustomWall.class),
        ;

        public final Class<? extends IBlockCustom> clazz;
        
        @SuppressWarnings("unchecked")
        @SneakyThrows
        public <T extends IBlockCustom> T create(BlockData data, BlockType... types)
        {
            return (T) clazz.getConstructor(BlockData.class, BlockType[].class).newInstance(data, types);
        }
        
        public void registerBlock(IBlockCustom block, int index)
        {
            GameRegistry.register(((Block)block).setRegistryName("block" + index));
            GameRegistry.register(new ItemBlockCustom((Block) block).setRegistryName(((Block)block).getRegistryName()));
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
    public int      burnTime        = 0;
    /* End JSON Fields @formatter:on */

    private transient DropData[] stackDrops;

    private transient BlockData data;
    
    private transient IBlockCustom block;
    
    @Getter
    private transient int variant;

    private static Map<String, BlockType> propertyToType = Maps.newHashMap();
    private static Table<IBlockCustom, Integer, BlockType> stateLookup = HashBasedTable.create();
    private static Multimap<BlockData, BlockType> blockTypes = LinkedHashMultimap.create();

    public static Optional<BlockType> getType(String value) 
    {
        return Optional.fromNullable(propertyToType.get(value));
    }
    
    public static BlockType fromMeta(IBlockCustom block, int meta)
    {
        return stateLookup.get(block, meta);
    }
    
    @Override
    public void register()
    {
        initData();
        lightLevel = MathHelper.clamp_int(lightLevel, 0, 15);
        maxStackSize = MathHelper.clamp_int(maxStackSize, 1, 64);
        burnTime = Math.max(0, burnTime);
        propertyToType.put(name.toLowerCase(Locale.US), this);
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
            MaxTypes a = d.shape.clazz.getAnnotation(MaxTypes.class);
            int maxTypes = a == null ? 16 : a.value();
            Collection<BlockType> blocks = blockTypes.get(d);
            if (!blocks.isEmpty())
            {
                List<BlockType> types = Lists.newArrayList();
                for (BlockType block : blocks)
                {
                    types.add(block);
                    block.variant = meta++;
                    if (meta > maxTypes)
                    {
                        registerBlock(d, blockNum++, types);
                        meta = 0;
                    }
                }
                if (meta != 0)
                {
                    registerBlock(d, blockNum++, types);
                }
                meta = 0;
            }
        }
    }

    private static void registerBlock(BlockData data, int index, List<BlockType> typelist)
    {
        BlockType[] types = typelist.toArray(new BlockType[typelist.size()]);
        IBlockCustom block = data.getShape().create(data, types);
        data.getShape().registerBlock(block, index);
        
        for (int i = 0; i < types.length; i++)
        {
            BlockType type = types[i];
            type.block = block;
            stateLookup.put(type.block, type.variant, type);
            
            ItemStack stack = new ItemStack((Block) block, 1, i);
            OreDictionary.registerOre("block" + StringUtils.capitalize(type.name), stack);
            if (type.oreDictNames != null) {
                for (String s : type.oreDictNames) {
                    OreDictionary.registerOre(s, stack);
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

    @Override
    public int compareTo(BlockType o) 
    {
        Preconditions.checkNotNull(o);
        
        if (block == o.block) return variant - o.variant;
        else return Block.getIdFromBlock((Block) block) - Block.getIdFromBlock((Block) o.block);
    }
}
