package pluginminecraft;

import game.Spawn;
import java.util.ArrayList;
import listeners.BlockListener;
import listeners.EntityListener;
import listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

/**
 * Plugin UltraHardCore
 *
 * @author Guillaume Golfieri
 */
public class SpeedUHC extends JavaPlugin {

    // Constantes
    public int TIME_GAME = 30;

    private Spawn spawn;
    private Location posArene;
    private static Server server;
    private static ConsoleCommandSender console;
    private PluginManager pm;
    private ScoreboardManager scoreboardManager;
    private BukkitScheduler scheduler;
    private int temp_restant = TIME_GAME - 5;

    // Les listeners
    private PlayerListener playerListener;
    private BlockListener blockListener;
    private EntityListener entityListener;

    public ArrayList<String> joueurs;
    public ArrayList<String> joueursAlive;
    public ArrayList<Team> teams;
    public ArrayList<Location> spawns;
    public Etat etat;
    public Score score;
    public Scoreboard scoreboard;

    @Override
    public void onEnable() {

        // Déclaration
        spawn = new Spawn();
        spawns = new ArrayList();
        joueurs = new ArrayList();
        joueursAlive = new ArrayList();
        playerListener = new PlayerListener(this);
        blockListener = new BlockListener(this);
        entityListener = new EntityListener(this);

        // Recupération des informations
        server = this.getServer();
        scheduler = server.getScheduler();
        console = this.getServer().getConsoleSender();
        scoreboardManager = Bukkit.getScoreboardManager();
        scoreboard = scoreboardManager.getNewScoreboard();
        etat = Etat.ENABLE;

        // On supprime toutes les tâches en cours
        scheduler.cancelAllTasks();

        // On modifie l'eternal day et la regeneration de vie
        this.delay(new Runnable() {
            @Override
            public void run() {
                allPlayersMessage("Définition des variables d'environnement", ChatColor.GOLD);
                execCommand("time set 6000");
                execCommand("gamerule doDaylightCycle false");
                execCommand("gamerule naturalRegeneration false");
            }
        }, secondesToTick(5));

        // On enregistre les listeners
        pm = this.getServer().getPluginManager();
        pm.registerEvents(playerListener, this);
        pm.registerEvents(blockListener, this);
        pm.registerEvents(entityListener, this);

    }

    @Override
    public void onDisable() {

        // On supprime toutes les tâches en cours
        scheduler.cancelAllTasks();

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // On teste si on a un argument
        if (args.length == 0) {
            return false;
        }

        // Récupère le joueur
        Player player = (Player) sender;
        
        // On teste si le joueur est OP
        if(!player.isOp()) {
            return false;
        }

        // On regarde l'argument
        switch (args[0]) {

            // Prepare l'UHC
            case "prepare":
                UHC_prepare(sender);
                break;

            // Prepare l'UHC
            case "time":
                player.sendMessage(ChatColor.BLUE + "Le temps de la partie a été modifié à " + args[1]);
                TIME_GAME = Integer.valueOf(args[1]);
                temp_restant = TIME_GAME - 5;
                break;

            // Prepare l'UHC
            case "run":
                UHC_run(sender);
                break;

            // Ajoute un spawn
            case "addSpawn":
                player.sendMessage(ChatColor.BLUE + "Le spawn a été ajouté");
                spawns.add(player.getLocation());
                break;

            // Reset les spawns
            case "resetSpawn":
                player.sendMessage(ChatColor.BLUE + "Les spawns ont été reset");
                spawns.clear();
                break;

            // Ajoute une arene
            case "addArene":
                player.sendMessage(ChatColor.BLUE + "L'arene a été ajouté");
                posArene = player.getLocation();
                spawn.addArene(sender);
                break;

            // Help for moderators
            case "help":
                player.sendMessage(ChatColor.BLUE + "---- Help ----");
                player.sendMessage(ChatColor.BLUE + "/uhc addSpawn Ajoute un spawn");
                player.sendMessage(ChatColor.BLUE + "/uhc resetSpawn Reset les spawns");
                player.sendMessage(ChatColor.BLUE + "/uhc addArene Ajoute l'arene");
                player.sendMessage(ChatColor.BLUE + "/uhc prepare Prépare l'UHC");
                player.sendMessage(ChatColor.BLUE + "/uhc run Lance l'UHC");
                player.sendMessage(ChatColor.BLUE + "/uhc time <temps> Changer le temps de la partie");
                break;

            // La commande n'a pas été trouvée
            default:
                player.sendMessage(ChatColor.RED + "La commande n'existe pas /uhc help pour avoir les commandes");
                break;

        }

        return true;
    }

    /**
     * Execute une commande serveur
     *
     * @param commande
     */
    public static void execCommand(String commande) {
        server.dispatchCommand(console, commande);
    }

    /**
     * Execute un Runnable avec un delay
     *
     * @param fonction
     * @param delay
     */
    public void delay(Runnable fonction, int delay) {
        server.getScheduler().scheduleSyncDelayedTask(this, fonction, delay);
    }

    /**
     * Convertit un nombre de secondes en tick et le retourne
     *
     * @param secondes
     * @return
     */
    public int secondesToTick(int secondes) {
        return secondes * 20;
    }

