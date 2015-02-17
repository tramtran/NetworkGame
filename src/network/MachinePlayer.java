/* MachinePlayer.java */
package network;

import java.lang.management.ManagementFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An implementation of an automatic Network player. Keeps track of moves made
 * by both players. Can select a move for itself.
 */
public class MachinePlayer{

    int sDepth;
    private float sum;
    int t = 0;
    InBoard inBoard;
    int[][] killerMoves;
    long[][] code = {{0x60e0a729423b5dc8L,
        0x7d11e70e4dfefe90L,
        0x6c12a5ef606c9f60L,
        0x3f6add853340ca4L,
        0x38e7819d5a2c3855L,
        0x6e692b79447ad2dbL,
        0x37f013a87f646ef8L,
        0x198c3e502963467cL,
        0x469a9ba233cb293aL,
        0x42927de2495be99cL,
        0x11ae42241ab5550dL,
        0x1d091f70fe9de6dL,
        0x3418ef5e60addc51L,
        0x32a670b0374aa811L,
        0xb6aa300674cfeeeL,
        0x7415aa193ab163cdL,
        0x2edbc5a0549dbeacL,
        0x6a59f0e24dfd7f2eL,
        0x3eb124a85f7f1c73L,
        0x3a8639257471abcdL,
        0x243373cc089455e8L,
        0x4683063147f55820L,
        0x47a973a65fba4ffcL,
        0x5d038b407500ec43L,
        0x3b555d78183e86a7L,
        0x55416a9f72469d93L,
        0x780512f54643977aL,
        0x2064718f37affb1aL,
        0x7a50f51234c5097L,
        0xc0eef255f8024dfL,
        0x3b429da15fce3253L,
        0x5cf030e63dd0d80fL,
        0xc35d77d1e1ce5L,
        0x4b1d1b75372fc165L,
        0x179c60c51b930e9dL,
        0x6ec71b4127ae9cdfL,
        0x1dad534704400aeL,
        0x34bed3896caee731L,
        0x6c13562a059f947aL,
        0x2f02daf6759d7698L,
        0x29689fd26bfb1db5L,
        0x3557075147054976L,
        0x61ba9e83638b307bL,
        0x73750ee8278dfe46L,
        0x83bda973ae24b48L,
        0x722cf3597d979659L,
        0x64e4954758fa3f60L,
        0x13aba32e428768ceL,
        0x1725277a017f185cL,
        0x4f7c978b0e2dbc33L,
        0x6dacd8a6283311ccL,
        0x1ca4bd9940d83165L,
        0x496c874e0fef8d7fL,
        0x57e4e5a01f8e5946L,
        0x7f48a9435c4c3483L,
        0x514d20c60ca27e3eL,
        0x24e104e57a226f84L,
        0x6c690d18646e200dL,
        0x2ab1b7874b162916L,
        0x2c624310a6b3382L,
        0x2db7841939c6733eL,
        0x1946981d548935bfL,
        0x34576812114798f8L,
        0x4f1871074f24a6deL,}, {0x4c42c3c3175fdf39L,
        0x4e8fa09e662c0163L,
        0x1bf100070862b42L,
        0x1834c8211a0f9d55L,
        0xa539e033f12718cL,
        0x2bc158be17d4aee8L,
        0x1d7443624c771e58L,
        0x421494f06b7d34c2L,
        0x577852770ccf59c9L,
        0x53d4a33f358f41c2L,
        0x191a723d0c8f8126L,
        0x341d7f6c3c595a04L,
        0x773ba54c696898a5L,
        0x67002efe4be4c445L,
        0x24df03a6388aa6d4L,
        0x7b120fa21237371dL,
        0x13b64f796332e704L,
        0x7160a3385f0d7bdeL,
        0x7408daa23e54b44L,
        0x64bd7ca92e2a03f7L,
        0x3e19917715fe7717L,
        0x358cd05d34d579a0L,
        0x1121ae23626ecee9L,
        0x6f114d2713f2520cL,
        0xe14c1917a7dcea9L,
        0x5eebeeb6099da63eL,
        0x54b3cf545779f385L,
        0x61e527070f9cab20L,
        0x49631e5f62a9b67cL,
        0x3732ec3b6b8a544dL,
        0x7cd1ed454bea5e4cL,
        0x1b0f052a6751c8eeL,
        0x7eb1a8274d4148c5L,
        0x336d4a28352c5a28L,
        0x25b2856a3de74d8bL,
        0x57f6eae1624a88e4L,
        0x215cfa714d1e532fL,
        0x64f3021702674579L,
        0x4ede63d110f2f8c1L,
        0x7c702d8353e87ffbL,
        0x60b7d9c4348c7d03L,
        0x6a1bbec503363103L,
        0xfc5b22943e33195L,
        0x3c8b99777830e5L,
        0x60e0c98a47e0f888L,
        0x13c5bcce38a4c074L,
        0x712f67486c4176eaL,
        0x7e78ae07122efd81L,
        0x7561e48566c287bdL,
        0x45d0039c4d109146L,
        0x70f5dc8a55b35934L,
        0x3dd5d2b41f6eea2L,
        0x57f565b90d823617L,
        0x4257afb753795ddbL,
        0x35e82cc424f979ecL,
        0x38ebcbf847008d89L,
        0x417e5c32206a4ae9L,
        0x2a07f1277ebbc07bL,
        0x5635b401381adb08L,
        0x47b78629111aa488L,
        0x73c45b042af74740L,
        0x16819b8d135388d3L,
        0x5f3de71f7a4cec4aL,
        0x619eb53860505d5fL}};
    public static void heapMemUsage() 
{
    long used = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
    long max = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax();
    System.out.println( ""+used+" of "+max+" ("+ used/(double)max*100.0 + "%)");
}
 private Move chooseMove(int side, float alpha, float beta, int cur) {
       
     boolean human = false;
        Move best = new Move();
        Move reply;
        float alphaOrig=alpha;
        int sizeOfM = inBoard.sizeOfM();
        int sizeOfH = inBoard.sizeOfH();
        long key = inBoard.key;
        int remainPly=sDepth-cur;
        Move temp=new Move();
        SeparateChainingHash.Bucket b=inBoard.hashTable.get(key);
        if(b!=null&&b.getRemainPly()>=remainPly){
            temp.score=b.getScore();
            temp.move=b.getBestMove();
            if(b.getFlag()==SeparateChainingHash.EXACT) {
       //     System.out.println("SeparateChainingHash.EXACT");
                return temp;
            }else if(b.getFlag()==SeparateChainingHash.LOWERBOUND&&b.getScore()>=alpha){
            //System.out.println("SeparateChainingHash.LOWERBOUND");
               alpha=b.getScore();
            }else if(b.getFlag()==SeparateChainingHash.UPPERBOUND&&b.getScore()<=beta){
         //   System.out.println("SeparateChainingHash.UPPERBOUND");
                beta=b.getScore();
            }
        }
        if(alpha>=beta){
            return temp;
        }
        if ((sizeOfH >= 10 && sizeOfM >= 10)
                || inBoard.Winner() != NetworkGame.NONE
                || cur == sDepth+1 ) {
            Move n = new Move();  
                n.score = evaluationFunction();
                return n;
        }
        //   System.out.println("after condition ");
        if (side == NetworkGame.MACHINE) {
            best.score = alpha;
        } else {
            best.score = beta;
            human = true;
        }  
        int ply = inBoard.sizeOfH() + inBoard.sizeOfM();
        int[] arrayOfMove = move(ply);
            for (int co : arrayOfMove) {
                int x1 = co % 8;
                int y1 = co / 8;
                if (co == 0) {
                    continue;
                }
                if (!inBoard.isLegal(x1, y1, side)) {
                    continue;
                }
                inBoard.addNode(co, side);
                if (human) {
                    reply = chooseMove(NetworkGame.MACHINE, alpha, beta, cur + 1);
                } else {
                    reply = chooseMove(NetworkGame.HUMAN, alpha, beta, cur + 1);
                }
                inBoard.addNode(co, NetworkGame.NONE);
                if (side == NetworkGame.MACHINE && reply.score > best.score) {
                    best.move = co;
                    best.score = reply.score;
                    alpha = best.score;
                } else if (side == NetworkGame.HUMAN && reply.score <= best.score) {
                    best.move = co;
                    best.score = reply.score;
                    beta = reply.score;
                }
         //   System.out.println("x "+x1+" y "+y1);
         //   System.out.println("cur " +cur);
                if (alpha >= beta) {
                    insertKillerMove(ply, co);
                    break;
                }
                
            }
            if(b==null)b=inBoard.hashTable.new Bucket();
            b.setKey(key);
            b.setScore(best.score);
            b.setRemainPly(remainPly);
            b.setBestMove(best.move);
            if(best.score<=alphaOrig) b.setFlag(SeparateChainingHash.UPPERBOUND);
            else if(best.score>=beta) b.setFlag(SeparateChainingHash.LOWERBOUND);
            else b.setFlag((SeparateChainingHash.EXACT));
            inBoard.hashTable.put(b);
        return best;
    }

