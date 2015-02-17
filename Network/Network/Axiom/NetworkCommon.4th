\ TODO: Test mutual connection case.
\ Remove output from OnEdits...
\ Restrict edits?
\ Improve AI.
\ Try:
\ a) Favor centermost positions via a board weight (secondary consideration)
\ b) For pieces with no links in a direction, count empty spaces in that direction until piece/edge/opponentzone.
\ StackCheck.4th
\ 10x10 board
\ 12x12 board?
$shareMovesList OFF
\ $gameLog ON

8 CONSTANT #Dim
#Dim #Dim * CONSTANT #BoardSize
#Dim 2 - CONSTANT #Nodes
#Dim 3 - 2* ( 4 - ) CONSTANT #InitialPiecesInReserve \ Does this scale well?
#InitialPiecesInReserve #Nodes - CONSTANT #ConnectionReserve \ If this many or fewer pieces in reserve, then connection possible.

{board
	#Dim #Dim {grid}

	#BoardSize {carray} northLinks
	#BoardSize {carray} southLinks
	#BoardSize {carray} eastLinks
	#BoardSize {carray} westLinks
	#BoardSize {carray} northwestLinks
	#BoardSize {carray} northeastLinks
	#BoardSize {carray} southwestLinks
	#BoardSize {carray} southeastLinks

	{variable} passing

	{variable} lightWins
	{variable} darkWins

	{variable} lightLeafZonePieceCount
	{variable} darkLeafZonePieceCount

	2 {array} piecesInReserve
board}

1 $scanPositions !	\ Trick to gain control of scanning.

{directions
	-1  0 {direction} North
	 1  0 {direction} South
	 0  1 {direction} East
	 0 -1 {direction} West
	-1  1 {direction} Northeast
	 1  1 {direction} Southeast
	-1 -1 {direction} Northwest
	 1 -1 {direction} Southwest
directions}

{players
	{player}	Dark
	{player}	Light
	{neutral}	Marker
players}

BINARY
	0001 CONSTANT N_Bit
	0010 CONSTANT NE_Bit
	0100 CONSTANT E_Bit
	1000 CONSTANT NW_Bit
DECIMAL

{pieces
	{piece}		Checker
	{dummy-pieces}
	{piece}		N
	{piece}		NE
	{piece}		N_NE
	{piece}		E
	{piece}		N_E
	{piece}		NE_E
	{piece}		N_NE_E
	{piece}		NW
	{piece}		N_NW
	{piece}		NE_NW
	{piece}		N_NE_NW
	{piece}		E_NW
	{piece}		N_E_NW
	{piece}		NE_E_NW
	{piece}		N_NE_E_NW
pieces}

LOAD Repetition.4th
LOAD Edges.4th

CREATE edgeZone
1 C, 1 C, 1 C, 1 C, 1 C, 1 C, 1 C, 1 C,
1 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 1 C,
1 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 1 C,
1 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 1 C,
1 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 1 C,
1 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 1 C,
1 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 1 C,
1 C, 1 C, 1 C, 1 C, 1 C, 1 C, 1 C, 1 C,

: InEdgeZone? ( pos -- ? )
	edgeZone + C@
;

CREATE lightLeafZone
0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C,
0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 1 C,
0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 1 C,
0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 1 C,
0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 1 C,
0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 1 C,
0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 1 C,
0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C,

CREATE darkLeafZone
0 C, 1 C, 1 C, 1 C, 1 C, 1 C, 1 C, 0 C,
0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C,
0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C,
0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C,
0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C,
0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C,
0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C,
0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C,

: InLightLeafZone? ( pos -- ? )
	lightLeafZone + C@
;

: InDarkLeafZone? ( pos -- ? )
	darkLeafZone + C@
;

: LightRootPositions ( -- pos1...posn )
	a7 a6 a5 a4 a3 a2
;

VARIABLE pos1

