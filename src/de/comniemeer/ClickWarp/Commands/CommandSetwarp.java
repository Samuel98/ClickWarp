package de.comniemeer.ClickWarp.Commands;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.comniemeer.ClickWarp.AutoCommand;
import de.comniemeer.ClickWarp.ClickWarp;
import de.comniemeer.ClickWarp.Metrics;

public class CommandSetwarp extends AutoCommand<ClickWarp> {
	
	public CommandSetwarp (ClickWarp plugin) {
		super(plugin, "setwarp", "Set a warp at the current location");
	}
	
	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender.hasPermission("clickwarp.setwarp")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (args.length == 1) {
					if (args[0].contains(".yml") || args[0].contains("\\") || args[0].contains("|") || args[0].contains("/") || args[0].contains(":")) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpInvalidName));
					} else {
						String str = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', args[0].toLowerCase()));
						File warp = new File("plugins/ClickWarp/Warps", str + ".yml");
						FileConfiguration cfg = YamlConfiguration.loadConfiguration(warp);
						Location loc = player.getLocation();
						
						cfg.set(str + ".name", args[0]);
						cfg.set(str + ".world", loc.getWorld().getName());
						cfg.set(str + ".x", loc.getX());
						cfg.set(str + ".y", loc.getY());
						cfg.set(str + ".z", loc.getZ());
						cfg.set(str + ".yaw", loc.getYaw());
						cfg.set(str + ".pitch", loc.getPitch());
						
						try {
							cfg.save(warp);
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpSuccess).replace("{warp}", ChatColor.translateAlternateColorCodes('&', args[0])));
						} catch (IOException e) {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.ErrorFileSaving));
							e.printStackTrace();
						}
						
						File warps_folder = new File("plugins/ClickWarp/Warps");
						File[] warps = warps_folder.listFiles();
						
						final int files = warps.length;
						
						try {
						    Metrics metrics = new Metrics(plugin);
						    
						    metrics.addCustomData(new Metrics.Plotter("Warps") {
						        @Override
						        public int getValue() {
						            return files;
						        }
						    });
						    
						    metrics.start();
						} catch (IOException e) {
						    e.printStackTrace();
						}
					}
				} else if (args.length == 2) {
					Material item_name = null;
					
					if (args[0].contains(".yml") || args[0].contains("\\") || args[0].contains("|") || args[0].contains("/") || args[0].contains(":")) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpInvalidName));
					} else {
						String str = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', args[0].toLowerCase()));
						File file = new File("plugins/ClickWarp/Warps", str + ".yml");
						FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
						Location loc = player.getLocation();

						cfg.set(str + ".name", args[0]);
						cfg.set(str + ".world", loc.getWorld().getName());
						cfg.set(str + ".x", loc.getX());
						cfg.set(str + ".y", loc.getY());
						cfg.set(str + ".z", loc.getZ());
						cfg.set(str + ".yaw", loc.getYaw());
						cfg.set(str + ".pitch", loc.getPitch());
						
						String item_ = args[1];
						int item_id = 0;
						int item_meta = 0;
						
						if (item_.contains(":")) {
							String[] item__ = item_.split(":");
							
							if (item__.length > 2) {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpWrongItemFormat));
								return true;
							}
							
							for (int i = 0; i < item__.length; i++) {
								try {
									item_meta = Integer.parseInt(item__[i]);
								} catch (NumberFormatException e) {
									player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpNeedNumber));
									return true;
								}
								
								if (i + 1 < item__.length) {
									try {
										item_id = Integer.parseInt(item__[i]);
									} catch (NumberFormatException e) {
										player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpNeedNumber));
										return true;
									}
								}
							}
							
							item_name = Material.getMaterial(item_id);
							
							try {
								item_id = item_name.getId();
							} catch (NullPointerException npe) {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpInvalidItem));
								return true;
							}
							
							cfg.set(str + ".item", item_id + ":" + item_meta);
						} else {					
							try {
								item_id = Integer.parseInt(item_);
							} catch (NumberFormatException e) {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpNeedNumber));
								return true;
							}
							
							item_name = Material.getMaterial(item_id);
							
							try {
								item_id = item_name.getId();
							} catch (NullPointerException npe) {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpInvalidItem));
								return true;
							}
							
							cfg.set(str + ".item", item_id);
						}
						
						try {
							cfg.save(file);
						} catch (IOException e) {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.ErrorFileSaving));
							e.printStackTrace();
							return true;
						}
						
						File warps_folder = new File("plugins/ClickWarp/Warps");
						File[] warps = warps_folder.listFiles();
						
						final int files = warps.length;
						
						try {
						    Metrics metrics = new Metrics(plugin);
						    
						    metrics.addCustomData(new Metrics.Plotter("Warps") {
						        @Override
						        public int getValue() {
						            return files;
						        }
						    });
						    
						    metrics.start();
						} catch (IOException e) {
						    e.printStackTrace();
						}
						
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpSuccess).replace("{warp}", ChatColor.translateAlternateColorCodes('&', args[0])));
					}
				} else if (args.length == 3) {
					Material item_name = null;
					
					if (args[0].contains(".yml") || args[0].contains("\\") || args[0].contains("|") || args[0].contains("/") || args[0].contains(":")) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpInvalidName));
					} else {
						String str = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', args[0].toLowerCase()));
						File file = new File("plugins/ClickWarp/Warps", str + ".yml");
						FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
						Location loc = player.getLocation();

						cfg.set(str + ".name", args[0]);
						cfg.set(str + ".world", loc.getWorld().getName());
						cfg.set(str + ".x", loc.getX());
						cfg.set(str + ".y", loc.getY());
						cfg.set(str + ".z", loc.getZ());
						cfg.set(str + ".yaw", loc.getYaw());
						cfg.set(str + ".pitch", loc.getPitch());
						
						String item_ = args[1];
						int item_id = 0;
						int item_meta = 0;
						double price = 0;
						
						if (item_.contains(":")) {
							String[] item__ = item_.split(":");
							
							if (item__.length > 2) {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpWrongItemFormat));
								return true;
							}
							
							for (int i = 0; i < item__.length; i++) {
								try {
									item_meta = Integer.parseInt(item__[i]);
								} catch (NumberFormatException e) {
									player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpNeedNumber));
									return true;
								}
								
								if (i + 1 < item__.length) {
									try {
										item_id = Integer.parseInt(item__[i]);
									} catch (NumberFormatException e) {
										player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpNeedNumber));
										return true;
									}
								}
							}
							
							item_name = Material.getMaterial(item_id);
							
							try {
								item_id = item_name.getId();
							} catch (NullPointerException npe) {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpInvalidItem));
								return true;
							}
							
							cfg.set(str + ".item", item_id + ":" + item_meta);
						} else {
							try {
								item_id = Integer.parseInt(item_);
							} catch (NumberFormatException e) {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpNeedNumber));
								return true;
							}
							
							item_name = Material.getMaterial(item_id);
							
							try {
								item_id = item_name.getId();
							} catch (NullPointerException npe) {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpInvalidItem));
								return true;
							}
							
							cfg.set(str + ".item", item_id);
						}
						
						try {
							price = Double.parseDouble(args[2]);
						} catch (NumberFormatException e) {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpNeedNumber));
							return true;
						}
						
						cfg.set(str + ".price", price);
						
						try {
							cfg.save(file);
						} catch (IOException e) {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.ErrorFileSaving));
							e.printStackTrace();
							return true;
						}
						
						File warps_folder = new File("plugins/ClickWarp/Warps");
						File[] warps = warps_folder.listFiles();
						
						final int files = warps.length;
						
						try {
						    Metrics metrics = new Metrics(plugin);
						    
						    metrics.addCustomData(new Metrics.Plotter("Warps") {
						        @Override
						        public int getValue() {
						            return files;
						        }
						    });
						    
						    metrics.start();
						} catch (IOException e) {
						    e.printStackTrace();
						}
						
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpSuccess).replace("{warp}", ChatColor.translateAlternateColorCodes('&', args[0])));
					}
				} else {
					sender.sendMessage("�e/setwarp <name>");
					sender.sendMessage("�e/setwarp <name> <item>");
					sender.sendMessage("�e/setwarp <name> <item> <price>");
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.OnlyPlayers));
			}
		} else {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoPermission));
		}
		
		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String label, String[] args) {
		return null;
	}
}