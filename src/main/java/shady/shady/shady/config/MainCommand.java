package shady.shady.shady.config;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import shady.shady.shady.Shady;
import shady.shady.shady.features.dungeonscanner.DungeonScanner;
import shady.shady.shady.features.dungeonscanner.DungeonScannerGui;
import shady.shady.shady.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MainCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "sh";
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<String>(){{
            add("shady");
            add("shadyaddons");
        }};
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if(!Shady.enabled) {
            Utils.sendMessage("&cUnknown command. Try /help for a list of commands");
            return;
        }

        if(args.length > 0) {
            switch(args[0]) {
                case "force_dungeon":
                    Utils.forceDungeon = !Utils.forceDungeon;
                    Utils.sendModMessage("Toggled Forcing Dungeon");
                    break;

                case "force_skyblock":
                    Utils.forceSkyBlock = !Utils.forceSkyBlock;
                    Utils.sendModMessage("Toggled Forcing SkyBlock");
                    break;

                case "copy_core":
                    Utils.copyToClipboard(DungeonScanner.getCore(
                            Shady.mc.thePlayer.getPosition().getX(),
                            Shady.mc.thePlayer.getPosition().getZ()
                    ));
                    break;

                case "scan":
                    if(Utils.inDungeon) {
                        Shady.guiToOpen = new DungeonScannerGui();
                        Utils.sendModMessage("Thanks for testing this unstable feature! Report any bugs you encounter or feedback you have!");
                    } else {
                        Utils.sendModMessage("You have to be in dungeons to use this command");
                    }
                    break;

                case "rescan":
                    if(Utils.inDungeon) {
                        DungeonScanner.reScan();
                        Utils.sendModMessage("Refreshed dungeon scan");
                    } else {
                        Utils.sendModMessage("You have to be in dungeons to use this command");
                    }
                    break;

                case "disable":
                    Shady.enabled = false;
                    break;
            }
        } else {
            Shady.guiToOpen = new ConfigGui();
        }
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if(args.length == 1 && Shady.enabled) {
            return getListOfStringsMatchingLastWord(args, "force_dungeon", "force_skyblock", "get_block", "copy_core", "scan", "rescan", "disable");
        }
        return null;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

}