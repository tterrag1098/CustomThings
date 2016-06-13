package tterrag.customthings.common.config.json.items;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

import org.apache.commons.lang3.ArrayUtils;

import tterrag.customthings.common.config.json.IHasMaterial;
import tterrag.customthings.common.item.ItemCustomArmor;

import com.enderio.core.common.util.ItemUtil;

import static net.minecraft.inventory.EntityEquipmentSlot.*;
 
public class ArmorType extends ItemType implements IHasMaterial
{
    /* JSON Fields @formatter:off */
    public int[]    armors              = {0, 1, 2, 3};
    public int[]    durabilities        = {100, 400, 500, 150};
    public int[]    protectionDisplays  = {1, 2, 3, 1};
    public double[] protectionRatios    = {0.1, 0.1, 0.1, 0.1};
    public int[]    protectionMaxes     = {5, 15, 20, 7};
    public int[]    priorities          = {0, 0, 0, 0};
    public int      enchantability      = 15;
    public String   material            = "null";
    /* End JSON Fields @formatter:on */

    private transient Item[] items;
    
    @Getter
    private transient ItemStack repairMat;

    public String getTextureName(int armorType)
    {
        int num = armorType == 2 ? 2 : 1;
        return name + num;
    }

    public String getMaterialName()
    {
        return name + "Material";
    }
    
    public ArmorMaterial getMaterial()
    {
        return ArmorMaterial.valueOf(getMaterialName());
    }

    private static final EntityEquipmentSlot[] VALID_SLOTS = { HEAD, CHEST, LEGS, FEET };
    private static final String[] names = { "Helm", "Chest", "Legs", "Boots" };
    public static final List<ArmorType> types = new ArrayList<ArmorType>();

    @Override
    public void register()
    {
        super.register();
        EnumHelper.addArmorMaterial(getMaterialName(), name, 0, protectionDisplays, enchantability, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, enchantability); // dummy, used for enchantability
        items = new Item[4];
        for (EntityEquipmentSlot slot : VALID_SLOTS)
        {
            if (ArrayUtils.contains(armors, slot.getIndex()))
            {
                items[slot.getIndex()] = new ItemCustomArmor(this, slot);
                GameRegistry.register(items[slot.getIndex()].setRegistryName(name + names[slot.getIndex()]));
                addOreDictNames(new ItemStack(items[slot.getIndex()]));
            }
        }
        types.add(this);
    }
    
    @Override
    public void postInit()
    {
        repairMat = material.equals("null") ? null : ItemUtil.parseStringIntoItemStack(material);
    }

    public String getUnlocName(EntityEquipmentSlot slot)
    {
        return name + names[slot.getIndex()];
    }

    public Item[] getItems()
    {
        return items;
    }
}
