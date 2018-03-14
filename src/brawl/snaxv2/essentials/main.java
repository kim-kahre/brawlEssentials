package brawl.snaxv2.essentials;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;

public class main extends JavaPlugin implements Listener {
    public Connection connection;
    public String host, database, username, password;
    public int port;
    public String database2, username2, password2;
    public class ban {
    	public String reason;
    	public double startTime;
    	public double endTime;
    	public String banner;
    	public int perm;
    }
    @Override
    public void onEnable() {
    	Bukkit.getServer().getPluginManager().registerEvents(this, this);
        host = "localhost";
        port = 3306;
        database = "brawl";
        username = "root";
        password = "haxor";  
        database2 = "friends";
        username2 = "root";
        password2 = "haxor";  
        try {     
            openConnection();
            Statement statement = connection.createStatement();          
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        new BukkitRunnable() {
            @Override
            public void run() {
            	for (Player player:Bukkit.getOnlinePlayers()) {
	                permissionStuff perms = new permissionStuff();
	                String rankname = perms.getRankName(player);
	                player.setPlayerListName(perms.getRankColor(player) + rankname + " "+ ChatColor.WHITE +player.getName());
            	}
            }    
        }.runTaskTimer(this, 5, 5);
    }
    
    @Override
    public void onDisable() {
    	
    }
    
	@EventHandler
	public void playerJoinEvent (PlayerLoginEvent e){
		final Player player = e.getPlayer();
		  API thisAPI = new API();
		  ban thisBan = thisAPI.getBan(player.getUniqueId());
		  if (!(thisBan.endTime == 0)) {
			  if (thisBan.perm == 1) {
					e.disallow(Result.KICK_BANNED, "You are permanently banned. Reason: " + thisBan.reason);
			  }else {
				  if (System.currentTimeMillis() < thisBan.endTime) {
						e.disallow(Result.KICK_BANNED, "You are currently banned. Reason: " + thisBan.reason + ". Time left: " + String.valueOf((thisBan.endTime - System.currentTimeMillis())/60000) + " minutes.");
				  }
			  }
		  }
		BukkitRunnable r = new BukkitRunnable() {
			   @Override
			   public void run() {
			      try {
			         openConnection();
			         Statement statement = connection.createStatement();
			         ResultSet result = statement.executeQuery("SELECT * FROM rank WHERE UUID = '" + player.getUniqueId().toString() + "';");
			         int rank = -1;
			         if (result != null) {
			        	 while(result.next()) {
			        		 rank = result.getInt("RANK");
			        	 }
			         }
			         if (rank == -1) {
    			         Statement statement2 = connection.createStatement();
    			         statement2.executeUpdate("INSERT INTO rank (UUID, RANK) VALUES ('"+player.getUniqueId().toString()+"', 0);");
    			         openConnection2();
    			         Statement statement3 = connection.createStatement();
    			         statement3.executeUpdate("CREATE TABLE " + player.getUniqueId().toString()+ "();");
    			         Bukkit.broadcastMessage(player.getName() + " has joined the server for the first time, welcome!");
    			         rank = 0;
			         }	
			         PermissionAttachment attachment = player.addAttachment(Bukkit.getPluginManager().getPlugin("brawlEssentials"));
			         attachment.setPermission(new Permission("acpfood.foods.*"), true);					         attachment.setPermission(new Permission("AAC.*"), true);
			         attachment.setPermission(new Permission("grapplinghook.pull.players"), true);
			         attachment.setPermission(new Permission("grapplinghook.pull.items"), true);
			         attachment.setPermission(new Permission("grapplinghook.pull.self"), true);
			         attachment.setPermission(new Permission("grapplinghook.pull.mobs"), true);
			         attachment.setPermission(new Permission("sn4x5.diamond"), true);
			         attachment.setPermission(new Permission("acpfood.foods.cannedbeans"), true);
			         attachment.setPermission(new Permission("acpfood.foods.cannedpasta"), true);
			         attachment.setPermission(new Permission("acpfood.foods.cannedfish"), true);
			         attachment.setPermission(new Permission("acpfood.foods.mountaindew"), true);
			         attachment.setPermission(new Permission("acpfood.foods.pepsi"), true);
			         attachment.setPermission(new Permission("acpfood.foods.sugar"), true);
			         if (rank > 3) {
				         attachment.setPermission(new Permission("aac.notify"), true);
				         if (rank > 5) {
					         attachment.setPermission(new Permission("AAC.*"), true);
					         attachment.setPermission(new Permission("aacadditionpro.entitycheck"), true);
					         attachment.setPermission(new Permission("aacadditionpro.info"), true);
					         attachment.setPermission(new Permission("aacadditionpro.verbose"), true);
					         attachment.setPermission(new Permission("aacadditionpro.neutral.*"), true);
					         attachment.setPermission(new Permission("aacadditionpro.tablistremove"), true);
				         }
			         }else {
				         attachment.setPermission(new Permission("aac.notify"), false);
			         }
			         metaData meta = new metaData();
			         meta.setMetadata(player, "RANK", rank);
			      } catch(ClassNotFoundException e) {
			         e.printStackTrace();
			      } catch(SQLException e) {
			         e.printStackTrace();
			      }
			   }
			};
			 
			r.runTaskAsynchronously(this);
	}
    public void openConnection() throws SQLException, ClassNotFoundException {
	    if (connection != null && !connection.isClosed()) {
	        return;
	    }
	 
	    synchronized (this) {
	        if (connection != null && !connection.isClosed()) {
	            return;
	        }
	        Class.forName("com.mysql.jdbc.Driver");
	        connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
	    }
    }
    public void openConnection2() throws SQLException, ClassNotFoundException {
	    if (connection != null && !connection.isClosed()) {
	        return;
	    }
	 
	    synchronized (this) {
	        if (connection != null && !connection.isClosed()) {
	            return;
	        }
	        Class.forName("com.mysql.jdbc.Driver");
	        connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database2, this.username2, this.password2);
	    }
    }
    public class API {
    	public int getRank(Player player) {
    		if (player.hasMetadata("RANK")) {
    			metaData meta = new metaData();
    			int rank = (int) meta.getMetadata(player, "RANK");
    			return rank;
    		}else {
    			return 0;
    		}
    	}
    	
