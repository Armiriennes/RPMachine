package net.zyuiop.rpmachine.shops;

import net.zyuiop.rpmachine.RPMachine;
import net.zyuiop.rpmachine.VirtualLocation;
import net.zyuiop.rpmachine.database.StoredEntity;
import net.zyuiop.rpmachine.entities.RoleToken;
import net.zyuiop.rpmachine.entities.Ownable;
import net.zyuiop.rpmachine.permissions.ShopPermissions;
import org.bukkit.Bukkit;
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

    public void setPrice(double price) {
        this.price = price;
    }

    public void rightClick(Player player, PlayerInteractEvent event) {
        RoleToken token = RPMachine.getPlayerRoleToken(player);
        if (!owner.equals(token.getTag())) {
            clickUser(player, event);
        } else {
            clickPrivileged(player, token, event);
        }
    }

    public boolean breakSign(Player player) {
        if (player == null) {
            breakSign();
            return true;
        }

        RoleToken token = RPMachine.getPlayerRoleToken(player);
        if (owner.equals(token.getTag())) {
            if (!token.checkDelegatedPermission(ShopPermissions.DESTROY_SHOP))
                return false;

            breakSign();
            return true;
        }

        return false;
    }

    /**
     * Renders the sign
     */
    public abstract void display();

    /**
     * Breaks the sign, effectively removing the block and its content
     */
    public void breakSign() {
        location.getLocation().getBlock().breakNaturally();
        RPMachine.getInstance().getShopsManager().remove(this);
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
}
