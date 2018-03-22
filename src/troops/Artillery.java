package troops;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Artillery extends Militia{

	public Artillery(ArrayList<Creature> z) {
		super(z);
		
		for(Creature c: z){
			c.removePotionEffect(PotionEffectType.SPEED);
			c.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 1));
		}
	}
	
	@Override
	public boolean fire(){
		if(angle == null)return false;
		if(shootTargets.size() == 0)return false;
		
		for(Creature z: troops){
			LivingEntity t = shootTargets.get((int) (shootTargets.size() * Math.random()));
			
			Vector shootSpeed = t.getLocation().toVector().subtract(z.getLocation()
					.toVector()).normalize().multiply(5);
			for(int i = 0; i < 25; i++){
				shootSpeed.setX(shootSpeed.getX() + Math.random() - 0.5);
				shootSpeed.setY(shootSpeed.getY() + Math.random() - 0.5);
				shootSpeed.setZ(shootSpeed.getZ() + Math.random() - 0.5);
				z.launchProjectile(Arrow.class, shootSpeed);
				/*Or shoot witherskull*/
			}
			 
			Location l = z.getLocation().clone().add(shootSpeed.multiply(1));
			t.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, l.getX(), l.getY() + 5, l.getZ(), 1);
			
		}
		return true;
		
	}

}
