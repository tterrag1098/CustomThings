package tterrag.customthings.common.config.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;
import tterrag.customthings.CustomThings;
import tterrag.customthings.common.item.ICustomTool;
import tterrag.customthings.common.item.ItemCustomPickaxe;
import cpw.mods.fml.common.registry.GameRegistry;

public class ToolType extends JsonType
{
    public static enum ToolClass
    {
        PICKAXE(ItemCustomPickaxe.class);//, SHOVEL, AXE, SWORD, HOE;
        
        public final Class<? extends ICustomTool> itemClass;
        ToolClass(Class<? extends ICustomTool> itemClass)
        {
            this.itemClass = itemClass;
        }
    }

    /* JSON Fields @formatter:off */    
    public String    type           = "PICKAXE";
    public int       level          = 1;
    public int       durability     = 500;
    public float     efficiency     = 4.0f;
    public float     damage         = 1.0f;
    public int       enchantability = 5;
    /* End JSON Fields @formatter:on */
    
    private transient Item item;

    public ToolClass getToolClass()
    {
        return ToolClass.valueOf(type.toUpperCase());
    }

    public ToolMaterial getToolMaterial()
    {
        return ToolMaterial.valueOf(name);
    }
    
    public static final List<ToolType> types = new ArrayList<ToolType>();

    private void register()
    {  
        try
        {
            item = (Item) this.getToolClass().itemClass.getDeclaredConstructor(ToolType.class).newInstance(this);
        }
        catch (NoSuchMethodException e)
        {
            throw new NoSuchMethodError("Class " + this.getToolClass().itemClass.getName() + " must have a constructor that takes a ToolType object");
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        
        GameRegistry.registerItem(item, name);
    }
    
    public String getUnlocName()
    {
        return name;
    }

    public String getIconName()
    {
        return CustomThings.MODID.toLowerCase() + ":" + getUnlocName();
    }
    
    public Item getItem()
    {
        return item;
    }
    
    public static void addType(ToolType type)
    {
        EnumHelper.addToolMaterial(type.name, type.level, type.durability, type.efficiency, type.damage, type.enchantability);
        types.add(type);
        type.register();
    }

    public static void addAll(Collection<? extends ToolType> col)
    {
        for (ToolType type : col)
        {
            addType(type);
        }
    }
}
