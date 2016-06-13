package tterrag.customthings.common.item;

import lombok.Getter;
import lombok.experimental.Delegate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import tterrag.customthings.CustomThings;
import tterrag.customthings.common.config.json.items.ArmorType;

public class ItemCustomArmor extends ItemArmor implements ISpecialArmor, ICustomRepair<ArmorType>
{
    private String textureName;
    @Getter
    private ArmorType type;
    
    @Delegate
    private final ItemProxy<ArmorType, ItemCustomArmor> proxy = new ItemProxy<ArmorType, ItemCustomArmor>(this);
    
    public ItemCustomArmor(ArmorType type, EntityEquipmentSlot slot)
    {
        super(type.getMaterial(), 0, slot);
        this.type = type;
        setUnlocalizedName(type.getUnlocName(slot));
    }
    
    @Override
    public int getMaxDamage(ItemStack stack)
    {
        return type.durabilities[armorType.getIndex()];
    }
    
    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot)
    {
        if (!source.isUnblockable())
        {
            return new ArmorProperties(type.priorities[armorType.getIndex()], type.protectionRatios[armorType.getIndex()], type.protectionMaxes[armorType.getIndex()]);
        }
        return new ArmorProperties(0, 0, 0);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot)
    {
        return type.protectionDisplays[slot];
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot)
    {
        stack.damageItem(damage, entity);
    }
    
    @Override
    public boolean getIsRepairable(ItemStack stack, ItemStack material)
    {
        return ItemCustomPickaxe.repairMatMatchesOredict(stack, material);
    }
    
    @Override
    public ArmorType getType(ItemStack stack)
    {
        return type;
    }
}
