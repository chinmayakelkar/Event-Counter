import java.util.*;
import java.io.*;
public class bbst {
	public Node root; //RB Tree root
	private static final boolean RED   = true;
    private static final boolean BLACK = false;
    private static bbst st = new bbst();
    private class Node{
    	private int key;           // key
        private int val;         // value in the node
        private Node left, right;  // links to left and right subtrees
        private boolean color;     // color of parent link
        //constructor
        public Node(int key, int val, boolean color) {
            this.key = key;
            this.val = val;
            this.color = color;
        }
       
    }
    public bbst()
    {
    	//bbst constructor
    }
    //method to check if a node is red
    private boolean checkIfRed(Node a)
    {
    	if(a==null) return false;
    	return a.color==RED;
    }
    //method to check if tree is empty
    public boolean isEmpty()
    {
    	return root==null;
    }
    public int compare(int a, int b)
    {
    	if(a>b)
    		return 1;
    	if(a==b)
    		return 0;
    	return -1;
    }
  //public get function to find key in tree and return
    public int get(int key)
    {
    	return get(root,key);    		
    }
    private int get(Node a, int key)
    {
    	while(a!=null)
    	{
    		int Compare = compare(key,a.key);
            if(Compare < 0) a = a.left; //recurse on left tree
            else if (Compare > 0) a = a.right; // recurse on right tree
            else              return a.val;
        }
        return -1; // if not found return -1
    	}
    //public method to update value at a key
    public int update(int key, int value)
    {
    	return update(root,key,value);
    }
    private int update(Node a, int key,int value)
    {
    	while(a!=null)
    	{
    		int Compare = compare(key,a.key);
            if(Compare < 0) a = a.left; //recurse on left tree
            else if (Compare > 0) a = a.right; // recurse on right tree
            else  
            	{
            		a.val=value;
            		return a.val;
            	}
        }
        return -1; // if not found return -1
    }
    //Find if a node is present in the tree if not found return false
    public boolean contains (int key)
    {
    	return get(key)!=-1;
    }
    
    //add a node in the tree
    public void add(int key, int val) {
        root = add(root, key, val);
        root.color = BLACK;
    }
    //recursive add takes care of rb tree props on the way up
    private Node add(Node a, int key, int value)
    {
    	if (a == null) return new Node(key, value, RED);
    	int Cmpare=compare(key,a.key);
    	if (Cmpare<0) a.left=add(a.left,key,value); //recurse on left tree
    	else if(Cmpare >0)a.right=add(a.right,key,value); //recurse on right tree
    	else a.val=value;
    	if(checkIfRed (a.right) && !checkIfRed(a.left)) a=rotateLeft(a);
    	if(checkIfRed (a.left) && checkIfRed(a.left.left)) a=rotateRight(a);
    	if(checkIfRed (a.left)&& checkIfRed(a.right)) flipColor(a);
    	return a;
    }
    
    //private method to rotate right
    private Node rotateRight(Node a) {
        
        Node temp = a.left;
        a.left = temp.right;
        temp.right = a;
        temp.color = temp.right.color;
        temp.right.color = RED;
        return temp;
    }
    //private method to rotate left
    private Node rotateLeft(Node a){
    	Node temp = a.right;
        a.right = temp.left;
        temp.left = a;
        temp.color = temp.left.color;
        temp.left.color = RED;
        return temp;
    }
    //private method to change color of node and left and right child
    private void flipColor(Node a)
    {
    	a.color=!a.color; //change color of Node a
    	a.left.color=!a.left.color; //flip color of left node
    	a.right.color=!a.right.color; //flip color of right node
    }
    //public method to delete a node
    public void delete (int key)
    {
        if (!contains(key)) return; //key not in tree to delete
        // if both children of root are black, set root to red
        if (!checkIfRed(root.left) && !checkIfRed(root.right))
            root.color = RED;
        root = delete(root, key);
        //if tree not empty set root to black again
        if (!isEmpty()) root.color = BLACK;
    }
    //private delete method
    private Node delete(Node a, int key) {
        if (compare(key,a.key) < 0)  {
            if (!checkIfRed(a.left) && !checkIfRed(a.left.left))
                a = turnRedLeft(a);
            a.left = delete(a.left, key);
        }
        else {
            if (checkIfRed(a.left))
                a = rotateRight(a);
            if (compare(key,a.key) == 0 && (a.right == null))
                return null;
            if (!checkIfRed(a.right) && !checkIfRed(a.right.left))
                a = turnRedRight(a);
            if (compare(key,a.key) == 0) {
                Node x = minimum(a.right);
                a.key = x.key;
                a.val = x.val;
                a.right = deleteMinimum(a.right);
            }
            else a.right = delete(a.right, key);
        }
        return restore(a); //restores balance of the tree on the way up
    }
    
