package Service;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import Model.Area;
import Model.Areas;

@Path("/areas")
public class AreaService {

	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;
	
	public static int counterArea = 0;
	public static int idArea;

	@GET
	@Path("/getAllAreas")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllAreas() {
		Collection<Area> areas = getAreas().getValues();
		return Response.status(Status.OK).entity(areas).build();
	}
	
	@POST
	@Path("/getArea/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllAreas(@PathParam("id") int id) {
		Area area = null;
		for(Area a : getAreas().getValues()) {
			if(a.getId() == id) {
				area = a;
			}
		}
		return Response.status(Status.OK).entity(area).build();
	}
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addArea(Area a) {
		if(getAreas().getArea(a.getName()) != null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		if(counterArea == 0) {
			idArea = getAreas().getValues().size();
			counterArea = 1;
		}
		idArea++;

		Area area = new Area(idArea, a.getName(), a.getSurface(), a.getPopulation());
		getAreas().getAreas().put(area.getName(), area);
		try {
			writeToFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Response.status(Status.OK).entity(area).build();
	}
	
	@DELETE
	@Path("/delete/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteArea(@PathParam("id") int id) {
		Area area = null;
		for(Area a : getAreas().getValues()) {
			if(a.getId() == id) {
				area = a;
			}
		}
		getAreas().getAreas().remove(area.getName());
		try {
			writeToFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Response.status(Status.OK).entity(area).build();
	}

	@POST
	@Path("/change")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeArea(Area a) {
		Area newArea = null;
		for(Area area : getAreas().getValues()) {
			if(a.getId() == area.getId()) {
				newArea = area;
			}
		}
		String name = newArea.getName();
		newArea.setName(a.getName());
		newArea.setPopulation(a.getPopulation());
		newArea.setSurface(a.getSurface());
		getAreas().getAreas().remove(name);
		getAreas().getAreas().put(newArea.getName(), newArea);
		try {
			writeToFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Response.status(Status.OK).entity(newArea).build();
	}
	
	private void writeToFile() throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ctx.getRealPath("") + "/areas.txt"), StandardCharsets.UTF_16));
		for(Area a : getAreas().getValues()) {
			writer.write(String.valueOf(a.getId()) + ";" + a.getName() + ";" + String.valueOf(a.getSurface()) + ";" + String.valueOf(a.getPopulation()));
			writer.newLine();
		}
		writer.close();
	}
	
	private Areas getAreas() {
		Areas areas = (Areas) ctx.getAttribute("areas");
		if(areas == null) {
			areas = new Areas(ctx.getRealPath(""));
			ctx.setAttribute("areas", areas);
		}
		return areas;
	}
}
