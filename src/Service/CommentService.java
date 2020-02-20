package Service;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
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
import Model.Comment;
import Model.Comments;
import Model.EmergencySituation;
import Model.EmergencySituations;
import Model.User;
import Model.UserType;
import Model.Users;

@Path("/comments")
public class CommentService {

	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;

	public static int counterComment = 0;
	public static int idComment;
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response add(Comment c) {
		
		User user =  (User) request.getSession().getAttribute("user");
		if(user.getUserType() == UserType.VOLUNTEER && user.isBlocked() == true) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		if(counterComment == 0) {
			counterComment = 1;
			idComment = getComments().getComments().size();
		}
		idComment++;
		
		EmergencySituation emergency = getEmergencies().getEmergencySituation(c.getEmergencySituation().getId());
		
		Comment comment = new Comment(idComment, c.getText(), new Date(), user, emergency);
		getComments().getComments().add(comment);
		try {
			writeToFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Response.status(Status.OK).entity(comment).build();
	}
	
	@GET
	@Path("/emergency/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response users(@PathParam("id") int id) {
		ArrayList<Comment> comments = new ArrayList<Comment>();
		
		for(Comment c : getComments().getComments()) {
			if(c.getEmergencySituation().getId() == id) {
				comments.add(c);
			}
		}
		return Response.status(Status.OK).entity(comments).build();
	}
	
	private void writeToFile() throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ctx.getRealPath("") + "/areas.txt"), StandardCharsets.UTF_16));
		for(Comment c : getComments().getComments()) {
			writer.write(String.valueOf(c.getId()) + ";" + c.getText() + ";" + String.valueOf(c.getDate()) + ";" + c.getUser().getUsername() +
					";" + String.valueOf(c.getEmergencySituation().getId()));
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
	
	private Users getUsers() {
		Users users = (Users) ctx.getAttribute("users");
		if(users == null) {
			users = new Users(ctx.getRealPath(""), getAreas());
			ctx.setAttribute("users", users);
		}
		return users;
	}
	
	private EmergencySituations getEmergencies() {
		EmergencySituations emergencies= (EmergencySituations) ctx.getAttribute("emergencies");
		if(emergencies == null) {
			emergencies = new EmergencySituations(ctx.getRealPath(""), getUsers(), getAreas());
			ctx.setAttribute("emergencies", emergencies);
		}
		return emergencies;
	}

	private Comments getComments() {
		Comments comments= (Comments) ctx.getAttribute("comments");
		if(comments == null) {
			comments = new Comments(ctx.getRealPath(""), getEmergencies(), getUsers());
			ctx.setAttribute("comments", comments);
		}
		return comments;
	}
}
