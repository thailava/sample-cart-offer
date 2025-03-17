package com.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.controller.ApplyOfferRequest;
import com.springboot.controller.OfferRequest;
import com.springboot.controller.SegmentResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartOfferApplicationTests {

	String urlString_Offer = "http://localhost:9001/api/v1/offer";
	String urlString_ApplyOffer = "http://localhost:9001/api/v1/cart/apply_offer";


	@Test
	public void checkFlatXForOneSegment() throws Exception {
		List<String> segments = new ArrayList<>();
		segments.add("p1");
		OfferRequest offerRequest = new OfferRequest(1,"FLATX",10,segments);
		ObjectMapper mapper = new ObjectMapper();

		String offerRequestBody = mapper.writeValueAsString(offerRequest);
		String offerResponse = postMethodReturn(urlString_Offer, offerRequestBody);

		ApplyOfferRequest applyOfferRequest = new ApplyOfferRequest();
		applyOfferRequest.setRestaurant_id(1);
		applyOfferRequest.setCart_value(200);
		applyOfferRequest.setUser_id(1);

		String applyOfferRequestBody = mapper.writeValueAsString(applyOfferRequest);
		String applyOfferResponse = postMethodReturn(urlString_ApplyOffer, applyOfferRequestBody);
		int cartValue = mapper.readTree(applyOfferResponse).get("cart_value").asInt();
		Assert.assertEquals("Verify the Cart value after applying discount", cartValue, 190);
	}

	@Test
	public void checkFlatXForAllSegments() throws Exception {
		List<String> segments = new ArrayList<>();
		segments.add("p1");
		segments.add("p2");
		segments.add("p3");
		OfferRequest offerRequest = new OfferRequest(1, "FLATX", 10, segments);
		ObjectMapper mapper = new ObjectMapper();
		String offerRequestBody = mapper.writeValueAsString(offerRequest);
		String offerResponse = postMethodReturn(urlString_Offer, offerRequestBody);

		ApplyOfferRequest applyOfferRequest = new ApplyOfferRequest();
		applyOfferRequest.setRestaurant_id(1);
		applyOfferRequest.setCart_value(200);
		applyOfferRequest.setUser_id(1);
		String applyOfferRequestBody = mapper.writeValueAsString(applyOfferRequest);
		String applyOfferResponse = postMethodReturn(urlString_ApplyOffer, applyOfferRequestBody);

		int cartValue = mapper.readTree(applyOfferResponse).get("cart_value").asInt();
		Assert.assertEquals("verify the cart value after applying discount", 190, cartValue);
	}

	@Test
	public void checkFlatXPercentageOffer() throws Exception {
		List<String> segments = new ArrayList<>();
		segments.add("p1");
		OfferRequest offerRequest = new OfferRequest(1, "FLATX%", 10, segments);
		ObjectMapper mapper = new ObjectMapper();
		String offerRequestBody = mapper.writeValueAsString(offerRequest);
		String offerResponse = postMethodReturn(urlString_Offer, offerRequestBody);

		ApplyOfferRequest applyOfferRequest = new ApplyOfferRequest();
		applyOfferRequest.setRestaurant_id(1);
		applyOfferRequest.setCart_value(200);
		applyOfferRequest.setUser_id(1);
		String applyOfferRequestBody = mapper.writeValueAsString(applyOfferRequest);
		String applyOfferResponse = postMethodReturn(urlString_ApplyOffer, applyOfferRequestBody);

		int cartValue = mapper.readTree(applyOfferResponse).get("cart_value").asInt();
		Assert.assertEquals("verify the cart value after applying discount", 180, cartValue);
	}

	@Test
	public void checkFlatXPercentageOfferForAllSegments() throws Exception {
		List<String> segments = new ArrayList<>();
		segments.add("p1");
		segments.add("p2");
		segments.add("p3");
		OfferRequest offerRequest = new OfferRequest(1, "FLATX%", 10, segments);
		ObjectMapper mapper = new ObjectMapper();
		String offerRequestBody = mapper.writeValueAsString(offerRequest);
		String offerResponse = postMethodReturn(urlString_Offer, offerRequestBody);

		ApplyOfferRequest applyOfferRequest = new ApplyOfferRequest();
		applyOfferRequest.setRestaurant_id(1);
		applyOfferRequest.setCart_value(200);
		applyOfferRequest.setUser_id(1);
		String applyOfferRequestBody = mapper.writeValueAsString(applyOfferRequest);
		String applyOfferResponse = postMethodReturn(urlString_ApplyOffer, applyOfferRequestBody);

		int cartValue = mapper.readTree(applyOfferResponse).get("cart_value").asInt();
		Assert.assertEquals("verify the cart value after applying discount", 180, cartValue);
	}

	@Test
	public void checkFlatXOfferHigherThanCartValue() throws Exception {
		List<String> segments = new ArrayList<>();
		segments.add("p1");
		OfferRequest offerRequest = new OfferRequest(1, "FLATX", 500, segments);
		ObjectMapper mapper = new ObjectMapper();
		String offerRequestBody = mapper.writeValueAsString(offerRequest);
		String offerResponse = postMethodReturn(urlString_Offer, offerRequestBody);

		ApplyOfferRequest applyOfferRequest = new ApplyOfferRequest();
		applyOfferRequest.setRestaurant_id(1);
		applyOfferRequest.setCart_value(400);
		applyOfferRequest.setUser_id(1);
		String applyOfferRequestBody = mapper.writeValueAsString(applyOfferRequest);
		String applyOfferResponse = postMethodReturn(urlString_ApplyOffer, applyOfferRequestBody);

		int cartValue = mapper.readTree(applyOfferResponse).get("cart_value").asInt();
		Assert.assertEquals("Verify vart value is 0 if the discount amount is higher", 0, cartValue);
	}

	@Test
	public void checkNegativeValueInFlatXAmount() throws Exception {
		List<String> segments = new ArrayList<>();
		segments.add("p1");
		OfferRequest offerRequest = new OfferRequest(1, "FLATX", -10, segments);
		ObjectMapper mapper = new ObjectMapper();
		String offerRequestBody = mapper.writeValueAsString(offerRequest);
		String offerResponse = postMethodReturn(urlString_Offer, offerRequestBody);

		Assert.assertTrue("Verify the error message", offerResponse.contains("Invalid discount value"));
	}

	@Test
	public void checkZeroPercentageFlatXOffer() throws Exception {
		List<String> segments = new ArrayList<>();
		segments.add("p1");
		OfferRequest offerRequest = new OfferRequest(1, "FLATX%", 0, segments);
		ObjectMapper mapper = new ObjectMapper();
		String offerRequestBody = mapper.writeValueAsString(offerRequest);
		String offerResponse = postMethodReturn(urlString_Offer, offerRequestBody);

		ApplyOfferRequest applyOfferRequest = new ApplyOfferRequest();
		applyOfferRequest.setRestaurant_id(1);
		applyOfferRequest.setCart_value(200);
		applyOfferRequest.setUser_id(1);
		String applyOfferRequestBody = mapper.writeValueAsString(applyOfferRequest);
		String applyOfferResponse = postMethodReturn(urlString_ApplyOffer, applyOfferRequestBody);

		int cartValue = mapper.readTree(applyOfferResponse).get("cart_value").asInt();
		Assert.assertEquals("Verify cart value is not affected", 200, cartValue);
	}

	@Test
	public void checkInvalidSegmentOffer() throws Exception {
		List<String> segments = new ArrayList<>();
		segments.add("invalid_segment");
		OfferRequest offerRequest = new OfferRequest(1, "FLATX", 10, segments);
		ObjectMapper mapper = new ObjectMapper();
		String offerRequestBody = mapper.writeValueAsString(offerRequest);
		String offerResponse = postMethodReturn(urlString_Offer, offerRequestBody);

		Assert.assertTrue("Verify the error message", offerResponse.contains("Segment not found"));
	}

	@ParameterizedTest
	@CsvSource({
			"1,190",
			"50,150",
			"100,100"
	})
	public void checkFlatXForMultipleAmounts(int discountAmount, int expectedCartValue) throws Exception {
		List<String> segments = new ArrayList<>();
		segments.add("p1");
		OfferRequest offerRequest = new OfferRequest(1, "FLATX", discountAmount, segments);
		ObjectMapper mapper = new ObjectMapper();

		String offerRequestBody = mapper.writeValueAsString(offerRequest);
		String offerResponse = postMethodReturn(urlString_Offer, offerRequestBody);

		ApplyOfferRequest applyOfferRequest = new ApplyOfferRequest();
		applyOfferRequest.setRestaurant_id(1);
		applyOfferRequest.setCart_value(200);
		applyOfferRequest.setUser_id(1);

		String applyOfferRequestBody = mapper.writeValueAsString(applyOfferRequest);
		String applyOfferResponse = postMethodReturn(urlString_ApplyOffer, applyOfferRequestBody);
		int cartValue = mapper.readTree(applyOfferResponse).get("cart_value").asInt();
		Assert.assertEquals("Verify the cart value after applying the discount", expectedCartValue, cartValue);
	}

	@ParameterizedTest
	@CsvSource({
			"10,180",
			"50,100",
			"60,80"
	})
	public void checkFlatXPercentForMultipleAmounts(int discountPercent, int expectedCartValue) throws Exception {
		List<String> segments = new ArrayList<>();
		segments.add("p1");
		OfferRequest offerRequest = new OfferRequest(1, "FLATX%", discountPercent, segments);
		ObjectMapper mapper = new ObjectMapper();

		String offerRequestBody = mapper.writeValueAsString(offerRequest);
		String offerResponse = postMethodReturn(urlString_Offer, offerRequestBody);

		ApplyOfferRequest applyOfferRequest = new ApplyOfferRequest();
		applyOfferRequest.setRestaurant_id(1);
		applyOfferRequest.setCart_value(200);
		applyOfferRequest.setUser_id(1);

		String applyOfferRequestBody = mapper.writeValueAsString(applyOfferRequest);
		String applyOfferResponse = postMethodReturn(urlString_ApplyOffer, applyOfferRequestBody);
		int cartValue = mapper.readTree(applyOfferResponse).get("cart_value").asInt();
		Assert.assertEquals("Verify the cart value after applying discount", expectedCartValue, cartValue);
	}

	@Test
	public void checkInvalidRestaurantErrorMessage() throws Exception {
		List<String> segments = new ArrayList<>();
		segments.add("p1");
		OfferRequest offerRequest = new OfferRequest(1, "FLATX%", 0, segments);
		ObjectMapper mapper = new ObjectMapper();
		String offerRequestBody = mapper.writeValueAsString(offerRequest);
		String offerResponse = postMethodReturn(urlString_Offer, offerRequestBody);

		ApplyOfferRequest applyOfferRequest = new ApplyOfferRequest();
		applyOfferRequest.setRestaurant_id(199888); //invalid restaurant id
		applyOfferRequest.setCart_value(200);
		applyOfferRequest.setUser_id(1);
		String applyOfferRequestBody = mapper.writeValueAsString(applyOfferRequest);
		String applyOfferResponse = postMethodReturn(urlString_ApplyOffer, applyOfferRequestBody);
		//verify the error message in response
	}

	public boolean addOffer(OfferRequest offerRequest) throws Exception {
		String urlString = "http://localhost:9001/api/v1/offer";
		URL url = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setDoOutput(true);
		con.setRequestProperty("Content-Type", "application/json");

		ObjectMapper mapper = new ObjectMapper();

		String POST_PARAMS = mapper.writeValueAsString(offerRequest);
		OutputStream os = con.getOutputStream();
		os.write(POST_PARAMS.getBytes());
		os.flush();
		os.close();
		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			// print result
			System.out.println(response.toString());
		} else {
			System.out.println("POST request did not work.");
		}
		return true;
	}

	//common method for all post calls

	private static String postMethodReturn(String urlString, String requestBody) {
		StringBuffer response = new StringBuffer();
		try{
			URL url = new URL(urlString);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			System.out.println(requestBody);

			OutputStream os = con.getOutputStream();
			os.write(requestBody.getBytes());
			os.flush();
			os.close();
			int responseCode = con.getResponseCode();
			Assert.assertEquals("Verify the status code ", responseCode, HttpURLConnection.HTTP_OK);

			if (responseCode == HttpURLConnection.HTTP_OK) { //success
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;


				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				// print result
				System.out.println(response.toString());
			} else {
				System.out.println("POST request did not work.");
			}

		}
		catch (Exception e){
			System.out.println(e.getMessage());
		}
		return response.toString();
	}
}
