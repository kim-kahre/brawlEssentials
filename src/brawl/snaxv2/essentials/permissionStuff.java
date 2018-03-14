package brawl.snaxv2.essentials;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;

import net.md_5.bungee.api.ChatColor;

public class permissionStuff {
		public String getRankName(Player object) {
		  List<MetadataValue> values = object.getMetadata("RANK");  
		  for (MetadataValue value : values) {
		     if (value.getOwningPlugin() == Bukkit.getPluginManager().getPlugin("brawlEssentials")) {
		        if (value.asInt() == 1) {
		        	return "VIP";
		        }
		        if (value.asInt() == 2) {
		        	return "MVP";
		        }
		        if (value.asInt() == 3) {
		        	return "ULTIMATE";
		        }
		        if (value.asInt() == 4) {
		        	return "TRAINEE";
		        }
		        if (value.asInt() == 5) {
		        	return "MOD";
		        }
		        if (value.asInt() == 6) {
		        	return "ADMIN";
		        }
		        if (value.asInt() == 7) {
		        	return "DEV";
		        }
		        if (value.asInt() == 8) {
		        	return "OWNER";
		        }
		        return "NORMAL";
		        
		     }
		  }
		  return null;
		}
		public ChatColor getRankColor(Player object) {
			  List<MetadataValue> values = object.getMetadata("RANK");  
			  for (MetadataValue value : values) {
			     if (value.getOwningPlugin() == Bukkit.getPluginManager().getPlugin("brawlEssentials")) {
			        if (value.asInt() == 1) {
			        	return ChatColor.AQUA;
			        }
			        if (value.asInt() == 2) {
			        	return ChatColor.BLUE;
			        }
			        if (value.asInt() == 3) {
			        	return ChatColor.YELLOW;
			        }
			        if (value.asInt() == 4) {
			        	return ChatColor.LIGHT_PURPLE;
			        }
			        if (value.asInt() == 5) {
			        	return ChatColor.DARK_PURPLE;
			        }
			        if (value.asInt() == 6) {
			        	return ChatColor.RED;
			        }
			        if (value.asInt() == 7) {
			        	return ChatColor.DARK_GREEN;
			        }
			        if (value.asInt() == 8) {
			        	return ChatColor.DARK_RED;
			        }
			        return ChatColor.GRAY;
			        
			     }
			  }
			  return null;
			}
		public int getRankId(Player object) {
			  List<MetadataValue> values = object.getMetadata("RANK");  
			  for (MetadataValue value : values) {
			     if (value.getOwningPlugin() == Bukkit.getPluginManager().getPlugin("brawlEssentials")) {
			    	 return value.asInt();		        
			     }
			  }
			  return 0;
			}
}
