package body;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class HandlerLoop extends BukkitRunnable{
	private HashMap<UUID, PlayerHandler> handlers = new HashMap<>();
	@Override
	public void run() {
		for(UUID id: handlers.keySet()){
			handlers.get(id).tick();
		}
	}
	public void addTroops(Player p, ArrayList<Creature> z, Class<?> c){
		ensure(p);
		
		handlers.get(p.getUniqueId()).addZombies(z, c);
	}
	public boolean fire(Player p){
		ensure(p);
		
		return handlers.get(p.getUniqueId()).fire();
	}
	public void incIndex(Player p, int change){
		ensure(p);
		
		handlers.get(p.getUniqueId()).incIndex(change);
	}
	public void setGoals(Player p, Location loc, Vector look){
		ensure(p);
		
		handlers.get(p.getUniqueId()).setGoals(loc, look);
	}
	public void setTargets(Player p, List<LivingEntity> ret){
		ensure(p);
		
		handlers.get(p.getUniqueId()).setTargets(ret);
	}
	private void ensure(Player p){
		if(!handlers.containsKey(p.getUniqueId()))handlers.put(p.getUniqueId(), new PlayerHandler());
	}
	

}
