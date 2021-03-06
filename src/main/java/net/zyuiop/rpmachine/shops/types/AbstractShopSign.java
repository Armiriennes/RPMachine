package net.zyuiop.rpmachine.shops.types;

import net.zyuiop.rpmachine.RPMachine;
import net.zyuiop.rpmachine.common.VirtualLocation;
import net.zyuiop.rpmachine.cities.City;
import net.zyuiop.rpmachine.database.StoredEntity;
import net.zyuiop.rpmachine.entities.RoleToken;
import net.zyuiop.rpmachine.entities.Ownable;
import net.zyuiop.rpmachine.permissions.ShopPermissions;
import net.zyuiop.rpmachine.utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import javax.annotation.Nullable;

public abstract class AbstractShopSign implements Ownable, StoredEntity {
    private String fileName;
    private String owner;
    protected VirtualLocation location;
    protected double price;

    public AbstractShopSign() { }

    public AbstractShopSign(Location location) {
        this.location = new VirtualLocation(location);
    }

    @Nullable
    @Override
    public String ownerTag() {
        return owner;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Location getLocation() {
        return location.getLocation();
    }

    public double getPrice() {
        return price;
    }

    public void rightClick(Player player, PlayerInteractEvent event) {
        RoleToken token = RPMachine.getPlayerRoleToken(player);
        if (!owner.equals(token.getTag())) {
            clickUser(player, event);
        } else {
            clickPrivileged(player, token, event);
        }

        RPMachine.getInstance().getShopsManager().save(this);
    }

    public boolean breakSign(Player player) {
        if (player == null) {
            breakSign();
            return true;
        }

        RoleToken token = RPMachine.getPlayerRoleToken(player);
        if (owner.equals(token.getTag())) {
            if (!token.checkDelegatedPermission(ShopPermissions.DESTROY_SHOP)) {
                player.sendMessage(ChatColor.RED + "Impossible de casser ce shop!");
                return false;
            }

            breakSign();
            player.sendMessage(ChatColor.GREEN + "Le shop a bien été supprimé.");
            return true;
        } else {
            player.sendMessage(ChatColor.RED + "Impossible de casser ce shop!");
        }

        return false;
    }

    protected City getCity() {
        return RPMachine.getInstance().getCitiesManager().getCityHere(location.getLocation().getChunk());
    }

    protected double creditToOwner() {
        double toThePlayer = takeVat();
        owner().creditMoney(toThePlayer);
        return toThePlayer;
    }

    protected double takeVat() {
        City city = getCity();
        double vat = city == null ? RPMachine.getInstance().getProjectsManager().getGlobalVat() : city.getVat();
        if (vat == 0)
            return price;

        double forTheCity = price * vat;
        double forThePlayer = price - forTheCity;

        if (city != null) {
            city.creditMoney(forTheCity);
            Messages.credit(city, forTheCity, "TVA automatique");
        }

        return forThePlayer;
    }


    /**
     * Renders the sign
     */
    public abstract void display();

    /**
     * Prints the debug info to the player
     */
    public abstract void debug(Player player);

    /**
     * Breaks the sign, effectively removing the block and its content
     */
    public void breakSign() {
        RPMachine.getInstance().getShopsManager().remove(this);
        location.getLocation().getBlock().breakNaturally();
    }

    /**
     * Use the sign as a privileged user (someone acting on behalf of the owner)
     * @param player the player using the sign
     * @param token the entity the player is acting on behalf of
     * @param event the event that triggered the action
     */
    abstract void clickPrivileged(Player player, RoleToken token, PlayerInteractEvent event);

    /**
     * Use the sign as a simple user
     * @param player the user using the sign
     * @param event the event that triggered the action
     */
    abstract void clickUser(Player player, PlayerInteractEvent event);

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String describe() {
        return ChatColor.GOLD + "(" + ChatColor.YELLOW + getLocation().getBlockX() + "; " + getLocation().getBlockY() + "; " + getLocation().getBlockZ() + ChatColor.GOLD + ") ";
    }
}
