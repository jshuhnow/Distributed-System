pragma solidity ^0.4.24;

contract DCC_Auction {

    uint public totalToken; // ��ū �� ����
    uint public balanceTokens; // ���� ��ū ��
    uint public tokenPrice;
    uint constant numItems = 6;

    struct bidder {
        address bidderAddress;
        uint tokenBought;
    }

    mapping (address => bidder) public bidders;

    struct item {
        uint highestBid;
        address highestBidder;
        mapping (address => uint) biddings;
    }

    item[] items;

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

    function buy() payable public returns (int) {
        uint tokensToBuy = msg.value / tokenPrice;
        require (tokensToBuy <= balanceTokens);

        bidders[msg.sender].bidderAddress = msg.sender;
        bidders[msg.sender].tokenBought += tokensToBuy;
        balanceTokens -= tokensToBuy;
    }

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

    function getBiddingInformation() view public returns (uint[], uint[]) {
        uint[] memory highestBids = new uint[](numItems);
        uint[] memory userBids = new uint[](numItems);

        for (uint i=0; i<numItems; i++) {
            highestBids[i] = items[i].highestBid;
            userBids[i] = items[i].biddings[msg.sender];
        }
        return (highestBids, userBids);
    }

    function getTokenPrice() view public returns(uint)
    {
        return tokenPrice;
    }

    function getTokenBought() view public returns(uint)
    {
        return bidders[msg.sender].tokenBought;
    }
}
