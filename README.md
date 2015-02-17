# NetworkGame
An 8x8 board game
This game is built from scratch. See how it works here: https://www.youtube.com/watch?v=HNnpGMG5fs0


RULES OF THIS GAME
This is taken from http://www.cs.berkeley.edu/~jrs/61b/hw/pj2/readme
"Network
=======
Network is played on an 8-by-8 board.  There are two players, "Black" and "White."  Each player has ten
chips of its own color to place on the board.  White moves first.

                 -----------------------------------------
                 |    | 10 | 20 | 30 | 40 | 50 | 60 |    |
                 -----------------------------------------
                 | 01 | 11 | 21 | 31 | 41 | 51 | 61 | 71 |
                 -----------------------------------------
                 | 02 | 12 | 22 | 32 | 42 | 52 | 62 | 72 |
                 -----------------------------------------
                 | 03 | 13 | 23 | 33 | 43 | 53 | 63 | 73 |
                 -----------------------------------------
                 | 04 | 14 | 24 | 34 | 44 | 54 | 64 | 74 |
                 -----------------------------------------
                 | 05 | 15 | 25 | 35 | 45 | 55 | 65 | 75 |
                 -----------------------------------------
                 | 06 | 16 | 26 | 36 | 46 | 56 | 66 | 76 |
                 -----------------------------------------
                 |    | 17 | 27 | 37 | 47 | 57 | 67 |    |
                 -----------------------------------------

The board has four goal areas:  the top row, the bottom row, the left column,
and the right column.  Black's goal areas are squares 10, 20, 30, 40, 50, 60
and 17, 27, 37, 47, 57, 67.  Only Black may place chips in these areas.
White's goal areas are 01, 02, 03, 04, 05, 06 and 71, 72, 73, 74, 75, 76; only
White may play there.  The corner squares--00, 70, 07, and 77--are dead;
neither player may use them.  Either player may place a chip in any square not
on the board's border.

Object of Play
==============
Each player tries to complete a "network" joining its two goal areas.
A network is a sequence of six or more chips that starts in one of the player's
goal areas and terminates in the other.  Each consecutive pair of chips in the
sequence are connected to each other along straight lines, either orthogonally
(left, right, up, down) or diagonally.

The diagram below shows a winning configuration for Black.  (There should be
White chips on the board as well, but for clarity these are not shown.)  Here
are two winning black networks.  Observe that the second one crosses itself.

    60 - 65 - 55 - 33 - 35 - 57
    20 - 25 - 35 - 13 - 33 - 55 - 57

                 -----------------------------------------
                 |    |    | BB |    |    |    | BB |    | _0
                 -----------------------------------------
                 |    |    |    |    |    |    |    |    | _1
                 -----------------------------------------
                 |    |    |    |    | BB |    |    |    | _2
                 -----------------------------------------
                 |    | BB |    | BB |    |    |    |    | _3
                 -----------------------------------------
                 |    |    |    |    |    |    |    |    | _4
                 -----------------------------------------
                 |    |    | BB | BB |    | BB | BB |    | _5
                 -----------------------------------------
                 |    |    |    |    |    |    |    |    | _6
                 -----------------------------------------
                 |    |    |    |    |    | BB |    |    | _7
                 -----------------------------------------
                   0_   1_   2_   3_   4_   5_   6_   7_

An enemy chip placed in the straight line between two chips breaks the
connection.  In the second network listed above, a white chip in square 56
would break the connection to Black's lower goal.

Although more than one chip may be placed in a goal area, a network can have
only two chips in the goal areas:  the first and last chips in the network.
Neither of the following are networks, because they both make use of two chips
in the upper goal.

    60 - 20 - 42 - 33 - 35 - 57
    20 - 42 - 60 - 65 - 55 - 57

A network cannot pass through the same chip twice, even if it is only counted
once.  For that reason the following is not a network.

    20 - 25 - 35 - 33 - 55 - 35 - 57

A network cannot pass through a chip without turning a corner (i.e. changing
direction).  Because of the chip in square 42, the following is not a network.

    60 - 42 - 33 - 35 - 55 - 57

Legal Moves
===========
To begin the game, choose who is Black and who is White in any manner (we use a
random number generator).  The players alternate taking turns, with White
moving first.

The first three rules of legal play are fairly simple.
  1)  No chip may be placed in any of the four corners. 
  2)  No chip may be placed in a goal of the opposite color.
  3)  No chip may be placed in a square that is already occupied.

The fourth rule is a bit trickier.
  4)  A player may not have more than two chips in a connected group, whether
      connected orthogonally or diagonally.

This fourth rule means that you cannot have three or more chips of the same
color in a cluster.  A group of three chips form a cluster if one of them is
adjacent to the other two.  In the following diagram, Black is not permitted to
place a chip in any of the squares marked with an X, because doing so would
form a group of 3 or more chips.  (Of course, the far left and right columns
are also off-limits to Black.)

                 -----------------------------------------
                 |    |  X |  X | BB |  X |    |    |    |
                 -----------------------------------------
                 |    |  X | BB |  X |  X |  X |  X |    |
                 -----------------------------------------
                 |    |  X |  X |  X |  X | BB |  X |    |
                 -----------------------------------------
                 |    |    |    |    |  X | BB |  X |    |
                 -----------------------------------------
                 |    |    | BB |    |  X |  X |  X |    |
                 -----------------------------------------
                 |    |  X |  X |    |    |    | BB |    |
                 -----------------------------------------
                 |    | BB |    |    |    |  X |    |    |
                 -----------------------------------------
                 |    |    |    |    | BB |    |    |    |
                 -----------------------------------------

There are two kinds of moves:  add moves and step moves.  In an add move, a
player places a chip on the board (following the rules above).  Each player has
ten chips, and only add moves are permitted until those chips are exhausted.
If neither player has won when all twenty chips are on the board, the rest of
the game comprises step moves.  In a step move, a player moves a chip to a
different square, subject to the same restrictions.  A player is not permitted
to decline to move a piece (nor to "move from square ij to square ij").

A step move may create a network for the opponent by unblocking a connection
between two enemy chips.  If the step move breaks the network at some other
point, the enemy does not win, but if the network is still intact when the chip
has been placed back on the board, the player taking the step move loses.  If
a player makes a move that causes both players to complete a network, the other
player wins."
