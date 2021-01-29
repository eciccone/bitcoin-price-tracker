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

Example output:

```
Bitcoin Price Tracker
1. Get Current Price
2. N-Day Price Change
0. Quit
:1

2021-01-29                  USD 34943.87
```

Option 2 displays the past date, current date, past price, and current price. The gain/loss is also displayed in terms of USD and percentages. Requires asking the user how many days in the past should it should get data on bitcoin. *Note: Comparing the price to 0 days in the past (intra-day price) is a valid option depending on the time of day. At a certain time each day Coindesk writes the current bitcoin price to their historical Bitcoin price API. Only after Coindesk writes this data does a 0 day option work.* 

Example output:

```
Bitcoin Price Tracker
1. Get Current Price
2. N-Day Price Change
0. Quit
:2

How many days in the past to compare price?
:7

2021-01-22                  USD 33018.83
2021-01-29                  USD 34727.54

                            Price Diff.         Gain/Loss
7 Day Price Change          USD 1708.72         5.17%
```

Option 0 terminates the program.