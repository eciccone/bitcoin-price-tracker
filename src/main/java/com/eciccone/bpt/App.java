package com.eciccone.bpt;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.json.JSONObject;

import com.eciccone.bpt.model.BitcoinPrice;
import com.eciccone.bpt.util.Output;

/**
 * Bitcoin Price Tracker CLI App.
 * 
 * @author eddie ciccone
 *
 */
public class App {
	
	/**
	 * Gets the unparsed JSON response as a String.
	 * 
	 * @param connection the established connection receiving data from
	 * @return the unparsed JSON response
	 * @throws IOException
	 */
	private String getResponse(HttpURLConnection connection) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		StringBuilder sb = new StringBuilder();
		
		String line;
		while((line = reader.readLine()) != null) {
			sb.append(line);
		}
				
		reader.close();
		
		return sb.toString();
	}
	
	/**
	 * Creates a GET connection to a url.
	 * 
	 * @param urlString the url to make a connection to
	 * @return the HttpURLConnection as a GET request
	 * @throws IOException
	 */
	private HttpURLConnection getConnection(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		return connection;
	}
	
	/**
	 * Gets the current price of Bitcoin using the Coindesk API.
	 * 
	 * @return the price of Bitcoin
	 * @throws IOException
	 */
	public double getCurrentPrice() throws IOException {
		HttpURLConnection connection = getConnection("https://api.coindesk.com/v1/bpi/currentprice/usd.json");
		JSONObject json = new JSONObject(getResponse(connection));
		String currentPrice = json.getJSONObject("bpi").getJSONObject("USD").getString("rate");
		double price = Double.parseDouble(currentPrice.replace(",", ""));
		connection.disconnect();
		return price;
	}
	
	/**
	 * Gets the price of Bitcoin on a specific date using the Coindesk API.
	 * 
	 * @param date the date to get data on Bitcoin
	 * @return the price of Bitcoin
	 * @throws IOException
	 */
	public double getHistoricalPrice(LocalDate date) throws IOException {
		HttpURLConnection connection = getConnection("https://api.coindesk.com/v1/bpi/historical/close.json?start=" + date + "&end=" + date);
		double prevPrice = 0;
		try {
			JSONObject json = new JSONObject(getResponse(connection));
			prevPrice = json.getJSONObject("bpi").getDouble(date.toString());
		} catch(FileNotFoundException e) {
			System.out.println("\nData not availble for that date.");
		}

		connection.disconnect();
		
		return prevPrice;
	}

	/**
	 * Gets all the prices of Bitcoin since a certain day.
	 * 
	 * @param start the date to start getting Bitcoin data
	 * @return a map of dates with their associated Bitcoin price
	 */
	public ArrayList<BitcoinPrice> getHistoricalData(LocalDate start) {
		ArrayList<BitcoinPrice> prices = new ArrayList<BitcoinPrice>();
		LocalDate end = LocalDate.now();
				
		try {
			HttpURLConnection connection = getConnection("https://api.coindesk.com/v1/bpi/historical/close.json?start=" + start + "&end=" + end);
			JSONObject json = new JSONObject(getResponse(connection));

			for(LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
				double price = json.getJSONObject("bpi").getDouble(date.toString());
				prices.add(new BitcoinPrice(price, date));
			}

		} catch(FileNotFoundException e) {
			System.out.println("\nData not availble for that date.");
		} catch (IOException e) {
			System.out.println("Could not connect to Coindesk API.");
		}

		return prices;
	}
	
	/**
	 * Get the menu option selected by the user.
	 * 
	 * @param scan the Scanner object user to get input from the user
	 * @return the users response
	 */
	public int userResponse(Scanner scan) {
		int response = -1;
				
		while(response < 0) {
			try {
				response = scan.nextInt();
				
				if(response < 0) {
					System.out.println("\n Please input a valid option.");
					System.out.print("\n:");
				}
				
			} catch (InputMismatchException e) {
				System.out.println("\n Please input an integer.");
				scan.nextLine();
				System.out.print("\n: ");
			}
		}
		
		return response;
	}
	
	/**
	 * Get the number of days before today from the user.
	 * 
	 * @param scan the Scanner object used to get input from the user
	 * @return the users response
	 */
	public int getNumberOfPastDays(Scanner scan) {
		int response = -1;
		
		while(response < 0) {
			try {
				System.out.println("\n How many days in the past to compare price?");
				System.out.print(": ");
				response = scan.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("\n Please input an integer.");
				scan.nextLine();
			}
		}
		
		return response;
	}

	public static void main(String[] args) throws IOException {
		App app = new App();
		
		// store current price of Bitcoin
		BitcoinPrice currentPrice = new BitcoinPrice();
		currentPrice.setDate(LocalDate.now());
		currentPrice.setPrice(app.getCurrentPrice());
		
		// create object to store historical price
		BitcoinPrice historicalPrice = new BitcoinPrice();
		
		// display menu to the user
		Output.printMenu();
		
		// create scanner used to get user responses
		Scanner scan = new Scanner(System.in);
		
		// create and store user response
		int response = app.userResponse(scan);
		
		// loop until user quits
		while(response != 0) {
			
			// update and display current bitcoin response -> user response = 1
			if(response == 1) {
				
				// update current price of bitcoin
				currentPrice.setPrice(app.getCurrentPrice());
				
				// display updated price
				Output.printPrice(currentPrice);
			} 
			// fetch historical data of bitcoin and display -> user response = 2
			else if(response == 2) {
				
				// get how many days in the past from user
				int pastDays = app.getNumberOfPastDays(scan);
				
				// calculate the date from the days in the past
				LocalDate prevDate = LocalDate.now().minusDays(pastDays);
				
				// fetches the historical price
				double prevPrice = app.getHistoricalPrice(prevDate);
				
				// if the historical price is 0, data is not available for the specific date and step is skipped
				if(prevPrice != 0) {
					// store historical date and price then display to user
					historicalPrice.setDate(prevDate);
					historicalPrice.setPrice(app.getHistoricalPrice(prevDate));
					Output.printPriceChange(currentPrice, historicalPrice, pastDays);
				}
			}
			// fetch historical data for each day and display -> user response = 3
			else if(response == 3) {

				// get how many days in the past from user
				int pastDays = app.getNumberOfPastDays(scan);

				// calculate the date from the days in the past
				LocalDate prevDate = LocalDate.now().minusDays(pastDays);

				// get historical prices and store in hashmap
				ArrayList<BitcoinPrice> history = app.getHistoricalData(prevDate);

				// print each date and associated price
				System.out.println("\n Past " + pastDays + " days: ");
				for(BitcoinPrice bp : history) {
					Output.printPrice(bp);
				}
			}
			
			// display menu and get user response
			Output.printMenu();
			response = app.userResponse(scan);
		}
		
		scan.close();
		
	}

}
