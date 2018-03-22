package troops;

import java.util.ArrayList;

import org.bukkit.entity.Creature;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Melee extends Group{

	public Melee(ArrayList<Creature> z) {
		super(z);
		
		for(Creature c: z)c.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 2));
	}
	
	
}
