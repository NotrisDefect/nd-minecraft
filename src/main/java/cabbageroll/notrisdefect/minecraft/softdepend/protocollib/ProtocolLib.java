package cabbageroll.notrisdefect.minecraft.softdepend.protocollib;

import cabbageroll.notrisdefect.minecraft.Main;
import cabbageroll.notrisdefect.minecraft.menus.SkinMenu;
import cabbageroll.notrisdefect.minecraft.playerdata.BuiltInSkins;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface ProtocolLib {

    @SuppressWarnings("deprecation")
    static void sendBlockChangeCustom(Player player, Location loc, int color) {
        XMaterial xm = (Main.gs.getData(player).isCustom() ? Main.gs.getSkin(player) : BuiltInSkins.DEFAULTSKIN).get(color);

        if (xm == SkinMenu.EXISTING_MATERIAL) {
            resendBlock(player, loc);
        } else {
            if (Main.plugin.numericalVersion < 13) {
                player.sendBlockChange(loc, xm.parseItem().getType(), xm.parseItem().getData().getData());
            } else {
                player.sendBlockChange(loc, xm.parseItem().getType().createBlockData());
            }
        }
    }

    @SuppressWarnings("deprecation")
    static void resendBlock(Player player, Location loc) {
        if (Main.plugin.numericalVersion < 13) {
            player.sendBlockChange(loc, loc.getBlock().getBlockData());
        } else {
            player.sendBlockChange(loc, loc.getBlock().getType(), loc.getBlock().getData());
        }
    }

    void sendActionBarCustom(Player player, String message);

    void sendFallingBlockCustom(Player player, Location loc, int color, double xVel, double yVel, double zVel);

    void sendTitleCustom(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut);

}
