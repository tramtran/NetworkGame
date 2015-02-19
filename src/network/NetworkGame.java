/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.awt.Color;
import java.awt.Font;

public class NetworkGame extends javax.swing.JFrame {
	private static Board mainBoard;
	public final static int WHITE = -2;
	public final static int BLACK = 2;
	public final static int NONE = 0;
	public final static int HORIZONTAL = 1;
	public final static int VERTICAL = 2;
	public final static int BOTTOMUP = 3;
	public final static int TOPDOWN = 4;
	public final static int HUMAN = -1;
	public final static int MACHINE = 1;
        private boolean isPlayerTurned=true; 
        private MachinePlayer machinePlayer;
	public void printBoard() {
		mainBoard.printBoard();
	}

public class chooseMoveThread implements Runnable{

        @Override
        public void run() {
        int w=mainBoard.Winner();
        if(w!=NONE){
               winner.setFont(new Font(Font.SERIF,Font.BOLD,50));
           if(w==HUMAN){
               winner.setText("PLAYER WINS");
           } else{
               winner.setText("COMPUTER WINS");
           }
        }else{
            int move=machinePlayer.chooseMove();
            mainBoard.addNode(move%8, move/8, MACHINE);
            System.out.printf("x: %d,y %d",move/8,move%8);
            setMove(move,"/pics/black.gif");
        }
        w=mainBoard.Winner();
        if(w!=NONE){
               winner.setFont(new Font(Font.SERIF,Font.BOLD,50));
           if(w==HUMAN){
               winner.setText("PLAPYER WINS");
           } else{
               winner.setText("COMPUTER WINS");
           }
        }
        isPlayerTurned=true;
        }
    
}
	public void addNode(int x, int y, int horm) {
		mainBoard.addNode(x, y, horm);
	}

	public void Winner() {
		int n = mainBoard.Winner();
	}
     
	public static class Board {
		private Node[] board;
		public final static int goalFirst = 1;
		public final static int goalSecond = 2;
		private int mCur = -1;
		private int sizeOfH = -1;
		private int hCur = -1;
		private int sizeOfM = -1;
		private int[] hList;
		private int[] hSortedList;
		private int[] mSortedList;
		private int[] mList;
		private int[] inNet;
		private boolean[] isCheck;
                private int newNode;
                private int[] isChoosen;
		public Board() {// OK
			inNet = new int[64];
			int i = -1;
			while (++i < 64)
				inNet[i] = i;
			isCheck = new boolean[64];
			board = new Node[64];
			int n = 0;
			hList = new int[10];
			hSortedList = new int[10];
			mSortedList = new int[10];
			mList = new int[10];
                        isChoosen=new int[2];
			while (n++ < 63) {
				board[n] = null;
			}
		}

		
		public int[] getConnectionAt(int x, int y) {
			return nodeAt(x, y).connection();
		}
		public void connectTest(int x, int y) {
			System.out.println("-------------");
			int i = -1;
			while (++i < nodeAt(x, y).connectedNode.length)
				System.out.println("x: " + nodeAt(x, y).connectedNode[i] % 8
						+ " y: " + nodeAt(x, y).connectedNode[i] / 8);
		}

		public int direct(int i) {
			switch (i) {
			case 0:
				return NetworkGame.HORIZONTAL;
			case 1:
				return NetworkGame.HORIZONTAL;
			case 2:
				return NetworkGame.VERTICAL;
			case 3:
				return NetworkGame.VERTICAL;
			case 4:
				return NetworkGame.TOPDOWN;
			case 5:
				return NetworkGame.TOPDOWN;
			case 6:
				return NetworkGame.BOTTOMUP;
			case 7:
				return NetworkGame.BOTTOMUP;
			default:
				return NetworkGame.NONE;
			}
		}

		public int nextNodeCo(int HorM) {
			if (HorM == HUMAN) {
				// System.out.print("hCru "+hCur);
				// for(int i:hList) System.out.println("hco "+i);
				if (++hCur > sizeOfH) {
					resetH();
					return NONE;
				}
				return hSortedList[hCur];
			}
			if (HorM == MACHINE) {
				// System.out.printf("mCru %d, sizeofM %d",hCur,sizeOfM);
				if (++mCur > sizeOfM) {
					resetM();
					return NONE;
				}
				return mSortedList[mCur];
			}
			return NONE;
		}

		public int[] nodesOnTheBoard() {
			int[] arrayOfNode = new int[sizeOfH() + sizeOfM()];
			for (int i = 0; i <= sizeOfH; i++) {
				arrayOfNode[i] = hSortedList[i];
				if (sizeOfH > sizeOfM && i == sizeOfH)
					break;
				arrayOfNode[i + sizeOfH] = mSortedList[i];
				if (sizeOfM > sizeOfH && i == sizeOfH)
					arrayOfNode[i + sizeOfH + 1] = mSortedList[i + 1];
			}

			return arrayOfNode;
		}

		private void resetH() {
			hCur = -1;
		}

		private void resetM() {
			mCur = -1;
		}

		public int sizeOfH() {
			return sizeOfH + 1;
		}

		public int sizeOfM() {
			return sizeOfM + 1;
		}

		private void addHNode(int co) {
			hList[++sizeOfH] = co;
			hSortedList[sizeOfH] = co;
                        newNode=co;
			// System.out.printf("sizeOfH %d", sizeOfH);
			java.util.Arrays.sort(hSortedList, 0, sizeOfH + 1);
			// for(int i:hSortedList) System.out.println("i "+i);
			// System.out.printf("sizeOfH in addHNode %d,co %d" ,sizeOfH,co);
			// System.out.printf("x %d y %d",co%8,co/8);
		}
		private void addMNode(int co) {
			mList[++sizeOfM] = co;// coordinate of this node if x is vertical
					// axis, and y is horizontal axis
			mSortedList[sizeOfM] = co;
			for (int i = 0; i <= sizeOfM; i++)
				mSortedList[i] = ((mSortedList[i] / 8) + (mSortedList[i] % 8) * 8);
			java.util.Arrays.sort(mSortedList, 0, sizeOfM + 1);
			// for(int i:mSortedList) System.out.println("i2 "+i);
			for (int i = 0; i <= sizeOfM; i++)
				mSortedList[i] = (mSortedList[i] / 8) + (mSortedList[i] % 8)* 8;
		
		}

		private Node nodeAt(int x, int y) {
			int co = y * 8 + x;
			return this.board[co];
		}
                public int kindOfNode(int x,int y){
                    if(nodeAt(x,y)!=null)
                    return nodeAt(x,y).hOrM;
                    else return NONE;
                }
		
