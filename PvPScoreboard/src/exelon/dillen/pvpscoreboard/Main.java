package exelon.dillen.pvpscoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import com.earth2me.essentials.Essentials;

import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin implements Listener {

	static Plugin plugin;

	public Economy economy = null;

	public Essentials essentials = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");

	public void onEnable() {
		plugin = this;
		getServer().getPluginManager().registerEvents(this, this);

		if (!setupEconomy()) {
			Bukkit.getLogger().severe(String.format("Äh det här funkade ju inte! Du har ju inge economy plugin!",
					getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

	}

	@EventHandler
	public void playerJoin(PlayerJoinEvent e) {

		Player p = e.getPlayer();

		Team spelare;
		Team pengar;
		Team mord;
		Team deaths;

		ScoreboardManager manager = this.getServer().getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		Objective o = board.registerNewObjective("PvP", "Dummy");
		o.setDisplayName(ChatColor.DARK_AQUA + "Exelon PvP");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);

		spelare = board.registerNewTeam("spelare");
		spelare.addEntry("Spelare: " + ChatColor.AQUA);
		o.getScore("Spelare: " + ChatColor.AQUA).setScore(9);
		spelare.setPrefix("");
		spelare.setSuffix("");

		pengar = board.registerNewTeam("pengar");
		pengar.addEntry("Pengar: " + ChatColor.AQUA);
		o.getScore("Pengar: " + ChatColor.AQUA).setScore(8);
		pengar.setPrefix("");
		pengar.setSuffix("");

		mord = board.registerNewTeam("mord");
		mord.addEntry("Mord: " + ChatColor.AQUA);
		o.getScore("Mord: " + ChatColor.AQUA).setScore(6);
		mord.setPrefix("");
		mord.setSuffix("");

		deaths = board.registerNewTeam("deaths");
		deaths.addEntry("Dödsfall: " + ChatColor.AQUA);
		o.getScore("Dödsfall: " + ChatColor.AQUA).setScore(5);
		deaths.setPrefix("");
		deaths.setSuffix("");

		Team du = board.registerNewTeam("du");
		du.addEntry("Du: " + ChatColor.AQUA);
		o.getScore("Du: " + ChatColor.AQUA).setScore(3);
		du.setPrefix("");
		du.setSuffix(p.getName());

		Score ip = o.getScore(ChatColor.DARK_AQUA + "mc.exelonserver.se");
		ip.setScore(1);

		Score tom4 = o.getScore("    ");
		tom4.setScore(10);

		Score tom = o.getScore(" ");
		tom.setScore(2);

		Score tom2 = o.getScore("  ");
		tom2.setScore(4);

		Score tom3 = o.getScore("   ");
		tom3.setScore(7);

		p.setScoreboard(board);

		new BukkitRunnable() {
			public void run() {
				
				if(!(Bukkit.getServer().getOnlinePlayers().contains(p)))
					this.cancel();
				
				spelare.setSuffix(
						Bukkit.getServer().getOnlinePlayers().size() + "/" + Bukkit.getServer().getMaxPlayers());

				pengar.setSuffix("$" + economy.getBalance(p));

				mord.setSuffix(p.getStatistic(Statistic.PLAYER_KILLS) + "");

				deaths.setSuffix(p.getStatistic(Statistic.DEATHS) + "");
			}
		}.runTaskTimer(this, 0, 20 * 5);

	}

	private boolean setupEconomy() {
		RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> economyProvider = getServer().getServicesManager()
				.getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}

		return (economy != null);
	}
	
	
}