    class InBoard extends NetworkGame.Board {

        private boolean[] isCheck;
        private int[] inNet;
        private long key;
        private SeparateChainingHash hashTable;

        public InBoard() {
            inNet = new int[64];
            int i = -1;
            while (++i < 64) {
                inNet[i] = i;
            }
            isCheck = new boolean[64];
            hashTable = new SeparateChainingHash(100000000);
            //	isCheckInChooseMove=new boolean[64];
        }

        @Override
        public void addNode(int x, int y, int hOrM) {
            int co = x + 8 * y;
            key = inBoard.hCode(co, hOrM, key);
            super.addNode(x, y, hOrM);
          //  System.out.println("key "+key);
        }

        private long hCode(int co, int hOrM, long key) {
            int human = NetworkGame.HUMAN;
            int machine = NetworkGame.MACHINE;
            int none = NetworkGame.NONE;
            if (hOrM == none) {
                if (inBoard.nodeOfHOrM(co) == human) {
                    hOrM = human;
                } else if (inBoard.nodeOfHOrM(co) == machine) {
                    hOrM = machine;
                }
            }
            if (hOrM == human) {
        //    System.out.println("human co "+co);
                key = key ^ code[0][co];
            }
            if (hOrM == machine) {
         //   System.out.println("machine co "+co);
                key = key ^ code[1][co];
            }
            return key;
        }
    }

