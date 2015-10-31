package AssessmentOne;

import edu.princeton.cs.introcs.StdIn;
import edu.princeton.cs.introcs.StdOut;

public class WeightedQuickUnionUF 
{
	private int[] id;    // id[i] = component identifier of i
	private int[] size;
    private int count;   // number of components

    /**
     * Reads in a sequence of pairs of integers (between 0 and N-1) from standard input, 
     * where each integer represents some site;
     * if the sites are in different components, merge the two components
     * and print the pair to standard output.10
     * 
     */
    public static void main(String[] args) {
        StdOut.println("Enter the number of 'sites':");
        int N = StdIn.readInt();
        WeightedQuickUnionUF uf = new WeightedQuickUnionUF(N);
        StdOut.println("Input pairs of integers:");
        while (!StdIn.isEmpty()) 
        {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            if (uf.connected(p, q)) continue;
            uf.union(p, q);
            StdOut.println(p + " " + q);
            StdOut.println(uf.count() + " components");
        }
    }
    
    /**
     * Initializes an empty union-find data structure with N sites
     */
    public WeightedQuickUnionUF(int N) 
    {
    	size = new int[N];
    	id = new int[N];
    	count = N;
    	for (int i = 0; i < id.length; i++) 
    	{
    		size[i] = i;
    		id[i] = i;
    	}
    }

    /**
     * Returns the number of components.
     */
    public int count() {
        
        return count;
    }
    
    public int size()
    {
    	return size.length;
    }

    /**
     * Returns the component identifier for the component containing site p.
     */
    public int find(int p) {
        
        return 0;
    }

    /**
     * Returns true if the the two sites are in the same component.
     */
    public boolean connected(int p, int q) 
    {       
        return root(p) == root(q);
    }

    /**
     * Merges the component containing site p with the 
     * the component containing site q.
     */
    public void union(int p, int q) 
    {
    	if (!connected(p,q)) 
    	{
		
    		int pRoot = root(p), qRoot = root(q);
    		if (size[pRoot]<size[qRoot])
    		{
    			id[pRoot] = qRoot;
    			size[pRoot] += size[qRoot];
    			count--;
    		}
    		else 
    		{
    			id[qRoot] = pRoot;
    			size[qRoot] += size[pRoot];
    			count--;
    		}
    	}
    }

	private int root(int i) {
		while (i != id[i]) 
		{
			 i = id[i];
		}
		return i;
	}
}
