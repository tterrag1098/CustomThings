package tterrag.customthings;

import static tterrag.customthings.CustomThings.*;
import tterrag.core.IModTT;
import tterrag.core.common.compat.CompatabilityRegistry;
import tterrag.core.common.util.RegisterTime;
import tterrag.customthings.common.config.ConfigHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = MODID, name = NAME, version = VERSION, dependencies = "required-after:ttCore@[MC1.7.10-0.0.3-36,)")
public class CustomThings implements IModTT
{
    public static final String MODID = "customthings";
    public static final String NAME = "Custom Things";
    public static final String VERSION = "@VERSION@";
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ConfigHandler.preInit(event);
        CompatabilityRegistry.INSTANCE.registerCompat(RegisterTime.POSTINIT, "tterrag.customthings.common.nei.NEIHider", "NotEnoughItems");
    }
    
    @EventHandler
    public void preInit(FMLInitializationEvent event)
    {
        ConfigHandler.init();
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
