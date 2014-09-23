package tterrag.customthings.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import tterrag.customthings.CustomThings;
import tterrag.customthings.common.config.json.ArmorType;

public class ItemCustomArmor extends ItemArmor
{
    public String textureName;
    public ItemCustomArmor(ArmorType type, int slot)
    {
        super(type.getMaterial(), 0, slot);
        setTextureName(type.name);
        this.textureName = CustomThings.MODID.toLowerCase() + ":textures/items/" + type.getTextureName(slot);
    }
    
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
        return textureName;
    }
}