    public MachinePlayer(int searchDepth) {
        inBoard = new InBoard();
        sDepth = searchDepth;
        killerMoves = new int[24][5];
    }

    public MachinePlayer() {
        this(6);
    }

    public int chooseMove() {
        int co = inBoard.newNodeAt();
        inBoard.addNode(co % 8, co / 8, NetworkGame.HUMAN);
        Move m = chooseOneMove();
        if(m!=null) return m.move;
      /*  m=chooseOneMove();
        if(m!=null) return m.move;*/
        m=chooseMove(NetworkGame.MACHINE);
        inBoard.addNode(m.move % 8, m.move / 8, NetworkGame.MACHINE);
        return m.move;
    }
    private Move chooseOneMove(){
        Move move=null;
        if(inBoard.Winner()!=NetworkGame.NONE) return null;
        for(int co=8;co<=55;co++){
            if(inBoard.isLegal(co%8, co/8, NetworkGame.MACHINE)){
            inBoard.addNode(co, NetworkGame.MACHINE);
            if(inBoard.Winner()==NetworkGame.MACHINE){
                move=new Move();
                move.move=co;
            inBoard.addNode(co, NetworkGame.NONE);
                break;
            }
            inBoard.addNode(co, NetworkGame.NONE);
        }
        }
        return move;
    }
    private void insertKillerMove(int ply, int co) {
        for(int i=0;i< killerMoves[ply].length;i++){
            killerMoves[ply][i]=co;
            return;
        }
        for (int i = killerMoves[ply].length - 2; i >= 0; i--) {
            killerMoves[ply][i + 1] = killerMoves[ply][i];
        }
        killerMoves[ply][0] = co;
    }
  // Returns a new move by "this" player.  Internally records the move (updates
    // the internal game board) as a move by "this" player.

