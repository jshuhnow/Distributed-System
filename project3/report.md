## Distributed Computing - Assignment 3
Jaeseok Huh, 2015005241, Department of Computer Science and Engineering  
jaeseok@hanyang.ac.kr


### Environment
Ubuntu 18.04 LTS 64bit  
Google Chrome Version 70.0.3538.110 (Official Build) (64-bit)
Ethereum Browser Extension Version 5.2.2
Solidity Version 0.4.24

### Data Structure

#### The accounts of bidders

```
struct bidder {
    address bidderAddress;
    uint tokenBought;
}

mapping (address => bidder) public bidders;
```

#### The auction items
Each *item* stores the highest bidding of each bidder.

```
struct item {
    uint highestBid;
    address highestBidder;
    mapping (address => uint) biddings;
}

item[] items;
```

### The core of programs
#### A constructor of the contract

```
constructor(uint _totalToken,
	uint _tokenPrice) public {

	totalToken = _totalToken;
	balanceTokens = _totalToken;
	tokenPrice = _tokenPrice;

	for (uint i=0; i<numItems; i++) {
		item memory x = item(0, 0);
		items.push(x);
	}
}
```

When the contract is deployed, totalToken and tokenPrice is given. Also, to save the bidding price of each bidder for each item, item is pushed as many as the number of items. The *items* should be initialized in this way due to the strict rule regarding memory allocation in Ethereum, which costs the gas.

#### Charge tokens

```
function buy() payable public returns (int) {
	uint tokensToBuy = msg.value / tokenPrice;
	require (tokensToBuy <= balanceTokens);

	bidders[msg.sender].bidderAddress = msg.sender;
	bidders[msg.sender].tokenBought += tokensToBuy;
	balanceTokens -= tokensToBuy;
}
```

Since the global variable *msg* contains the information related to transaction, you may access it as above. The *payable* must be provided to make sure that the function can receive the funds.

#### Bidding

```

function bid(uint idx, uint token) public {
	require(0<= idx && idx < numItems);

	/* Check that the bidder has the sufficient amount of the additional
	token to be bid in his account */
	require(token - items[idx].biddings[msg.sender]
		<= bidders[msg.sender].tokenBought);

	/* The proposed price must be higher than the highest one so far */
	require(token > items[idx].highestBid);

	/* All but the higest bidder are eligible for bidding */
	require(msg.sender != items[idx].highestBidder);

	/* The difference is deducted from his account */
	bidders[msg.sender].tokenBought -=
		token - items[idx].biddings[msg.sender];

	items[idx].biddings[msg.sender] = token;
	items[idx].highestBid = token;
	items[idx].highestBidder = msg.sender;
}
```

The *require* function throws an exception (and terminate the execution) if the provided condition does not meet. It is necessary to do so to make sure that, for example, the account balance does not go below zero as prescribed.

#### Access APIs

```
function getBiddingInformation() view public returns (uint[], uint[]) {
	uint[] memory highestBids = new uint[](numItems);
	uint[] memory userBids = new uint[](numItems);

	for (uint i=0; i<numItems; i++) {
		highestBids[i] = items[i].highestBid;
		userBids[i] = items[i].biddings[msg.sender];
	}
	return (highestBids, userBids);
}
```

This function is to return the information with respect to the bidding without changing the state of the contract account. The keyword *memory* must be presented for the temporary variable within a function call.

```
function getTokenPrice() view public returns(uint)
{
	return tokenPrice;
}

function getTokenBought() view public returns(uint)
{
	return bidders[msg.sender].tokenBought;
}
```

The functions above returns the unit token price and the token bought by the accessing user.

### The linkage with [*Web3.js*](https://web3js.readthedocs.io/en/1.0/getting-started.html) Library
The [*web3.js*](https://web3js.readthedocs.io/en/1.0/getting-started.html) library is a collection of modules which contain specific functionality for the ethereum ecosystem.

#### Add an EventListener

```
// Checking if Web3 has been injected by the browser (Mist/MetaMask)
if (typeof web3 !== 'undefined') {
// Use Mist/MetaMask's provider
window.web3 = new Web3(web3.currentProvider);
} else {
console.log('No web3? You should consider trying MetaMask!')
// fallback - use your fallback strategy (local node / hosted node + in-dapp id mgmt / fail)
window.web3 = new Web3(new Web3.providers.HttpProvider("http://localhost:8545"));
}
// Now you can start your app & access web3 freely:
startApp();
```

*When a user loads a webpage, MetaMask automatically injects an Ethereum provider and a Web3 instance for the webpage to use. This allows dapps to access the blockchain, propose transactions, and read their users’ account addresses.* [\(Metamask Medium\)](https://medium.com/metamask/https-medium-com-metamask-breaking-change-injecting-web3-7722797916a8) Let's move on to the *startApp()*.

#### startApp()

```
function startApp() {
  DCC_AuctionContract = web3.eth.contract(abi);
  DCC_Auction = DCC_AuctionContract.at(contractAddress);
  document.getElementById('contractAddr').innerHTML = getLink(contractAddress);

  web3.eth.getAccounts(function(e,r){
  document.getElementById('accountAddr').innerHTML = getLink(r[0]);
  accountAddress = r[0];
  getValue();
  });
}
```

*The Contract Application Binary Interface (ABI) is the standard way to interact with contracts in the Ethereum ecosystem, both from outside the blockchain and for contract-to-contract interaction. Data is encoded according to its type, as described in [the] specification. The encoding is not self describing and thus requires a schema in order to decode.*[\(Solidity Docs\)](https://solidity.readthedocs.io/en/develop/abi-spec.html) The variable *abi* is extracted from the [Remix](https://remix.ethereum.org/), a Solidity IDE, after compiling. The two variable of the top serve as the references to the solidity backend.  

*The web3-eth package allows you to interact with an Ethereum blockchain and Ethereum smart contracts.*[\(Web3.js Docs\)](https://web3js.readthedocs.io/en/1.0/web3-eth.html) The method *getAccounts* takes a callback function so that the result can be shown in the HTML as above.  

1) *web3.eth.getBalance(address \[, defaultBlock\] \[, callback\])*  
Get the balance of an address at a given block.
In our source code, the *defaultBlock* is not specified, meaning that the default block set with *web3.eth.defaultBlock* will replace.  

2) *web3.fromWei(number \[, unit\])*  
Since the *Ethereum* does not support the floating type, it should be converted to *uint* type. The function converts any *wei* value into another unit. In our case, *unit* is not specified, thus it is converted into *ether* unit as default.  

3) *web3.toWei(number \[, unit\])*  
Likewise, it converts to *wei*. The *unit* is *ether* unless specified.

