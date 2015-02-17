\ On an 8x8 board, return all neighbors.

: _a8 b8 a7 b7 3 ;
: _b8 a8 c8 a7 b7 c7 5 ;
: _c8 b8 d8 b7 c7 d7 5 ;
: _d8 c8 e8 c7 d7 e7 5 ;
: _e8 d8 f8 d7 e7 f7 5 ;
: _f8 e8 g8 e7 f7 g7 5 ;
: _g8 f8 h8 f7 g7 h7 5 ;
: _h8 g8 g7 h7 3 ;

: _a7 a8 b8 b7 a6 b6 5 ;
: _b7 a8 b8 c8 a7 c7 a6 b6 c6 8 ;
: _c7 b8 c8 d8 b7 d7 b6 c6 d6 8 ;
: _d7 c8 d8 e8 c7 e7 c6 d6 e6 8 ;
: _e7 d8 e8 f8 d7 f7 d6 e6 f6 8 ;
: _f7 e8 f8 g8 e7 g7 e6 f6 g6 8 ;
: _g7 f8 g8 h8 f7 h7 f6 g6 h6 8 ;
: _h7 g8 h8 g7 g6 h6 5 ;

: _a6 a7 b7 b6 a5 b5 5 ;
: _b6 a7 b7 c7 a6 c6 a5 b5 c5 8 ;
: _c6 b7 c7 d7 b6 d6 b5 c5 d5 8 ;
: _d6 c7 d7 e7 c6 e6 c5 d5 e5 8 ;
: _e6 d7 e7 f7 d6 f6 d5 e5 f5 8 ;
: _f6 e7 f7 g7 e6 g6 e5 f5 g5 8 ;
: _g6 f7 g7 h7 f6 h6 f5 g5 h5 8 ;
: _h6 g7 h7 g6 g5 h5 5 ;

: _a5 a6 b6 b5 a4 b4 5 ;
: _b5 a6 b6 c6 a5 c5 a4 b4 c4 8 ;
: _c5 b6 c6 d6 b5 d5 b4 c4 d4 8 ;
: _d5 c6 d6 e6 c5 e5 c4 d4 e4 8 ;
: _e5 d6 e6 f6 d5 f5 d4 e4 f4 8 ;
: _f5 e6 f6 g6 e5 g5 e4 f4 g4 8 ;
: _g5 f6 g6 h6 f5 h5 f4 g4 h4 8 ;
: _h5 g6 h6 g5 g4 h4 5 ;

: _a4 a5 b5 b4 a3 b3 5 ;
: _b4 a5 b5 c5 a4 c4 a3 b3 c3 8 ;
: _c4 b5 c5 d5 b4 d4 b3 c3 d3 8 ;
: _d4 c5 d5 e5 c4 e4 c3 d3 e3 8 ;
: _e4 d5 e5 f5 d4 f4 d3 e3 f3 8 ;
: _f4 e5 f5 g5 e4 g4 e3 f3 g3 8 ;
: _g4 f5 g5 h5 f4 h4 f3 g3 h3 8 ;
: _h4 g5 h5 g4 g3 h3 5 ;

: _a3 a4 b4 b3 a2 b2 5 ;
: _b3 a4 b4 c4 a3 c3 a2 b2 c2 8 ;
: _c3 b4 c4 d4 b3 d3 b2 c2 d2 8 ;
: _d3 c4 d4 e4 c3 e3 c2 d2 e2 8 ;
: _e3 d4 e4 f4 d3 f3 d2 e2 f2 8 ;
: _f3 e4 f4 g4 e3 g3 e2 f2 g2 8 ;
: _g3 f4 g4 h4 f3 h3 f2 g2 h2 8 ;
: _h3 g4 h4 g3 g2 h2 5 ;

: _a2 a3 b3 b2 a1 b1 5 ;
: _b2 a3 b3 c3 a2 c2 a1 b1 c1 8 ;
: _c2 b3 c3 d3 b2 d2 b1 c1 d1 8 ;
: _d2 c3 d3 e3 c2 e2 c1 d1 e1 8 ;
: _e2 d3 e3 f3 d2 f2 d1 e1 f1 8 ;
: _f2 e3 f3 g3 e2 g2 e1 f1 g1 8 ;
: _g2 f3 g3 h3 f2 h2 f1 g1 h1 8 ;
: _h2 g3 h3 g2 g1 h1 5 ;

: _a1 a2 b2 b1 3 ;
: _b1 a2 b2 c2 a1 c1 5 ;
: _c1 b2 c2 d2 b1 d1 5 ;
: _d1 c2 d2 e2 c1 e1 5 ;
: _e1 d2 e2 f2 d1 f1 5 ;
: _f1 e2 f2 g2 e1 g1 5 ;
: _g1 f2 g2 h2 f1 h1 5 ;
: _h1 g2 h2 g1 3 ; 

CREATE neighbors
' _a8 , ' _b8 , ' _c8 , ' _d8 , ' _e8 , ' _f8 , ' _g8 , ' _h8 ,
' _a7 , ' _b7 , ' _c7 , ' _d7 , ' _e7 , ' _f7 , ' _g7 , ' _h7 ,
' _a6 , ' _b6 , ' _c6 , ' _d6 , ' _e6 , ' _f6 , ' _g6 , ' _h6 ,
' _a5 , ' _b5 , ' _c5 , ' _d5 , ' _e5 , ' _f5 , ' _g5 , ' _h5 ,
' _a4 , ' _b4 , ' _c4 , ' _d4 , ' _e4 , ' _f4 , ' _g4 , ' _h4 ,
' _a3 , ' _b3 , ' _c3 , ' _d3 , ' _e3 , ' _f3 , ' _g3 , ' _h3 ,
' _a2 , ' _b2 , ' _c2 , ' _d2 , ' _e2 , ' _f2 , ' _g2 , ' _h2 ,
' _a1 , ' _b1 , ' _c1 , ' _d1 , ' _e1 , ' _f1 , ' _g1 , ' _h1 ,

: Neighbors ( pos -- pos1...posn count )
	CELLS neighbors + @ EXECUTE
;