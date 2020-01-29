package Controller;

public class TerrainTuple implements Comparable<TerrainTuple> {
	public String	time,
					place,
					cloudness,
					wind,
					direction;
	
	public TerrainTuple() {
		this.time = new String();
		this.place = new String();
		this.cloudness = new String();
		this.wind = new String();
		this.direction = new String();
	}
	
	public TerrainTuple(String time, String place, String cloudness, String wind, String direction) {
		this.time = time;
		this.place = place;
		this.cloudness = cloudness;
		this.wind = wind;
		this.direction = direction;
	}
	
	@Override
	public int compareTo(TerrainTuple t) {
		
		return (this.time.equals(t.time)) && (this.place.equals(t.place)) ? 1 : 0;
	}
	
	
	public void set() { }
	public void get() { }
}
