package body;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class Util {
	public static Location getSurface(double x, double z, World w){
		int currY = 255;
		
		List<Material> checked = Arrays.asList(Material.AIR, Material.LONG_GRASS, Material.DOUBLE_PLANT, Material.YELLOW_FLOWER, Material.RED_ROSE);
		
		while(checked.indexOf(w.getBlockAt(new Location(w, x, currY, z)).getType()) != -1){
			currY--;
		}
		return new Location(w, x, currY+1, z);
	}
	public static Vector rotateVectorAroundY(Vector vector, double degrees) {
	    double rad = Math.toRadians(degrees);
	   
	    double currentX = vector.getX();
	    double currentZ = vector.getZ();
	   
	    double cosine = Math.cos(rad);
	    double sine = Math.sin(rad);
	   
	    return new Vector((cosine * currentX - sine * currentZ), vector.getY(), (sine * currentX + cosine * currentZ)).normalize();
	}
}
