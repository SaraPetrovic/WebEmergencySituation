package Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Areas {

	private HashMap<String, Area> areas = new HashMap<String, Area>();
	
	public Areas() {
		
	}
	public Areas(String path) {
		BufferedReader in = null;
		try {
			File file = new File(path + "/areas.txt");
			in = new BufferedReader(new FileReader(file));
			readAreas(in);
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if ( in != null ) {
				try {
					in.close();
				}
				catch (Exception e) { }
			}
		}
	}
	
	public void readAreas(BufferedReader in) {
		String line, id = "", name = "", surface = "", population = "";
		StringTokenizer st;
		try {
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					id = st.nextToken().trim();
					name = st.nextToken().trim();
					surface = st.nextToken().trim();
					population = st.nextToken().trim();
				}
				Area area = new Area(Integer.parseInt(id), name, Double.parseDouble(surface), Integer.parseInt(population));
				areas.put(name, area);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Collection<Area> getValues(){
		return areas.values();
	}
	public Area getArea(String name) {
		return areas.get(name);
	}
	public HashMap<String, Area> getAreas(){
		return areas;
	}
	
}
