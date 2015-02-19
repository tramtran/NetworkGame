/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;


public class SeparateChainingHash {
    private int size;
    private int nOfBuckets;
    private Bucket[] bucket;
    public static final int EXACT=0;
    public static final int UPPERBOUND=1;
    public static final int LOWERBOUND=-1;
    public static final int NONE=-3;
    public SeparateChainingHash(int size){
        this.size=size;
        bucket=new Bucket[size];
    }
    public void put(Bucket b){
        put(b.key,b.score,b.flag,b.remainPly);
    }
    public void put(long key,float score,int flag,int remainPly){
     //   System.out.println("key "+key );
       int index= hash(key);
     //  System.out.println("index "+index);
      Bucket temp=bucket[index];
       if(temp==null) {
           bucket[index]=new Bucket(key,score,flag,remainPly);
           return;
       }
       Bucket preTemp=null;
       while(true){
           if(temp.key==key) {
               if(preTemp==null){
               preTemp=new Bucket(key,score,flag,remainPly);
               preTemp.next=temp.next;
               bucket[index]=preTemp;
               temp=null;
               }else{
               preTemp.next=new Bucket(key,score,flag,remainPly);
               preTemp.next.next=temp.next;
               temp=null;
               }
               
               return;
           }
           if(temp.next==null) {
               temp.next=new Bucket(key,score,flag,remainPly);
               break;
           }
           preTemp=temp;
           temp=temp.next;
       }
       nOfBuckets++;
      /* if(nOfBuckets>=size*0.75){
           transferBuckets(new Bucket[size*2]);
       }*/
    }
    private void transferBuckets(Bucket[] newBucket){
        for(int index=0;index<bucket.length;index++){
            Bucket temp=bucket[index];
            if(temp!=null){
            while(true){
            long key=temp.key;
            int flag=temp.flag;
            float score=temp.score;
            int remainPly=temp.remainPly;
            int  nIndex=(int)(key% newBucket.length);
            newBucket[nIndex]=new Bucket(key,score,flag,remainPly);
            if(temp.next==null) break;
            temp=temp.next;
            }
            }
        }
        bucket=newBucket;
        size=bucket.length;
    }
    public int hash(long key){
        return (int)(key % (size));
    }
    public Bucket get(long key){
       int index= hash(key);
       Bucket cur= bucket[index];
       if(cur==null) {
           return null;
       }
       while(true){
       if(cur.key==key){
      // System.out.println("gethere2 ");
           Bucket temp=new Bucket(cur.key,cur.score,cur.flag,cur.remainPly);
           return temp;
       }
       if(cur.next==null){
           break;
       }
       cur=cur.next;
       }
       return null;
    }
 class Bucket{
   //     Network.Board board;
       private long key;
       private float score;
       private int flag;
       private Bucket next;
       private int remainPly;
       private int bestMove;
        public Bucket(long k,float s,int f,int rPly){
            key=k;
            score=s;
            flag=f;
            remainPly=rPly;
        }
        public Bucket(){
            
        }
        public float getScore(){
            return score;
        }
        public int getBestMove(){
            return bestMove;
        }
        public int getFlag(){
            return flag;
        }
        public int getRemainPly(){
            return remainPly;
        }
        public void setScore(float s){
            score=s;
        }
        public void setFlag(int f){
            flag=f;
        }
        public void setBestMove(int co){
            bestMove=co;
        }
        public void setRemainPly(int r){
            remainPly=r;
        }
        public void setKey(long k){
            key=k;
        }
    }  
}
