import java.util.*;
class Node{
    int id, counter, burger, enterTime, state, orderTime ,waitTime,ht;
    Node leftChild, rightChild;

    Node(int ID, int b, int enter){
        id=ID;
        burger=b;
        enterTime= enter;
        state=0;
        ht=0;
    }
}

class Order{
    int id, quant, endTime, deliveryTime;
    Order(int ID, int q ){
        id=ID;
        quant= q;
    }
}

class AvlTree{
	Node avlroot;

	int hieght(Node n){
		if(n==null){
			return -1;
		}
		else{
			return n.ht;
		}
	}

	Node find(int key){
		Node temp= avlroot;
		while(temp!=null){
			if(key==temp.id){
				return temp;
			}
			else if(key>temp.id){
				temp=temp.rightChild;
			}
			else{
				temp=temp.leftChild;
			}
		}
		return null;	
	}

	Node update(Node n, Node nhier){
		if(n.id == nhier.id){
			return nhier;
		}
		else if(nhier.id>n.id){
			n.rightChild= update(n.rightChild,nhier);
			return n;
		}
		else{
			n.leftChild= update(n.leftChild, nhier);
			return n;
		}
		
	}

	Node leftRotate(Node z1){
		Node y1= z1.rightChild;
		Node t2= y1.leftChild;

		y1.leftChild= z1;
		z1.rightChild= t2;

		z1.ht= Math.max(hieght(z1.leftChild), hieght(z1.rightChild))+1;
		y1.ht= Math.max(hieght(y1.leftChild), hieght(y1.rightChild))+1;

		return y1;
	}

	Node rightRotate(Node z2){
		Node y2=z2.leftChild;
		Node t2= y2.rightChild;

		y2.rightChild=z2;
		z2.leftChild=t2;

		z2.ht=Math.max(hieght(z2.leftChild), hieght(z2.rightChild))+1;
		y2.ht=Math.max(hieght(y2.leftChild), hieght(y2.rightChild))+1;

		return y2;
	}

	Node insert(Node n, Node nhier){
		if (n==null){
			return nhier;
		}
		
		if(nhier.id< n.id){
			n.leftChild= insert(n.leftChild, nhier);
		}
		else if(nhier.id> n.id){
			n.rightChild= insert(n.rightChild, nhier);
		}
		

		n.ht= Math.max(hieght(n.leftChild),hieght(n.rightChild))+1;
		int hdiff= hieght(n.leftChild)- hieght(n.rightChild);

		if(hdiff>1 && nhier.id<n.leftChild.id){
			return rightRotate(n);
		}
		if(hdiff<-1 && nhier.id>n.rightChild.id){
			return leftRotate(n);
		}
		if(hdiff>1 && nhier.id>n.leftChild.id){
			n.leftChild= leftRotate(n.leftChild);
			return rightRotate(n);
		}
		if(hdiff<-1 && nhier.id<n.rightChild.id){
			n.rightChild= rightRotate(n.rightChild);
			return leftRotate(n);
		}

		return n;
	}

	Node nextMin(Node n){
		Node tmp= n;
		while(tmp.leftChild!=null){
			tmp=tmp.leftChild;
		}
		return tmp;
	}

    int waitSum(Node n){
        if(n==null){
            return 0;
        }
        else{
            return n.waitTime+ waitSum(n.leftChild)+ waitSum(n.rightChild);
        }
    }

}

class Queue{
    ArrayList<Node> queue= new ArrayList<Node>();
    int countId;
    int refIndex;
    Queue(int a, int index){
        countId=a;
        refIndex= index;
    }

    boolean isEmpty(){
        return queue.isEmpty();
    }

    int counterNum(){
        return countId;
    }

    void put(Node n){
        n.counter= countId;
        if(queue.isEmpty()){
            n.orderTime= n.enterTime+ countId;
        }
        else{
            n.orderTime= queue.get(queue.size()-1).orderTime+ countId;
        }
        queue.add(n);
    }

    Node remove(){
       return queue.remove(0);
    }

    Node first(){
        return queue.get(0);
    }

    int size(){
        return queue.size();
    }
    
}

class BillingHeap{
    Queue[] heap;
    Queue[] heapt;
    int num;

    BillingHeap(int a){
        num=a;
        heap= new Queue[num+1];
        heapt= new Queue[num+1];
        for(int j=1; j<=num;++j){
            heap[j]= new Queue(j,j);
            heapt[j]= new Queue(j,j);
        }
    }

    int comp(Queue q1, Queue q2){
        if(q1.size()<q2.size()){
            return -1;
        }
        else if(q1.size()>q2.size()){
            return 1;
        }
        else{
            if(q1.counterNum()<q2.counterNum()){
                return -1;
            }
            else{
                return 1;
            }
        }
    }