    private Node turnRedLeft(Node a) {
        flipColor(a);
        if (checkIfRed(a.right.left)) { 
            a.right = rotateRight(a.right);
            a = rotateLeft(a);
            flipColor(a);
        }
        return a;
    }
    
    private Node turnRedRight(Node a) {
        flipColor(a);
        if (checkIfRed(a.left.left)) { 
            a = rotateRight(a);
            flipColor(a);
        }
        return a;
    }
    
    private Node restore(Node a) {

        if (checkIfRed(a.right)) a = rotateLeft(a);
        if (checkIfRed(a.left) && checkIfRed(a.left.left)) a = rotateRight(a);
        if (checkIfRed(a.left) && checkIfRed(a.right))     flipColor(a);
        return a;
    }
    //public method to find minimum node in the tree
    public int minimum()
    {
    	if (isEmpty()) throw new NoSuchElementException("tree is empty");
        return minimum(root).key;
    }
    //find the leftmost node or minimum node in the tree
    private Node minimum(Node temp)
    {
    	if(temp.left==null) return temp;
    	else return minimum(temp.left);
    }
    //public method to delete the minimum element
    public void deleteMinimum()
    {
    	if (isEmpty()) throw new NoSuchElementException("RBTree is empty");
        // if both children of root are black, set root to red
        if (!checkIfRed(root.left) && !checkIfRed(root.right))
            root.color = RED;

        root = deleteMinimum(root);
        if (!isEmpty()) root.color = BLACK;
    }
    private Node deleteMinimum(Node a)
    {
    	 if (a.left == null)
             return null;
    	 //maintain the invariant traversing down the tree
         if (!checkIfRed(a.left) && !checkIfRed(a.left.left))
             a = turnRedLeft(a);

         a.left = deleteMinimum(a.left);
         //restore on the way up by calling restore
         return restore(a);
    }
    //add keys in the range lo,hi to the queue
    public Iterable<Integer> keys(int lo, int hi) {
        Queue<Integer> queue = new LinkedList<Integer>();
        keys(root, queue, lo, hi);
        return queue;
    } 

    // add the keys between lo and hi in the subtree rooted at a to the queue
    private void keys(Node a, Queue<Integer> queue, int lo, int hi) { 
        if (a == null) return; 
        int cmplo = compare(lo,a.key); 
        int cmphi = compare(hi,a.key); 
        if (cmplo < 0) keys(a.left, queue, lo, hi); 
        if (cmplo <= 0 && cmphi >= 0) queue.add(a.key); 
        if (cmphi > 0) keys(a.right, queue, lo, hi); 
    } 
    //returns the node with key less than given key. key may not be present in the tree
    public int Previous(int key) {
        if (isEmpty()) throw new NoSuchElementException("no element in the tree");
        Node a = Previous(root, key);
        if (a == null) return 0; //Print "0 0" if this returns null
        else
        	{
        	 int x=(Integer)a.key;
        	 return x;
        	}
    }    
    // the largest key in the subtree rooted at a less than given key
    private Node Previous(Node a, int key) {
        if (a == null) 
        	return null;
        int Cmpare = compare(key,a.key);
        if(Cmpare==0)
        {	if(a.left!=null)
        	{
        	Node temp=a.left;
        	while(temp.right!=null)
        		temp=temp.right;
        	return temp;
        }
        }
        	Node pred=null;
        	while(a!=null)
        	{	
        		if(compare(key,a.key) >0)
        		{
        			pred=a;
        			a=a.right;
        		}
        		else if (compare(key,a.key) < 0)  
        			a=a.left;
        		else
        			break;
        	}
        	if(a!=null)
        	{	int Cmpare2=compare(key,a.key);
        		if(Cmpare2==0)
        		{	if(a.left!=null)
        			{
        				Node temp=a.left;
        				while(temp.right!=null)
        						temp=temp.right;
        				return temp;
        		}
        	}
        	}
        return pred;
 }
    
 //returns node with smallest key greater than given key
  public int Next(int key) {
        if (isEmpty()) throw new NoSuchElementException("RB tree is empty");
        Node x = Next(root, key);
        if (x == null) return 0;
        else {
        	int y=(Integer)x.key;
        	return y;
        }
    }
    private Node Next(Node a, int key)
    {    
    	if (a == null) return null;
        int Cmpare = compare(key,a.key);
        if(Cmpare==0)
        {	if(a.right!=null)
        	{
        	Node temp=a.right;
        	while(temp.left!=null)
        		temp=temp.left;
        	return temp;
        }
        }
        	Node succ=null;
        	while(a!=null)
        	{	
        		if(compare(key,a.key) <0)
        		{
        			succ=a;
        			a=a.left;
        		}
        		else if (compare(key,a.key) > 0)  
        			a=a.right;
        		else
        			break;
        	}
        	if(a!=null)
        	{	int Cmpare2=compare(key,a.key);
        		if(Cmpare2==0)
        		{	if(a.right!=null)
        			{
        				Node temp=a.right;
        				while(temp.left!=null)
        						temp=temp.left;
        				return temp;
        		}
        	}
        	}
        	return succ;
    }
  
