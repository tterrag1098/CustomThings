package tterrag.customthings.common.command;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import tterrag.customthings.common.config.ConfigHandler;

public class CommandCustomThings extends CommandBase
{
    public static final String[] ARGS = { "reloadResources" };

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
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
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

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        return getListOfStringsMatchingLastWord(args, ARGS);
    }
}
