package tterrag.customthings;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import tterrag.customthings.common.command.CommandCustomThings;
import tterrag.customthings.common.config.ConfigHandler;
import tterrag.customthings.common.config.json.items.ItemType;
import tterrag.customthings.common.handlers.FuelHandler;

import com.enderio.core.IEnderMod;
import com.enderio.core.common.compat.CompatRegistry;
import com.enderio.core.common.util.RegisterTime;

import static tterrag.customthings.CustomThings.*;

@Mod(modid = MODID, name = NAME, version = VERSION, dependencies = DEPENDENCIES)
public class CustomThings implements IEnderMod
{
    public static final String MODID = "customthings";
    public static final String NAME = "Custom Things";
    public static final String VERSION = "@VERSION@";
    public static final String DEPENDENCIES = "after:endercore";
    
    public CustomThings() {
        ConfigHandler.preInit();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        CompatRegistry.INSTANCE.registerCompat(RegisterTime.POSTINIT, "tterrag.customthings.common.nei.NEIHider", "NotEnoughItems");
        if (event.getSide().isClient())
        {
            ClientCommandHandler.instance.registerCommand(new CommandCustomThings());
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        ConfigHandler.init();
        GameRegistry.registerFuelHandler(new FuelHandler());
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