    int compt(Queue q1,Queue q2){
        if(q1.isEmpty() && q2.isEmpty()){
            if(q1.counterNum()<q2.counterNum()){
                return 1;
            }
            else{
                return -1;
            }
        }

        else if(q1.isEmpty()){
            return 1;
        }

        else if(q2.isEmpty()){
            return -1;
        }

        else if(q1.first().orderTime<q2.first().orderTime){
            return -1;
        }

        else if(q1.first().orderTime> q2.first().orderTime){
            return 1;
        }
        else{
            if(q1.counterNum()<q2.counterNum()){
                return 1;
            }
            else{
                return -1;
            }
        }
    }

    boolean isEmpty(){
        return heapt[1].isEmpty();
    }



    Node topt(){
        return heapt[1].first();
    }

    void rebuildDownAdd(){
        int i=1;
        while(2*i<=num){
            int smallIndex=2*i;
            if(2*i+1<=num && comp(heap[2*i], heap[2*i+1])>0){
                smallIndex= 2*i+1;
            }

            if(comp(heap[i], heap[smallIndex])>0){
                Queue tmp= heap[i];
                heap[i]=heap[smallIndex];
                heap[smallIndex]= tmp;
                heapt[heap[i].refIndex].refIndex= i;
                heapt[heap[smallIndex].refIndex].refIndex=smallIndex;
                i= smallIndex;
            }

            else{
                break;
            }
        }
    }

    void rebuildDownRemove(){
        int i=1;
        while(2*i<=num){
            int smallIndex=2*i;
            if(2*i+1<=num && compt(heapt[2*i], heapt[2*i+1])>0){
                smallIndex=2*i+1;
            }

            if(compt(heapt[i],heapt[smallIndex])>0){
                Queue tmp= heapt[i];
                heapt[i]=heapt[smallIndex];
                heapt[smallIndex]= tmp;
                heap[heapt[i].refIndex].refIndex= i;
                heap[heapt[smallIndex].refIndex].refIndex=smallIndex;
                i= smallIndex;
            }


            else{
                break;
            }
        }
    }

    void rebuildUpAdd(int i){
        while(i>1){
            if(compt(heapt[i], heapt[i/2])<0){
                Queue tmp= heapt[i];
                heapt[i]=heapt[i/2];
                heapt[i/2]= tmp;
                heap[heapt[i].refIndex].refIndex=i;
                heap[heapt[i/2].refIndex].refIndex=i/2;
                i=i/2;
            }
            else{
                break;
            }
        }
    }

    void rebuildUpRemove(int i){
        while(i>1){
            if(comp(heap[i], heap[i/2])<0){
                Queue tmp= heap[i];
                heap[i]=heap[i/2];
                heap[i/2]= tmp;
                heapt[heap[i].refIndex].refIndex=i;
                heapt[heap[i/2].refIndex].refIndex=i/2;
                i=i/2;
            }
            else{
                break;
            }
        }
    }

    void addCustomer(Node n){
        heap[1].put(n);
        heapt[heap[1].refIndex].put(n);
        rebuildUpAdd(heap[1].refIndex);
        rebuildDownAdd();
    }

    Node removeCustomer(){
        Node tmp=heapt[1].remove();
        heap[heapt[1].refIndex].remove();
        rebuildUpRemove(heapt[1].refIndex);
        rebuildDownRemove();
        return tmp;
    }

}



public class MMBurgers implements MMBurgersInterface {

    ArrayList<Order> griddle= new ArrayList<Order>();
    int griddleSize=0;
    ArrayList<Order> chefq= new ArrayList<Order>();
    int chefqSize=0;
    ArrayList<Order> deliver= new ArrayList<Order>();
    int k1, m1;
    int clock=0;
    int prevTime=0;
    AvlTree avlt = new AvlTree();
    int avlSize=0;
    BillingHeap bh;


    public boolean isEmpty(){
        //your implementation
	    return (bh.isEmpty() && griddle.isEmpty() && chefq.isEmpty());	
    } 
    
    public void setK(int k) throws IllegalNumberException{
        //your implementation
	    if(k<=0){
            throw new IllegalNumberException("Number is illegal");
        }
        else{
            k1=k;
            bh= new BillingHeap(k);
            // System.out.println("k set");
            // System.out.println("----------");
        }
    }   
    
    public void setM(int m) throws IllegalNumberException{
        //your implementation
        if(m<=0){
            throw new IllegalNumberException("Number is illegal");
        }
	    else{
            m1=m;
            // System.out.println("m set");
            // System.out.println("----------");
        }	
    } 

    public void advanceTime(int t) throws IllegalNumberException{
        //your implementation
        if(t<clock){
            throw new IllegalNumberException("Number is illegal");
        }
        else{
            prevTime=clock;
            clock=t;
	        simulate(prevTime, clock);
            // System.out.println("time advanded to "+t);
            // System.out.println("----------");
        }	
    } 

