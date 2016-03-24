package tterrag.customthings.common.config.json;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.oredict.OreDictionary;

import com.enderio.core.common.util.ItemUtil;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

public class AchievementType extends JsonType
{
    public enum AchievementSource
    {
        CRAFTING
        {
            @Override
            protected Object getObjectFromString(String s)
            {
                return ItemUtil.parseStringIntoRecipeItem(s);
            }

            @Override
            public boolean matchesObject(Object test, Object... in)
            {
                Object obj = in[0];
                if (obj instanceof String)
                {
                    if (test instanceof ItemStack)
                    {
                        return ItemUtil.itemStackMatchesOredict((ItemStack) test, (String) obj);
                    }
                    else
                    {
                        return obj.equals(test);
                    }
                }
                else if (obj instanceof Block || obj instanceof Item)
                {
                    return obj == test;
                }
                else if (obj instanceof ItemStack && test instanceof ItemStack)
                {
                    ItemStack i1 = (ItemStack) obj;
                    ItemStack i2 = (ItemStack) test;

                    return i1.getItem() == i2.getItem()
                            && (i1.getItemDamage() == i2.getItemDamage() || i2.getItemDamage() == OreDictionary.WILDCARD_VALUE);
                }
                else
                {
                    return false;
                }
            }
        },
        ITEM_PICKUP
        {
            @Override
            protected Object getObjectFromString(String s)
            {
                return CRAFTING.getObjectFromString(s);
            }

            @Override
            public boolean matchesObject(Object test, Object... in)
            {
                return CRAFTING.matchesObject(test, in);
            }
        },
        BLOCK_BREAK
        {
            @Override
            protected Object getObjectFromString(String s)
            {
                return ItemUtil.parseStringIntoItemStack(s);
            }

            @Override
            public boolean matchesObject(Object test, Object... in)
            {
                ItemStack block = (ItemStack) test;
                if (in[0] instanceof Block && in[1] instanceof Integer)
                {
                    return Item.getItemFromBlock((Block) in[0]) == block.getItem() && ((Integer)in[1] == block.getItemDamage() || block.getItemDamage() == OreDictionary.WILDCARD_VALUE);
                }
                return false;
            }
        },
        ENTITY_KILL
        {
            @Override
            protected Object getObjectFromString(String s)
            {
                return s;
            }

            @Override
            public boolean matchesObject(Object test, Object... in)
            {
                if (in[0] instanceof EntityLivingBase)
                {
                    String entityName = (String) EntityList.classToStringMapping.get(in[0].getClass());
                    if (entityName != null)
                    {
                        return entityName.toLowerCase().contains(((String) test).toLowerCase());
                    }
                    return in[0].getClass().getName().toLowerCase().contains(((String) test).toLowerCase());
                }
                return false;
            }
        };

        protected abstract Object getObjectFromString(String s);

        public abstract boolean matchesObject(Object test, Object... in);
    }

    /* JSON Fields @formatter:off */
    public int x = 0;
    public int y = 0;
    public String page = null; // null means minecraft
    public String parent = null;
    public String source = "CRAFTING";
    public String required = "minecraft:stone";
    public String displayItem = null; // null means show required item
    public String reward = null;
    public boolean isSpecial = false;
    /* End JSON Fields @formatter:on */

    private transient Achievement parentAchievement = null;

    public transient Achievement achievement;
    public transient AchievementSource achievementSource;
    public transient Object sourceObj;
    
    public transient ItemStack rewardStack;

    public static final List<AchievementType> achievements = Lists.newArrayList();
    public static final Multimap<AchievementSource, AchievementType> lookup = HashMultimap.create();

    @SuppressWarnings("unchecked")
    @Override
    public void register()
    {
        if (parent != null)
        {
            for (Achievement a : (List<Achievement>) AchievementList.achievementList)
            {
                if (a.statId.equals(parent))
                {
                    parentAchievement = a;
                }
            }
        }

        if (displayItem == null)
        {
            displayItem = required;
        }

        achievement = new Achievement(name, name, x, y, ItemUtil.parseStringIntoItemStack(displayItem), parentAchievement);

        if (parentAchievement == null)
        {
            achievement.initIndependentStat();
        }
        if (isSpecial)
        {
            achievement.setSpecial();
        }

        AchievementPage achievementPage = page == null ? null : AchievementPage.getAchievementPage(page);
        boolean newPage = false;
        if (achievementPage == null && page != null)
        {
            achievementPage = new AchievementPage(page);
            newPage = true;
        }
        if (achievementPage != null)
        {
            if (newPage)
            {
                AchievementPage.registerAchievementPage(achievementPage);
            }
            achievementPage.getAchievements().add(achievement);
        }
        else
        {
            AchievementList.achievementList.add(achievement);
        }

        achievement.registerStat();

        achievementSource = AchievementSource.valueOf(source.toUpperCase());
        sourceObj = achievementSource.getObjectFromString(required);
        
        achievements.add(this);
        lookup.put(achievementSource, this);
    }

    @Override
    public void postInit()
    {
        if (reward != null)
        {
            rewardStack = ItemUtil.parseStringIntoItemStack(reward);
        }
    }
}
