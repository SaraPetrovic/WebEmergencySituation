package Model;

public class Area {

	private int id;
	private String name;
	private double surface;
	private int population;
	
	public Area() {
		
	}
	public Area(int id, String name, double surface, int population) {
		super();
		this.id = id;
		this.name = name;
		this.surface = surface;
		this.population = population;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getSurface() {
		return surface;
	}
	public void setSurface(double surface) {
		this.surface = surface;
	}
	public int getPopulation() {
		return population;
	}
	public void setPopulation(int population) {
		this.population = population;
	}
	
	
	
	
}