		public void copy(int[] copy, int[] a) {
                    for(int i=0;i<a.length;i++){
                        int connect=0;
                    }
                    System.arraycopy(a, 0, copy, 0, a.length);
		}
                public void addNode(int co,int hOrM){
                    addNode(co%8,co/8,hOrM);
                }
		public void addNode(int x, int y, int hOrM) {
			int co = 8 * y + x;
			if (hOrM == NONE&&nodeAt(x,y)!=null) {
				if (nodeAt(x, y).getColor() == HUMAN) {
					hList[sizeOfH] = 0;
					sizeOfH--;
					copy(hSortedList, hList);
				}
				if (nodeAt(x, y).getColor() == MACHINE) {
					mList[sizeOfM] = 0;
					sizeOfM--;
					copy(mSortedList, mList);
				}
				board[co].hOrM = NONE;
				int[] connection = getConnectionAt(x, y);
				if (connection != null) {
					for (int cur : connection) {
						if (cur != 0)
							checkConnection(board[co]);
					}
				}
				board[co] = null;
				Node[] n = new Node[4];
				n[0] = ccAt(x, y, 1, 0);
				n[1] = ccAt(x, y, 1, -1);
				n[2] = ccAt(x, y, 0, 1);
				n[3] = ccAt(x, y, -1, -1);
                            for (Node n1 : n) {
                                if (n1 != null) {
                                    checkConnection(n1);
                                }
                            }
				return;
			}
			if (isConner(x, y) || nodeAt(x, y) != null)
				return;
			int n = nodeAdjacent(x, y, hOrM);
			if (n >= 2)
				return;
			if ((hOrM == HUMAN && sizeOfH == 9)
					|| (hOrM == MACHINE && sizeOfM == 9))
				return;
			board[co] = new Node(x, y, hOrM);
			checkConnection(board[co]);
			if (MACHINE == hOrM)
				addMNode(co);
			if (HUMAN == hOrM)
				addHNode(co);

		}

		public int isInGoalArea(int co) {
			if (co / 8 == 0 || co % 8 == 0)
				return goalFirst;
			if (co / 8 == 7 || co % 8 == 7)
				return goalSecond;
			return NONE;
		}

		public boolean isInGoalArea2(int co) {
                    return co / 8 == 7 || co % 8 == 7;
		}

		// Find connected nodes to this node and save them to this node's
		// variable (connectedNode)
		private void checkConnection(Node m) {
			// - Direct
			// save in element number 0 and 1(negative 1 means the the smaller x
			// or y)
			ccDirect(m, -1, 0, 0);
			ccDirect(m, 1, 0, 1);
			// | Direct
			// save in element number 2 and 3
			ccDirect(m, 0, -1, 2);
			ccDirect(m, 0, 1, 3);
			// \ Direct
			// save in element number 4 and 5
			ccDirect(m, -1, -1, 4);
			ccDirect(m, 1, 1, 5);
			// / Direct
			// save in element number 6 and 7
			ccDirect(m, -1, 1, 6);
			ccDirect(m, 1, -1, 7);
		}

		// find same color node in this direction
		private void ccDirect(Node m, int x, int y, int number) {
			Node n;
			n = ccAt(m.getX() + x, m.getY() + y, x, y);
			if (n != null) {
				if (n.getColor() == m.getColor()) {
					m.connectedNode[number] = n.getY() * 8 + n.getX();
					if (x == -1) {
						n.connectedNode[number + 1] = m.getY() * 8 + m.getX();
						return;
					}
					if (x == 0 && y == -1) {
						n.connectedNode[number + 1] = m.getY() * 8 + m.getX();
						return;
					}
					n.connectedNode[number - 1] = m.getY() * 8 + m.getX();
				} else {
					// block the path of the enemy's node if this node is on it
					if (x == -1) {
						n.connectedNode[number + 1] = NONE;
						return;
					}
					if (x == 0 && y == -1) {
						n.connectedNode[number + 1] = NONE;
						return;
					}
					n.connectedNode[number - 1] = NONE;
				}
			}
		}

		// find connected node even enemy's node
		private Node ccAt(int x, int y, int n, int m) {
			int co = 8 * y + x;
			if (co <= 0 || co >= 63 || isConner(x, y) || x < 0 || y < 0
					|| x >= 8 || y >= 8)
				return null;
			if (board[co] != null) {
				return board[co];
			}
			// check whether the node has the same color and has been used in
			// the network or not
			return ccAt(x + n, y + m, n, m);
		}
                public int nodeOfHOrM(int co){
                   if (board[co]==null) return NONE;
                   if( board[co].hOrM==HUMAN) return HUMAN;
                   else if(board[co].hOrM==MACHINE) return MACHINE;
                   else return NONE;
                }
		public int newNodeAt() {
                    int co=mainBoard.newNode;
			
                    return co;
		}

		public void printBoard() {
			int i = 0;
			System.out.println("---------");
			System.out.print("  0  1  2  3  4  5  6  7");
			int m = 0;
			while (true) {
				if (i >= board.length)
					break;
				if (board[i] != null) {
					if (i % 8 == 0) {
						System.out.println();
						System.out.print(m);
						m++;
					}
					if (board[i].getColor() == HUMAN)
						System.out.print(" H ");
					if (board[i].getColor() == MACHINE)
						System.out.print(" M ");
				} else {
					if (i % 8 == 0) {
						System.out.println();
						System.out.print(m);
						m++;
					}
					System.out.print(" 0 ");
				}
				i++;
			}
		}
		public int Winner() {
			for (int x = 1; x < 7; x++) {
				if (isHumanPlayerWin(x))
					return HUMAN;
				if (isMachinePlayerWin(x))
					return MACHINE;
				resetCheck();
			}
			return 0;
		}

		private boolean isHumanPlayerWin(int x) {
			//System.out.println("HUMAN");
			if (nodeAt(x, 0) != null)
				return isThisPlayerWin(x, 0, 1, HORIZONTAL);
			else
				return false;
		}

		private boolean isThisPlayerWin(int x, int y, int count, int prevLink) {
			// resetH();
			boolean result = false;
			int co = 8 * y + x;
			// System.out.println("x " + x);
			// System.out.println("y " + y);
			// connectTest(x, y);
			if (isInGoalArea(co) == NetworkGame.Board.goalSecond) {
				if (count >= 6)
					return true;
				else
					return false;
			}
			isCheck[co] = true;
			int[] connectNode = getConnectionAt(x, y);
			if (connectNode != null) {
				for (int i = 0; i < connectNode.length; i++) {
					int aco = connectNode[i];
					int ax = aco % 8;
					int ay = aco / 8;
					if (aco != NONE && inNet[aco] != inNet[co]
							&& isInGoalArea(aco) != goalFirst) {
						int direct = direct(i);
						if (prevLink != direct) {
							inNet[aco] = inNet[co];
							result = isThisPlayerWin(ax, aco / 8, count + 1,
									direct);
							inNet[aco] = aco;
							if (result)
								break;
						}
					}
				}
			}
			return result;
		}