: HorizontalScan ( pos -- )
	here SWAP
	0 pos1 !
	( pos ) WestEdgePos ( pos' ) to

	BEGIN
		not-empty?
		IF
			\ reset existing links
			0 here westLinks C!
			0 here eastLinks C!

			pos1 @ 0=
			IF \ found first piece
				here pos1 ! \ save it
			ELSE \ found additional piece
				player pos1 @ player-at = \ friends at both ends?
				IF \ update west & east
					pos1 @ here westLinks C! \ this position points to previous
					here pos1 @ eastLinks C! \ previous position points to this one
				ENDIF
				here pos1 ! \ save it
			ENDIF
		ENDIF
		East NOT	\ continue scanning
	UNTIL

	to
;

: HorizontalScan ( pos -- )
	DUP InEdgeZone? current-player Dark = AND
	IF
		DROP
	ELSE
		HorizontalScan
	ENDIF
;

: VerticalScan ( pos -- )
	here SWAP
	0 pos1 !
	( pos ) NorthEdgePos ( pos' ) to

	BEGIN
		not-empty?
		IF
			\ reset existing links
			0 here northLinks C!
			0 here southLinks C!

			pos1 @ 0=
			IF \ found first piece
				here pos1 ! \ save it
			ELSE \ found additional piece
				player pos1 @ player-at = \ friends at both ends?
				IF \ update north & south
					pos1 @ here northLinks C! \ this position points to previous
					here pos1 @ southLinks C! \ previous position points to this one
				ENDIF
				here pos1 ! \ save it
			ENDIF
		ENDIF
		South NOT	\ continue scanning
	UNTIL

	to
;

: VerticalScan ( pos -- )
	DUP InEdgeZone? current-player Light = AND
	IF
		DROP
	ELSE
		VerticalScan
	ENDIF
;

: \DiagonalScan ( pos -- )
	here SWAP
	0 pos1 !
	( pos ) NorthwestEdgePos ( pos' ) to

	BEGIN
		not-empty?
		IF
			\ reset existing links
			0 here northwestLinks C!
			0 here southeastLinks C!

			pos1 @ 0=
			IF \ found first piece
				here pos1 ! \ save it
			ELSE \ found additional piece
				player pos1 @ player-at = \ friends at both ends?
				IF \ update northwest & southeast
					pos1 @ here northwestLinks C! \ this position points to previous
					here pos1 @ southeastLinks C! \ previous position points to this one
				ENDIF
				here pos1 ! \ save it
			ENDIF
		ENDIF
		Southeast NOT	\ continue scanning
	UNTIL

	to
;

: /DiagonalScan ( pos -- )
	here SWAP
	0 pos1 !
	( pos ) NortheastEdgePos ( pos' ) to

	BEGIN
		not-empty?
		IF
			\ reset existing links
			0 here northeastLinks C!
			0 here southwestLinks C!

			pos1 @ 0=
			IF \ found first piece
				here pos1 ! \ save it
			ELSE \ found additional piece
				player pos1 @ player-at = \ friends at both ends?
				IF \ update northeast & southwest
					pos1 @ here northeastLinks C! \ this position points to previous
					here pos1 @ southwestLinks C! \ previous position points to this one
				ENDIF
				here pos1 ! \ save it
			ENDIF
		ENDIF
		Southwest NOT	\ continue scanning
	UNTIL

	to
;

: Rescan ( pos -- )
	DUP HorizontalScan
	DUP VerticalScan
	DUP \DiagonalScan
	( pos ) /DiagonalScan
;

: AddPiece ( pos -- )
	DUP current-player Dark =
	IF
		InDarkLeafZone? IF darkLeafZonePieceCount ++ ENDIF
	ELSE
		InLightLeafZone? IF lightLeafZonePieceCount ++ ENDIF
	ENDIF

	( pos ) Rescan
;

: RemovePiece ( pos -- )
	DUP current-player Dark =
	IF
		InDarkLeafZone? IF darkLeafZonePieceCount -- ENDIF
	ELSE
		InLightLeafZone? IF lightLeafZonePieceCount -- ENDIF
	ENDIF

	\ remove links for piece to be removed
	\   (the scan will clear out and recalc the links of the existing pieces)
	0 OVER northLinks C!
	0 OVER southLinks C!
	0 OVER eastLinks C!
	0 OVER westLinks C!
	0 OVER northeastLinks C!
	0 OVER northwestLinks C!
	0 OVER southeastLinks C!
	0 OVER southwestLinks C!
	( pos ) Rescan
;

: ProhibitTrio ( 'dir -- )
	here SWAP
	EXECUTE
	IF
		friend?
		IF
			to R> DROP EXIT
		ENDIF
	ENDIF
	to
;

LOAD 8x8Neighbors.4th

VARIABLE neighborCount

VARIABLE pos

: LegalPlacement? ( -- ? )
	here pos !
	FALSE

	\ First check for unconnected neighbors
	0 neighborCount !
	here Neighbors 0
	DO
		friend-at? IF neighborCount ++ ENDIF
	LOOP
	neighborCount @ 2 >= IF EXIT ENDIF

	\ Check for connected neighbors in all directions

	North
	IF
		friend?
		IF
			['] Southwest	ProhibitTrio
			['] West	ProhibitTrio
			['] Northwest	ProhibitTrio
			['] North	ProhibitTrio
			['] Northeast	ProhibitTrio
			['] East	ProhibitTrio
			['] Southeast	ProhibitTrio
		ENDIF
		pos @ to
	ENDIF

	Northeast
	IF
		friend?
		IF
			['] West	ProhibitTrio
			['] Northwest	ProhibitTrio
			['] North	ProhibitTrio
			['] Northeast	ProhibitTrio
			['] East	ProhibitTrio
			['] Southeast	ProhibitTrio
			['] South	ProhibitTrio
		ENDIF
		pos @ to
	ENDIF

	East
	IF
		friend?
		IF
			['] Northwest	ProhibitTrio
			['] North	ProhibitTrio
			['] Northeast	ProhibitTrio
			['] East	ProhibitTrio
			['] Southeast	ProhibitTrio
			['] South	ProhibitTrio
			['] Southwest	ProhibitTrio
		ENDIF
		pos @ to
	ENDIF

	Southeast
	IF
		friend?
		IF
			['] West	ProhibitTrio
			['] North	ProhibitTrio
			['] Northeast	ProhibitTrio
			['] East	ProhibitTrio
			['] Southeast	ProhibitTrio
			['] South	ProhibitTrio
			['] Southwest	ProhibitTrio
		ENDIF
		pos @ to
	ENDIF

	South
	IF
		friend?
		IF
			['] Northwest	ProhibitTrio
			['] Northeast	ProhibitTrio
			['] East	ProhibitTrio
			['] Southeast	ProhibitTrio
			['] South	ProhibitTrio
			['] Southwest	ProhibitTrio
			['] West	ProhibitTrio
		ENDIF
		pos @ to
	ENDIF

	Southwest
	IF
		friend?
		IF
			['] Northwest	ProhibitTrio
			['] North	ProhibitTrio
			['] East	ProhibitTrio
			['] Southeast	ProhibitTrio
			['] South	ProhibitTrio
			['] Southwest	ProhibitTrio
			['] West	ProhibitTrio
		ENDIF
		pos @ to
	ENDIF

	West
	IF
		friend?
		IF
			['] Northwest	ProhibitTrio
			['] North	ProhibitTrio
			['] Northeast	ProhibitTrio
			['] Southeast	ProhibitTrio
			['] South	ProhibitTrio
			['] Southwest	ProhibitTrio
			['] West	ProhibitTrio
		ENDIF
		pos @ to
	ENDIF

	Northwest
	IF
		friend?
		IF
			['] West	ProhibitTrio
			['] Northwest	ProhibitTrio
			['] North	ProhibitTrio
			['] Northeast	ProhibitTrio
			['] East	ProhibitTrio
			['] South	ProhibitTrio
			['] Southwest	ProhibitTrio
		ENDIF
		pos @ to
	ENDIF

	DROP TRUE
;

: DarkRootPositions ( -- pos1...posn )
	b1 c1 d1 e1 f1 g1
;

LOAD Marks.4th

CREATE searchPath 32 ALLOT
CREATE winPath 32 ALLOT
VARIABLE pathLength

VARIABLE nodeCount
VARIABLE found

: SavePath
	searchPath winPath nodeCount @ CMOVE
	nodeCount @ pathLength !
;

: PosName. ( pos -- )
	CELLS $posNames @ + @ TYPE SPACE
;

: DumpPath
	CR
	." pathLength = " pathLength ? CR
	pathLength @ 0
	DO
		winPath I + C@ CELLS $posNames @ + @ TYPE SPACE
	LOOP
	CR CR
;

: DumpBoard
	CR
	#BoardSize 0
	DO
		I piece-at 5 .R
		I 1+ #Dim MOD 0= IF CR ENDIF
	LOOP
	CR CR
;

: MarkAll ( pos1...posn -- )
	[ #Dim 2- ] LITERAL 0
	DO
		Mark
	LOOP
;

DEFER _ScanLightFrom_N_S
DEFER _ScanLightFrom_NE_SW
DEFER _ScanLightFrom_NW_SE
DEFER _ScanLightFrom_E_W

: ScanLightFrom_N_S ( pos -- )
	DUP Unmarked?
	IF
		DUP searchPath nodeCount @ + C!
		nodeCount ++
		DUP Mark
		DUP InLightLeafZone?
		IF
			nodeCount @ #Nodes >=
			IF
				found ON
				SavePath
			ENDIF
		ELSE
			DUP northeastLinks C@ DUP	IF _ScanLightFrom_NE_SW ELSE DROP ENDIF
			DUP northwestLinks C@ DUP	IF _ScanLightFrom_NW_SE ELSE DROP ENDIF
			DUP eastLinks C@ DUP		IF _ScanLightFrom_E_W ELSE DROP ENDIF
			DUP westLinks C@ DUP		IF _ScanLightFrom_E_W ELSE DROP ENDIF
			DUP southeastLinks C@ DUP	IF _ScanLightFrom_NW_SE ELSE DROP ENDIF
			DUP southwestLinks C@ DUP	IF _ScanLightFrom_NE_SW ELSE DROP ENDIF
		ENDIF
		nodeCount --
		DUP Unmark
	ENDIF
	DROP
;

: ScanLightFrom_NE_SW ( pos -- )
	DUP Unmarked?
	IF
		DUP searchPath nodeCount @ + C!
		nodeCount ++
		DUP Mark
		DUP InLightLeafZone?
		IF
			nodeCount @ #Nodes >=
			IF
				found ON
				SavePath
			ENDIF
		ELSE
			DUP northLinks C@ DUP		IF _ScanLightFrom_N_S ELSE DROP ENDIF
			DUP northwestLinks C@ DUP	IF _ScanLightFrom_NW_SE ELSE DROP ENDIF
			DUP eastLinks C@ DUP		IF _ScanLightFrom_E_W ELSE DROP ENDIF
			DUP westLinks C@ DUP		IF _ScanLightFrom_E_W ELSE DROP ENDIF
			DUP southeastLinks C@ DUP	IF _ScanLightFrom_NW_SE ELSE DROP ENDIF
			DUP southLinks C@ DUP		IF _ScanLightFrom_N_S ELSE DROP ENDIF
		ENDIF
		nodeCount --
		DUP Unmark
	ENDIF
	DROP
;

: ScanLightFrom_NW_SE ( pos -- )
	DUP Unmarked?
	IF
		DUP searchPath nodeCount @ + C!
		nodeCount ++
		DUP Mark
		DUP InLightLeafZone?
		IF
			nodeCount @ #Nodes >=
			IF
				found ON
				SavePath
			ENDIF
		ELSE
			DUP northLinks C@ DUP		IF _ScanLightFrom_N_S ELSE DROP ENDIF
			DUP northeastLinks C@ DUP	IF _ScanLightFrom_NE_SW ELSE DROP ENDIF
			DUP eastLinks C@ DUP		IF _ScanLightFrom_E_W ELSE DROP ENDIF
			DUP westLinks C@ DUP		IF _ScanLightFrom_E_W ELSE DROP ENDIF
			DUP southwestLinks C@ DUP	IF _ScanLightFrom_NE_SW ELSE DROP ENDIF
			DUP southLinks C@ DUP		IF _ScanLightFrom_N_S ELSE DROP ENDIF
		ENDIF
		nodeCount --
		DUP Unmark
	ENDIF
	DROP \ pos
;

: ScanLightFrom_E_W ( pos -- )
	DUP Unmarked?
	IF
		DUP searchPath nodeCount @ + C!
		nodeCount ++
		DUP Mark
		DUP InLightLeafZone?
		IF
			nodeCount @ #Nodes >=
			IF
				found ON
				SavePath
			ENDIF
		ELSE
			DUP northLinks C@ DUP		IF _ScanLightFrom_N_S ELSE DROP ENDIF
			DUP northeastLinks C@ DUP	IF _ScanLightFrom_NE_SW ELSE DROP ENDIF
			DUP northwestLinks C@ DUP	IF _ScanLightFrom_NW_SE ELSE DROP ENDIF
			DUP southeastLinks C@ DUP	IF _ScanLightFrom_NW_SE ELSE DROP ENDIF
			DUP southwestLinks C@ DUP	IF _ScanLightFrom_NE_SW ELSE DROP ENDIF
			DUP southLinks C@ DUP		IF _ScanLightFrom_N_S ELSE DROP ENDIF
		ENDIF
		nodeCount --
		DUP Unmark
	ENDIF
	DROP \ pos
;

' ScanLightFrom_N_S IS _ScanLightFrom_N_S
' ScanLightFrom_NE_SW IS _ScanLightFrom_NE_SW
' ScanLightFrom_NW_SE IS _ScanLightFrom_NW_SE
' ScanLightFrom_E_W IS _ScanLightFrom_E_W

: LightConnection? ( -- ? )
	found OFF

	lightLeafZonePieceCount @ 0>
	[ Light player-index ] LITERAL piecesInReserve @ #ConnectionReserve <=
	AND
	IF
		UnmarkAll
		0 nodeCount !

		LightRootPositions MarkAll

		LightRootPositions
		[ #Dim 2- ] LITERAL 0
		DO
			DUP not-empty-at?
			IF
				DUP searchPath C!
				nodeCount ++

				DUP eastLinks C@ DUP		IF _ScanLightFrom_E_W ELSE DROP ENDIF
				DUP northeastLinks C@ DUP	IF _ScanLightFrom_NE_SW ELSE DROP ENDIF
				DUP southeastLinks C@ DUP	IF _ScanLightFrom_NW_SE ELSE DROP ENDIF
				nodeCount --
			ENDIF
			DROP \ pos
		LOOP
	ENDIF
	found @
;

DEFER _ScanDarkFrom_N_S
DEFER _ScanDarkFrom_NE_SW
DEFER _ScanDarkFrom_NW_SE
DEFER _ScanDarkFrom_E_W

: ScanDarkFrom_N_S ( pos -- )
	DUP Unmarked?
	IF
		DUP searchPath nodeCount @ + C!
		nodeCount ++
		DUP Mark
		DUP InDarkLeafZone?
		IF
			nodeCount @ #Nodes >=
			IF
				found ON
				SavePath
			ENDIF
		ELSE
			DUP northeastLinks C@ DUP	IF _ScanDarkFrom_NE_SW ELSE DROP ENDIF
			DUP northwestLinks C@ DUP	IF _ScanDarkFrom_NW_SE ELSE DROP ENDIF
			DUP eastLinks C@ DUP		IF _ScanDarkFrom_E_W ELSE DROP ENDIF
			DUP westLinks C@ DUP		IF _ScanDarkFrom_E_W ELSE DROP ENDIF
			DUP southeastLinks C@ DUP	IF _ScanDarkFrom_NW_SE ELSE DROP ENDIF
			DUP southwestLinks C@ DUP	IF _ScanDarkFrom_NE_SW ELSE DROP ENDIF
		ENDIF
		nodeCount --
		DUP Unmark
	ENDIF
	DROP
;

: ScanDarkFrom_NE_SW ( pos -- )
	DUP Unmarked?
	IF
		DUP searchPath nodeCount @ + C!
		nodeCount ++
		DUP Mark
		DUP InDarkLeafZone?
		IF
			nodeCount @ #Nodes >=
			IF
				found ON
				SavePath
			ENDIF
		ELSE
			DUP northLinks C@ DUP		IF _ScanDarkFrom_N_S ELSE DROP ENDIF
			DUP northwestLinks C@ DUP	IF _ScanDarkFrom_NW_SE ELSE DROP ENDIF
			DUP eastLinks C@ DUP		IF _ScanDarkFrom_E_W ELSE DROP ENDIF
			DUP westLinks C@ DUP		IF _ScanDarkFrom_E_W ELSE DROP ENDIF
			DUP southeastLinks C@ DUP	IF _ScanDarkFrom_NW_SE ELSE DROP ENDIF
			DUP southLinks C@ DUP		IF _ScanDarkFrom_N_S ELSE DROP ENDIF
		ENDIF
		nodeCount --
		DUP Unmark
	ENDIF
	DROP
;

: ScanDarkFrom_NW_SE ( pos -- )
	DUP Unmarked?
	IF
		DUP searchPath nodeCount @ + C!
		nodeCount ++
		DUP Mark
		DUP InDarkLeafZone?
		IF
			nodeCount @ #Nodes >=
			IF
				found ON
				SavePath
			ENDIF
		ELSE
			DUP northLinks C@ DUP		IF _ScanDarkFrom_N_S ELSE DROP ENDIF
			DUP northeastLinks C@ DUP	IF _ScanDarkFrom_NE_SW ELSE DROP ENDIF
			DUP eastLinks C@ DUP		IF _ScanDarkFrom_E_W ELSE DROP ENDIF
			DUP westLinks C@ DUP		IF _ScanDarkFrom_E_W ELSE DROP ENDIF
			DUP southwestLinks C@ DUP	IF _ScanDarkFrom_NE_SW ELSE DROP ENDIF
			DUP southLinks C@ DUP		IF _ScanDarkFrom_N_S ELSE DROP ENDIF
		ENDIF
		nodeCount --
		DUP Unmark
	ENDIF
	DROP \ pos
;

: ScanDarkFrom_E_W ( pos -- )
	DUP Unmarked?
	IF
		DUP searchPath nodeCount @ + C!
		nodeCount ++
		DUP Mark
		DUP InDarkLeafZone?
		IF
			nodeCount @ #Nodes >=
			IF
				found ON
				SavePath
			ENDIF
		ELSE
			DUP northLinks C@ DUP		IF _ScanDarkFrom_N_S ELSE DROP ENDIF
			DUP northeastLinks C@ DUP	IF _ScanDarkFrom_NE_SW ELSE DROP ENDIF
			DUP northwestLinks C@ DUP	IF _ScanDarkFrom_NW_SE ELSE DROP ENDIF
			DUP southeastLinks C@ DUP	IF _ScanDarkFrom_NW_SE ELSE DROP ENDIF
			DUP southwestLinks C@ DUP	IF _ScanDarkFrom_NE_SW ELSE DROP ENDIF
			DUP southLinks C@ DUP		IF _ScanDarkFrom_N_S ELSE DROP ENDIF
		ENDIF
		nodeCount --
		DUP Unmark
	ENDIF
	DROP \ pos
;

' ScanDarkFrom_N_S IS _ScanDarkFrom_N_S
' ScanDarkFrom_NE_SW IS _ScanDarkFrom_NE_SW
' ScanDarkFrom_NW_SE IS _ScanDarkFrom_NW_SE
' ScanDarkFrom_E_W IS _ScanDarkFrom_E_W

: DarkConnection? ( -- ? )
	found OFF

	darkLeafZonePieceCount @ 0>
	[ Dark player-index ] LITERAL piecesInReserve @ #ConnectionReserve <=
	AND
	IF
		UnmarkAll
		0 nodeCount !

		DarkRootPositions MarkAll

		DarkRootPositions
		[ #Dim 2- ] LITERAL 0
		DO
			DUP not-empty-at?
			IF
				DUP searchPath C!
				nodeCount ++

				DUP northLinks C@ DUP		IF _ScanDarkFrom_N_S ELSE DROP ENDIF
				DUP northwestLinks C@ DUP	IF _ScanDarkFrom_NW_SE ELSE DROP ENDIF
				DUP northeastLinks C@ DUP	IF _ScanDarkFrom_NE_SW ELSE DROP ENDIF
				nodeCount --
			ENDIF
			DROP \ pos
		LOOP
	ENDIF
	found @
;

CREATE legalLightSquares
0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C,
1 C, 1 C, 1 C, 1 C, 1 C, 1 C, 1 C, 1 C,
1 C, 1 C, 1 C, 1 C, 1 C, 1 C, 1 C, 1 C,
1 C, 1 C, 1 C, 1 C, 1 C, 1 C, 1 C, 1 C,
1 C, 1 C, 1 C, 1 C, 1 C, 1 C, 1 C, 1 C,
1 C, 1 C, 1 C, 1 C, 1 C, 1 C, 1 C, 1 C,
1 C, 1 C, 1 C, 1 C, 1 C, 1 C, 1 C, 1 C,
0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C, 0 C,

CREATE legalDarkSquares
0 C, 1 C, 1 C, 1 C, 1 C, 1 C, 1 C, 0 C,
0 C, 1 C, 1 C, 1 C, 1 C, 1 C, 1 C, 0 C,
0 C, 1 C, 1 C, 1 C, 1 C, 1 C, 1 C, 0 C,
0 C, 1 C, 1 C, 1 C, 1 C, 1 C, 1 C, 0 C,
0 C, 1 C, 1 C, 1 C, 1 C, 1 C, 1 C, 0 C,
0 C, 1 C, 1 C, 1 C, 1 C, 1 C, 1 C, 0 C,
0 C, 1 C, 1 C, 1 C, 1 C, 1 C, 1 C, 0 C,
0 C, 1 C, 1 C, 1 C, 1 C, 1 C, 1 C, 0 C,

: LegalSquare? ( -- ? )
	current-player Dark =
	IF
		here #Dim MOD \ column
	ELSE \ Light
		here #Dim / \ row
	ENDIF
	DUP 0= SWAP [ #Dim 1- ] LITERAL  = OR NOT
;

: LegalSquare? ( -- ? )
	here
	current-player Dark =
	IF
		legalDarkSquares
	ELSE \ Light
		legalLightSquares
	ENDIF
	+ C@
;

: add-piece ( pos -- )
	( pos ) COMPILE-LITERAL
	COMPILE AddPiece
;

: remove-piece ( pos -- )
	( pos ) COMPILE-LITERAL
	COMPILE RemovePiece
;

CREATE trailMap #BoardSize ALLOT

: GetDirection ( toPos fromPos -- dir dirMask )
	2DUP
	#Dim MOD	\ fromCol
	SWAP #Dim MOD	\ toCol
	-		\ toPos fromPos colDelta
	>R
	#Dim /		\ fromRow
	SWAP #Dim /	\ toRow
	-		\ rowDelta
	R>		\ colDelta
	DUP 0=
	IF
		DROP 0>
		IF ['] North ELSE ['] South ENDIF N_Bit
	ELSE
		OVER 0=
		IF
			SWAP DROP 0>
			IF ['] West ELSE ['] East ENDIF E_Bit
		ELSE
			2DUP 0> SWAP 0< AND
			IF \ - + SW
				2DROP ['] Southwest NE_Bit
			ELSE
				2DUP 0< SWAP 0> AND
				IF
					2DROP ['] Northeast NE_Bit
				ELSE
					0< SWAP 0< AND
					IF \ - - SE
						['] Southeast
					ELSE
						['] Northwest
					ENDIF
					NW_Bit
				ENDIF
			ENDIF
		ENDIF
	ENDIF
;

VARIABLE dirMask

: MakePath ( toPos fromPos -- )
	DUP to
	GetDirection dirMask !
	BEGIN
		DUP EXECUTE empty? AND
	WHILE
		here trailMap + DUP C@ dirMask @ OR SWAP C! \ update mask
	REPEAT
	DROP	\ 'dir
;

: MakeTrail ( -- )
	here
	trailMap #BoardSize ERASE
	pathLength @ 1- 0
	DO
		winPath I + 1+ C@ ( toPos ) winPath I + C@ ( fromPos ) MakePath
	LOOP
	to
;

: GenerateTrail
	#BoardSize 0
	DO
		trailMap I + C@ DUP 0>
		IF
			1+ Marker SWAP I create-player-piece-type-at
		ELSE
			DROP
		ENDIF
	LOOP
;

: LightWins
	lightWins ON
;

: DarkWins
	darkWins ON
;

: light-wins
	COMPILE LightWins
;

: dark-wins
	COMPILE DarkWins
;

: DecrementReservePieceCount ( playerIndex -- )
	piecesInReserve --
;

: decrement-reserve-piece-count
	current-player player-index COMPILE-LITERAL
	COMPILE DecrementReservePieceCount
;

LOAD CopyBoard.4th

Light Checker OR CONSTANT #LightChecker
Dark Checker OR CONSTANT #DarkChecker

VARIABLE moveCount
VARIABLE win

: CheckerDrops
	#BoardSize 0
	DO
		I empty-at?
		IF
			I home I to
			LegalSquare?
			IF
				LegalPlacement?
				IF
					win OFF
					\ Determine if move results in a win.
					save-board
					current-player Light =
					IF
						#LightChecker here board[] !
						here AddPiece
						LightConnection?
						IF
							light-wins
							MakeTrail win ON
						ENDIF
					ELSE
						#DarkChecker here board[] !
						here AddPiece
						DarkConnection?
						IF
							dark-wins
							MakeTrail win ON
						ENDIF
					ENDIF
					restore-board

					drop
					win @ IF GenerateTrail ENDIF
					here add-piece
					decrement-reserve-piece-count
					add-move
					moveCount ++
				ENDIF
			ENDIF
		ENDIF
	LOOP
;

: CheckerMove ( pos -- )
	to empty?
	IF
		LegalSquare?
		IF
			win OFF
			TRUE \ Assume legal move.
			\ Determine if move results in a win.
			save-board
			\ tentative move
			#Empty from board[] !
			current-player Light = IF #LightChecker ELSE #DarkChecker ENDIF
			here board[] !
			#Empty from board[] !
			LegalPlacement?
			IF
				\ tentative link update
				from RemovePiece
				here AddPiece
				DarkConnection?
				LightConnection?
				2DUP AND \ both win?
				IF
					2DROP \ darkConn? lightConn?
					DROP FALSE \ illegal move
				ELSE
					IF \ lightConn?
						light-wins
						MakeTrail win ON
					ENDIF
					IF \ darkConn?
						dark-wins
						MakeTrail win ON
					ENDIF
				ENDIF
			ELSE
				DROP FALSE \ illegal move
			ENDIF
			restore-board
			\ legalmove?
			IF
				\ TODO: Must remove-piece come before the move?
				from here move
				win @ IF GenerateTrail ENDIF
				from remove-piece
				here add-piece
				add-move
				moveCount ++
			ENDIF
		ENDIF
	ENDIF
	back
;

: CheckerMoves
	#BoardSize 0
	DO
		I friend-at?
		IF
			I home
			#BoardSize 0
			DO
				I CheckerMove
			LOOP
		ENDIF
	LOOP
;

: AllMoves
	current-player player-index piecesInReserve @ 0>
	IF
		CheckerDrops
	ELSE
		CheckerMoves
	ENDIF
;

: OnGenerateMoves
	0 moveCount !
	OnGenerateMoves
	moveCount @ 0= passing !
;

{moves CheckerDrops
	{move}		AllMoves
moves}

{pieces
	{piece}		Checker		{drops} CheckerDrops
	{dummy-pieces}
	{piece}		N
	{piece}		NE
	{piece}		N_NE
	{piece}		E
	{piece}		N_E
	{piece}		NE_E
	{piece}		N_NE_E
	{piece}		NW
	{piece}		N_NW
	{piece}		NE_NW
	{piece}		N_NE_NW
	{piece}		E_NW
	{piece}		N_E_NW
	{piece}		NE_E_NW
	{piece}		N_NE_E_NW
pieces}

: OnEvaluate ( -- score )
	0
	#BoardSize 0
	DO
		I not-empty-at?
		IF
			I friend-at?
			IF
				I northLinks C@ IF 1+ ENDIF
				I southLinks C@ IF 1+ ENDIF
				I eastLinks C@ IF 1+ ENDIF
				I westLinks C@ IF 1+ ENDIF
				I southeastLinks C@ IF 1+ ENDIF
				I northwestLinks C@ IF 1+ ENDIF
				I southwestLinks C@ IF 1+ ENDIF
				I northeastLinks C@ IF 1+ ENDIF
			ELSE
				I northLinks C@ IF 1- ENDIF
				I southLinks C@ IF 1- ENDIF
				I eastLinks C@ IF 1- ENDIF
				I westLinks C@ IF 1- ENDIF
				I southeastLinks C@ IF 1- ENDIF
				I northwestLinks C@ IF 1- ENDIF
				I southwestLinks C@ IF 1- ENDIF
				I northeastLinks C@ IF 1- ENDIF
			ENDIF
		ENDIF
	LOOP
	10 *	\ Scale for the emoticon
;

: OnIsGameOver ( -- gameResult )
	#UnknownScore

	repetition?
	IF
		DROP #DrawScore
		^" 3-Time Repetition"
	ELSE
		darkWins @
		IF
			DROP current-player Dark = IF #WinScore ELSE #LossScore ENDIF
		ENDIF

		lightWins @
		IF
			DROP current-player Light = IF #WinScore ELSE #LossScore ENDIF
		ENDIF
	ENDIF
	passing @ IF DROP #DrawScore ENDIF
;

: DumpNodes ( pos -- )
	." NorthLink = " DUP northLinks C@ . CR
	." SouthLink = " DUP southLinks C@ . CR
	." EastLink = " DUP eastLinks C@ . CR
	." WestLink = " DUP westLinks C@ . CR
	." SoutheastLink = " DUP southeastLinks C@ . CR
	." NorthwestLink = " DUP northwestLinks C@ . CR
	." SouthwestLink = " DUP southwestLinks C@ . CR
	." NortheastLink = " DUP northeastLinks C@ . CR
	CR CR
;

: OnNewGame
	OnNewGame
	#InitialPiecesInReserve [ Dark player-index ] LITERAL piecesInReserve !
	#InitialPiecesInReserve [ Light player-index ] LITERAL piecesInReserve !
;

: OnEditAdd ( player pieceType pos -- )
	DUP not-empty-at?
	IF
		DUP player-at player-index piecesInReserve ++
	ENDIF
	2 PICK player-index piecesInReserve --

	DUP >R
	OnEditAdd

	R> ( pos ) AddPiece
;

: OnEditRemove ( pos -- )
	DUP player-at player-index piecesInReserve ++

	DUP Rescan
	DUP OnEditRemove
	( pos ) RemovePiece
;
