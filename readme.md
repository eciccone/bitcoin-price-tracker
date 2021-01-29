# Bitcoin Price Tracker
The **Bitcoin Price Tracker** is a simple command line interface application for tracking the price of Bitcoin using the Coindesk API.

### Functionality
- Get the current USD price of Bitcoin
- Get price change since a specifc date while displaying gain/loss in both USD and percentage

Upon execution, the menu is displayed to the user. There are 3 options to choose from.

1. Get Current Price
2. N-Day Price Change
0. Quit

Option 1 displays the current date and price of Bitcoin to the user.

Option 2 displays the past date, current date, past price, and current price. The gain/loss is also displayed in terms of USD and percentages. Requires asking the user how many days in the past should it should get data on bitcoin. *Note: Comparing the price to 0 days in the past (intra-day price) is a valid option depending on the time of day. At a certain time each day Coindesk writes the current bitcoin price to their historical Bitcoin price API. Only after Coindesk writes this data does a 0 day option work.* 

Option 0 terminates the program.