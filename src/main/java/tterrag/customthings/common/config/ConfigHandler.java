package tterrag.customthings.common.config;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import com.enderio.core.common.config.JsonConfigReader;
import com.enderio.core.common.config.JsonConfigReader.ModToken;
import com.enderio.core.common.util.EnderFileUtils;
import com.enderio.core.common.util.ResourcePackAssembler;
import com.enderio.core.common.util.ResourcePackAssembler.ModelType;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import tterrag.customthings.CustomThings;
import tterrag.customthings.common.config.json.AchievementType;
import tterrag.customthings.common.config.json.BlockType;
import tterrag.customthings.common.config.json.IJsonType;
import tterrag.customthings.common.config.json.crafting.ShapedJsonRecipe;
import tterrag.customthings.common.config.json.crafting.ShapelessJsonRecipe;
import tterrag.customthings.common.config.json.crafting.SmeltingJsonRecipe;
import tterrag.customthings.common.config.json.items.ArmorType;
import tterrag.customthings.common.config.json.items.ItemType;
import tterrag.customthings.common.config.json.items.RecordType;
import tterrag.customthings.common.config.json.items.ToolType;

public class ConfigHandler
{
//    private static class EnumMapInstanceCreator<V extends IJsonType> implements InstanceCreator<EnumMap<Difficulty, V>>
//    {
//        @Override
//        public EnumMap<Difficulty, V> createInstance(final Type type)
//        {
//            return new EnumMap<Difficulty, V>(Difficulty.class);
//        }
//    };
    
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static File baseDir;

    private static JsonConfigReader<ArmorType> armorReader;
    private static JsonConfigReader<ToolType> toolReader;
    private static JsonConfigReader<ItemType> itemReader;
    private static JsonConfigReader<RecordType> recordReader;
    private static JsonConfigReader<BlockType> blockReader;
//    private static JsonConfigReader<FluidType> fluidReader;
    
    private static JsonConfigReader<ShapedJsonRecipe> shapedReader;
    private static JsonConfigReader<ShapelessJsonRecipe> shapelessReader;
    private static JsonConfigReader<SmeltingJsonRecipe> smeltingReader;
    
    private static JsonConfigReader<AchievementType> achievementReader;
    
//    private static JsonConfigReader<ShapedJsonDifficultyRecipe> shapedDifficultyReader;
//    private static JsonConfigReader<ShapelessJsonDifficultyRecipe> shapelessDifficultyReader;

    private static ResourcePackAssembler assembler;

    @SuppressWarnings("serial")
    public static void preInit()
    {
        baseDir = new File(Loader.instance().getConfigDir(), CustomThings.MODID);

        ModToken token = new ModToken(CustomThings.class, CustomThings.MODID + "/misc");
        armorReader = new JsonConfigReader<ArmorType>(token, baseDir.getAbsolutePath() + "/" + "customArmors.json", ArmorType.class);
        toolReader = new JsonConfigReader<ToolType>(token, baseDir.getAbsolutePath() + "/" + "customTools.json", ToolType.class);
        itemReader = new JsonConfigReader<ItemType>(token, baseDir.getAbsolutePath() + "/" + "customItems.json", ItemType.class);
        recordReader = new JsonConfigReader<RecordType>(token, baseDir.getAbsolutePath() + "/" + "customRecords.json", RecordType.class);
        blockReader = new JsonConfigReader<BlockType>(token, baseDir.getAbsolutePath() + "/" + "customBlocks.json", BlockType.class);
//        fluidReader = new JsonConfigReader<FluidType>(token, baseDir.getAbsolutePath() + "/" + "customFluids.json", FluidType.class);
        
        shapedReader = new JsonConfigReader<ShapedJsonRecipe>(token, baseDir.getAbsolutePath() + "/" + "shapedRecipes.json", ShapedJsonRecipe.class);
        shapelessReader = new JsonConfigReader<ShapelessJsonRecipe>(token, baseDir.getAbsolutePath() + "/" + "shapelessRecipes.json", ShapelessJsonRecipe.class);
        smeltingReader = new JsonConfigReader<SmeltingJsonRecipe>(token, baseDir.getAbsolutePath() + "/" + "smeltingRecipes.json", SmeltingJsonRecipe.class);
        
        achievementReader = new JsonConfigReader<AchievementType>(token, baseDir.getAbsolutePath() + "/" + "customAchievements.json",
                AchievementType.class);

//        if (CompatUtil.isDifficultyRecipesLoaded())
//        {            
//            EnumMapInstanceCreator<ShapedJsonDifficultyRecipe> creatorShaped = new EnumMapInstanceCreator<ShapedJsonDifficultyRecipe>();
//            
//            shapedDifficultyReader = new JsonConfigReader<ShapedJsonDifficultyRecipe>(token, baseDir.getAbsolutePath() + "/difficultyrecipes/shaped.json", ShapedJsonDifficultyRecipe.class);
//            shapedDifficultyReader.getBuilder().registerTypeAdapter(new TypeToken<EnumMap<Difficulty, ShapedJsonRecipe>>(){}.getType(), creatorShaped);
//            CompatUtil.registerDifficultyAdapter(shapedDifficultyReader.getBuilder());
//            
//            EnumMapInstanceCreator<ShapelessJsonDifficultyRecipe> creatorShapeless = new EnumMapInstanceCreator<ShapelessJsonDifficultyRecipe>();
//            
//            shapelessDifficultyReader = new JsonConfigReader<ShapelessJsonDifficultyRecipe>(token, baseDir.getAbsolutePath() + "/difficultyrecipes/shapeless.json", ShapelessJsonDifficultyRecipe.class);
//            shapelessDifficultyReader.getBuilder().registerTypeAdapter(new TypeToken<EnumMap<Difficulty, ShapelessJsonRecipe>>(){}.getType(), creatorShapeless);
//            CompatUtil.registerDifficultyAdapter(shapelessDifficultyReader.getBuilder());
//        }

        assembleResourcePack();

        addAll(armorReader.getElements());
        addAll(toolReader.getElements());
        addAll(itemReader.getElements());
        addAll(recordReader.getElements());
        addAll(blockReader.getElements());

        BlockType.registerBlocks();
        
        if (FMLCommonHandler.instance().getSide().isClient()) 
        {
            registerItemModels();
        }
    }
    
