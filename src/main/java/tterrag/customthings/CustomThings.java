package tterrag.customthings;

import static tterrag.customthings.CustomThings.*;
import net.minecraftforge.client.ClientCommandHandler;
import tterrag.core.IModTT;
import tterrag.core.common.compat.CompatabilityRegistry;
import tterrag.core.common.util.RegisterTime;
import tterrag.customthings.common.command.CommandCustomThings;
import tterrag.customthings.common.config.ConfigHandler;
import tterrag.customthings.common.config.json.items.ItemType;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = MODID, name = NAME, version = VERSION, dependencies = DEPENDENCIES)
public class CustomThings implements IModTT
{
    public static final String MODID = "customthings";
    public static final String NAME = "Custom Things";
    public static final String VERSION = "@VERSION@";
    public static final String DEPENDENCIES = "after:ttCore";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ConfigHandler.preInit(event);
        CompatabilityRegistry.INSTANCE.registerCompat(RegisterTime.POSTINIT, "tterrag.customthings.common.nei.NEIHider", "NotEnoughItems");
        if (event.getSide().isClient())
        {
            ClientCommandHandler.instance.registerCommand(new CommandCustomThings());
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        ConfigHandler.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        ConfigHandler.postInit();
    }

    @EventHandler
    public void onMissingMapping(FMLMissingMappingsEvent event)
    {
        for (MissingMapping m : event.get())
        {
            if (m.type == GameRegistry.Type.ITEM && m.name.contains("customthings.item"))
            {
                m.remap(ItemType.getItem());
            }
        }
    }

    @Override
    public String modid()
    {
        return MODID;
    }

    @Override
    public String name()
    {
        return NAME;
    }

    @Override
    public String version()
    {
        return VERSION;
    }
}
