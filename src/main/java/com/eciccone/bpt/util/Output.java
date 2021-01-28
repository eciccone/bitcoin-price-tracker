package com.eciccone.bpt.util;

import com.eciccone.bpt.model.BitcoinPrice;

/**
 * Class to hold static methods to easily output data to user.
 * 
 * @author eddie ciccone
 *
 */
public class Output {

	public static void printMenu() {
		System.out.println("\n Bitcoin Price Tracker");
		System.out.println(" 1. Get Current Price");
		System.out.println(" 2. N-Day Price Change");
		System.out.println(" 0. Quit");
		System.out.print(":");
	}
	
	public static void printPrice(BitcoinPrice bitcoin) {
		System.out.printf("\n %-30s %-30s", bitcoin.getDate(), "USD " + String.format("%.2f \n", bitcoin.getPrice()));
	}
	
	public static void printPriceChange(BitcoinPrice bitcoin, BitcoinPrice historical, int n) {
		double diffPrice = bitcoin.getPrice() - historical.getPrice();
		double percentage = (diffPrice / historical.getPrice()) * 100;
		
		System.out.printf("\n %-30s %-30s", historical.getDate(), "USD " + String.format("%.2f", historical.getPrice()));
		System.out.printf("\n %-30s %-30s", bitcoin.getDate(), "USD " + String.format("%.2f", bitcoin.getPrice()));
		
		System.out.printf("\n\n %-30s %-30s %-30s", "", "Price Diff.", "Gain/Loss");
		
		String nStr = "";
		
		if(n == 0)
			nStr += "Intra";
		else 
			nStr += n;
		
		System.out.printf("\n %-30s %-30s %-30s", nStr + " Day Price Change", 
				"USD " + String.format("%.2f", diffPrice),
				String.format("%.2f", percentage) + "%\n");
	}
}
