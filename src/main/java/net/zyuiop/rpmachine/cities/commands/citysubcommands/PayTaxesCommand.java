package net.zyuiop.rpmachine.cities.commands.citysubcommands;

import net.zyuiop.rpmachine.RPMachine;
import net.zyuiop.rpmachine.cities.CitiesManager;
import net.zyuiop.rpmachine.cities.data.City;
import net.zyuiop.rpmachine.commands.SubCommand;
import net.zyuiop.rpmachine.economy.EconomyManager;
import net.zyuiop.rpmachine.economy.TaxPayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PayTaxesCommand implements SubCommand {

    private final CitiesManager citiesManager;

    public PayTaxesCommand(CitiesManager citiesManager) {
        this.citiesManager = citiesManager;
    }

    @Override
    public String getUsage() {
        return "<ville>";
    }

    @Override
    public String getDescription() {
        return "paye vos impôts en retard";
    }

    @Override
    public boolean run(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Utilisation incorrecte : utilisez /city paytaxes " + getUsage());
            return false;
        } else {
            City city = citiesManager.getCity(args[0]);
            if (city == null) {
                player.sendMessage(ChatColor.RED + "Cette ville n'exite pas.");
                return false;
            }

            TaxPayer payer = RPMachine.getPlayerRoleToken(player).getTaxPayer();
            double topay = payer.getUnpaidTaxes(city.getCityName());
            if (topay == 0D) {
                player.sendMessage(ChatColor.GREEN + "Vous ne devez pas d'argent à cette ville.");
            } else {
                double amount = payer.getMoney();
                if (amount >= topay) {
                    RPMachine.getInstance().getEconomyManager().withdrawMoney(player.getUniqueId(), topay);
                    payer.setUnpaidTaxes(city.getCityName(), 0D);
                    player.sendMessage(ChatColor.GREEN + "Vous ne devez plus rien à cette ville.");
                    city.pay(payer, topay);
                } else {
                    RPMachine.getInstance().getEconomyManager().withdrawMoney(player.getUniqueId(), amount);
                    city.setMoney(city.getMoney() + amount);
                    topay = topay - amount;
                    payer.setUnpaidTaxes(city.getCityName(), topay);
                    player.sendMessage(ChatColor.RED + "Vous devez encore " + topay + " " + EconomyManager.getMoneyName() + " à la ville.");
                    city.pay(payer, amount);
                }
                citiesManager.saveCity(city);
            }
            return true;
        }
    }
}
