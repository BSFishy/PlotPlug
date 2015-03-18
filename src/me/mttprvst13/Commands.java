package me.mttprvst13;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class Commands {

	public Commands(CommandSender sender, Command cmd, String label,
			String[] args, plotMain plugin) {

		if (cmd.getName().equalsIgnoreCase("plot")) {

			if (args.length == 0) {

				sender.sendMessage(help(1));

				return;

			}else if(args.length == 1 && isInteger(args[0])){
				
				sender.sendMessage(help(Integer.parseInt(args[0])));
				
				return;
				
			}else if(args.length == 2 && args[0].equalsIgnoreCase("create")){
				
				Plugin mv = plugin.getServer().getPluginManager().getPlugin("Multiverse-Core");
				
				if(mv == null){
					
					sender.sendMessage(ChatColor.RED + "You must have Multiverse-Core installed to do it over commands. Otherwise use your bukkit.yml file.");
					
					return;
					
				}
				
				plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "mvc " + args[1] + " NORMAL -g PlotPlug");
				
				return;
				
			}
			
			sender.sendMessage(ChatColor.RED + "There is no subcommand \"" + args[0] + "\"");
			
			return;

		}

	}
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    // only got here if we didn't return false
	    return true;
	}

	public String[] msg;

	public String help(int page) {

		msg = new String[1];
		
		if (page > msg.length) {

			return ChatColor.RED + "There is no help for page \""
					+ Integer.toString(page) + "\"!";

		}
		
		page = page - 1;

		String topPage = ChatColor.BOLD + "" + ChatColor.GREEN
				+ "PlotPlug v1.0 page " + Integer.toString(page + 1) + "/"
				+ Integer.toString(msg.length) + " \n";

		String dash = ChatColor.RESET + "" + ChatColor.GOLD + " - "
				+ ChatColor.DARK_AQUA;

		String ncommand = ChatColor.BLUE + "" + ChatColor.BOLD;

		msg[0] = ncommand + "/plot [page]" + dash + "Displays a help page.\n"
				+ ncommand + "/getplot" + dash + "Displays what plot your in.";

		return topPage + msg[page];

	}

}
