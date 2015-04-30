package tterrag.customthings.common.config;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

import org.apache.commons.io.filefilter.FileFilterUtils;

import tterrag.core.common.config.JsonConfigReader;
import tterrag.core.common.config.JsonConfigReader.ModToken;
import tterrag.core.common.util.ResourcePackAssembler;
import tterrag.core.common.util.TTFileUtils;
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

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ConfigHandler
{
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static File baseDir;

    private static JsonConfigReader<ArmorType> armorReader;
    private static JsonConfigReader<ToolType> toolReader;
    private static JsonConfigReader<ItemType> itemReader;
    private static JsonConfigReader<RecordType> recordReader;
    private static JsonConfigReader<BlockType> blockReader;
    private static JsonConfigReader<ShapedJsonRecipe> shapedReader;
    private static JsonConfigReader<ShapelessJsonRecipe> shapelessReader;
    private static JsonConfigReader<SmeltingJsonRecipe> smeltingReader;
    private static JsonConfigReader<AchievementType> achievementReader;
    
    private static ResourcePackAssembler assembler;

    public static void preInit(FMLPreInitializationEvent event)
    {
        baseDir = new File(event.getSuggestedConfigurationFile().getParent() + "/" + CustomThings.MODID);
        assembler = new ResourcePackAssembler(new File(baseDir.getAbsolutePath() + "/CustomThings-Resourcepack"), "Custom Things Resource Pack", CustomThings.MODID);
        // .setHasPackPng(CustomThings.class);

        ModToken token = new ModToken(CustomThings.class, CustomThings.MODID + "/misc");
        armorReader = new JsonConfigReader<ArmorType>(token, baseDir.getAbsolutePath() + "/" + "customArmors.json", ArmorType.class);
        toolReader = new JsonConfigReader<ToolType>(token, baseDir.getAbsolutePath() + "/" + "customTools.json", ToolType.class);
        itemReader = new JsonConfigReader<ItemType>(token, baseDir.getAbsolutePath() + "/" + "customItems.json", ItemType.class);
        recordReader = new JsonConfigReader<RecordType>(token, baseDir.getAbsolutePath() + "/" + "customRecords.json", RecordType.class);
        blockReader = new JsonConfigReader<BlockType>(token, baseDir.getAbsolutePath() + "/" + "customBlocks.json", BlockType.class);
        shapedReader = new JsonConfigReader<ShapedJsonRecipe>(token, baseDir.getAbsolutePath() + "/" + "shapedRecipes.json", ShapedJsonRecipe.class);
        shapelessReader = new JsonConfigReader<ShapelessJsonRecipe>(token, baseDir.getAbsolutePath() + "/" + "shapelessRecipes.json", ShapelessJsonRecipe.class);
        smeltingReader = new JsonConfigReader<SmeltingJsonRecipe>(token, baseDir.getAbsolutePath() + "/" + "smeltingRecipes.json", SmeltingJsonRecipe.class);
        achievementReader = new JsonConfigReader<AchievementType>(token, baseDir.getAbsolutePath() + "/" + "customAchievements.json", AchievementType.class);
        
        addIcons(assembler);
        addLangs(assembler);
        addCustoms(assembler);

        assembler.assemble().inject();
    }

    private static void addIcons(ResourcePackAssembler assembler)
    {
        initialize("icons");
        for (File f : new File(baseDir.getAbsolutePath() + "/icons").listFiles(TTFileUtils.pngFilter))
        {
            assembler.addIcon(f);
        }
    }

    private static void addLangs(ResourcePackAssembler assembler)
    {
        initialize("lang");
        for (File f : new File(baseDir.getAbsolutePath() + "/lang").listFiles(TTFileUtils.langFilter))
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
        assembler.addCustomFile("assets/minecraft", TTFileUtils.writeToFile(baseDir.getAbsolutePath() + "/recordMusic/sounds.json", gson.toJson(root)));
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
    
    public static void init()
    {
        addAll(armorReader.getElements());
        addAll(toolReader.getElements());
        addAll(itemReader.getElements());
        addAll(recordReader.getElements());
        addAll(blockReader.getElements());
        
        BlockType.registerBlocks();
    }
    
    public static void postInit()
    {
        addAll(shapedReader.getElements());
        addAll(shapelessReader.getElements());
        addAll(smeltingReader.getElements());
        addAll(achievementReader.getElements());

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
