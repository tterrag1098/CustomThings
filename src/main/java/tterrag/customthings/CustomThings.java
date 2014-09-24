package tterrag.customthings;

import tterrag.core.IModTT;
import tterrag.core.common.compat.CompatabilityRegistry;
import tterrag.core.common.util.RegisterTime;
import tterrag.customthings.common.config.ConfigHandler;
import tterrag.customthings.common.nei.NEIHider;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import static tterrag.customthings.CustomThings.*;

@Mod(modid = MODID, name = NAME, version = VERSION, dependencies = "required-after:ttCore@1.7.10-0.0.2-12,)")
public class CustomThings implements IModTT
{
    public static final String MODID = "customthings";
    public static final String NAME = "Custom Things";
    public static final String VERSION = "@VERSION@";
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ConfigHandler.preInit(event);
        CompatabilityRegistry.instance().registerCompat(RegisterTime.POSTINIT, NEIHider.class, "NotEnoughItems");
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
