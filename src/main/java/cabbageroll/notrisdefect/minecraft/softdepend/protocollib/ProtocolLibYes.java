package cabbageroll.notrisdefect.minecraft.softdepend.protocollib;

import cabbageroll.notrisdefect.minecraft.Main;
import cabbageroll.notrisdefect.minecraft.playerdata.BuiltInSkins;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class ProtocolLibYes implements ProtocolLib {

    private final ProtocolManager pm = ProtocolLibrary.getProtocolManager();
    private final int version = Main.PLUGIN.VERSION;

    @Override
    public void sendFallingBlockCustom(Player player, Location loc, int color, double xVel, double yVel, double zVel) {
        if (version > 16) {
            return;
        }

        ItemStack block;
        if (Main.GS.getData(player).isCustom()) {
            block = Main.GS.getSkin(player).get(color).parseItem();
        } else {
            block = BuiltInSkins.DEFAULTSKIN.get(color).parseItem();
        }

        PacketContainer spawn = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);
        PacketContainer velocity = new PacketContainer(PacketType.Play.Server.ENTITY_VELOCITY);
        PacketContainer remove = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        int id = (int) (Math.random() * Integer.MAX_VALUE);

        //1.8 - 1.8.x (packet changed)
        //1.9 - 1.12.x (packet changed)
        //1.13 - 1.13.x (flattening changed data)
        //1.14 - 1.16.x (packet changed)
        //1.17+ not supported

        if (version == 8) {
            spawn.getIntegers().write(0, id);
            spawn.getIntegers().write(1, (int) (loc.getX() * 32.0D));
            spawn.getIntegers().write(2, (int) (loc.getY() * 32.0D));
            spawn.getIntegers().write(3, (int) (loc.getZ() * 32.0D));
            spawn.getIntegers().write(9, 70);
            spawn.getIntegers().write(10, block.getType().getId() + (block.getData().getData() << 12));
        } else if (version < 13) {
            spawn.getIntegers().write(0, id);
            spawn.getUUIDs().write(0, UUID.randomUUID());
            spawn.getDoubles().write(0, loc.getX());
            spawn.getDoubles().write(1, loc.getY());
            spawn.getDoubles().write(2, loc.getZ());
            spawn.getIntegers().write(6, 70);
            spawn.getIntegers().write(7, block.getType().getId() + (block.getData().getData() << 12));
        } else if (version == 13) {
            spawn.getIntegers().write(0, id);
            spawn.getUUIDs().write(0, UUID.randomUUID());
            spawn.getDoubles().write(0, loc.getX());
            spawn.getDoubles().write(1, loc.getY());
            spawn.getDoubles().write(2, loc.getZ());
            spawn.getIntegers().write(6, 70);

            try {
                BlockData blockData = block.getType().createBlockData();
                String nmsVersion = Main.PLUGIN.NMSVERSION;
                Class<?> c_Block = Class.forName("net.minecraft.server." + nmsVersion + ".Block");
                Class<?> c_IBlockData = Class.forName("net.minecraft.server." + nmsVersion + ".IBlockData");
                Class<?> c_CraftBlockData = Class.forName("org.bukkit.craftbukkit." + nmsVersion + ".block.data.CraftBlockData");
                Method m_getCombinedId = c_Block.getMethod("getCombinedId", c_IBlockData);
                Method m_getState = c_CraftBlockData.getMethod("getState");
                Object o_iBlockData = m_getState.invoke(blockData);
                int result = (int) m_getCombinedId.invoke(null, o_iBlockData);
                spawn.getIntegers().write(7, result);
            } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else if (version < 17) {
            spawn.getIntegers().write(0, id);
            spawn.getUUIDs().write(0, UUID.randomUUID());
            spawn.getDoubles().write(0, loc.getX());
            spawn.getDoubles().write(1, loc.getY());
            spawn.getDoubles().write(2, loc.getZ());
            spawn.getEntityTypeModifier().write(0, EntityType.FALLING_BLOCK);

            try {
                BlockData blockData = block.getType().createBlockData();
                String nmsVersion = Main.PLUGIN.NMSVERSION;
                Class<?> c_Block = Class.forName("net.minecraft.server." + nmsVersion + ".Block");
                Class<?> c_IBlockData = Class.forName("net.minecraft.server." + nmsVersion + ".IBlockData");
                Class<?> c_CraftBlockData = Class.forName("org.bukkit.craftbukkit." + nmsVersion + ".block.data.CraftBlockData");
                Method m_getCombinedId = c_Block.getMethod("getCombinedId", c_IBlockData);
                Method m_getState = c_CraftBlockData.getMethod("getState");
                Object o_iBlockData = m_getState.invoke(blockData);
                int result = (int) m_getCombinedId.invoke(null, o_iBlockData);
                spawn.getIntegers().write(6, result);
            } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        velocity.getIntegers().write(0, id);
        velocity.getIntegers().write(1, (int) (xVel * 8000.0D));
        velocity.getIntegers().write(2, (int) (yVel * 8000.0D));
        velocity.getIntegers().write(3, (int) (zVel * 8000.0D));

        remove.getIntegerArrays().write(0, new int[]{id});

        try {
            pm.sendServerPacket(player, spawn);
            pm.sendServerPacket(player, velocity);
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        pm.sendServerPacket(player, remove);
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }.runTaskLaterAsynchronously(Main.PLUGIN, 40L - (long) (Math.random() * 20));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendTitleCustom(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        if (version < 12) {
            PacketContainer pcTitle = new PacketContainer(PacketType.Play.Server.TITLE);
            PacketContainer pcSubtitle = new PacketContainer(PacketType.Play.Server.TITLE);
            PacketContainer pcTimes = new PacketContainer(PacketType.Play.Server.TITLE);

            pcTitle.getTitleActions().write(0, EnumWrappers.TitleAction.TITLE);
            pcTitle.getChatComponents().write(0, WrappedChatComponent.fromText(title));

            pcSubtitle.getTitleActions().write(0, EnumWrappers.TitleAction.SUBTITLE);
            pcSubtitle.getChatComponents().write(0, WrappedChatComponent.fromText(subtitle));

            pcTimes.getTitleActions().write(0, EnumWrappers.TitleAction.TIMES);
            pcTimes.getIntegers().write(0, fadeIn);
            pcTimes.getIntegers().write(1, stay);
            pcTimes.getIntegers().write(2, fadeOut);

            try {
                pm.sendServerPacket(player, pcTitle);
                pm.sendServerPacket(player, pcSubtitle);
                pm.sendServerPacket(player, pcTimes);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        }
    }

}
