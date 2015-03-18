package me.mttprvst13;

import java.io.File;
import java.io.Reader;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.jline.internal.InputStreamReader;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class plotMain extends JavaPlugin {

	Logger log = this.getLogger();
	public File plotsFile = new File(this.getDataFolder() + "/plots.yml");
	public FileConfiguration plots = YamlConfiguration
			.loadConfiguration(plotsFile);

	public void onEnable() {

		loadConfig();

		// if(plots.getString("plots.1/1.owner").equalsIgnoreCase("mttprvst13")){
		//
		// plots.set("plots.1/1.owner", "mttprvst13");
		//
		// }

		log.info("Enabled!");

	}

	public void onDisable() {

		savePlots();

		log.info("Disabled.");

	}

	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		int path = getConfig().getInt("PathSize");
		int plotx = getConfig().getInt("PlotSizeX");
		int plotz = getConfig().getInt("PlotSizeZ");
		int podzialx = getConfig().getInt("PlotSplitX");
		int podzialz = getConfig().getInt("PlotSplitZ");
		int wysokosc = getConfig().getInt("Height");
		String pathMaterial = getConfig().getString("Path_Material");
		String plotMaterial = getConfig().getString("Plot_Material");
		String podMaterial = getConfig().getString("Under_Plot_Material");
		return new plotGen(path, plotx, plotz, wysokosc, podzialx, podzialz,
				pathMaterial, plotMaterial, podMaterial, log);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (cmd.getName().equalsIgnoreCase("getplot")
				&& sender instanceof Player) {

			Player player = (Player) sender;

			String world = player.getWorld().getGenerator().getClass().getName();
			
			if (world != "me.mttprvst13.plotGen") {

				player.sendMessage(ChatColor.RED
						+ "You must be in the plotworld to do this.");
				return false;

			}

			sender.sendMessage(getPlot(player.getLocation().getX(), player
					.getLocation().getZ(), ((args.length == 1 && args[0].equalsIgnoreCase("debug")) ? true : false)));

			return false;

		} else if (cmd.getName().equalsIgnoreCase("getplot")) {

			sender.sendMessage(ChatColor.RED
					+ "You must be a player to send that command.");

		}

		new Commands(sender, cmd, label, args, this);

		return false;

	}

	public String getPlot(double x, double z, boolean debug) {
		
		String debuginfo = ChatColor.BOLD + "" + ChatColor.GREEN + "\nPlotPlug Getplot debug info:\n"
				+ ChatColor.RESET + ChatColor.AQUA;

		x = Math.round(x);
		z = Math.round(z);
		
		int plotx = getConfig().getInt("PlotSizeX");
		int plotz = getConfig().getInt("PlotSizeZ");
		int podzialx = getConfig().getInt("PlotSplitX");
		int podzialz = getConfig().getInt("PlotSplitZ");
		int road = getConfig().getInt("PathSize");

		int px = (plotx * podzialx) + road;
		int pz = (plotz * podzialz) + road;

		String plot;

		if (x > 0 && z < 0) {
			// z is negative
			z = Math.abs(z);

			double nx = x / px;
			double nz = z / pz;
			
			int plox = (int) Math.ceil(nx);
			int ploz = (int) Math.ceil(nz);

			plot = Integer.toString(plox) + " | -" + Integer.toString(ploz) + ((debug) ? debuginfo + "\n px=" + Integer.toString(px)
					+ " and pz=" + Integer.toString(pz) + " and x=" + Double.toString(x) + " and z="
					+ Double.toString(z) + " and nx=" + Double.toString(nx) + " and nz=" + 
					Double.toString(nz) : "");

		} else if (x < 0 && z > 0) {
			// x is negative
			x = Math.abs(x);

			double nx = x / px;
			double nz = z / pz;
			
			int plox = (int) Math.ceil(nx);
			int ploz = (int) Math.ceil(nz);

			plot = "-" + Integer.toString(plox) + " | " + Integer.toString(ploz) + ((debug) ? debuginfo + "\n px=" + Integer.toString(px)
					+ " and pz=" + Integer.toString(pz) + " and x=" + Double.toString(x) + " and z="
					+ Double.toString(z) + " and nx=" + Double.toString(nx) + " and nz=" + 
					Double.toString(nz) : "");

		} else if (x < 0 && z < 0) {
			// both is negative
			x = Math.abs(x);
			z = Math.abs(z);

			double nx = x / px;
			double nz = z / pz;
			
			int plox = (int) Math.ceil(nx);
			int ploz = (int) Math.ceil(nz);

			plot = "-" + Integer.toString(plox) + " | -" + Integer.toString(ploz) + ((debug) ? debuginfo + "\n px=" + Integer.toString(px)
					+ " and pz=" + Integer.toString(pz) + " and x=" + Double.toString(x) + " and z="
					+ Double.toString(z) + " and nx=" + Double.toString(nx) + " and nz=" + 
					Double.toString(nz) : "");

		} else {

			double nx = x / px;
			double nz = z / pz;
			
			int plox = (int) Math.ceil(nx);
			int ploz = (int) Math.ceil(nz);

			plot = Integer.toString(plox) + " | " + Integer.toString(ploz) + ((debug) ? debuginfo + "\n px=" + Integer.toString(px)
					+ " and pz=" + Integer.toString(pz) + " and x=" + Double.toString(x) + " and z="
					+ Double.toString(z) + " and nx=" + Double.toString(nx) + " and nz=" + 
					Double.toString(nz) : "");

		}

		return plot;

	}

	public void loadConfig() {

		this.saveDefaultConfig();
		loadPlots();

	}

	public void savePlots() {

		try {

			plots.save(plotsFile);
			Reader defConfigStream = new InputStreamReader(
					this.getResource("plots.yml"), "UTF8");

			YamlConfiguration defConfig = YamlConfiguration
					.loadConfiguration(defConfigStream);
			if (defConfig != null) {
				if(defConfig.getString("plots.1.owner").equalsIgnoreCase("mttprvst13")){
				plots.setDefaults(defConfig);
				plots.save(plotsFile);
				}else{
					log.info("not");
				}
				// this.saveResource("plots.yml", false);
			} else
				log.info("The default plots file could not bo loaded.");
		} catch (Exception e) {

			log.warning("There was an error trying to save the plots file.");
			e.printStackTrace();

		}

	}

	public void loadPlots() {

		if (plotsFile.exists()) {

			try {

				plots.load(plotsFile);

			} catch (Exception e) {

				log.warning("There was an error loading the plots file.");
				e.printStackTrace();

			}

		} else {

			savePlots();

		}

	}

}
