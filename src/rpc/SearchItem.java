package rpc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Item;
import external.TicketMasterAPI;


/**
 * Servlet implementation class SearchItem
 */
@WebServlet("/search")
public class SearchItem extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SearchItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//		response.addHeader("Access-Control-Allow-Origin", "*");

		// edition 1
		//		//Create a PrintWriter from response such that we can add data to response.
		//		PrintWriter out = response.getWriter();
		//		if (request.getParameter("username") != null) {
		//			//Get the username sent from the client
		//			String username = request.getParameter("username");
		//            //In the output stream, add something to response body. 
		//			out.print("Hello " + username);
		//		}
		//		// Send response back to client
		//		out.close();

		// edition 2
		//		response.setContentType("application/json");
		//		response.addHeader("Access-Control-Allow-Origin", "*");
		//		PrintWriter out = response.getWriter();
		//
		//		JSONArray array = new JSONArray();
		//		try {
		//			array.put(new JSONObject().put("username", "abcd"));
		//			array.put(new JSONObject().put("username", "1234"));
		//		} catch (JSONException e) {
		//			e.printStackTrace();
		//		}
		//		out.print(array);
		//		out.close();

		// edit 3: json
		//		JSONArray array = new JSONArray();
		//		try {
		//			array.put(new JSONObject().put("username", "abcd"));
		//			array.put(new JSONObject().put("username", "1234"));
		//		} catch (JSONException e) {
		//			e.printStackTrace();
		//		}
		//		RpcHelper.writeJsonArray(response, array);

		// edit 4: call tm api
		String userId = request.getParameter("user_id");
		double lat = Double.parseDouble(request.getParameter("lat"));
		double lon = Double.parseDouble(request.getParameter("lon"));
		// term can be empty or null.
		String term = request.getParameter("term");

		// edit 5
		//		TicketMasterAPI tmAPI = new TicketMasterAPI();
		//		List<Item> items = tmAPI.search(lat, lon, term);
		//		JSONArray array = new JSONArray();

		DBConnection connenction = DBConnectionFactory.getDBConnection();
		List<Item> items = connenction.searchItems(lat, lon, term);
		Set<String> favorite = connenction.getFavoriteItemIds(userId);
		List<JSONObject> list = new ArrayList<>();

		try {
			for (Item item : items) {
				// Add a thin version of item object
				JSONObject obj = item.toJSONObject();
				// Check if this is a favorite one.
				// This field is required by frontend to correctly display favorite items.
				obj.put("favorite", favorite.contains(item.getItemId()));
				list.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONArray array = new JSONArray(list);
		RpcHelper.writeJsonArray(response, array);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
