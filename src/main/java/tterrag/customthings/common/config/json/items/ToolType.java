package tterrag.customthings.common.config.json.items;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

import org.apache.commons.lang3.StringUtils;

import tterrag.core.common.json.JsonUtils;
import tterrag.customthings.CustomThings;
import tterrag.customthings.common.config.json.JsonType;
import tterrag.customthings.common.item.ICustomTool;
import tterrag.customthings.common.item.ItemCustomAxe;
import tterrag.customthings.common.item.ItemCustomHoe;
import tterrag.customthings.common.item.ItemCustomPickaxe;
import tterrag.customthings.common.item.ItemCustomShovel;
import tterrag.customthings.common.item.ItemCustomSword;
import cpw.mods.fml.common.registry.GameRegistry;

public class ToolType extends JsonType
{
    public enum ToolClass
    {
        PICKAXE (ItemCustomPickaxe.class), 
        SHOVEL  (ItemCustomShovel.class), 
        AXE     (ItemCustomAxe.class), 
        SWORD   (ItemCustomSword.class), 
        HOE     (ItemCustomHoe.class);

        public final Class<? extends ICustomTool> itemClass;

        ToolClass(Class<? extends ICustomTool> itemClass)
        {
            this.itemClass = itemClass;
        }

        public String getUnlocName(String base)
        {
            return base + StringUtils.capitalize(this.name().toLowerCase());
        }
    }

    /* JSON Fields @formatter:off */    
    public String[]  tools          = {"PICKAXE", "SHOVEL", "AXE", "SWORD", "HOE"}; 
    public int       level          = 1;
    public int       durability     = 500;
    public float     efficiency     = 4.0f;
    public float     damage         = 1.0f;
    public int       enchantability = 5;
    public String    material       = "null";
    /* End JSON Fields @formatter:on */

    private transient Item pickaxe, shovel, axe, sword, hoe;
    
    @Getter
    private transient ItemStack repairMat;

    public List<ToolClass> getToolClasses()
    {
        List<ToolClass> list = new ArrayList<ToolClass>();
        for (String s : tools)
        {
            list.add(ToolClass.valueOf(s.toUpperCase()));
        }
        return list;
    }

    public ToolMaterial getToolMaterial()
    {
        return ToolMaterial.valueOf(name);
    }

    public static final List<ToolType> types = new ArrayList<ToolType>();

    @Override
    public void register()
    {        
        EnumHelper.addToolMaterial(name, level, durability, efficiency, damage, enchantability);

        for (ToolClass clazz : getToolClasses())
        {
            switch (clazz)
            {
            case PICKAXE:
                pickaxe = instantiate(clazz);
                GameRegistry.registerItem(pickaxe, clazz.getUnlocName(name));
                break;
            case AXE:
                axe = instantiate(clazz);
                GameRegistry.registerItem(axe, clazz.getUnlocName(name));
                break;
            case HOE:
                hoe = instantiate(clazz);
                GameRegistry.registerItem(hoe, clazz.getUnlocName(name));
                break;
            case SHOVEL:
                shovel = instantiate(clazz);
                GameRegistry.registerItem(shovel, clazz.getUnlocName(name));
                break;
            case SWORD:
                sword = instantiate(clazz);
                GameRegistry.registerItem(sword, clazz.getUnlocName(name));
                break;
            }
        }
        
        types.add(this);
    }
    
    @Override
    public void postInit()
    {
        repairMat = material.equals("null") ? null : JsonUtils.parseStringIntoItemStack(material);
    }

    private Item instantiate(ToolClass clazz)
    {
        try
        {
            return (Item) clazz.itemClass.getDeclaredConstructor(ToolType.class).newInstance(this);
        }
        catch (NoSuchMethodException e)
        {
            throw new NoSuchMethodError("Class " + clazz.itemClass.getName() + " must have a constructor that takes a ToolType object");
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public String getUnlocName(ToolClass toolClass)
    {
        return toolClass.getUnlocName(name);
    }

    public String getIconName(ToolClass toolClass)
    {
        return CustomThings.MODID.toLowerCase() + ":" + getUnlocName(toolClass);
    }

    public Item getItem(ToolClass toolClass)
    {
        switch(toolClass)
        {
            case PICKAXE: return pickaxe;
            case AXE:     return axe;
            case HOE:     return hoe;
            case SHOVEL:  return shovel;
            case SWORD:   return sword;
            default:      return null;
        }
    }
    
    public boolean hasItem(ToolClass toolClass)
    {
        return getItem(toolClass) != null;
    }
}
