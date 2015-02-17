\ Board marking support - Typically this is used to track previously visited board positions.

CREATE Marks #BoardSize ALLOT

: UnmarkAll
	Marks #BoardSize ERASE
;

: Mark ( index -- )
	Marks + TRUE SWAP C!
;

: Unmark ( index -- )
	Marks + FALSE SWAP C!
;

: Marked? ( index -- ? )
	Marks + C@
;

: Unmarked? ( index -- ? )
	Marked? NOT
;
