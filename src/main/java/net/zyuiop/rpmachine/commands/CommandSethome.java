package net.zyuiop.rpmachine.commands;

import net.zyuiop.rpmachine.RPMachine;
import net.zyuiop.rpmachine.common.VirtualLocation;
import net.zyuiop.rpmachine.database.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSethome implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player))
            return false;

        Player player = (Player) commandSender;
        if (!player.getWorld().getName().equalsIgnoreCase("world")) {
            player.sendMessage(ChatColor.RED + "Vous ne pouvez fixer votre domicile que dans l'overworld.");
            return true;
        }
        VirtualLocation home = new VirtualLocation(player.getLocation());
        PlayerData data = RPMachine.database().getPlayerData(player.getUniqueId());
        data.setHome(home);
        player.sendMessage(ChatColor.GREEN + "Votre domicile a bien été fixé !");
        return true;
    }
}
