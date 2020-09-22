package me.terroFlys.testEWand;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;




public class Main extends JavaPlugin implements Listener{
	
	public Integer cd_count = 0;
	public Integer currentSpell = 0;
	public Boolean invState = false;
	
	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable() {

	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			return true;
		}
		Player player = (Player) sender;
		if(label.equalsIgnoreCase("wand")) {
			player.getInventory().addItem(GetWand());
			player.sendMessage(ChatColor.GOLD + "You have recieved the Blaze Wand.");
			return true;
			
		}
		
		return false;
	}
	
	public ItemStack GetWand() {
		ItemStack item = new ItemStack(Material.BLAZE_ROD);
		ItemMeta meta = item.getItemMeta();
		currentSpell = 0;
		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "BLAZE WAND");
		List<String> lore = new ArrayList<String>();
		lore.add("");
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7(Right Click) &a&5Change Spell"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7(Left Click) &a&4Attack"));
		meta.setLore(lore);
		//meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		
		item.setItemMeta(meta);
		return item;
		
	}
	
	

	
	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			 public void run() {
				 if(cd_count >= 0) {
					 cd_count -= 1;
				 }
	            }
		}, 2L, 2L);
		Player player = (Player) event.getPlayer();
		if(player.getInventory().getItemInMainHand().getType() == Material.BLAZE_ROD)
			if(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("BLAZE WAND"))
				if(player.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
					if(cd_count <= 1) {
						if(event.getAction() == Action.RIGHT_CLICK_AIR && player.isSneaking() == false) {
							if(currentSpell < 8) {
								currentSpell += 1;
							} else {
								currentSpell = 1;
							}
							cd_count = 7;
							
						} else if(event.getAction() == Action.RIGHT_CLICK_AIR && player.isSneaking()) {
							if(currentSpell > 1) {
								currentSpell -= 1;
							} else {
								currentSpell = 8;
							}
							cd_count = 7;

						} else if(event.getAction() == Action.RIGHT_CLICK_BLOCK && player.isSneaking()) {
							if(currentSpell > 1) {
								currentSpell -= 1;
							} else {
								currentSpell = 8;
							}
							cd_count = 7;
						} else if(event.getAction() == Action.RIGHT_CLICK_BLOCK && player.isSneaking() == false) {
							if(currentSpell < 8) {
								currentSpell += 1;
							} else {
								currentSpell = 1;
							}
							cd_count = 7;
							
						}
						if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
							if(currentSpell == 1) {
								player.sendMessage(ChatColor.GOLD + "Spell: Explosion");
							} else if(currentSpell == 2) {
								player.sendMessage(ChatColor.GOLD + "Spell: Spark");
							} else if(currentSpell == 3) {
								player.sendMessage(ChatColor.GOLD + "Spell: FireBall");
							} else if(currentSpell == 4) {
								player.sendMessage(ChatColor.GOLD + "Spell: Explosion Wave");
							} else if(currentSpell == 5) {
								player.sendMessage(ChatColor.GOLD + "Spell: Nuke");
							} else if(currentSpell == 6) {
								player.sendMessage(ChatColor.GOLD + "Spell: Cubic Nuke");
							} else if(currentSpell == 7) {
								player.sendMessage(ChatColor.GOLD + "Spell: Launch");
							} else if(currentSpell == 8) {
								player.sendMessage(ChatColor.GOLD + "Spell: Lightning Strike");
							}
						}
					}
					
					
					if(event.getAction() == Action.LEFT_CLICK_AIR && invState == false || event.getAction() == Action.LEFT_CLICK_BLOCK && invState == false) {
						World w = player.getWorld();
						Block block = player.getTargetBlock(null, 50);
						
						Location playerLoc = (Location) player.getLocation();
						Location blockLoc = (Location) block.getLocation();
						
						if(currentSpell == 1) {
							//Explosion Spell
							w.createExplosion(blockLoc, 6, false);
							
						} else if (currentSpell == 2) {
							//Spark Spell
							w.playEffect(blockLoc, Effect.MOBSPAWNER_FLAMES, 0);
							Location temp_loc = blockLoc;
							//Collection<Entity> sparkVictims = w.getNearbyEntities(blockLoc, 3, 3, 3);
							 for(Entity en : w.getNearbyEntities(blockLoc, 4, 3, 4)){
					              if (en instanceof LivingEntity) {
					            	  ((LivingEntity) en).damage(13);
					              }
					        }
							
							
							w.playSound(blockLoc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 2.0f, 1f);
							w.playSound(blockLoc, Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST_FAR, 2.0f, 1f);
							w.playSound(blockLoc, Sound.ENTITY_FIREWORK_ROCKET_BLAST_FAR, 2.0f, 1f);
							w.playSound(blockLoc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 3.0f, 1f);
							Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					            public void run() {
					                // Code here...
					                // This code will fire after the specified delay [below]
					            	w.playSound(blockLoc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 5.0f, 1.1f);
					            }
					        }, 2L); // 600L (ticks) is equal to 30 seconds (20 ticks = 1 second)

							temp_loc.setY(blockLoc.getY()+1);
							w.playEffect(temp_loc, Effect.MOBSPAWNER_FLAMES, 0);
							temp_loc.setX(blockLoc.getX()+1);
							w.playEffect(temp_loc, Effect.MOBSPAWNER_FLAMES, 0);
							temp_loc.setZ(blockLoc.getZ()+1);
							w.playEffect(temp_loc, Effect.MOBSPAWNER_FLAMES, 0);
							temp_loc.setY(blockLoc.getY()-1);
							w.playEffect(temp_loc, Effect.MOBSPAWNER_FLAMES, 0);
							temp_loc.setX(blockLoc.getX()-1);
							w.playEffect(temp_loc, Effect.MOBSPAWNER_FLAMES, 0);
							temp_loc.setZ(blockLoc.getZ()-1);
							w.playEffect(temp_loc, Effect.MOBSPAWNER_FLAMES, 0);
							
							
							

						} else if (currentSpell == 3) {
							//FireBall Spell
							w.playSound(blockLoc, Sound.BLOCK_GLASS_FALL, 4.0f, 1f);
							player.launchProjectile(Fireball.class);
							//FireBall Spell
						} else if (currentSpell == 4) {
							//Explosion Wave Spell
							
							w.createExplosion(blockLoc, 4, false);
							
							//Vector rot = player.getLocation().getDirection();
							

							Location targetLoc = (Location) blockLoc;
							//Integer count = (Integer) 0;
							Integer temp_x;
		                	Integer temp_y;
		                	Integer temp_z;
		                	if(blockLoc.getBlockX() - playerLoc.getBlockX() > 0) {
		                		temp_x = 1;
		                	} else if(blockLoc.getBlockX() - playerLoc.getBlockX() < 0){
		                		temp_x = -1;
		                	} else {
		                		temp_x = 0;
		                	}
		                	if(blockLoc.getBlockY() - playerLoc.getBlockY() > 0) {
		                		temp_y = 1;
		                	} else if(blockLoc.getBlockY() - playerLoc.getBlockY() < 0){
		                		temp_y = -1;
		                	} else {
		                		temp_y = 0;
		                	}
		                	if(blockLoc.getBlockZ() - playerLoc.getBlockZ() > 0) {
		                		temp_z = 1;
		                	} else if(blockLoc.getBlockZ() - playerLoc.getBlockZ() < 0){
		                		temp_z = -1;
		                	} else {
		                		temp_z = 0;
		                	}
							this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
								Integer count = (Integer) 0;
								
				                public void run() {
				                	count += 1;
				                	if(count > 12) {
				                		return;
				                	}
				                	
				                	
									targetLoc.setX(blockLoc.getBlockX() + temp_x);
									targetLoc.setY(blockLoc.getBlockY() + temp_y);
									targetLoc.setZ(blockLoc.getBlockZ() + temp_z);
									//blockLoc = targetLoc;
									w.createExplosion(targetLoc, 4, false);
				                }
				            }, 2, 2);
							
							
						}else if(currentSpell == 5) {
							//Nuke
							w.createExplosion(blockLoc, 40, true);
						}
						else if(currentSpell == 6) {
							//Insane Nuke / Cubic Nuke
							w.createExplosion(blockLoc, 90, false);
						} else if(currentSpell == 7) {
							//Launch
							w.playSound(blockLoc, Sound.BLOCK_BEACON_ACTIVATE, 3.0f, 1f);
							player.setVelocity(player.getLocation().getDirection().multiply(4).setY(2));
							
						} else if(currentSpell == 8) {
							//Lightning strike
							w.playSound(blockLoc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 6f, 1f);
							w.playSound(blockLoc, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1f, 1f);
							
							w.strikeLightning(blockLoc);
							w.strikeLightningEffect(blockLoc);
						}
						else {
							player.sendMessage(ChatColor.DARK_PURPLE + "No spell selected.");
						}
						
					}
					
					
				}
		
		
		
	}
	
	
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		World w = event.getLocation().getWorld();
		
		if(event.getEntityType() == EntityType.FIREBALL) {
			w.createExplosion(event.getLocation(), 7, true);
			w.playSound(event.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 30.0f, 1f);
			
		}
	}
	
	@EventHandler
	public void onFall(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if(event.getCause() == DamageCause.FALL || event.getCause() == DamageCause.BLOCK_EXPLOSION || 
					event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.FIRE_TICK) {
				if(player.getInventory().getItemInMainHand().getType() == Material.BLAZE_ROD)
					if(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("BLAZE WAND"))
						if(player.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
							event.setCancelled(true);
							if(event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.FIRE_TICK) {
								player.setFireTicks(0);
							}
						}
			}
			
		}
		
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if(event.getPlayer().getInventory().getItemInMainHand().getItemMeta() != null)
			if(event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("WAND")) 
				if(event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore())
					if(event.getPlayer().getInventory().getItemInMainHand().getType() == Material.BLAZE_ROD) {
						event.setCancelled(true);
						return;
					}
		
		return;
		
	}
	
	@EventHandler
	public void invCheckOpen(InventoryOpenEvent event) {
		invState = true;
	}
	@EventHandler 
	public void invCheckClose(InventoryCloseEvent event){
		invState = false;
	}
	
	

}























