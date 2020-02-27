package fr.xxgoldenbluexx.vi4interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class Vi4iMain extends JavaPlugin implements Listener,CommandExecutor{
	
	private Inventory maingui;
	private Inventory gardegui;
	private Inventory voleurgui;
	private Scoreboard scoreboard;
	
	private enum RuneType{
		GARDEPRIMAIRE,
		GARDESECONDAIRE,
		GARDETERTIAIRE,
		VOLEURPRIMAIRE,
		VOLEURSECONDAIRE,
		VOLEURTERTIAIRE
	}
	
	private void setRuneToPlayer(HumanEntity player,RuneType rune,int num) {
		Set<Score> pscores=scoreboard.getScores(player.getName());
		Objective objt = null;
		switch (rune) {
		case GARDEPRIMAIRE:
			objt=scoreboard.getObjective("gardeprimaire");
			break;
		case GARDESECONDAIRE:
			objt=scoreboard.getObjective("gardesecondaire");
			break;
		case GARDETERTIAIRE:
			objt=scoreboard.getObjective("gardetertiaire");
			break;
		case VOLEURPRIMAIRE:
			objt=scoreboard.getObjective("voleurprimaire");
			break;
		case VOLEURSECONDAIRE:
			objt=scoreboard.getObjective("voleursecondaire");
			break;
		case VOLEURTERTIAIRE:
			objt=scoreboard.getObjective("voleurtertiaire");
			break;
		}
		for (Score sc : pscores) {
			if (sc.getObjective().equals(objt)) {
				sc.setScore(num);
			}
		}
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		getServer().getPluginManager().registerEvents(this, this);
		getCommand("vi4i").setExecutor(this);
		scoreboard=Bukkit.getScoreboardManager().getMainScoreboard();
		//MAIN
		maingui=Bukkit.createInventory(null, 27, ChatColor.GREEN+"Menu Vi4");
		maingui.setItem(10,makeGuiItem(Material.DIAMOND,ChatColor.BLUE+"Runes Garde",ChatColor.DARK_PURPLE+"Cliquez ici pour choisir vos runes en tant que "+ChatColor.BLUE+"Garde"));
		maingui.setItem(13,makeGuiItem(Material.FIREWORK_ROCKET,ChatColor.GREEN+"Lancer la partie",ChatColor.DARK_PURPLE+"Cliquez ici pour lancer la partie"));
		maingui.setItem(16,makeGuiItem(Material.REDSTONE,ChatColor.RED+"Runes Voleur",ChatColor.DARK_PURPLE+"Cliquez ici pour choisir vos runes en tant que "+ChatColor.RED+"Voleur"));
		//GARDE
		gardegui=Bukkit.createInventory(null, 45, ChatColor.BLUE+"Runes Garde");
		gardegui.setItem(0,makeGuiItem(Material.ORANGE_DYE,ChatColor.GOLD+"Runes Principales"));
		gardegui.setItem(18,makeGuiItem(Material.PURPLE_DYE,ChatColor.DARK_PURPLE+"Runes Secondaires"));
		gardegui.setItem(35,makeGuiItem(Material.CYAN_DYE,ChatColor.AQUA+"Runes Tertiaires"));
		gardegui.setItem(2,makeGuiItem(Material.DIAMOND_SWORD,ChatColor.GOLD+"Matraque de Th�o",ChatColor.LIGHT_PURPLE+"Permet de tuer en un coup","",ChatColor.LIGHT_PURPLE+""+ChatColor.ITALIC+"Grosse matraque dans tes fesses"));
		gardegui.setItem(4,makeGuiItem(Material.REDSTONE_TORCH,ChatColor.GOLD+"Balise",ChatColor.LIGHT_PURPLE+"Permet de poser une balise qui r�velle les voleurs a proximit�e"));
		gardegui.setItem(6,makeGuiItem(Material.COMPASS,ChatColor.GOLD+"Sonnar",ChatColor.LIGHT_PURPLE+"Permet d'�mettre un son, aigu si un voleur est proche"));
		gardegui.setItem(20,makeGuiItem(Material.FIREWORK_ROCKET,ChatColor.DARK_PURPLE+"Surcharge",ChatColor.LIGHT_PURPLE+"Permet d'augmenter grandement sa vitesse et sa force pour 1s",ChatColor.LIGHT_PURPLE+"D�lais de r�cup�ration: "+ChatColor.WHITE+"20s"));
		gardegui.setItem(22,makeGuiItem(Material.BRICK_WALL,ChatColor.DARK_PURPLE+"Sonnar",ChatColor.LIGHT_PURPLE+"Permet de poser 2 murs sur les blocs violets",ChatColor.LIGHT_PURPLE+"Les gardes et les voleurs crocheteurs peuvent passer a travers"));
		gardegui.setItem(37,makeGuiItem(Material.FEATHER,ChatColor.AQUA+"Marathonien",ChatColor.LIGHT_PURPLE+"Permet d'avoir une vitesse de d�placement plus �lev�e"));
		gardegui.setItem(39,makeGuiItem(Material.FISHING_ROD,ChatColor.AQUA+"Traceur",ChatColor.LIGHT_PURPLE+"Permet d'acrocher un voleur pour garder ca trace ou le tirer vers soi"));
		//VOLEUR
		voleurgui=Bukkit.createInventory(null, 45, ChatColor.RED+"Runes Voleur");
	}
	private ItemStack makeGuiItem(Material mat, String name, String... lore) {
		ItemStack item=new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		List<String> listLore=new ArrayList<String>();
		for (String i : lore) {
			listLore.add(i);
		}
		meta.setLore(listLore);
		item.setItemMeta(meta);
		return item;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,String[] args) {
		if (sender instanceof Player) {
			Player player=(Player)sender;
			if (command.getName().equalsIgnoreCase("vi4i")) {
				player.openInventory(maingui);
				return true;
			}
			return false;
		}else {
			return false;
		}
	}
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Inventory inv = event.getInventory();
		ItemStack item = event.getCurrentItem();
		HumanEntity player = event.getWhoClicked();
		if (inv.equals(maingui)) {
			event.setCancelled(true);
			switch (item.getType()){
			case DIAMOND:
				player.openInventory(gardegui);
				break;
			case FIREWORK_ROCKET:
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"function vi4:start");
				break;
			case REDSTONE:
				player.openInventory(voleurgui);
			default:
				break;
			}
			return;
		}else if(inv.equals(gardegui)) {
			event.setCancelled(true);
			switch (item.getType()) {
			case DIAMOND_SWORD:
				setRuneToPlayer(player, RuneType.GARDEPRIMAIRE, 0);
				break;
			case REDSTONE_TORCH:
				setRuneToPlayer(player, RuneType.GARDEPRIMAIRE, 1);
				break;
			case COMPASS:
				setRuneToPlayer(player, RuneType.GARDEPRIMAIRE, 2);
				break;
			case FIREWORK_ROCKET:
				setRuneToPlayer(player, RuneType.GARDESECONDAIRE, 1);
				break;
			case BRICK_WALL:
				setRuneToPlayer(player, RuneType.GARDESECONDAIRE, 2);
				break;
			case FEATHER:
				setRuneToPlayer(player, RuneType.GARDETERTIAIRE, 0);
				break;
			case FISHING_ROD:
				setRuneToPlayer(player, RuneType.GARDETERTIAIRE, 1);
				break;
			default:
				break;
			}
			return;
		}else if(inv.equals(voleurgui)) {
			event.setCancelled(true);
			switch (item.getType()) {
			case GLASS_PANE:
				setRuneToPlayer(player, RuneType.VOLEURPRIMAIRE, 0);
				break;
			case GRASS:
				setRuneToPlayer(player, RuneType.VOLEURPRIMAIRE, 1);
				break;
			case CLOCK:
				setRuneToPlayer(player, RuneType.VOLEURPRIMAIRE, 2);
				break;
			case RABBIT_FOOT:
				setRuneToPlayer(player, RuneType.VOLEURSECONDAIRE, 0);
				break;
			case TRIPWIRE_HOOK:
				setRuneToPlayer(player, RuneType.VOLEURSECONDAIRE, 1);
				break;
			case COAL:
				setRuneToPlayer(player, RuneType.VOLEURSECONDAIRE, 2);
				break;
			case LANTERN:
				setRuneToPlayer(player, RuneType.VOLEURSECONDAIRE, 3);
				break;
			case HONEY_BOTTLE:
				setRuneToPlayer(player, RuneType.VOLEURSECONDAIRE, 4);
				break;
			case IRON_CHESTPLATE:
				setRuneToPlayer(player, RuneType.VOLEURTERTIAIRE, 0);
				break;
			case FISHING_ROD:
				setRuneToPlayer(player, RuneType.VOLEURTERTIAIRE, 1);
				break;
			default:
				break;
			}
			return;
		}else {
			return;
		}
	}
}