    	public void setRank(final Player player, final int rank ) {
    		BukkitRunnable r = new BukkitRunnable() {
    			   @Override
    			   public void run() {
    			      try {
    			         openConnection();
    			         Statement statement = connection.createStatement();
    			         statement.executeUpdate("UPDATE rank SET RANK="+String.valueOf(rank)+" WHERE UUID='"+player.getUniqueId().toString()+"';");
    			      } catch(ClassNotFoundException e) {
    			         e.printStackTrace();
    			      } catch(SQLException e) {
    			         e.printStackTrace();
    			      }
    			   }
    			};
    			 
    			r.run();
    			player.kickPlayer("[BRAWL] - Your permissions have been updated, please relog");
    	}
    	public ban getBan(UUID playerUUID) {
	         try {
				openConnection();
		         Statement statement = connection.createStatement();
		         ResultSet result = statement.executeQuery("SELECT * FROM ban WHERE UUID = '" + playerUUID.toString() + "';");
		         ban cor = new ban();
		         cor.banner = "null";
		         cor.reason = "null";
		         cor.startTime = 0;
		         cor.endTime = 0;
		         cor.banner = "null";
		         if (result != null) {
		        	 while(result.next()) {
		        		 cor.startTime = Double.valueOf(result.getString("BANTIME"));
		        		 cor.endTime = Double.valueOf(result.getString("UNBANTIME"));
		        		 cor.banner = result.getString("BANNERUUID");
		        		 cor.perm = result.getInt("PERM");
		        		 cor.reason = result.getString("REASON");
		        	 }
		         }
		         return cor;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	         ban no = new ban();
	         no.banner = "null";
	         no.reason = "null";
	         no.startTime = 0;
	         no.endTime = 0;
	         no.banner = "null";
	         return no;
    	}
    	
    	public void setBan(final Player player, final ban thisBan) {
    		BukkitRunnable r = new BukkitRunnable() {
    			   @Override
    			   public void run() {
    			      try {
    			         openConnection();
    			         Statement statement = connection.createStatement();
    			         statement.executeUpdate("DELETE FROM ban WHERE UUID = '"+player.getUniqueId().toString()+"';");
    			         Statement statement2 = connection.createStatement();
    			         statement2.executeUpdate("INSERT INTO ban (UUID, BANTIME, UNBANTIME, BANNERUUID, PERM, REASON) VALUES ('"+player.getUniqueId().toString()+"', '" +thisBan.startTime+"', '"+thisBan.endTime+"', '"+thisBan.banner+"', '"+ String.valueOf(thisBan.perm)+"', '"+thisBan.reason+ "');");
    			      } catch(ClassNotFoundException e) {
    			         e.printStackTrace();
    			      } catch(SQLException e) {
    			         e.printStackTrace();
    			      }
    			   }
    			};
    			 
    			r.run();
  			  if (thisBan.perm == 1) {
				  player.kickPlayer("You are permanently banned. Reason: " + thisBan.reason);
			  }else {
				  player.kickPlayer("You are currently banned. Reason: " + thisBan.reason + ". Time left: " + String.valueOf((thisBan.endTime - System.currentTimeMillis())/60000) + " minutes.");
			  }
    	}
    	public void unBan(final UUID playerUUID) {
    		BukkitRunnable r = new BukkitRunnable() {
    			   @Override
    			   public void run() {
    			      try {
    			         openConnection();
    			         Statement statement = connection.createStatement();
    			         statement.executeUpdate("UPDATE ban SET UNBANTIME="+String.valueOf(0)+" WHERE UUID='"+playerUUID.toString()+"';");
    			         Statement statement2 = connection.createStatement();
    			         statement2.executeUpdate("UPDATE ban SET PERM="+String.valueOf(false)+" WHERE UUID='"+playerUUID.toString()+"';");
    			      } catch(ClassNotFoundException e) {
    			         e.printStackTrace();
    			      } catch(SQLException e) {
    			         e.printStackTrace();
    			      }
    			   }
    			};
    			 
    			r.run();
    	}
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
    	if (cmd.getName().equalsIgnoreCase("warp")) {
    		Player p = (Player) sender;
    		permissionStuff perm = new permissionStuff();
    		int rank = perm.getRankId(p);
    		if (rank>5) {
	    		if (sender instanceof Player) {
	    			if (args[0].equals("set")) {
	  			      try {
	 			         openConnection();
	     			     Statement statement = connection.createStatement();
	     			     statement.executeUpdate("INSERT INTO warps (name, x, y, z) VALUES ('"  + args[1] + "', '" + String.valueOf(p.getLocation().getX()) + "', '" + String.valueOf(p.getLocation().getY()) + "', '"   + String.valueOf(p.getLocation().getZ()) + "');");
	 			      } catch(ClassNotFoundException e) {
	 			         e.printStackTrace();
	 			      } catch(SQLException e) {
	 			         e.printStackTrace();
	 			      }
	    				return true;
	    			}
	    			if (args[0].equals("delete")) {
		  			      try {
			 			         openConnection();
			     			     Statement statement = connection.createStatement();
			     			     statement.executeUpdate("DELETE FROM warps WHERE name = '" + args[1] + "';");
			 			      } catch(ClassNotFoundException e) {
			 			         e.printStackTrace();
			 			      } catch(SQLException e) {
			 			         e.printStackTrace();
			 			      }
		  			      return true;
	    			}
	  			      try {
		 			         openConnection();
		     			     Statement statement = connection.createStatement();
		    		         ResultSet result = statement.executeQuery("SELECT * FROM warps WHERE name = '" + args[0]+ "';");
		    		         if (result != null) {
		    		        	 while(result.next()) {
		    		        		 p.teleport(new Location(p.getWorld(), Double.valueOf(result.getString("x")), Double.valueOf(result.getString("y")), Double.valueOf(result.getString("z"))));
		    		        	 }
		    		         }
		 			      } catch(ClassNotFoundException e) {
		 			         e.printStackTrace();
		 			      } catch(SQLException e) {
		 			         e.printStackTrace();
		 			      }
	  			      return true;
	    			
	    		}
    		}else {
    			sender.sendMessage("You do not have the permission to execute this command");
    			return true;
    		}
    		return false;
    	}
    	if (cmd.getName().equalsIgnoreCase("permission")) {
    		API api = new API();
    		if (sender.isOp()) {
    			if (args[0].equals("get")) {
    				metaData meta = new metaData();
    				sender.sendMessage(String.valueOf((int)meta.getMetadata(Bukkit.getPlayer(args[1]), "RANK")));
    				return true;
    			}
	    		api.setRank(Bukkit.getPlayer(args[0]), Integer.valueOf(args[1]));
	    		sender.sendMessage("Updated permissions");
	    		return true;
    		}
    	}
    	if (cmd.getName().equalsIgnoreCase("unban")) {
    		if (sender instanceof Player) {
        		metaData meta = new metaData();
        		int rank = (int) meta.getMetadata((Player) sender, "RANK");
    			if (rank>3) {
    				API thisAPI = new API();
    				thisAPI.unBan(Bukkit.getOfflinePlayer(args[0]).getUniqueId());
    			}    		
    		}
    	}
    	if (cmd.getName().equalsIgnoreCase("viewban")) {
        		metaData meta = new metaData();
        		int rank = (int) meta.getMetadata((Player) sender, "RANK");
    			if (rank>3) {
        			for (final Player player : Bukkit.getOnlinePlayers()) {
        				if (player.getName().equals(args[0])) {
            				final API thisAPI = new API();
            	    		BukkitRunnable r = new BukkitRunnable() {
            	    			   @Override
            	    			   public void run() {
            	    				   ban thisBan = thisAPI.getBan(player.getUniqueId());
            	    				   sender.sendMessage(args[0]+ ";");
            	    				   sender.sendMessage("Banner: " + String.valueOf(thisBan.banner));
            	    				   sender.sendMessage("Time of ban: " + String.valueOf(thisBan.startTime));
            	    				   sender.sendMessage("Time of unban: " + String.valueOf(thisBan.endTime));
            	    				   sender.sendMessage("Reason: " + String.valueOf(thisBan.reason));
            	    				   sender.sendMessage("Perm?: " + String.valueOf(thisBan.perm));
            	    			   }
            	    			};
            	    			 
            	    			r.runTaskAsynchronously(this);
            	    			return true;
        				}
        			}
    				final API thisAPI = new API();
    	    		BukkitRunnable r = new BukkitRunnable() {
    	    			   @Override
    	    			   public void run() {
    	    				   ban thisBan = thisAPI.getBan(Bukkit.getOfflinePlayer(args[0]).getUniqueId());
    	    				   sender.sendMessage(args[0]+ ";");
    	    				   sender.sendMessage("Banner: " + String.valueOf(thisBan.banner));
    	    				   sender.sendMessage("Time of ban: " + String.valueOf(thisBan.startTime));
    	    				   sender.sendMessage("Time of unban: " + String.valueOf(thisBan.endTime));
    	    				   sender.sendMessage("Reason: " + String.valueOf(thisBan.reason));
    	    				   sender.sendMessage("Perm?: " + String.valueOf(thisBan.perm));
    	    			   }
    	    			};
    	    			 
    	    			r.runTaskAsynchronously(this);
    			}    
    			return true;
    	}
    	if (cmd.getName().equalsIgnoreCase("ban")) {
    		metaData meta = new metaData();
    		int maxtime = 0;
    		if (sender instanceof Player) {
        		int rank = (int) meta.getMetadata((Player) sender, "RANK");
        		if (rank==4) {
        			maxtime = 10;
        		}
        		if (rank==5) {
        			maxtime = 120;
        		}
        		if (rank==6) {
        			maxtime = 600000000;
        		}
        		if (rank==7) {
        			maxtime = 1000000000;
        		}
        		if (rank==8) {
        			maxtime = 1000000000;
        		}
    		}else {
    			maxtime = 100000000;
    		}
    		if (maxtime == 0) {
    			sender.sendMessage(ChatColor.DARK_RED + "You can not ban other players!");
    		}else {
    			API okban = new API ();
    			ban thisBan = new ban();
    			if (sender instanceof Player) {
    				thisBan.banner = ((Player) sender).getUniqueId().toString();
    			}else{
    				thisBan.banner = "CONSOLE";
    			}
    			thisBan.startTime = System.currentTimeMillis();
    			if (args[1].toUpperCase().equals("FOREVER") || args[1].toUpperCase().equals("PERM")) {
    				thisBan.endTime = System.currentTimeMillis();
    				thisBan.perm = 1;
    			}else {
        			thisBan.endTime = System.currentTimeMillis() + Double.valueOf(args[1])*60000;
        			if (Double.valueOf(args[1])*60000 > maxtime) {
        				thisBan.endTime = System.currentTimeMillis() + maxtime*60000;
        			}
        			thisBan.perm = 0;
    			}
    			thisBan.reason = args[2];
    			okban.setBan(Bukkit.getPlayer(args[0]), thisBan);
    		}
    	}
    	return false; 
    }
}