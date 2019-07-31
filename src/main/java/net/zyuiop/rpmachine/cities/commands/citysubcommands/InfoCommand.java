package net.zyuiop.rpmachine.cities.commands.citysubcommands;

import net.zyuiop.rpmachine.RPMachine;
import net.zyuiop.rpmachine.cities.CitiesManager;
import net.zyuiop.rpmachine.cities.data.City;
import net.zyuiop.rpmachine.cities.data.CityFloor;
import net.zyuiop.rpmachine.commands.SubCommand;
import net.zyuiop.rpmachine.economy.EconomyManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class InfoCommand implements SubCommand {

    private final CitiesManager citiesManager;

    public InfoCommand(CitiesManager citiesManager) {
        this.citiesManager = citiesManager;
    }

    @Override
    public String getUsage() {
        return "[ville]";
    }

    @Override
    public String getDescription() {
        return "affiche les informations de la ville actuelle ou de celle passée en argument.";
    }

    @Override
    public boolean run(Player player, String[] args) {
        City target;
        if (args.length > 0) {
            target = citiesManager.getCity(args[0]);
        } else {
            if (!player.getLocation().getWorld().getName().equalsIgnoreCase("world")) {
                player.sendMessage(ChatColor.RED + "Vous n'êtes dans aucune ville actuellement.");
                return false;
            }

            target = citiesManager.getCityHere(player.getLocation().getChunk());
            if (target == null) {
                player.sendMessage(ChatColor.RED + "Vous n'êtes dans aucune ville actuellement.");
                return false;
            }
        }

        if (target != null) {
            player.sendMessage(ChatColor.YELLOW + "-----[ Ville de " + ChatColor.GOLD + target.getCityName() + ChatColor.YELLOW + " ]-----");
            player.sendMessage(ChatColor.YELLOW + "Maire actuel : " + RPMachine.database().getUUIDTranslator().getName(target.getMayor()));
            player.sendMessage(ChatColor.YELLOW + "Nombre d'habitants : " + target.countInhabitants());
            player.sendMessage(ChatColor.YELLOW + "Type de ville : " + ((target.isRequireInvite() ? ChatColor.RED + "Sur invitation" : ChatColor.GREEN + "Publique")));
            player.sendMessage(ChatColor.YELLOW + "Impôts : " + target.getTaxes() + " " + EconomyManager.getMoneyName() + " par semaine");

            CityFloor floor = citiesManager.getFloor(target);
            player.sendMessage(ChatColor.YELLOW + "Palier : " + floor.getName());

            if (player.getUniqueId().equals(target.getMayor()) || target.getCouncils().contains(player.getUniqueId())) {
                player.sendMessage(ChatColor.YELLOW + "Monnaie : " + target.getMoney() + " " + EconomyManager.getMoneyName());
                player.sendMessage(ChatColor.YELLOW + "Taille : " + target.getChunks().size() + " / " + floor.getMaxsurface());
            }
        }
        return true;
    }
}
