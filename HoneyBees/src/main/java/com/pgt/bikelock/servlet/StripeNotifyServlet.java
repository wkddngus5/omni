package com.pgt.bikelock.servlet;

import java.io.BufferedReader;
import javax.servlet.ServletException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.pgt.bikelock.bo.MembershipPlanBO;
import java.math.BigDecimal;



public class StripeNotifyServlet extends BaseServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		

		try {

			JsonElement body = new JsonParser().parse(req.getReader());
			String eventType = body.getAsJsonObject().get("type").getAsString();

			if (!eventType.equals("invoice.payment_failed") && !eventType.equals("invoice.payment_succeeded"))
			{
				System.out.println("was expecting invoice.* event type");
				return;
			}

			JsonObject lineItem = body.getAsJsonObject()
				.get("data").getAsJsonObject()
				.get("object").getAsJsonObject()
				.get("lines").getAsJsonObject()
				.get("data").getAsJsonArray()
				.get(0).getAsJsonObject();

			if (!lineItem.get("type").getAsString().equals("subscription")) {
				System.out.println("was expecting body.data.object.lines.data[0] to be subscription line item");
				return;
			}

			String stripeSubId = lineItem.get("id").getAsString();
			boolean wasPaid = eventType.equals("invoice.payment_succeeded");
			BigDecimal amount = lineItem.get("amount").getAsBigDecimal().divide(new BigDecimal(100));

			new MembershipPlanBO().recieveStripeSubscriptionPayment(stripeSubId, wasPaid, amount);

		}
		catch (Exception e) {
			System.out.println("body was malformed");
		}
		
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {


		try {

			JsonElement body = new JsonParser().parse(req.getReader());
			String eventType = body.getAsJsonObject().get("type").getAsString();

			if (!eventType.equals("invoice.payment_failed") && !eventType.equals("invoice.payment_succeeded"))
			{
				System.out.println("was expecting invoice.* event type");
				return;
			}

			JsonObject lineItem = body.getAsJsonObject()
					.get("data").getAsJsonObject()
					.get("object").getAsJsonObject()
					.get("lines").getAsJsonObject()
					.get("data").getAsJsonArray()
					.get(0).getAsJsonObject();

			if (!lineItem.get("type").getAsString().equals("subscription")) {
				System.out.println("was expecting body.data.object.lines.data[0] to be subscription line item");
				return;
			}

			String stripeSubId = lineItem.get("id").getAsString();
			boolean wasPaid = eventType.equals("invoice.payment_succeeded");
			BigDecimal amount = lineItem.get("amount").getAsBigDecimal().divide(new BigDecimal(100));

			new MembershipPlanBO().recieveStripeSubscriptionPayment(stripeSubId, wasPaid, amount);

		}
		catch (Exception e) {
			System.out.println("body was malformed");
		}

	}

}

