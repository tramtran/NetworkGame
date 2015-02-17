(
  The word "save-board" makes a copies the current board to a temporary
  area.  The word "restore-board" copies the temporary board back to the
  current board.  These words are intended to be used primarily during
  move generation where "what if" questions need to be posed about the
  current state of the board.  Note that during move generation, the
  words $CloneBoard and $DeallocatBoard cannot be used as these use
  the Forth dictionary which is also used during move generation.

  Once saved, the current board can then be modified directly, typically
  typically by using board[] to move/remove pieces and then use the
  current board to drive move generation.  The advantage of being able
  to modify the current board is that all of the direction words and
  words such as "empty?" still apply.  The general pattern is:

  : GenerateMoves
	save-board

	\ tentatively move a piece.
	a10 piece-at
	#Empty a10 board[] !
	a11 board[] !

	\ Place a White Pawn on a20
	White Pawn OR a20 board[] !

	\ generate moves based on the revised board...

	restore-board
  ;

  Note: The game "Limit" uses this pattern.
)

CREATE tempBoard
$boardSize @ ALLOT

: save-board
	$board @ tempBoard $boardSize @ CMOVE
;

: restore-board
	tempBoard $board @ $boardSize @ CMOVE
;