    /**
     * Send a message to all players with a color
     *
     * @param msg
     * @param color
     */
    public void allPlayersMessage(String msg, ChatColor color) {
        Player[] players = getServer().getOnlinePlayers();
        for (Player player : players) {
            player.sendMessage(color + msg);
        }
    }

    /**
     * Prépare l'UHC
     *
     * @param sender Joueur qui a utilisé la commande
     */
    public void UHC_prepare(CommandSender sender) {

        // Récupère le joueur
        Player player = (Player) sender;

        // Envoie un message au joueur
        player.sendMessage(ChatColor.BLUE + "L'UHC est en cours de lancement...");
        allPlayersMessage("Génération du spawn et de l'arène. Vous allez être téléporté dans 10 secondes...", ChatColor.GOLD);

        // On change l'état de l'UHC
        etat = Etat.PREPARE;

        // On ajoute le spawn
        spawn.addSpawn(sender);

        // Téléportations des joueurs dans le spawn
        this.delay(new Runnable() {

            @Override
            public void run() {
                Player[] players = server.getOnlinePlayers();
                for (Player playerTmp : players) {
                    playerTmp.teleport(new Location(playerTmp.getWorld(), 10, 205, 10));
                }
            }
        }, secondesToTick(10));

    }

    /**
     * Lance l'UHC
     *
     * @param sender Joueur qui a utilisé la commande
     */
    public void UHC_run(CommandSender sender) {

        // Récupère le joueur
        Player player = (Player) sender;

        // On teste s'il y a assez de spawns pour tous les joueurs
        if (getServer().getOnlinePlayers().length > spawns.size()) {
            player.sendMessage(ChatColor.RED + "Il n'y pas assez de spawns");
        } else {
            player.sendMessage(ChatColor.BLUE + "L'UHC a été lancé");

            // Temps restant
            Objective tempRestant = scoreboard.registerNewObjective("Temps restant", "Temps restant");
            tempRestant.setDisplaySlot(DisplaySlot.SIDEBAR);
            tempRestant.setDisplayName("Speed UHC");
            score = tempRestant.getScore(ChatColor.GREEN + "Temps restant:");
            score.setScore(temp_restant);

            // Gestion des joueurs
            Player[] players = getServer().getOnlinePlayers();
            for (Player playerTmp : players) {
                int cpt = (int) Math.round(Math.random()) * (spawns.size() - 1);
                joueurs.add(playerTmp.getName());
                joueursAlive.add(playerTmp.getName());

                playerTmp.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, secondesToTick(60 * (TIME_GAME - 5)), 1));
                playerTmp.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, secondesToTick(60 * (TIME_GAME - 5)), 1));
                playerTmp.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, secondesToTick(60 * (TIME_GAME - 5)), 1));

                playerTmp.setGameMode(GameMode.SURVIVAL);
                playerTmp.getEquipment().clear();
                playerTmp.getInventory().clear();
                playerTmp.setHealth(20.0);
                playerTmp.setFoodLevel(20);
                playerTmp.getEquipment().clear();
                playerTmp.teleport(spawns.get(cpt));
                playerTmp.setScoreboard(scoreboard);
                spawns.remove(cpt);
            }

            // L'UHC est lancé
            allPlayersMessage("Let's go!! GL & HF. 1 min d'invincibilité. " + Integer.toString(TIME_GAME - 5) + "min avant la bataille finale", ChatColor.GOLD);
            etat = Etat.RUNNING;

            // Lancement de l'arene
            this.delay(new Runnable() {
                @Override
                public void run() {
                    entityListener.invincible = false;
                    allPlayersMessage("Fin de l'invincibilité", ChatColor.RED);
                }
            }, secondesToTick(60));

            // Temps restant
            scheduler.scheduleSyncRepeatingTask(this, new Runnable() {

                @Override
                public void run() {
                    temp_restant--;
                    score.setScore(temp_restant);
                }
            }, secondesToTick(60), secondesToTick(60));

            // Message 10 minutes restants
            this.delay(new Runnable() {
                @Override
                public void run() {
                    allPlayersMessage("Bataille finale dans 10 minutes!", ChatColor.GOLD);
                }
            }, secondesToTick(60 * (TIME_GAME - 15)));

            // Message 5 minutes restants
            this.delay(new Runnable() {
                @Override
                public void run() {
                    allPlayersMessage("Bataille finale dans 5 minutes!", ChatColor.GOLD);
                }
            }, secondesToTick(60 * (TIME_GAME - 10)));

            // Lancement de l'arene
            this.delay(new Runnable() {
                @Override
                public void run() {
                    allPlayersMessage("[Fin] Bataille finale !", ChatColor.RED);

                    // Gestion des joueurs
                    Player[] players = getServer().getOnlinePlayers();
                    for (Player playerTmp : players) {
                        Location loc = posArene;
                        loc.setX(posArene.getX() - 20 + Math.round(Math.random() * 20));
                        loc.setY(200);
                        loc.setZ(posArene.getZ() - 20 + Math.round(Math.random() * 20));
                        playerTmp.teleport(loc);
                    }

                    entityListener.invincible = true;

                    server.getScheduler().cancelAllTasks();

                    // Lancement de l'arene
                    delay(new Runnable() {
                        @Override
                        public void run() {
                            entityListener.invincible = false;
                        }
                    }, secondesToTick(10));

                }
            }, secondesToTick(60 * (TIME_GAME - 5)));

        }

    }

}
