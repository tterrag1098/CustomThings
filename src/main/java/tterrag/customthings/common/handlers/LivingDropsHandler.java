package tterrag.customthings.common.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import tterrag.customthings.common.config.json.items.RecordType;

import com.enderio.core.common.Handlers.Handler;
import com.enderio.core.common.Handlers.Handler.HandlerType;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@Handler(HandlerType.FORGE)
public class LivingDropsHandler
{
    private static final Random rand = new Random();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingDrop(LivingDropsEvent event)
    {
        if (event.entityLiving instanceof EntityCreeper)
        {
            boolean foundRecord = false;
            Iterator<EntityItem> iter = event.drops.iterator();
            while (iter.hasNext())
            {
                EntityItem e = iter.next();
                if (e.getEntityItem().getItem() instanceof ItemRecord)
                {
                    foundRecord = true;
                }
            }

            if (foundRecord)
            {
                addRandomRecordToDrops(event.drops);
            }
        }
    }

    private void addRandomRecordToDrops(ArrayList<EntityItem> drops)
    {
        ArrayList<EntityItem> temp = new ArrayList<EntityItem>();
        temp.addAll(drops);
        drops.clear();

        for (EntityItem item : temp)
        {
            if (item.getEntityItem().getItem() instanceof ItemRecord)
            {
                int max = 12 + RecordType.getLootRecordCount();
                int random = rand.nextInt(max);
                ItemStack record;
                if (random < 12)
                {
                    int id = Item.getIdFromItem(Items.record_13) + random;
                    record = new ItemStack(Item.getItemById(id));
                }
                else
                {
                    random -= 12;
                    record = getDroppableRecord(random);
                }

                EntityItem entity = new EntityItem(item.worldObj, item.posX, item.posY, item.posZ, record);
                drops.add(entity);
            }
            else
            {
                drops.add(item);
            }
        }
    }

    private ItemStack getDroppableRecord(int random)
    {
        if (RecordType.getType(random).isCreeperLoot)
        {
            return new ItemStack(RecordType.getItem(random));
        }
        else
        {
            return getDroppableRecord(rand.nextInt(RecordType.getNumRecords()));
        }
    }
}
