package net.zyuiop.rpmachine.multiverse;

import net.zyuiop.rpmachine.commands.AbstractCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Louis Vialar
 */
public class ListMultiversesCommand extends AbstractCommand {
    private final MultiverseManager manager;

    protected ListMultiversesCommand(MultiverseManager manager) {
        super("listworlds", "admin.listworlds");
        this.manager = manager;
    }

    @Override
    protected boolean onPlayerCommand(Player player, String command, String[] args) {
        for (MultiverseWorld w : manager.getWorlds()) {
            player.sendMessage(ChatColor.YELLOW +  "- " + w.getWorldName());

            for (MultiversePortal p : w.getPortals()) {
                player.sendMessage(ChatColor.GOLD + " - portail vers " + p.getTargetWorld() + " " + p.getPortalArea());
            }
        }
        return true;
    }
}
