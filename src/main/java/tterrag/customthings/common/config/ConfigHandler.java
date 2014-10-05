package tterrag.customthings.common.config;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.filefilter.FileFilterUtils;

import tterrag.core.common.json.JsonUtils;
import tterrag.core.common.util.IOUtils;
import tterrag.core.common.util.ResourcePackAssembler;
import tterrag.customthings.CustomThings;
import tterrag.customthings.common.config.json.ArmorType;
import tterrag.customthings.common.config.json.ItemType;
import tterrag.customthings.common.config.json.RecordType;
import tterrag.customthings.common.config.json.ToolType;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ConfigHandler
{
    public static File baseDir;

    private static JsonConfigReader<ArmorType> armorReader;
    private static JsonConfigReader<ToolType> toolReader;
    private static JsonConfigReader<ItemType> itemReader;
    private static JsonConfigReader<RecordType> recordReader;

    private static ResourcePackAssembler assembler;

    public static void preInit(FMLPreInitializationEvent event)
    {
        baseDir = new File(event.getSuggestedConfigurationFile().getParent() + "/" + CustomThings.MODID);
        assembler = new ResourcePackAssembler(new File(baseDir.getAbsolutePath() + "/CustomThings-Resourcepack"), "Custom Things Resource Pack", CustomThings.MODID);
        // .setHasPackPng(CustomThings.class);

        armorReader = new JsonConfigReader<ArmorType>(baseDir.getAbsolutePath() + "/" + "customArmors.json", ArmorType.class);
        toolReader = new JsonConfigReader<ToolType>(baseDir.getAbsolutePath() + "/" + "customTools.json", ToolType.class);
        itemReader = new JsonConfigReader<ItemType>(baseDir.getAbsolutePath() + "/" + "customItems.json", ItemType.class);
        recordReader = new JsonConfigReader<RecordType>(baseDir.getAbsoluteFile() + "/" + "customRecords.json", RecordType.class);

        addIcons(assembler);
        addLangs(assembler);
        addCustoms(assembler);

        assembler.assemble().inject();
    }

    private static void addIcons(ResourcePackAssembler assembler)
    {
        initialize("icons");
        for (File f : new File(baseDir.getAbsolutePath() + "/icons").listFiles(IOUtils.pngFilter))
        {
            assembler.addIcon(f);
        }
    }

    private static void addLangs(ResourcePackAssembler assembler)
    {
        initialize("lang");
        for (File f : new File(baseDir.getAbsolutePath() + "/lang").listFiles(IOUtils.langFilter))
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
        assembler.addCustomFile("assets/minecraft", IOUtils.writeToFile(baseDir.getAbsolutePath() + "/recordMusic/sounds.json", JsonUtils.gson.toJson(root)));
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

    public static void init()
    {
        ArmorType.addAll(armorReader.getElements());
        ToolType.addAll(toolReader.getElements());
        ItemType.addAll(itemReader.getElements());
        RecordType.addAll(recordReader.getElements());

        if (ItemType.getTypes().size() > 0)
        {
            ItemType.register();
        }
    }
}