    public void BuildTree(int EventIds[], int CountIds[], int EvenT)
    {
    	if(EventIds==null || CountIds==null)
    		return;
    	double n=EventIds.length;
    	//calculate height of the tree
    	n=Math.ceil(Math.log(n)/Math.log(2));
    	root=BuildTree(EventIds,CountIds,0,EvenT,n);
    	root.color=BLACK;
    }
   
    //build tree from a sorted array
    private Node BuildTree(int EventIds[], int CountIds[], int start, int end, double n)
    {
    	if(start > end)
    		return null;
    	int mid=(start+end)/2;
    	double i=n;
    	Node a=new Node(EventIds[mid],CountIds[mid], BLACK);
    	i=i-1;
    	//at height n color the leaves red
    	if(i==0)
    		a.color=RED;
    	a.left=BuildTree(EventIds,CountIds,start,mid-1,i);
    	a.right=BuildTree(EventIds,CountIds,mid+1,end,i);
    	return a; 
    }
    public static void main(String[] args) { 
    	
    	String fname;
    	File file=null;
    	if(args.length>0)
    	{	
    		fname=args[0];
    	try{
    		file=new File(fname);
    		BufferedReader br = new BufferedReader(new FileReader(file));
    		String num=br.readLine();
    		int totalE=Integer.parseInt(num);
    		String line=null;
    		int []EventIds=new int[totalE];
    		int []CountIds=new int[totalE];
    		int i=0,j=0;
    		while((line = br.readLine()) != null)
    		{
    			String []nums=line.split("\\s+");
    			EventIds[i++]=Integer.parseInt(nums[0].trim());
    			CountIds[j++]=Integer.parseInt(nums[1].trim());
    		}
    		
    		st.BuildTree(EventIds, CountIds,totalE-1);
        	
    		br.close();
    	}catch (Exception e) {
			e.printStackTrace();
		}
    	}
    	else
    	{
    		System.err.println("Invalid arguments count:" + args.length);
    	}   	
    	try{
    		//File commandsfile=new File("commands.txt");
    	Scanner in=new Scanner(System.in);
    	while(in.hasNextLine())
    	{
    		String command=in.nextLine();
    		String []commands=command.split("\\s+");
    		int par1=0, par2=0;
    		if(commands[0].equals("quit"))
    			{
    				System.exit(1);
    			}
    		
    		String exec=commands[0];
    		if(exec.equals("increase") || exec.equals("reduce") || exec.equals("inrange"))
    		{
    			par1=Integer.parseInt(commands[1].trim());
    			par2=Integer.parseInt(commands[2].trim());
    			if(exec.equals("increase"))
    	    	{
    	    		int m=par1,n=par2;
    	        	if(!st.contains(m))
    	        		{     
    	        			st.add(m,n);
    	        			System.out.println(n);
    	        		}
    	        	else
    	        		{	
    	        			int k=st.get(m);
    	        			k=k+n;
    	        			k=st.update(m,k);
    	        			System.out.println(k);
    	        		}
    	    	}
    			if(exec.equals("reduce"))
    	    	{
    	    		int m=par1, n=par2;
    	    		if(!st.contains(m))
    	    			System.out.println(0);
    	    		else
    	    		{
    	    			int k=st.get(m);
    	    			k=k-n;
    	    			if(k<=0)
    	    			{
    	    				st.delete(m);
    	    				System.out.println(0);
    	    			}
    	    			else
    	    			{
    	    				k=st.update(m,k);
    	    				System.out.println(k);
    	    			}
    	    		}		
    	    	}
    			if(exec.equals("inrange"))
    	    	{
    	    		int m=par1, n=par2;
    	    		int total=0;
    	    		for(int k:st.keys(m, n))
    	    			total+=st.get(k);
    	    		System.out.println(total);
    	    	}
    	    	
    		}
    		else
    		{
    			par1=Integer.parseInt(commands[1].trim());
    			if(exec.equals("count"))
    	    	{
    	    		int m=par1;
    	    		if(!st.contains(m))
    	    			System.out.println(0);
    	    		else
    	    			System.out.println(st.get(m));
    	    	}
    			if(exec.equals("next"))
    			{
    				int m=par1;
    		    	int succ=st.Next(m);
    		    	if(succ!=0)
    		    		System.out.println(succ + " "+ st.get(succ));
    		    	else
    		    		System.out.println("0 0");	
    			}
    			if(exec.equals("previous"))
    			{
    				int m=par1;
    				int prev=st.Previous(m);
    		    	if(prev!=0)
    		    		System.out.println(prev+ " "+st.get(prev));
    		    	else
    		    		System.out.println("0 0");
    			}
    		}
    	}
    	//close the scanner
    	in.close();
    	}catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}

    }

}