		private void resetCheck() {
			while (true) {
				int co = nextNodeCo(HUMAN);
				isCheck[co] = false;
				if (co == NONE)
					break;
			}
			while (true) {
				int co = nextNodeCo(MACHINE);
				isCheck[co] = false;
				if (co == NONE)
					break;
			}
		}

		private boolean isMachinePlayerWin(int x) {
			//System.out.println("MACHINE");
			if (nodeAt(0, x) != null)
				return isThisPlayerWin(0, x, 1, VERTICAL);
			else
				return false;
		}
                public int[] listOfNode(int hOrM){
                    int[] array=null;
                    if(hOrM==HUMAN) System.arraycopy(hList, 0, array, 0, hList.length);
                    if(hOrM==MACHINE) System.arraycopy(mList,0,array,0,mList.length);
                    return array;
                }
		public void stepNode(int fromX, int fromY,int toX, int toY,int hOrM) {
                    addNode(fromX,fromY,NONE);
                    addNode(toX,toY,hOrM);
		}

		// to find nodes that are connecting (orthogonally or diagonally) to
		// this node
		// currLink is the network that this node is in
		private Node coNode(int x, int y, int n, int m, int col, int currlink) {
			int co = 8 * y + x;
			if (co <= 0 || co >= 63 || isConner(x, y) || x < 0 || y < 0
					|| x > 7 || y > 7
					|| (nodeAt(x, y) != null && nodeAt(x, y).getColor() != col))
				return null;
			if (board[co] != null && board[co].getColor() == col
					&& board[co].inLink[currlink] == false) {
				return board[co];
			}
			// check whether the node has the same color and has been used in
			// the network or not
			return coNode(x + n, y + m, n, m, col, currlink);
		}

		// / direction
		private Node[] bottomUpDirect(int x, int y, int col, int currlink) {
			Node[] n = new Node[2];
			n[0] = coNode(x - 1, y + 1, -1, 1, col, currlink);
			n[1] = coNode(x + 1, y - 1, 1, -1, col, currlink);
			int m = 0;
			for (int i = 0; i <= 1; i++) {
				if (n[0] != null) {
					m++;
				}
				if (n[1] != null) {
					m++;
				}
			}
			if (m == 0)
				return null;
			else
				return n;
		}

		// - direction
		private Node[] horizontalDirect(int x, int y, int col, int currlink) {
			Node[] n = new Node[2];
			n[0] = coNode(x - 1, y, -1, 0, col, currlink);
			n[1] = coNode(x + 1, y, 1, 0, col, currlink);
			int m = 0;
			for (int i = 0; i <= 1; i++) {
				if (n[0] != null)
					m++;
				if (n[1] != null)
					m++;
			}
			if (m == 0)
				return null;
			else
				return n;
		}

		// \ direction
		private Node[] topDownDirect(int x, int y, int col, int currlink) {
			Node[] n = new Node[2];
			n[0] = coNode(x - 1, y - 1, -1, -1, col, currlink);
			n[1] = coNode(x + 1, y + 1, 1, 1, col, currlink);
			int m = 0;
			for (int i = 0; i <= 1; i++) {
				if (n[0] != null)
					m++;
				if (n[1] != null)
					m++;
			}
			if (m == 0)
				return null;
			else
				return n;
		}

		// check whether | direction has any same color node
		private Node[] verticalDirect(int x, int y, int col, int currlink) {
			Node[] n = new Node[2];
			n[0] = coNode(x, y - 1, 0, -1, col, currlink);
			n[1] = coNode(x, y + 1, 0, 1, col, currlink);
			int m = 0;
			for (int i = 0; i <= 1; i++) {
				if (n[0] != null)
					m++;
				if (n[1] != null)
					m++;
			}
			if (m == 0)
				return null;
			else
				return n;
		}

		// preDi is the way that this node connect to the previous
		public boolean isEnd(int preDi, Node node, int currlink) {
			Node[] l = null;
			Node[] l1 = null;
			Node[] l2 = null;
			switch (preDi) {
			case HORIZONTAL:
				// don't check the horizontal direction
				l = checkDirect(VERTICAL, node, currlink);
				l1 = checkDirect(TOPDOWN, node, currlink);
				l2 = checkDirect(BOTTOMUP, node, currlink);
				break;
			case VERTICAL:
				// don't check the vertical direction
				l = checkDirect(HORIZONTAL, node, currlink);
				l1 = checkDirect(TOPDOWN, node, currlink);
				l2 = checkDirect(BOTTOMUP, node, currlink);
				break;
			case BOTTOMUP:
				// don't check the bottomup direction
				l = checkDirect(VERTICAL, node, currlink);
				l1 = checkDirect(HORIZONTAL, node, currlink);
				l2 = checkDirect(TOPDOWN, node, currlink);
				break;
			case TOPDOWN:
				// don't check the topdown direction
				l = checkDirect(VERTICAL, node, currlink);
				l1 = checkDirect(HORIZONTAL, node, currlink);
				l2 = checkDirect(BOTTOMUP, node, currlink);
				break;
			}
			if (l == null && l1 == null && l2 == null)
				return true;
			else
				return false;

		}

		private Node[] checkDirect(int direction, Node x, int currlink) {
			if (x == null)
				return null;
			Node[] m = null;
			switch (direction) {
			case VERTICAL:
				m = verticalDirect(x.getX(), x.getY(), x.getColor(), currlink);
				break;
			case HORIZONTAL:
				m = horizontalDirect(x.getX(), x.getY(), x.getColor(), currlink);
				break;
			case TOPDOWN:
				m = topDownDirect(x.getX(), x.getY(), x.getColor(), currlink);
				break;
			case BOTTOMUP:
				m = bottomUpDirect(x.getX(), x.getY(), x.getColor(), currlink);
				break;
			}
			return m;
		}
		private int nodeAdjacent(int x, int y, int col) {
			return nodeAdjacent(x, y, col, 1);
		}