    public void arriveCustomer(int id, int t, int numb) throws IllegalNumberException{
        //your implementation
	    if(t<clock || numb<=0){
            throw new IllegalNumberException("Number is illegal");
        }
        else{
            prevTime=clock;
            clock=t;
            simulate(prevTime, clock);
            Node tmp= new Node(id, numb, t);
            bh.addCustomer(tmp);
            avlt.avlroot=avlt.insert(avlt.avlroot, tmp);
            avlSize= avlSize+1;
            // System.out.println("customer "+id+" arrived");
            // System.out.println("----------");
        }
    } 

    public int customerState(int id, int t) throws IllegalNumberException{
        //your implementation
	    if(t<clock){
            throw new IllegalNumberException("Number is illegal");
        }
        else{
            prevTime=clock;
            clock=t;
            simulate(prevTime, clock);
            Node tmp= avlt.find(id);
            if(tmp==null){
                return 0;
            }
            else{
                if(tmp.state==0){
                    return tmp.counter;
                }
                else{
                return k1+tmp.state;
                }
                
            }
        }
    } 

    public int griddleState(int t) throws IllegalNumberException{
        //your implementation
	    if(t<clock){
            throw new IllegalNumberException("Number is illegal");
        }	
        else{
            prevTime=clock;
            clock=t;
            simulate(prevTime,clock);
            // System.out.println("State(size) of griddle found: "+griddleSize);
            // System.out.println("----------");
            return griddleSize;
        }
    } 

    public int griddleWait(int t) throws IllegalNumberException{
        //your implementation
	    if(t<clock){
            throw new IllegalNumberException("Number is illegal");
        }	
        else{
            prevTime=clock;
            clock=t;
            simulate(prevTime, clock);
            // System.out.println("griddle wait found: "+chefqSize);
            // System.out.println("----------");
            return chefqSize;

        }
    } 

    public int customerWaitTime(int id) throws IllegalNumberException{
        //your implementation
	    Node tmp= avlt.find(id);
        if(tmp==null){
            throw new IllegalNumberException("Number is illegal");
        }
        else{
            // System.out.println("customer wait time found: "+ tmp.waitTime);
            // System.out.println("----------");
            return tmp.waitTime;
        }
    } 

	public float avgWaitTime(){
        //your implementation
        // float tmp= (float)avlt.waitSum(avlt.avlroot)/avlSize;
        // System.out.println("average wait time is: "+tmp);
	    return (float)avlt.waitSum(avlt.avlroot)/avlSize;
    } 

    private void simulate(int start, int end){
        for(int time=start; time<=end; ++time){
            while(!bh.isEmpty() && bh.topt().orderTime== time){
                Node tmp= bh.removeCustomer();
                tmp.state=1;
                chefq.add(new Order(tmp.id, tmp.burger));
                chefqSize=chefqSize+ tmp.burger;
                avlt.avlroot=avlt.update(avlt.avlroot, tmp);
                // System.out.println("order of customer "+tmp.id+" has been sent to chef");
            }

            while(griddleSize!=0 && griddle.get(0).endTime==time){
                Order tmp= griddle.remove(0);
                griddleSize= griddleSize- tmp.quant;
                tmp.deliveryTime=time+1;
                deliver.add(tmp);
                // System.out.println(tmp.quant+" burgers of "+tmp.id+" have been removed from griddle");
                
            }

            while(griddleSize<m1 && chefqSize!=0){
                Order tmp= chefq.get(0);
                if(griddleSize+tmp.quant>m1){
                    Order tmp1=new Order(tmp.id, m1- griddleSize);
                    // int temp= m1-griddleSize; /**/
                    tmp1.endTime=time+10;
                    griddle.add(tmp1);
                    chefq.set(0, new Order(tmp.id, tmp.quant-(m1-griddleSize)));
                    chefqSize=chefqSize-(m1-griddleSize);
                    griddleSize=m1;
                    // System.out.println("chef puts "+temp+" burgers of customer "+tmp.id+" on gridddle");
                }
                else{
                    Order tmp2= chefq.remove(0);
                    tmp2.endTime= time+10;
                    griddle.add(tmp2);
                    chefqSize=chefqSize-tmp2.quant;
                    griddleSize=griddleSize+tmp2.quant;
                    // System.out.println("chef puts "+tmp2.quant+" burgers of customer "+tmp2.id+" on griddle");
                }
            }

            while(!deliver.isEmpty() && deliver.get(0).deliveryTime==time){
                Order tmp= deliver.remove(0);
                Node tmp1= avlt.find(tmp.id);
                tmp1.burger= tmp1.burger- tmp.quant;
                // System.out.println(tmp.quant+" burgers of "+tmp.id+" have been delivered");
                if(tmp1.burger==0){
                    tmp1.state=2;
                    tmp1.waitTime=time-tmp1.enterTime;
                    // System.out.println("customer "+tmp1.id+" has left");
                }
                avlt.avlroot= avlt.update(avlt.avlroot, tmp1);
            }

        }
    }

    
}