    public static void assembleResourcePack()
    {
        assembler = new ResourcePackAssembler(new File(baseDir.getAbsolutePath() + "/CustomThings-Resourcepack"), "Custom Things Resource Pack", CustomThings.MODID);
        // .setHasPackPng(CustomThings.class);
        
        addIcons(assembler);
        addBlockstates(assembler);
        addModels(assembler);
        addLangs(assembler);
        addCustoms(assembler);

        assembler.assemble().inject();
    }

    private static void addIcons(ResourcePackAssembler assembler)
    {
        initialize("icons");
        for (File f : new File(baseDir.getAbsolutePath() + "/icons").listFiles((FileFilter) FileFilterUtils.or((IOFileFilter) EnderFileUtils.pngFilter, FileFilterUtils.suffixFileFilter(".mcmeta"))))
        {
            assembler.addIcon(f);
        }
    }
    
    private static void addBlockstates(ResourcePackAssembler assembler)
    {
        initialize("blockstates");
        for (File f : new File(baseDir.getAbsolutePath() + "/blockstates").listFiles((FileFilter) FileFilterUtils.suffixFileFilter(".json")))
        {
            assembler.addModel(f, ModelType.BLOCKSTATE);
        }
    }
    
    private static void addModels(ResourcePackAssembler assembler)
    {
        initialize("models/item");
        for (File f : new File(baseDir.getAbsolutePath() + "/models/item").listFiles((FileFilter) FileFilterUtils.suffixFileFilter(".json")))
        {
            assembler.addModel(f, ModelType.ITEM);
        }
        initialize("models/block");
        for (File f : new File(baseDir.getAbsolutePath() + "/models/block").listFiles((FileFilter) FileFilterUtils.suffixFileFilter(".json")))
        {
            assembler.addModel(f, ModelType.BLOCK);
        }
    }

    private static void addLangs(ResourcePackAssembler assembler)
    {
        initialize("lang");
        for (File f : new File(baseDir.getAbsolutePath() + "/lang").listFiles(EnderFileUtils.langFilter))
        {
            assembler.addLang(f);
        }
    }

    private static void addCustoms(ResourcePackAssembler assembler)
    {
        initialize("recordMusic");
        JsonObject root = new JsonObject();
        for (File f : new File(baseDir.getAbsolutePath() + "/recordMusic").listFiles((FileFilter) FileFilterUtils.suffixFileFilter(".ogg")))
        {
            assembler.addCustomFile("assets/minecraft/sounds/records", f); // add record .ogg
            JsonObject event = new JsonObject();
            event.addProperty("category", "record"); // put under the "record" category for sound options
            JsonArray sounds = new JsonArray(); // array of sounds (will only ever be one)
            JsonObject sound = new JsonObject(); // sound object (instead of primitive to use 'stream' flag)
            sound.addProperty("name", "records/" + getSimpleName(f)); // path to file
            sound.addProperty("stream", true); // prevents lag for large files
            sounds.add(sound);
            event.add("sounds", sounds);
            root.add("records." + CustomThings.MODID + "." + getSimpleName(f), event); // event name (same as name sent to ItemCustomRecord)
        }
        assembler.addCustomFile("assets/minecraft", EnderFileUtils.writeToFile(baseDir.getAbsolutePath() + "/recordMusic/sounds.json", gson.toJson(root)));
    }

    private static String getSimpleName(File file)
    {
        return file.getName().substring(0, file.getName().indexOf('.'));
    }

    private static void initialize(String dir)
    {
        File temp = new File(baseDir.getAbsolutePath() + "/" + dir);
        temp.mkdirs();
    }

    private static List<IJsonType> allTypesCache = Lists.newArrayList();
    
    private static void registerItemModels() 
    {
        for (int i = 0; i < ItemType.types.size(); i++)
        {
            ItemType type = ItemType.types.get(i);
            ModelLoader.setCustomModelResourceLocation(ItemType.getItem(), i, new ModelResourceLocation(new ResourceLocation(CustomThings.MODID, type.name), "inventory"));
        }
    }
    
    public static void init()
    {

//        addAll(fluidReader.getElements());
    }

    public static void postInit()
    {
        addAll(shapedReader.getElements());
        addAll(shapelessReader.getElements());
        addAll(smeltingReader.getElements());
        addAll(achievementReader.getElements());

//        if (CompatUtil.isDifficultyRecipesLoaded())
//        {
//            addAll(shapedDifficultyReader.getElements());
//            addAll(shapelessDifficultyReader.getElements());
//        }

        for (IJsonType type : allTypesCache)
        {
            type.postInit();
        }
    }
    
    private static void addAll(Iterable<? extends IJsonType> types)
    {
        for (IJsonType type : types) 
        {
            type.register();
            allTypesCache.add(type);
        }
    }
}
