package troops;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
/**
 * Basic shooting class
 * @author rober_000
 *
 */
public class Militia extends Group{
	protected ArrayList<LivingEntity> shootTargets = new ArrayList<>();
	public Militia(ArrayList<Creature> z){	
		super(z);
	}
	@Override
	public boolean fire(){
		if(angle == null)return false;
		if(shootTargets.size() == 0)return false;
		
		for(Creature z: troops){
			LivingEntity t = shootTargets.get((int) (shootTargets.size() * Math.random()));
			
			Vector shootSpeed = t.getLocation().toVector().subtract(z.getLocation()
					.toVector()).normalize().multiply(5);
			z.launchProjectile(Arrow.class, shootSpeed);
			
			Location l = z.getLocation().clone().add(shootSpeed.multiply(1));
			t.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, l.getX(), l.getY() + 5, l.getZ(), 1);
			
		}
		return true;
	}
	@Override
	public void tick(){
		if(angle == null)return;
		
		Collection<Entity> all = new HashSet<>();	
		for(int i = 0; i < troops.size(); i++){
			Location loc = troops.get(i).getLocation();
			loc.add(angle.clone().multiply(25));
			Collection<Entity> entities = loc.getWorld().getNearbyEntities(loc, 15, 3, 15);
			
			all.addAll(entities);
		}
		
		List<LivingEntity> ret = new ArrayList<>();
		for(Entity c: all){
			if(c instanceof LivingEntity && c.hasMetadata("SSMTeam")){
				ret.add((LivingEntity) c);
			}
		}
		
		//Pretty hacky solution. Uses logic of super.setTargets to calculate shooting targets
		ArrayList<LivingEntity> tmp = targets;
		//So it doesn't clean up targets
		targets = new ArrayList<>();
		setTargets(ret);
		shootTargets = targets;
		targets = tmp;
	}
}
