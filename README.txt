=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: 64481111
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Arrays
    - I used a 4x4 2D array to store my game board. It stores the data in rows and columns, and I implemented it
      when shifting my game board up, down, right, or left through rotating and sliding the blocks.

  2. Collections or Maps
    - I used collections in the form of LinkedLists to store the score history and the game board history.
      For score history, I used a LinkedList of integers. For game board history, I used a LinkedList 2D int arrays.
      This allows me to implement the undo feature, which allows the user to go back to previous versions of the game
      board and score.

  3. File I/O
    - I used two files (gameHistory.txt and scoreHistory.txt) to save the state of the game. When the user chooses to
      load the previous game, it will allow the user to continue playing as if they never stopped. It also lets the user
      undo moves that were played from that previous game.

  4. JUnit Testable
    - The game follows an encapsulated game model that functions independently of the GUI. It can be updated and
      queried by calling methods and is unit testable.

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
    - G2048
        - The encapsulated game model that has all the core functionalities of the game
    - GameBoard
        - It listens to keyboard presses and calls on methods from G2048 to let the user play the game.
          In addition, it updates the status of the game and creates the visuals for the game board.
    - RunG2048
        - It implements the Runnable part of the game and creates the game board and clickable buttons.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
    - When I was implementing my game, I had a difficult time with figuring out how to shift the blocks up, down, right,
      and left. The way I figured out how to solve it was drawing out the different scenarios and trying to see how I
      could update my game board 2D array. Another challenge was checking out how to see when the game ended when all
      the blocks were filled. The way I addressed this was by trying to shift a copy of my game board in all directions
      to see if another move was possible.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
    - I think there was a good separation of functionality and that private state is encapsulated well. The game board,
      score, and history cannot be edited by the user. I provide methods that will fetch the values of some of my
      private variables, and it does not pass the reference of my object as the value. I have refactored my code
      several times, and I am happy with how it is.


========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.
  - https://www.washingtonpost.com/news/arts-and-entertainment/wp/2014/04/23/everything-you-ever-wanted-to-know-about-2048-the-internets-latest-impossible-hit-game/