    private Move chooseMove(int side) {
        /*if (inBoard.sizeOfH() >= 4) {
            sDepth = 4;
        }*/
        Move m= chooseMove(side, -4, 4, 1);
            return m;
    }

 
    private int[] move(int ply) {
        int length = killerMoves[ply].length;
        int[] arrayOfMove = new int[length + 64];
        for (int i = length; i < arrayOfMove.length; i++) {
            arrayOfMove[i] = i-length;
        }
        for (int cur = 0; cur < length; cur++) {
            if (killerMoves[ply][cur] != 0) {
                int killerMove = killerMoves[ply][cur];
                arrayOfMove[cur] = killerMove;
                arrayOfMove[killerMove + length] = 0;
            }
        }
        return arrayOfMove;
    }

    public float evaluationFunction() {
        float m = hCountLink();
        float n = mCountLink();
        resetCheck();
		//  System.out.println("m"+m);
        //  System.out.println("n"+n);
        return m + n;
    }

    private void resetCheck() {
        while (true) {
            int co = inBoard.nextNodeCo(NetworkGame.HUMAN);
            inBoard.isCheck[co] = false;
            if (co == NetworkGame.NONE) {
                break;
            }
        }
        while (true) {
            int co = inBoard.nextNodeCo(NetworkGame.MACHINE);
            inBoard.isCheck[co] = false;
            if (co == NetworkGame.NONE) {
                break;
            }
        }
    }

    public float hCountLink() {
        sum = 0;
        while (true) {
            int co = inBoard.nextNodeCo(NetworkGame.HUMAN);
            if (co == NetworkGame.NONE) {
                break;
            }
            //System.out.printf("Is x %d  y %d checked: %b",co%8,co/8,inBoard.isCheck[co]);
            if (!inBoard.isCheck[co]) {
                int firstNodeInGoal = inBoard.isInGoalArea(co);
                recurCount(co, NetworkGame.NONE, 1, false, false, firstNodeInGoal != NetworkGame.NONE, true);
            }
        float k=sum;
            // System.out.println("SUMH "+sum);
        }
        return -sum;
    }

    public float mCountLink() {
        sum = 0;
        while (true) {
            int co = inBoard.nextNodeCo(NetworkGame.MACHINE);
            if (co == 0) {
                break;
            }
            if (!inBoard.isCheck[co]) {
                int firstNodeInGoal = inBoard.isInGoalArea(co);
                recurCount(co, NetworkGame.NONE, 1, false, false, firstNodeInGoal != NetworkGame.NONE, false);
                // System.out.printf("co:  %d,sum: %.5f\n",co,sum);
            }
        }
        return sum;
    }