		// To find how many nodes that are adjacent to this node
		private int nodeAdjacent(int x, int y, int col, int t) {// OK
			int adj = 0;
			int co = 8 * y + x;
			int x2 = 0, y2 = 0;
			int h1 = x - 1;
			if (t >= 3)
				return 0;
			if (h1 <= 0)
				h1 = 0;
			int h2 = x + 1;
			if (h2 >= 7)
				h2 = 7;
			int v1 = y - 1;
			if (v1 <= 0)
				v1 = 0;
			int v2 = y + 1;
			if (v2 >= 7)
				v2 = 7;
			for (int x1 = h1; x1 <= h2; x1++) {
				for (int y1 = v1; y1 <= v2; y1++) {
					int cur = 8 * y1 + x1;
					if (!isConner(x1, y1)) {
						// System.out.printf("cur %d,x %d,y %d",cur,x1,y1);
						if (board[cur] != null && board[cur].getColor() == col
								&& cur != co) {
							x2 = x1;
							y2 = y1;
							if (++adj >= 2) {

							}
						}
					}
				}

				if (x2 != 0 || y2 != 0)
					adj += nodeAdjacent(x2, y2, col, ++t);
			}
			return adj;
		}

		public boolean isLegal(int x, int y, int side) {// OK
			if (isConner(x, y) || nodeAdjacent(x, y, side) >= 2
					|| nodeAt(x, y) != null || isInOtherGoal(x, y, side))
				return false;
                        else{
                            //    System.out.printf("x %d,y %d\n",x,y);
				return true;
                        }
		}

		//
		private boolean isConner(int x, int y) {
                    return (x == 0 && (y == 0 || y == 7))
                            || (x == 7 && (y == 0 || y == 7)); // System.out.println("Invalid move: Node cannot be placed on conners");
		}

		private boolean isInOtherGoal(int x, int y, int side) {
			if (side == HUMAN && (x == 0 || x == 7))
				return true;
			return side == MACHINE && (y == 0 || y == 7);
		}
	}
/**/
	// end board
	private static class Node {
		private int hOrM;
		private int[] co;
		private int[] connectedNode;
		private boolean[] inLink;// Is this node in the network of the array of
		private int nConnectNode;							// node

		public int getColor() {
			return hOrM;
		}

		public int getX() {
			return co[0];
		}

		public int getY() {
			return co[1];
		}

		public int getCo() {
			return co[0] + co[1] * 8;
		}

		public Node() {

		}

		public Node(int x, int y, int hOrM) {
			this.hOrM = hOrM;
			co = new int[2];
			co[0] = x;
			co[1] = y;
			inLink = new boolean[8];
			for (int i = 0; i < 7; i++) {
				inLink[i] = false;
			}
			connectedNode = new int[8];
			if (x == 0 || y == 0) {
				int i = 0;
				while (++i < 8)
					inLink[i] = true;
			}
		}

