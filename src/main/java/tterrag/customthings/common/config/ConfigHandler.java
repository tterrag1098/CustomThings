package tterrag.customthings.common.config;

import java.io.File;
import java.io.IOException;

import tterrag.core.common.util.IOUtils;
import tterrag.core.common.util.ResourcePackAssembler;
import tterrag.customthings.CustomThings;
import tterrag.customthings.common.config.json.ArmorType;
import tterrag.customthings.common.config.json.ToolType;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ConfigHandler
{
    public static File baseDir;

    private static JsonConfigReader<ArmorType> armorReader;
    private static JsonConfigReader<ToolType> toolReader;

    private static ResourcePackAssembler assembler;

    public static void preInit(FMLPreInitializationEvent event)
    {
        baseDir = new File(event.getSuggestedConfigurationFile().getParent() + "/" + CustomThings.MODID);
        assembler = new ResourcePackAssembler(new File(baseDir.getAbsolutePath() + "/CustomThings-Resourcepack"), "Custom Things Resource Pack", CustomThings.MODID);
//                .setHasPackPng(CustomThings.class);

        addIcons(assembler);
        addLangs(assembler);

        try
        {
            assembler.assemble().inject(new File(baseDir.getParentFile().getParentFile().getAbsolutePath() + "/resourcepacks"));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        
        armorReader = new JsonConfigReader<ArmorType>(baseDir.getAbsolutePath() + "/" + "customArmors.json", ArmorType.class);
        toolReader = new JsonConfigReader<ToolType>(baseDir.getAbsolutePath() + "/" + "customTools.json", ToolType.class);
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
    
    private static void initialize(String dir)
    {
        File temp = new File(baseDir.getAbsolutePath() + "/" + dir);
        temp.mkdirs();
    }

    public static void init()
    {
        ArmorType.addAll(armorReader.getElements());
        ToolType.addAll(toolReader.getElements());
    }
}
