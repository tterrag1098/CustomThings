package tterrag.customthings.common.item;

import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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
    public ItemCustomArmor(ArmorType type, int slot)
    {
        super(type.getMaterial(), 0, slot);
        this.type = type;
        setTextureName(type.getIconName(slot));
        setUnlocalizedName(type.getUnlocName(slot));
        this.textureName = CustomThings.MODID.toLowerCase() + ":textures/items/" + type.getTextureName(slot) + ".png";
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
        return textureName;
    }
    
    @Override
    public int getMaxDamage(ItemStack stack)
    {
        return type.durabilities[armorType];
    }
    
    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot)
    {
        if (!source.isUnblockable())
        {
            return new ArmorProperties(type.priorities[armorType], type.protectionRatios[armorType], type.protectionMaxes[armorType]);
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
