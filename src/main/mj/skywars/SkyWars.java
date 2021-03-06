/*
 * @author MJ
 * Created in 02.08.2018
 * Copyright (c) 2018 by MJ. All rights reserved.
 */

package main.mj.skywars;

import main.mj.skywars.commands.SetLocationCommand;
import main.mj.skywars.commands.SkyWarsMainCommand;
import main.mj.skywars.commands.StartCommand;
import main.mj.skywars.listener.*;
import main.mj.skywars.mysql.AsyncMySQL;
import main.mj.skywars.mysql.MySQLManager;
import main.mj.skywars.mysql.SQLSkyWars;
import main.mj.skywars.playerobject.KDManager;
import main.mj.skywars.playerobject.KitMenue;
import main.mj.skywars.playerobject.TeamManager;
import main.mj.skywars.utils.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class SkyWars extends JavaPlugin {

    private ConsoleCommandSender sender;

    //Commands
    private SetLocationCommand setLocationCommand;
    private SkyWarsMainCommand skyWarsMainCommand;
    private StartCommand startCommand;

    //Economy
    private static Economy economy;
    //MySQL
    private AsyncMySQL asyncMySQL;
    private MySQLManager sqlManager;
    private SQLSkyWars sqlSkyWars;
    //PlayerObject
    private KDManager kdManager;
    private KitMenue kitMenue;
    private DamageListener damageListener;
    private JoinListener joinListener;
    private TeamManager teamManager;
    //Listener
    private BlockListener blockListener;
    private PlayerDeathListener playerDeathListener;

    //Utils
    private ChestDetectionAndFill chestDetectionAndFill;
    private Data data;
    private ConfigUtil configUtil;
    private LobbyClickEvent lobbyClickEvent;
    private Game game;
    private GameScheduler gameScheduler;
    private LocationsUtil locationsUtil;
    private SchedulerSaver schedulerSaver;
    private StartScheduler startScheduler;
    private ItemCreator itemCreator;

    public void onEnable() {
        this.saveDefaultConfig();
        init();
        configUtil.loadDefaultConfig();
        configUtil.addDefaultChest();
        configUtil.kitFileDefault();
        configUtil.kitFileLoader();
        kitMenue.createKitInv();
        game.setGameState(GameEnum.LOBBY);
        sender.sendMessage(data.getPrefix() + "§awas successfully enabled!");
    }

    public void onDisable() {
        for (Player all : Bukkit.getOnlinePlayers()) {
            all.kickPlayer(" ");
        }
        game.setGameState(GameEnum.RESTART);
        schedulerSaver.cancelScheduler();
    }

    public static Economy getEconomy() {
        return economy;
    }

    public void setListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    public void setCommand (CommandExecutor commandExecutor, String command) {
        getCommand(command).setExecutor(commandExecutor);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    public Game getGame() {
        return game;
    }

    public ChestDetectionAndFill getChestDetectionAndFill() {
        return chestDetectionAndFill;
    }

    public ConfigUtil getConfigUtil() {
        return configUtil;
    }

    public LocationsUtil getLocationsUtil() {
        return locationsUtil;
    }

    public SchedulerSaver getSchedulerSaver() {
        return schedulerSaver;
    }

    public StartScheduler getStartScheduler() {
        return startScheduler;
    }

    public Data getData() {
        return data;
    }

    public ItemCreator getItemCreator() {
        return itemCreator;
    }

    public KitMenue getKitMenue() {
        return kitMenue;
    }

    public ConsoleCommandSender getSender() {
        return sender;
    }

    public AsyncMySQL getAsyncMySQL() {
        return asyncMySQL;
    }

    public MySQLManager getSqlManager() {
        return sqlManager;
    }

    public SQLSkyWars getSqlSkyWars() {
        return sqlSkyWars;
    }

    public KDManager getKdManager() {
        return kdManager;
    }

    private void init() {
        sender = Bukkit.getConsoleSender();

        //load conf and data
        configUtil = new ConfigUtil(this);
        data = new Data();
        if (data.isMySQLActive()) {
            sqlManager = new MySQLManager(this);
            asyncMySQL = new AsyncMySQL(this);
            sqlSkyWars = new SQLSkyWars(this);
        }

        //Commands
        setLocationCommand = new SetLocationCommand(this);
        skyWarsMainCommand = new SkyWarsMainCommand(this);
        startCommand = new StartCommand(this);

        //Listener
        blockListener = new BlockListener(this);
        damageListener = new DamageListener(this);
        joinListener = new JoinListener(this);
        lobbyClickEvent = new LobbyClickEvent(this);
        playerDeathListener = new PlayerDeathListener(this);

        //PlayerObject
        kdManager = new KDManager(this);
        kitMenue = new KitMenue(this);
        teamManager = new TeamManager(this);
        teamManager.createTeams();

        //Utils
        chestDetectionAndFill = new ChestDetectionAndFill(this);
        game = new Game(this);
        gameScheduler = new GameScheduler(this);
        itemCreator = new ItemCreator();
        locationsUtil = new LocationsUtil(this);
        locationsUtil.loadLocation();
        schedulerSaver = new SchedulerSaver();
        startScheduler = new StartScheduler(this);

        //Economy
        if (!setupEconomy()) {
            sender.sendMessage(data.getPrefix() + "§cVault dependency wasn't found - Disable Coins");
        }
    }

    public SetLocationCommand getSetLocationCommand() {
        return setLocationCommand;
    }

    public SkyWarsMainCommand getSkyWarsMainCommand() {
        return skyWarsMainCommand;
    }

    public StartCommand getStartCommand() {
        return startCommand;
    }

    public DamageListener getDamageListener() {
        return damageListener;
    }

    public JoinListener getJoinListener() {
        return joinListener;
    }

    public BlockListener getBlockListener() {
        return blockListener;
    }

    public PlayerDeathListener getPlayerDeathListener() {
        return playerDeathListener;
    }

    public LobbyClickEvent getLobbyClickEvent() {
        return lobbyClickEvent;
    }

    public GameScheduler getGameScheduler() {
        return gameScheduler;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }
}