		private int[] connection() {
			int[] m = new int[8];
			int i = -1;
			int k = 0;
			while (++i < 8) {
				m[i] = connectedNode[i];
                                
				if (m[i] != NONE) {
					k++;
					/*
					 * System.out.println("In copy x:" + m[i] % 8);
					 * System.out.println("In copy y:" + m[i] / 8);
					 */
				}
			}
                        this.nConnectNode=k;
			if (k != 0)
				return m;
			else
				return null;
		}
	}
    /**
     * Creates new form GUI
     */
    public NetworkGame() {
		mainBoard = new Board();
                machinePlayer=new MachinePlayer();
        initComponents();
    }
    public void reset(){
        NetworkGame newgame= new NetworkGame();
        newgame.setVisible(true);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jPanel1 = new javax.swing.JPanel();
        empty0 = new javax.swing.JLabel();
        empty1 = new javax.swing.JLabel();
        empty2 = new javax.swing.JLabel();
        empty3 = new javax.swing.JLabel();
        empty4 = new javax.swing.JLabel();
        empty5 = new javax.swing.JLabel();
        empty6 = new javax.swing.JLabel();
        empty7 = new javax.swing.JLabel();
        empty8 = new javax.swing.JLabel();
        empty9 = new javax.swing.JLabel();
        empty10 = new javax.swing.JLabel();
        empty11 = new javax.swing.JLabel();
        empty12 = new javax.swing.JLabel();
        empty13 = new javax.swing.JLabel();
        empty14 = new javax.swing.JLabel();
        empty15 = new javax.swing.JLabel();
        empty16 = new javax.swing.JLabel();
        empty17 = new javax.swing.JLabel();
        empty18 = new javax.swing.JLabel();
        empty19 = new javax.swing.JLabel();
        empty20 = new javax.swing.JLabel();
        empty21 = new javax.swing.JLabel();
        empty22 = new javax.swing.JLabel();
        empty23 = new javax.swing.JLabel();
        empty24 = new javax.swing.JLabel();
        empty25 = new javax.swing.JLabel();
        empty26 = new javax.swing.JLabel();
        empty27 = new javax.swing.JLabel();
        empty28 = new javax.swing.JLabel();
        empty29 = new javax.swing.JLabel();
        empty30 = new javax.swing.JLabel();
        empty31 = new javax.swing.JLabel();
        empty32 = new javax.swing.JLabel();
        empty33 = new javax.swing.JLabel();
        empty34 = new javax.swing.JLabel();
        empty35 = new javax.swing.JLabel();
        empty36 = new javax.swing.JLabel();
        empty37 = new javax.swing.JLabel();
        empty38 = new javax.swing.JLabel();
        empty39 = new javax.swing.JLabel();
        empty40 = new javax.swing.JLabel();
        empty41 = new javax.swing.JLabel();
        empty42 = new javax.swing.JLabel();
        empty43 = new javax.swing.JLabel();
        empty44 = new javax.swing.JLabel();
        empty45 = new javax.swing.JLabel();
        empty46 = new javax.swing.JLabel();
        empty47 = new javax.swing.JLabel();
        empty48 = new javax.swing.JLabel();
        empty49 = new javax.swing.JLabel();
        empty50 = new javax.swing.JLabel();
        empty51 = new javax.swing.JLabel();
        empty52 = new javax.swing.JLabel();
        empty53 = new javax.swing.JLabel();
        empty54 = new javax.swing.JLabel();
        empty55 = new javax.swing.JLabel();
        empty56 = new javax.swing.JLabel();
        empty57 = new javax.swing.JLabel();
        empty58 = new javax.swing.JLabel();
        empty59 = new javax.swing.JLabel();
        empty60 = new javax.swing.JLabel();
        empty61 = new javax.swing.JLabel();
        empty62 = new javax.swing.JLabel();
        empty63 = new javax.swing.JLabel();
        winner = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jMenuBar2 = new javax.swing.JMenuBar();
        newGameButton = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Network");

        jPanel1.setLayout(new java.awt.GridLayout(8, 8));

        empty0.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/bad.gif"))); // NOI18N
        empty0.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty0MousePressed(evt);
            }
        });
        jPanel1.add(empty0);

        empty1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty1MousePressed(evt);
            }
        });
        jPanel1.add(empty1);

        empty2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty2MousePressed(evt);
            }
        });
        jPanel1.add(empty2);

        empty3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty3MousePressed(evt);
            }
        });
        jPanel1.add(empty3);

        empty4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty4MousePressed(evt);
            }
        });
        jPanel1.add(empty4);

        empty5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty5MousePressed(evt);
            }
        });
        jPanel1.add(empty5);

        empty6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty6MousePressed(evt);
            }
        });
        jPanel1.add(empty6);

        empty7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/bad.gif"))); // NOI18N
        jPanel1.add(empty7);

        empty8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty8MousePressed(evt);
            }
        });
        jPanel1.add(empty8);

        empty9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty9MousePressed(evt);
            }
        });
        jPanel1.add(empty9);

        empty10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty10MousePressed(evt);
            }
        });
        jPanel1.add(empty10);

        empty11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty11MousePressed(evt);
            }
        });
        jPanel1.add(empty11);

        empty12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty12MousePressed(evt);
            }
        });
        jPanel1.add(empty12);

        empty13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty13MousePressed(evt);
            }
        });
        jPanel1.add(empty13);

        empty14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty14MousePressed(evt);
            }
        });
        jPanel1.add(empty14);

        empty15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty15MousePressed(evt);
            }
        });
        jPanel1.add(empty15);

        empty16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty16MousePressed(evt);
            }
        });
        jPanel1.add(empty16);

        empty17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty17MousePressed(evt);
            }
        });
        jPanel1.add(empty17);

        empty18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty18MousePressed(evt);
            }
        });
        jPanel1.add(empty18);

        empty19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty19MousePressed(evt);
            }
        });
        jPanel1.add(empty19);

        empty20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty20MousePressed(evt);
            }
        });
        jPanel1.add(empty20);

        empty21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty21MousePressed(evt);
            }
        });
        jPanel1.add(empty21);

        empty22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty22MousePressed(evt);
            }
        });
        jPanel1.add(empty22);

        empty23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty23MousePressed(evt);
            }
        });
        jPanel1.add(empty23);

        empty24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty24.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty24MousePressed(evt);
            }
        });
        jPanel1.add(empty24);

        empty25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty25.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty25MousePressed(evt);
            }
        });
        jPanel1.add(empty25);

        empty26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty26.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty26MousePressed(evt);
            }
        });
        jPanel1.add(empty26);

        empty27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty27.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty27MousePressed(evt);
            }
        });
        jPanel1.add(empty27);

        empty28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty28.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty28MousePressed(evt);
            }
        });
        jPanel1.add(empty28);

        empty29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty29.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty29MousePressed(evt);
            }
        });
        jPanel1.add(empty29);

        empty30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty30.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty30MousePressed(evt);
            }
        });
        jPanel1.add(empty30);

        empty31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty31.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty31MousePressed(evt);
            }
        });
        jPanel1.add(empty31);

        empty32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty32.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty32MousePressed(evt);
            }
        });
        jPanel1.add(empty32);

        empty33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty33.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty33MousePressed(evt);
            }
        });
        jPanel1.add(empty33);

        empty34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty34.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty34MousePressed(evt);
            }
        });
        jPanel1.add(empty34);

        empty35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty35.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty35MousePressed(evt);
            }
        });
        jPanel1.add(empty35);

        empty36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty36.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty36MousePressed(evt);
            }
        });
        jPanel1.add(empty36);

        empty37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty37.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty37MousePressed(evt);
            }
        });
        jPanel1.add(empty37);

        empty38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty38.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty38MousePressed(evt);
            }
        });
        jPanel1.add(empty38);

        empty39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty39.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty39MousePressed(evt);
            }
        });
        jPanel1.add(empty39);

        empty40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty40.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty40MousePressed(evt);
            }
        });
        jPanel1.add(empty40);

        empty41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty41.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty41MousePressed(evt);
            }
        });
        jPanel1.add(empty41);

        empty42.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty42.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty42MousePressed(evt);
            }
        });
        jPanel1.add(empty42);

        empty43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty43.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty43MousePressed(evt);
            }
        });
        jPanel1.add(empty43);

        empty44.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty44.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty44MousePressed(evt);
            }
        });
        jPanel1.add(empty44);

        empty45.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty45.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty45MousePressed(evt);
            }
        });
        jPanel1.add(empty45);

        empty46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty46.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty46MousePressed(evt);
            }
        });
        jPanel1.add(empty46);

        empty47.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty47.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty47MousePressed(evt);
            }
        });
        jPanel1.add(empty47);

        empty48.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty48.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty48MousePressed(evt);
            }
        });
        jPanel1.add(empty48);

        empty49.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty49.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty49MousePressed(evt);
            }
        });
        jPanel1.add(empty49);

        empty50.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty50.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty50MousePressed(evt);
            }
        });
        jPanel1.add(empty50);

        empty51.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty51.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty51MousePressed(evt);
            }
        });
        jPanel1.add(empty51);

        empty52.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty52.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty52MousePressed(evt);
            }
        });
        jPanel1.add(empty52);

        empty53.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty53.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty53MousePressed(evt);
            }
        });
        jPanel1.add(empty53);

        empty54.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty54.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty54MousePressed(evt);
            }
        });
        jPanel1.add(empty54);

        empty55.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty55.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty55MousePressed(evt);
            }
        });
        jPanel1.add(empty55);

        empty56.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/bad.gif"))); // NOI18N
        jPanel1.add(empty56);

        empty57.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty57.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty57MousePressed(evt);
            }
        });
        jPanel1.add(empty57);

        empty58.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty58.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty58MousePressed(evt);
            }
        });
        jPanel1.add(empty58);

        empty59.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty59.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty59MousePressed(evt);
            }
        });
        jPanel1.add(empty59);

        empty60.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty60.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty60MousePressed(evt);
            }
        });
        jPanel1.add(empty60);

        empty61.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty61.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty61MousePressed(evt);
            }
        });
        jPanel1.add(empty61);

        empty62.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/empty.gif"))); // NOI18N
        empty62.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                empty62MousePressed(evt);
            }
        });
        jPanel1.add(empty62);

        empty63.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/bad.gif"))); // NOI18N
        jPanel1.add(empty63);

        winner.setText("Winner: ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 26, Short.MAX_VALUE)
        );

        newGameButton.setText("File");

        jMenuItem2.setText("New Game");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        newGameButton.add(jMenuItem2);

        jMenuBar2.add(newGameButton);

        jMenu4.setText("Edit");
        jMenuBar2.add(jMenu4);

        setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 67, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(winner)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(22, 22, 22))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(286, 286, 286)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(404, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 161, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(150, 150, 150)
                .addComponent(winner)
                .addGap(31, 31, 31))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void setMove(int co,String s){
        switch(co){ 
            case 1 : empty1.setIcon(new javax.swing.ImageIcon(getClass().getResource(s))); return;   
            case 2 : empty2.setIcon(new javax.swing.ImageIcon(getClass().getResource(s))); return;
            case 3 : empty3.setIcon(new javax.swing.ImageIcon(getClass().getResource(s))); return;   
            case 4 : empty4.setIcon(new javax.swing.ImageIcon(getClass().getResource(s))); return;
            case 5: empty5.setIcon(new javax.swing.ImageIcon(getClass().getResource(s))); return;   
            case 6 : empty6.setIcon(new javax.swing.ImageIcon(getClass().getResource(s))); return;
            case 7: empty7.setIcon(new javax.swing.ImageIcon(getClass().getResource(s))); return;   
            case 8: empty8.setIcon(new javax.swing.ImageIcon(getClass().getResource(s))); return;   
            case 9: empty9.setIcon(new javax.swing.ImageIcon(getClass().getResource(s))); return;
            case 10: empty10.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                return;
            case 11: empty11.setIcon(new javax.swing.ImageIcon(getClass().getResource(s))); 
                return;
            case 12: empty12.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                return;
            case 13: empty13.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                return;
            case 14: empty14.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                return;
            case 15: empty15.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                    return;
            case 16: empty16.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                    return;
            case 17: empty17.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                   return;
            case 18: empty18.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                    return;
            case 19: empty19.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                   return;
            case 20: empty20.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                    return;
            case 21: empty21.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                   return;
            case 22: empty22.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                    return;
            case 23: empty23.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                   return;
            case 24: empty24.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                   return;
            case 25: empty25.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                   return;
            case 26: empty26.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                   return;
            case 27: empty27.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                   return;
            case 28: empty28.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                   return;
            case 29: empty29.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                   return;
            case 30: empty30.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                   return;
            case 31: empty31.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                   return;
            case 32: empty32.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                   return;
            case 33: empty33.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                   return;
            case 34: empty34.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                   return;
            case 35: empty35.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                   return;
            case 36: empty36.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                   return;
            case 37: empty37.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                   return;
            case 38: empty38.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                   return;
            case 39: empty39.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                   return;
            case 40: empty40.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                   return;
            case 41: empty41.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                   return;
            case 42: empty42.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                   return;
            case 43: empty43.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                   return;
            case 44: empty44.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                   return;
            case 45: empty45.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                  return;
            case 46: empty46.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                  return;
            case 47: empty47.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                  return;
            case 48: empty48.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                  return;
            case 49: empty49.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                  return;
            case 50: empty50.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                   return;
            case 51: empty51.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                  return;
            case 52: empty52.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                  return;
            case 53: empty53.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                  return;
            case 54: empty54.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                  return;
            case 55: empty55.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                  return;
            case 56: empty56.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                  return;
            case 57: empty57.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                  return;
            case 58: empty58.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                  return;
            case 59: empty59.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                  return;
            case 60: empty60.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                   return;
            case 61: empty61.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                  return;
            case 62: empty62.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
                   return;
            case 63: empty63.setIcon(new javax.swing.ImageIcon(getClass().getResource(s)));
        }
    }
    
    private int nodeOnBoard(){
        return mainBoard.sizeOfH()+mainBoard.sizeOfM();
    }
    private void mousePressed(int co){
        int x=co%8;
        
        int y=co/8;
        System.out.println(mainBoard.isLegal(x,y,MACHINE));
        int nodeOnBoard=nodeOnBoard();
        if(nodeOnBoard<20){
            if(mainBoard.isLegal(x,y,HUMAN)&&isPlayerTurned){
            mainBoard.addNode(x, y, HUMAN);
            setMove(co,"/pics/white1.gif");isPlayerTurned=false;
        (new Thread(new chooseMoveThread())).start();
            }
        
        }else{
            if(mainBoard.board[co].hOrM==HUMAN){
                mainBoard.isChoosen[0]=co;
            }
            if(mainBoard.isChoosen[0]!=0){
                mainBoard.isChoosen[1]=co;
                int fco=mainBoard.isChoosen[0];
                mainBoard.stepNode(fco%8, fco/8, co%8, co/8, HUMAN);
                setMove(co,"/pics/white1.gif");
                setMove(fco,"/pics/empty.gif");
                mainBoard.isChoosen[0]=0;
            }
        }
    }
    private void empty1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty1MousePressed
        // TODO add your handling code here: 
        int co=1;
        mousePressed(co);
    }//GEN-LAST:event_empty1MousePressed

    private void empty2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty2MousePressed
        // TODO add your handling code here:
        int co=2;
        mousePressed(co);
    }//GEN-LAST:event_empty2MousePressed

    private void empty3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty3MousePressed
        // TODO add your handling code here:
        int co=3;
        mousePressed(co);
    }//GEN-LAST:event_empty3MousePressed

    private void empty4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty4MousePressed
        // TODO add your handling code here:
        int co=4;
        mousePressed(co);
    }//GEN-LAST:event_empty4MousePressed

    private void empty5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty5MousePressed
        // TODO add your handling code here:
        int co=5;
        mousePressed(co);
    }//GEN-LAST:event_empty5MousePressed

    private void empty6MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty6MousePressed
        // TODO add your handling code here:
        int co=6;
        mousePressed(co);
    }//GEN-LAST:event_empty6MousePressed

    private void empty9MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty9MousePressed
        // TODO add your handling code here:
        int co=9;
        mousePressed(co);
    }//GEN-LAST:event_empty9MousePressed

    private void empty10MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty10MousePressed
        // TODO add your handling code here:
        int co=10;
        mousePressed(co);
    }//GEN-LAST:event_empty10MousePressed

    private void empty11MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty11MousePressed
        // TODO add your handling code here:
        int co=11;
        mousePressed(co);
    }//GEN-LAST:event_empty11MousePressed

    private void empty12MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty12MousePressed
        // TODO add your handling code here:
        int co=12;
        mousePressed(co);
    }//GEN-LAST:event_empty12MousePressed

    private void empty13MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty13MousePressed
        // TODO add your handling code here:
        int co=13;
        mousePressed(co);
    }//GEN-LAST:event_empty13MousePressed

    private void empty14MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty14MousePressed
        // TODO add your handling code here:
        int co=14;
        mousePressed(co);
    }//GEN-LAST:event_empty14MousePressed

    private void empty17MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty17MousePressed
        // TODO add your handling code here:
        int co=17;
        mousePressed(co);
    }//GEN-LAST:event_empty17MousePressed

    private void empty18MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty18MousePressed
        // TODO add your handling code here:
        int co=18;
        mousePressed(co);
    }//GEN-LAST:event_empty18MousePressed

    private void empty19MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty19MousePressed
        // TODO add your handling code here:
        int co=19;
        mousePressed(co);
    }//GEN-LAST:event_empty19MousePressed

    private void empty20MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty20MousePressed
        // TODO add your handling code here:
        int co=20;
        mousePressed(co);
    }//GEN-LAST:event_empty20MousePressed

    private void empty21MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty21MousePressed
        // TODO add your handling code here:
        int co=21;
        mousePressed(co);
    }//GEN-LAST:event_empty21MousePressed

    private void empty22MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty22MousePressed
        // TODO add your handling code here:
        int co=22;
        mousePressed(co);
    }//GEN-LAST:event_empty22MousePressed

    private void empty25MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty25MousePressed
        // TODO add your handling code here:
        int co=25;
        mousePressed(co);
    }//GEN-LAST:event_empty25MousePressed

    private void empty26MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty26MousePressed
        // TODO add your handling code here:
        int co=26;
        mousePressed(co);
    }//GEN-LAST:event_empty26MousePressed

    private void empty27MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty27MousePressed
        // TODO add your handling code here:
        int co=27;
        mousePressed(co);
    }//GEN-LAST:event_empty27MousePressed

    private void empty28MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty28MousePressed
        // TODO add your handling code here:
        int co=28;
        mousePressed(co);
    }//GEN-LAST:event_empty28MousePressed

    private void empty29MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty29MousePressed
        // TODO add your handling code here:
        int co=29;
        mousePressed(co);
    }//GEN-LAST:event_empty29MousePressed

    private void empty30MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty30MousePressed
        // TODO add your handling code here:
        int co=30;
        mousePressed(co);
    }//GEN-LAST:event_empty30MousePressed

    private void empty33MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty33MousePressed
        // TODO add your handling code here:
        int co=33;
        mousePressed(co);
    }//GEN-LAST:event_empty33MousePressed

    private void empty34MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty34MousePressed
        // TODO add your handling code here:
        int co=34;
        mousePressed(co);
    }//GEN-LAST:event_empty34MousePressed

    private void empty35MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty35MousePressed
        // TODO add your handling code here:
        int co=35;
        mousePressed(co);
    }//GEN-LAST:event_empty35MousePressed

    private void empty36MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty36MousePressed
        // TODO add your handling code here:
        int co=36;
        mousePressed(co);
    }//GEN-LAST:event_empty36MousePressed

    private void empty37MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty37MousePressed
        // TODO add your handling code here:
        int co=37;
        mousePressed(co);
    }//GEN-LAST:event_empty37MousePressed

    private void empty38MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty38MousePressed
        // TODO add your handling code here:
        int co=38;
        mousePressed(co);
    }//GEN-LAST:event_empty38MousePressed

    private void empty41MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty41MousePressed
        // TODO add your handling code here:
        int co=41;
        mousePressed(co);
    }//GEN-LAST:event_empty41MousePressed

    private void empty42MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty42MousePressed
        // TODO add your handling code here:
        int co=42;
        mousePressed(co);
    }//GEN-LAST:event_empty42MousePressed

    private void empty43MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty43MousePressed
        // TODO add your handling code here:
        int co=43;
        mousePressed(co);
    }//GEN-LAST:event_empty43MousePressed

    private void empty44MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty44MousePressed
        // TODO add your handling code here:
        int co=44;
        mousePressed(co);
    }//GEN-LAST:event_empty44MousePressed

    private void empty45MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty45MousePressed
        // TODO add your handling code here:
        int co=45;
        mousePressed(co);
    }//GEN-LAST:event_empty45MousePressed

    private void empty46MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty46MousePressed
        // TODO add your handling code here:
        int co=46;
        mousePressed(co);
    }//GEN-LAST:event_empty46MousePressed

    private void empty49MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty49MousePressed
        // TODO add your handling code here:
        int co=49;
        mousePressed(co);
    }//GEN-LAST:event_empty49MousePressed

    private void empty50MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty50MousePressed
        // TODO add your handling code here:
        int co=50;
        mousePressed(co);
    }//GEN-LAST:event_empty50MousePressed

    private void empty51MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty51MousePressed
        // TODO add your handling code here:
        int co=51;
        mousePressed(co);
    }//GEN-LAST:event_empty51MousePressed

    private void empty52MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty52MousePressed
        // TODO add your handling code here:
        int co=52;
        mousePressed(co);
    }//GEN-LAST:event_empty52MousePressed

    private void empty53MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty53MousePressed
        // TODO add your handling code here:
        int co=53;
        mousePressed(co);
    }//GEN-LAST:event_empty53MousePressed

    private void empty54MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty54MousePressed
        // TODO add your handling code here:
        int co=54;
        mousePressed(co);
    }//GEN-LAST:event_empty54MousePressed

    private void empty57MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty57MousePressed
        // TODO add your handling code here:
        int co=57;
        mousePressed(co);
    }//GEN-LAST:event_empty57MousePressed

    private void empty58MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty58MousePressed
        // TODO add your handling code here:
        int co=58;
        mousePressed(co);
    }//GEN-LAST:event_empty58MousePressed

    private void empty59MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty59MousePressed
        // TODO add your handling code here:
        int co=59;
        mousePressed(co);
    }//GEN-LAST:event_empty59MousePressed

    private void empty60MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty60MousePressed
        // TODO add your handling code here:
        int co=60;
        mousePressed(co);
    }//GEN-LAST:event_empty60MousePressed

    private void empty61MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty61MousePressed
        // TODO add your handling code here:
        int co=61;
        mousePressed(co);
    }//GEN-LAST:event_empty61MousePressed

    private void empty62MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty62MousePressed
        // TODO add your handling code here:
        int co=62;
        mousePressed(co);
    }//GEN-LAST:event_empty62MousePressed

    private void empty8MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty8MousePressed
        // TODO add your handling code here:
        int co=8;
        mousePressed(co);
    }//GEN-LAST:event_empty8MousePressed

    private void empty16MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty16MousePressed
        // TODO add your handling code here:
        int co=16;
        mousePressed(co);
    }//GEN-LAST:event_empty16MousePressed

    private void empty24MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty24MousePressed
        // TODO add your handling code here:
        int co=24;
        mousePressed(co);
    }//GEN-LAST:event_empty24MousePressed

    private void empty32MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty32MousePressed
        // TODO add your handling code here:
        int co=32;
        mousePressed(co);
    }//GEN-LAST:event_empty32MousePressed

    private void empty40MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty40MousePressed
        // TODO add your handling code here:
        int co=40;
        mousePressed(co);
    }//GEN-LAST:event_empty40MousePressed

    private void empty48MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty48MousePressed
        // TODO add your handling code here:
        int co=48;
        mousePressed(co);
    }//GEN-LAST:event_empty48MousePressed

    private void empty15MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty15MousePressed
        // TODO add your handling code here:
        int co=15;
        mousePressed(co);
    }//GEN-LAST:event_empty15MousePressed

    private void empty23MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty23MousePressed
        // TODO add your handling code here:
        int co=23;
        mousePressed(co);
    }//GEN-LAST:event_empty23MousePressed

    private void empty31MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty31MousePressed
        // TODO add your handling code here:
        int co=31;
        mousePressed(co);
    }//GEN-LAST:event_empty31MousePressed

    private void empty39MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty39MousePressed
        // TODO add your handling code here:
        int co=39;
        mousePressed(co);
    }//GEN-LAST:event_empty39MousePressed

    private void empty47MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty47MousePressed
        // TODO add your handling code here:
        int co=47;
        mousePressed(co);
    }//GEN-LAST:event_empty47MousePressed

    private void empty55MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty55MousePressed
        // TODO add your handling code here:
        int co=55;
        mousePressed(co);
    }//GEN-LAST:event_empty55MousePressed

    private void empty0MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empty0MousePressed
        // TODO add your handling code here:
            int move=machinePlayer.chooseMove();
           // System.out.printf("x: %d,y: %d",move/8, move%8);
            mainBoard.addNode(move%8, move/8, MACHINE);
            setMove(move,"/pics/black.gif");
        
    }//GEN-LAST:event_empty0MousePressed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        reset();
    }//GEN-LAST:event_jMenuItem2ActionPerformed
               

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) 
        /* Set the Nimbus look and feel 
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */{
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NetworkGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NetworkGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NetworkGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NetworkGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        // Create and display the form 
       java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               NetworkGame game= new NetworkGame();
                       game.setVisible(true);
            }
        });
       /*NetworkGame mc=new NetworkGame();
        mc.addNode(1,1 , HUMAN);
        mc.addNode(2,3 , HUMAN);
        mc.addNode(2,5 , HUMAN);
        mc.addNode(4, 2, HUMAN);
        mc.addNode(4,3 , HUMAN);
        mc.addNode(6,3 , HUMAN);
        mc.addNode(6,5 , HUMAN);
        mc.addNode(6, 6, HUMAN);
        mc.addNode(0,4 , MACHINE);
        mc.addNode(2, 4, MACHINE);
        mc.addNode(3,1 , MACHINE);
        mc.addNode(3,3 , MACHINE);
        mc.addNode(4,1 , MACHINE);
        mc.addNode(4,5 , MACHINE);
        mc.addNode(6,4 , MACHINE);
        mc.addNode(7,5 , MACHINE);
        mc.printBoard();
       System.out.println(mc.mainBoard.Winner() );*/
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel empty0;
    private javax.swing.JLabel empty1;
    private javax.swing.JLabel empty10;
    private javax.swing.JLabel empty11;
    private javax.swing.JLabel empty12;
    private javax.swing.JLabel empty13;
    private javax.swing.JLabel empty14;
    private javax.swing.JLabel empty15;
    private javax.swing.JLabel empty16;
    private javax.swing.JLabel empty17;
    private javax.swing.JLabel empty18;
    private javax.swing.JLabel empty19;
    private javax.swing.JLabel empty2;
    private javax.swing.JLabel empty20;
    private javax.swing.JLabel empty21;
    private javax.swing.JLabel empty22;
    private javax.swing.JLabel empty23;
    private javax.swing.JLabel empty24;
    private javax.swing.JLabel empty25;
    private javax.swing.JLabel empty26;
    private javax.swing.JLabel empty27;
    private javax.swing.JLabel empty28;
    private javax.swing.JLabel empty29;
    private javax.swing.JLabel empty3;
    private javax.swing.JLabel empty30;
    private javax.swing.JLabel empty31;
    private javax.swing.JLabel empty32;
    private javax.swing.JLabel empty33;
    private javax.swing.JLabel empty34;
    private javax.swing.JLabel empty35;
    private javax.swing.JLabel empty36;
    private javax.swing.JLabel empty37;
    private javax.swing.JLabel empty38;
    private javax.swing.JLabel empty39;
    private javax.swing.JLabel empty4;
    private javax.swing.JLabel empty40;
    private javax.swing.JLabel empty41;
    private javax.swing.JLabel empty42;
    private javax.swing.JLabel empty43;
    private javax.swing.JLabel empty44;
    private javax.swing.JLabel empty45;
    private javax.swing.JLabel empty46;
    private javax.swing.JLabel empty47;
    private javax.swing.JLabel empty48;
    private javax.swing.JLabel empty49;
    private javax.swing.JLabel empty5;
    private javax.swing.JLabel empty50;
    private javax.swing.JLabel empty51;
    private javax.swing.JLabel empty52;
    private javax.swing.JLabel empty53;
    private javax.swing.JLabel empty54;
    private javax.swing.JLabel empty55;
    private javax.swing.JLabel empty56;
    private javax.swing.JLabel empty57;
    private javax.swing.JLabel empty58;
    private javax.swing.JLabel empty59;
    private javax.swing.JLabel empty6;
    private javax.swing.JLabel empty60;
    private javax.swing.JLabel empty61;
    private javax.swing.JLabel empty62;
    private javax.swing.JLabel empty63;
    private javax.swing.JLabel empty7;
    private javax.swing.JLabel empty8;
    private javax.swing.JLabel empty9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JMenu newGameButton;
    private javax.swing.JLabel winner;
    // End of variables declaration//GEN-END:variables
}