    public void recurCount(int co, int prevLink, int count, boolean goalOne, boolean goalTwo, boolean isFirstNodeAGoal, boolean human) {
        if (co == 0) {
            return;
        }
        int x = co % 8;
        int y = co / 8;
        
        inBoard.isCheck[co] = true;
        // return if connect a second goal node
        if (inBoard.isInGoalArea(co) == NetworkGame.Board.goalFirst) {
            goalOne = true;
        }
        if (inBoard.isInGoalArea(co) == NetworkGame.Board.goalSecond) {
            goalTwo = true;
        }
        int[] connectNode = inBoard.getConnectionAt(x, y);
        if (nodeRemain(connectNode, inBoard.inNet[co], goalOne, goalTwo, prevLink)) {
            connectNode = null;
        }
	 // System.out.println((connectNode==null&&(goalOne||goalTwo)));
        //one goal node in connection
        if ((connectNode == null && (goalOne || goalTwo))) {
           /* if (count == 1) {
                sum += 0.000001f;
                return;
            }*/
            if (count == 2) {
                sum += 0.00002f;
                return;
            } else if (count == 3) {
               /* if (human) {
                    sum += 0.000035f;
                } else {*/
                    sum += 0.00003f;
            
                return;   
            }
            else if (count == 4) {
              /*  if (human) {
                    sum += 0.0005f;
                } else {*/
                    sum += 0.0004f;
            
                return;       
            }else if (count >= 5) {
               /* if (human) {
                    sum += 0.08f;
                } else {*/
                    sum += 0.07f;
                return;
                }
            return;
        }
        // no goal node in connection
        if (connectNode == null) {
           /* if (count == 1) {
                sum += 0.000001f;
            }*/
            if (count == 2) {
                sum += 0.00002f;
            } else if (count == 3) {
                sum += 0.00003f;
            } else if (count >= 4) {
              /*  if (human) {
                    sum += 0.0035f;
                } else {*/
                    sum += 0.0003f;
            }
            return;
        }
        // two goal node in connection
        if (goalOne && goalTwo) {
        /*    if (count == 1) {
                sum += 0.000001f;
            }*/
            if (count == 2) {
                sum += 0.00002f;
            } else if (count == 3) {
                sum += 0.00003f;
            } else if (count == 4) {
                sum += 0.0003f;
            } else if (count == 5) {
              /*  if (human) {
                    sum += 0.08f;
                } else {*/
                    sum += 0.07f;
                }
             else if (count >= 6) {
                sum += 3f;
            }
            return;
        }
        //  System.out.println("length" +connectNode.length);
        int size = connectNode.length;
        for (int i = 0; i < size; i++) {
            int aco = connectNode[i];
            if (aco != 0 && inBoard.inNet[aco] != inBoard.inNet[co]) { 
                if (!((inBoard.isInGoalArea(aco) == NetworkGame.Board.goalFirst) && goalOne)
                        || !((inBoard.isInGoalArea(aco) == NetworkGame.Board.goalSecond) && goalTwo)) {
                    int direct = inBoard.direct(i);
                    if (prevLink != direct) {
                        inBoard.inNet[aco] = inBoard.inNet[co];
                        recurCount(connectNode[i], direct, count + 1, goalOne, goalTwo, isFirstNodeAGoal, human);
                        inBoard.inNet[aco] = aco;

                    }
                }
            }
        }
    }

    //if no node remain return true

    private boolean nodeRemain(int[] connectionArray, int link, boolean goalOne, boolean goalTwo, int prevLink) {
        int i = -1;
        int t1 = 0;
        int t2 = 0;
        if (connectionArray == null) {
            return true;
        }
        while (++i < connectionArray.length) {
            int co = connectionArray[i];
            /* System.out.printf("co%d: %d",i,co);
             System.out.printf("prevLink: %d",prevLink);*/
            if (co != 0) {
                if (inBoard.inNet[co] == link || inBoard.direct(i) == prevLink) {
                    t2++;
                }
                if ((inBoard.inNet[co] != link && inBoard.isInGoalArea(co) == NetworkGame.Board.goalFirst && goalOne)) {
                    t2++;
                    goalOne = false;
                }
                if ((inBoard.inNet[co] != link && inBoard.isInGoalArea(co) == NetworkGame.Board.goalFirst && goalTwo)) {
                    t2++;
                    goalTwo = false;
                }
            }
            if (connectionArray[i] != 0) {
                t1++;
            }
            /* System.out.println("t1 "+t1);
             System.out.println ("---------------------- ");*/
        }
        if ((t2 - t1) == 0) {
            return true;
        } else {
            return false;
        }
    }

    class Move {

        float score;
        int move;
    }
}
