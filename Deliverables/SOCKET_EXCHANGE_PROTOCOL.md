# SOCKET EXCHANGE PROTOCOL

## Introduction:
The following document describes the messaging's rules and procedures that are followed during the socket communication between Client and Server.
Connection port will be n. 1234.

Messages will be strings. Their meaning and function will be defined in this document following this format:

**'To X from Y': 'Type of message': "messageString"**  
    **(FOLLOWING MESSAGE)**
where:
**'To X from Y':** sender and receiver of the message
**Type of message:** a brief description of the message's subject. 
**"messageString":** the string that will be send through socket 							commincation (i.e. the actual message)
**(FOLLOWING MESSAGE) :** The string who always follow the previous message.

## Login procedure
-   S -> C: Login request: "Login"
-   S -> C: Username request: "Send User"
-   C -> S: Send Credential:  `<ClientUser>`":"`<ClientPassword>`
-   S -> C: User acceptance/denial: "Login OK" / "Invalid Enter" / "Username already used" / "Punctuation found" / "Game already started"
----------

## Logout procedure

-   C -> S: Logout warning: "quit"
-   S -> C: Logout answer (client is logged out; connection closed): "quit"

----------

## Game Interaction

Java data will be exchanged by using string, too. The rappresentation used in this document for data produced in game will be `"type_of_data"`
For more complex type of data it will be followed the JSON format. In this case  it will be added `.json` to the rappresentation.

> The convention `"something.json"` shouldn't be mistaken for a JSON file. It's used for the purpose to differentiate between a predetermined message and the string that will be the result of serialization of some java data produced during the game. It'll be used the Gson library to implement de/serialization.

-   Game setup:
    -   S -> C: Prepare Client to receive four SchemaCard: "SchemaCard"
    -   S -> C: Sending four SchemaCard: `"SchemaCard.json"`
    -   S -> C: Prepare Client to recive Private Target Card: "Private"
    -   S -> C: Sending Private Target: `"PrivateTarget.json"`
    -   C -> S: Choice of one schema card:  number of schema choosen

-   Send The Object needed for playing:
    -   S -> C: Prepare Client to receive GlassDash: "Dash"
    -   S -> C: Sending the chosen card: `"Dash.json"`
    -   S -> C: Prepare Client to receive Public Target cards: "PublicTarget"
    -   S -> C: Sending the Public Target cards: `"PublicTarget.json"`
    -   S -> C: Prepare Client to receive Tool cards: "Tool"
    -   S -> C: Sending the Tool cards: `"Tool.json"`
    -   S -> C: Prepare Client to receive the other Players: "Players"
    -   S -> C: Sending the Tool cards: `"Players.json"`
    -   S -> C: Prepare Client to receive the dices in the Reserve: "ReserveView"
    -   S -> C: Sending the Tool cards: `"Reserve.json"`
    -   S -> C: Prepare Client to receive the value of the Turn Timer: "Timer"
    -   S -> C: Sending the number of milliseconds: number of milliseconds
    -   S -> C: Sending if is the turn of this player: "Your Turn"
-   Round and turns:
    -   S -> C: Begin of turn warning: "Your turn"
        (The following message is equal "1" or "2" if is the first or second turn of this round)    

    -   S -> C: When a Player try to do something but isn't his turn:"It's not your turn"
    -   C -> S: When a Client finished his turn (because timer, toolcards, placement): "skip"
    -   S -> C: When the turn is finished and remains one or more dices: "Remove Reserve"
- Tool Cards:
    - C -> S: The request to add some tokens on a tool card: "Token"
    (The following message is the number of tool where the client want to add the
tokens)

    - C -> S: Remove Token: "Tool deselected"
    (The following message contains the number of toolcard deselected)
    
    - C -> S: execute Tool card: "Tool card"
    (The following message contains the index and the `".json"` of the class ToolCardInfo)
    
    - S -> C: Sended if a ToolCard can be played with only one token: "ToolOneToken"
    (The following message contains the number of tool card with this property)
    
    - S -> C: If the player have not enough token for one tool: "notEnoughTokens"
    (The following message contains the number of the tool card)
    
    - S -> C: If the player could use the card: "tokenConfirmed"
    (The following message contains the number of token to subtract)
    
    - S -> C: How many Tokens can use the player:"Current tokens"
    (The following message contains the number of tokens left)
    
    - S -> C: If the current action on a tool card is valid:"validToolCardAction"
    
    - S -> C: If the current action on a tool card is not
    valid:"notValidToolCardAction"
    
    - S -> C : For update the Token's message on the GUI:"addTokenMessage"
    (The following message contains the number of tokens to add)
    
- Dice's placing:
    - S -> C: When the player correctly insert a dice into his Dash:"UpdateUserSchema"
    (The following message is divided into two parts:
        1.dice's position
        2.`"dice.json"`)
    
    - S -> C: When other player insert a dice into their Dash:"UpdateOtherUserSchema"
    (The following message is divided into three parts:
        1. id of the schema card
        2. dice's position
        3. `"dice.json"`)
    
    - S -> C: When the player try to make an uncorrectly placing: "notValidDice"
    
    - S -> C: When the turn is finished there is a dice to add to round track:"AddToRoundTrack"
    (The following message contains the `".json"` of the dice to add)
    
    - S -> C: Used to remove a dice in the player' Schema:"RemoveUserSchema"
    (The following message contains the position of the dice to remove)
    
    - S -> C: Used to update the Other User Schema in the GUI:"RemoveOtherUserSchema"
    (The following message is divides into two parts:
        1.Id of the schema card
        2. Dice's position)
    
    - S -> C: Change one dice on the round track:"UpdateRoundTrack"
    (The following message is divided into two parts:
        1. The position on the Round Track to Update
        2. The `".json"` of the new dice to add)
    
- Disconnection and Reconnection:
    - S -> C: Is used to show the disconnected player :"New Disconnected"
    (The following message contains the user's name who is disconnected)

    - S -> C: Is used to show the player who is reconnected to game:"Reconnected"
    (The following message contains the user's name who is reconnected)

- End Game:
    - S -> C: When the game is over and we have to show the ranking:"Ranking"
    (The following message contains a `"player.json"`)

- Other:
    - S -> C: Warn the player when it's time to show the RoundView:"showRoundView"
    
    
    