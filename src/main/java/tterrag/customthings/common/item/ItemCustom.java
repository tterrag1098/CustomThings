package tterrag.customthings.common.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import tterrag.customthings.CustomThings;
import tterrag.customthings.common.config.json.items.ItemType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCustom extends Item
{
    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    public ItemCustom()
    {
        super();
        setCreativeTab(CreativeTabs.tabMisc);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register)
    {
        List<ItemType> types = ItemType.getTypes();
        icons = new IIcon[types.size()];
        for (int i = 0; i < types.size(); i++)
        {
            icons[i] = register.registerIcon(CustomThings.MODID.toLowerCase() + ":" + types.get(i).name);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        List<ItemType> types = ItemType.getTypes();
        for (int i = 0; i < types.size(); i++)
        {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return ItemType.getType(stack.getItemDamage()).getUnlocName();
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass)
    {
        return stack == null ? null : getIconFromDamage(stack.getItemDamage());
    }

    @Override
    public IIcon getIconFromDamage(int damage)
    {
        return damage < icons.length ? icons[damage] : null;
    }

    @Override
    public boolean hasContainerItem(ItemStack stack)
    {
        return ItemType.getType(stack.getItemDamage()).getContainerItem() != null;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack)
    {
        ItemStack container = ItemType.getType(stack.getItemDamage()).getContainerItem();
        if (container != null)
        {
            return container.copy();
        }
        return null;
    }
}
