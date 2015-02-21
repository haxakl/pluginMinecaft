package game;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Gestion des spawns
 *
 * @author Guillaume
 */
public class Spawn {

    private static final int LGR_SPAWN = 20;
    private static final int LGR_ARENE = 400;

    /**
     * Par défaut
     */
    public Spawn() {
    }

    /**
     * Ajoute le spawn
     *
     * @param sender
     */
    public void addSpawn(CommandSender sender) {

        // On créé le spawn
        for (int i = 0; i < LGR_SPAWN; i++) {
            for (int j = 0; j < LGR_SPAWN; j++) {
                this.addBlock(sender, i, 195, j, Material.WOOL);
                this.addBlock(sender, i, 205, j, Material.GLASS);

                if (j == 0 || j == (LGR_SPAWN - 1)) {
                    this.addBlock(sender, i, 196, j, Material.BRICK);
                    this.addBlock(sender, i, 197, j, Material.BRICK);
                    this.addBlock(sender, i, 198, j, Material.BRICK);
                    this.addBlock(sender, i, 199, j, Material.BRICK);
                    this.addBlock(sender, i, 200, j, Material.BRICK);
                    this.addBlock(sender, i, 201, j, Material.BRICK);
                    this.addBlock(sender, i, 202, j, Material.BRICK);
                    this.addBlock(sender, i, 203, j, Material.BRICK);
                    this.addBlock(sender, i, 204, j, Material.BRICK);
                }

                if (i == 0 || i == (LGR_SPAWN - 1)) {
                    this.addBlock(sender, i, 196, j, Material.BRICK);
                    this.addBlock(sender, i, 197, j, Material.BRICK);
                    this.addBlock(sender, i, 198, j, Material.BRICK);
                    this.addBlock(sender, i, 199, j, Material.BRICK);
                    this.addBlock(sender, i, 200, j, Material.BRICK);
                    this.addBlock(sender, i, 201, j, Material.BRICK);
                    this.addBlock(sender, i, 202, j, Material.BRICK);
                    this.addBlock(sender, i, 203, j, Material.BRICK);
                    this.addBlock(sender, i, 204, j, Material.BRICK);
                }
            }
        }

    }

    /**
     * Ajoute l'arene
     *
     * @param sender
     */
    public void addArene(CommandSender sender) {

        Location loc = ((Player) sender).getLocation();

        // On créé le spawn
        for (int i = -50; i < 51; i++) {
            for (int j = 0; j < 80; j++) {
                this.addBlock(sender, (int) loc.getX() + i, j + 40, (int) loc.getZ() - 50, Material.BEDROCK);
                this.addBlock(sender, (int) loc.getX() + i, j + 40, (int) loc.getZ() + 50, Material.BEDROCK);
                this.addBlock(sender, (int) loc.getX() - 50, j + 40, (int) loc.getZ() + i, Material.BEDROCK);
                this.addBlock(sender, (int) loc.getX() + 50, j + 40, (int) loc.getZ() + i, Material.BEDROCK);
            }
        }

    }

    /**
     * Ajoute un block
     *
     * @param sender
     * @param x
     * @param y
     * @param z
     * @param material
     */
    public void addBlock(CommandSender sender, int x, int y, int z, Material material) {
        Location loc = ((Player) sender).getLocation();
        loc.setX(x);
        loc.setY(y);
        loc.setZ(z);
        Block block = loc.getBlock();
        block.setType(material);
    }
}
