package tterrag.customthings.common.command;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import tterrag.customthings.common.config.ConfigHandler;

public class CommandCustomThings extends CommandBase
{
    public static String[] ARGS = { "reloadResources" };

    @Override
    public String getCommandName()
    {
        return "customthings";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_)
    {
        return "customthings.command.usage";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender p_71515_1_, String[] args)
    {
        if (args.length < 1)
        {
            throw new WrongUsageException("customthings.command.noArgs");
        }

        String cmd = args[0];
        if (ARGS[0].equals(cmd))
        {
            ConfigHandler.assembleResourcePack();
            Minecraft.getMinecraft().refreshResources();
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List addTabCompletionOptions(ICommandSender player, String[] args)
    {
        return getListOfStringsMatchingLastWord(args, ARGS);
    }
}
