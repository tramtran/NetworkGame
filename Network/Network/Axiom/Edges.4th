\ ToDo: Eliminate corners

CREATE N_Edge
a8 C, b8 C, c8 C, d8 C, e8 C, f8 C, g8 C, h8 C,
a8 C, b8 C, c8 C, d8 C, e8 C, f8 C, g8 C, h8 C,
a8 C, b8 C, c8 C, d8 C, e8 C, f8 C, g8 C, h8 C,
a8 C, b8 C, c8 C, d8 C, e8 C, f8 C, g8 C, h8 C,
a8 C, b8 C, c8 C, d8 C, e8 C, f8 C, g8 C, h8 C,
a8 C, b8 C, c8 C, d8 C, e8 C, f8 C, g8 C, h8 C,
a8 C, b8 C, c8 C, d8 C, e8 C, f8 C, g8 C, h8 C,
a8 C, b8 C, c8 C, d8 C, e8 C, f8 C, g8 C, h8 C,

CREATE W_Edge
a8 C, a8 C, a8 C, a8 C, a8 C, a8 C, a8 C, a8 C,
a7 C, a7 C, a7 C, a7 C, a7 C, a7 C, a7 C, a7 C,
a6 C, a6 C, a6 C, a6 C, a6 C, a6 C, a6 C, a6 C,
a5 C, a5 C, a5 C, a5 C, a5 C, a5 C, a5 C, a5 C,
a4 C, a4 C, a4 C, a4 C, a4 C, a4 C, a4 C, a4 C,
a3 C, a3 C, a3 C, a3 C, a3 C, a3 C, a3 C, a3 C,
a2 C, a2 C, a2 C, a2 C, a2 C, a2 C, a2 C, a2 C,
a1 C, a1 C, a1 C, a1 C, a1 C, a1 C, a1 C, a1 C,

CREATE NW_Edge
a8 C, b8 C, c8 C, d8 C, e8 C, f8 C, g8 C, h8 C,
a7 C, a8 C, b8 C, c8 C, d8 C, e8 C, f8 C, g8 C,
a6 C, a7 C, a8 C, b8 C, c8 C, d8 C, e8 C, f8 C,
a5 C, a6 C, a7 C, a8 C, b8 C, c8 C, d8 C, e8 C,
a4 C, a5 C, a6 C, a7 C, a8 C, b8 C, c8 C, d8 C,
a3 C, a4 C, a5 C, a6 C, a7 C, a8 C, b8 C, c8 C,
a2 C, a3 C, a4 C, a5 C, a6 C, a7 C, a8 C, b8 C,
a1 C, a2 C, a3 C, a4 C, a5 C, a6 C, a7 C, a8 C,

CREATE NE_Edge
a8 C, b8 C, c8 C, d8 C, e8 C, f8 C, g8 C, h8 C,
b8 C, c8 C, d8 C, e8 C, f8 C, g8 C, h8 C, h7 C,
c8 C, d8 C, e8 C, f8 C, g8 C, h8 C, h7 C, h6 C,
d8 C, e8 C, f8 C, g8 C, h8 C, h7 C, h6 C, h5 C,
e8 C, f8 C, g8 C, h8 C, h7 C, h6 C, h5 C, h4 C,
f8 C, g8 C, h8 C, h7 C, h6 C, h5 C, h4 C, h3 C,
g8 C, h8 C, h7 C, h6 C, h5 C, h4 C, h3 C, h2 C,
h8 C, h7 C, h6 C, h5 C, h4 C, h3 C, h2 C, h1 C,

: NorthEdgePos ( pos - pos' )
	N_Edge + C@
;

: WestEdgePos ( pos - pos' )
	W_Edge + C@
;

: NorthwestEdgePos ( pos - pos' )
	NW_Edge + C@
;

: NortheastEdgePos ( pos - pos' )
	NE_Edge + C@
;
