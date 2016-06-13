package tterrag.customthings.common.handlers;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AchievementEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import tterrag.customthings.common.config.json.AchievementType;
import tterrag.customthings.common.config.json.AchievementType.AchievementData;
import tterrag.customthings.common.config.json.AchievementType.AchievementSource;

import com.enderio.core.EnderCore;
import com.enderio.core.common.Handlers.Handler;
import com.enderio.core.common.util.ItemUtil;

@Handler
public class AchievementHandler
{
    @SubscribeEvent
    public void onBlockBreak(BreakEvent event)
    {
        triggerAchievement(AchievementSource.BLOCK_BREAK, event.getPlayer(), event.getState());
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event)
    {
        if (event.getSource().getSourceOfDamage() instanceof EntityPlayer)
        {
            triggerAchievement(AchievementSource.ENTITY_KILL, (EntityPlayer) event.getSource().getSourceOfDamage(), event.getEntityLiving());
        }
    }

    @SubscribeEvent
    public void onItemPickup(ItemPickupEvent event)
    {
        triggerAchievement(AchievementSource.ITEM_PICKUP, event.player, event.pickedUp.getEntityItem());
    }

    @SubscribeEvent
    public void onItemCrafted(ItemCraftedEvent event)
    {
        triggerAchievement(AchievementSource.CRAFTING, event.player, event.crafting);
    }
    
    private static boolean ignoreAchievement = false;
    
    @SubscribeEvent
    public void onAchievement(final AchievementEvent event)
    {
        if (ignoreAchievement || event.getEntityPlayer().worldObj.isRemote || ((EntityPlayerMP)event.getEntityPlayer()).getStatFile().hasAchievementUnlocked(event.getAchievement()))
        {
            return;
        }
        
        AchievementPage page = null;
        for (AchievementPage p : AchievementPage.getAchievementPages())
        {
            if (p.getAchievements().contains(event.getAchievement()))
            {
                page = p;
            }
        }
       
        final AchievementPage pf = page;
        EnderCore.proxy.getScheduler().schedule(50, new Runnable()
        {
            @Override
            public void run()
            {
                ignoreAchievement = true;
                triggerAchievement(AchievementSource.ACHIEVEMENT_PAGE, event.getEntityPlayer(), pf, event.getEntityPlayer(), event.getAchievement());
                ignoreAchievement = false;
            }
        });
    }

    private static void triggerAchievement(AchievementSource source, EntityPlayer player, Object... in)
    {
        if (!player.worldObj.isRemote && player instanceof EntityPlayerMP) // LP made me do it
        {
            for (AchievementType type : AchievementType.lookup.get(source))
            {
                if (source.matchesObject(type.sourceObj, in))
                {
                    if (type.rewardStack != null)
                    {
                        StatisticsManagerServer file = ((EntityPlayerMP) player).getStatFile();
                        if (!file.hasAchievementUnlocked(type.achievement) && file.canUnlockAchievement(type.achievement))
                        {
                            ItemStack stack = type.rewardStack.copy();
                            if (type.rewardNBT != null) {
                                stack = ItemUtil.parseStringIntoItemStack(ItemUtil.getStringForItemStack(stack, true, true) + "$" + AchievementType.Template.format(type.rewardNBT, new AchievementData(type.achievement, player)));
                            }
                            if (!player.inventory.addItemStackToInventory(stack))
                            {
                                player.worldObj.spawnEntityInWorld(new EntityItem(player.worldObj, player.posX, player.posY, player.posZ,
                                        type.rewardStack.copy()));
                            }
                        }
                    }
                    player.addStat(type.achievement, 1);
                }
            }
        }
    }
}
