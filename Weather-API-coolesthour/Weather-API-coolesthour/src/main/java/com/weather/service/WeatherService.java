package com.weather.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.model.CoolestData;
import com.weather.model.Data;
import com.weather.model.Response;

public class WeatherService {

	public static CoolestData coolestTemp(String countryCode, Integer zipCode) {

		Float coolestTemp = null;
		String coolestHour = null;
		String city = null;
		SimpleDateFormat weatherSDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfFormat = new SimpleDateFormat("HH");

		try {

			Response response = getDataFromAPI(countryCode, zipCode);
			String tomorrowDate = getTomorrowDate(sdf);
			city = response.getCity_name() + "," + response.getCountry_code();

			for (Data data : response.getData()) {

				String weatherDateString = data.getTimestamp_local();
				Date weatherDate = weatherSDF.parse(weatherDateString);
				String weatherFormattedDate = sdf.format(weatherDate);

				if (weatherFormattedDate.equals(tomorrowDate)) {

					Float currentTemp = Float.parseFloat(data.getTemp());
					String formattedDate = sdfFormat.format(weatherDate);

					if (coolestTemp == null) {
						coolestTemp = currentTemp;
						coolestHour = formattedDate;
					} else if (currentTemp < coolestTemp) {
						coolestTemp = currentTemp;
						coolestHour = formattedDate;
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		CoolestData coolestData = new CoolestData();
		coolestData.setHour(coolestHour);
		coolestData.setTemp(coolestTemp);
		coolestData.setCity(city);
		return coolestData;

	}

	/**
	 * @param sdf
	 * @return
	 */
	private static String getTomorrowDate(SimpleDateFormat sdf) {

		Date currentDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		calendar.add(Calendar.DATE, 1);
		String currentDateString = sdf.format(calendar.getTime());

		return currentDateString;
	}

	/**
	 * @param countryCode
	 * @param zipCode
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ProtocolException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 */
	private static Response getDataFromAPI(String countryCode, Integer zipCode)
			throws MalformedURLException, IOException, ProtocolException, JsonParseException, JsonMappingException {

		String baseURL = "https://api.weatherbit.io/v2.0/forecast/hourly";
		String finalURL = baseURL + "?" + "postal_code=" + zipCode + "&country=" + countryCode + "&units=M&key="
				+ "e2d6801fcecb4784ad007356cd1cc960";
		
		URL url = new URL(finalURL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");

		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

		ObjectMapper mapper = new ObjectMapper();
		Response response = mapper.readValue(br.readLine(), Response.class);

		conn.disconnect();

		return response;
	}

}
