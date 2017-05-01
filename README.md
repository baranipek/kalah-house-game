## Kalah House Game
Here is the Kalah(is called Mangala as well) House game implementation with 6 stones. For further information please  read wiki link
(https://en.wikipedia.org/wiki/Kalah)

Each of the two players has six pits in front of him/her. To the right of the six pits, each player

has a larger pit, his Kalah or house. At the start of the game, six stones are put In each pit.

The player who begins picks up all the stones in any of their own pits, and sows the stones on to

the right, one in each of the following pits, including his own Kalah. No stones are put in the

opponent's' Kalah. If the players last stone lands in his own Kalah, he gets another turn. This can

be repeated any number of times before it's the other player's turn.

when the last stone lands in an own empty pit, the player captures this stone and all stones in

the opposite pit (the other players' pit) and puts them in his own Kalah.

The game is over as soon as one of the sides run out of stones. The player who still has stones

in his/her pits keeps them and puts them in his/hers Kalah. The winner of the game is the player

who has the most stones in his Kalah.

## How to run

First you need Java 8  and  can run the application using

mvn clean package and run the JAR

mvn spring-boot:run Or you can build the JAR file with

java -jar target/kalah-house-game-0.0.1-SNAPSHOT.jar

## Shipping Easily So lets Dockerize
If you want to build docker image the steps are below

docker build .

docker images //list the images id

docker tag efc63e8b44a8(custom value, image id) baranipek/kalah-house-game

docker run -p 8080:8080 baranipek/kalah-house-game  //run application on 8080 port


##  API EndPoints

1.You can reach swagger ui via http://localhost:8080/swagger-ui.html and show and list the endpoints
  also dummy inputs are ready by swagger

2.First Endpoint is post method provices gameboard id and default player and holes and seeds on it.

    Request : curl -X POST --header "Content-Type: application/json" --header "Accept: */*" "http://localhost:8080/api/gameboard"

    Response :
   {
     "id": "b37ee4a8-5e25-479e-8707-08f3c7a4d49c",
     "activePlayer": "NORTH",
     "playerNorth": {
       "id": "NORTH",
       "holeList": [
         {
           "id": "ONE",
           "seeds": 6
         },
         {
           "id": "TWO",
           "seeds": 6
         },
         {
           "id": "THREE",
           "seeds": 6
         },
         {
           "id": "FOUR",
           "seeds": 6
         },
         {
           "id": "FIVE",
           "seeds": 6
         },
         {
           "id": "SIX",
           "seeds": 6
         }
       ],
       "endZone": {
         "seeds": 0
       }
     },
     "playerSouth": {
       "id": "SOUTH",
       "holeList": [
         {
           "id": "ONE",
           "seeds": 6
         },
         {
           "id": "TWO",
           "seeds": 6
         },
         {
           "id": "THREE",
           "seeds": 6
         },
         {
           "id": "FOUR",
           "seeds": 6
         },
         {
           "id": "FIVE",
           "seeds": 6
         },
         {
           "id": "SIX",
           "seeds": 6
         }
       ],
       "endZone": {
         "seeds": 0
       }
     },
     "hasWinner": false
   }


  3.Other endpoint for getting information by gameboard id.
   Request : curl -X GET --header "Content-Type: application/json" "Content-Type: application/json" "Accept: */*" "http://localhost:8080/api/gameboard/b37ee4a8-5e25-479e-8707-08f3c7a4d49c"

  4. /api/gameboard is listed all stored gameboard's information
  
  5. If you want to move seeds into another hole use put operation. The game stars with the north player
     and players plays according to game rules and application manages it.Active player indicates whose
     turn it is.

    //north player starts with one hole
    http://localhost:8080/api/gameboard/b37ee4a8-5e25-479e-8707-08f3c7a4d49c"/hole/ONE

    {
      "id": "b37ee4a8-5e25-479e-8707-08f3c7a4d49c",
      "activePlayer": "NORTH",
      "playerNorth": {
        "id": "NORTH",
        "holeList": [
          {
            "id": "ONE",
            "seeds": 0
          },
          {
            "id": "TWO",
            "seeds": 7
          },
          {
            "id": "THREE",
            "seeds": 7
          },
          {
            "id": "FOUR",
            "seeds": 7
          },
          {
            "id": "FIVE",
            "seeds": 7
          },
          {
            "id": "SIX",
            "seeds": 7
          }
        ],
        "endZone": {
          "seeds": 1
        }
      },
      "playerSouth": {
        "id": "SOUTH",
        "holeList": [
          {
            "id": "ONE",
            "seeds": 6
          },
          {
            "id": "TWO",
            "seeds": 6
          },
          {
            "id": "THREE",
            "seeds": 6
          },
          {
            "id": "FOUR",
            "seeds": 6
          },
          {
            "id": "FIVE",
            "seeds": 6
          },
          {
            "id": "SIX",
            "seeds": 6
          }
        ],
        "endZone": {
          "seeds": 0
        }
      },
      "hasWinner": false
    }



ENJOY!
