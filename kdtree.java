import java.util.*;
import java.io.*; 
import java.lang.Math;
import java.util.Arrays;

class Pair<A, B>{
	public A First;
	public B Second;
	public Pair(A _first, B _second) {
        this.First = _first;
        this.Second = _second;
    }
    public Pair(){
    	
    }
    public A get_first() {
        return First;
    }
    public B get_second() {
        return Second;
    }
}

class TreeNode{
    public TreeNode parent ;
    public TreeNode left;
    public TreeNode right;
    public List<Pair<Integer,Integer>> val;
    public int numberLeaves;
    public boolean isLeaf;
    public int xmax;
    public int xmin;
    public int ymin;
    public int ymax;
    public int axis;
}

public class kdtree{
    // Let the points(latitude , longitude) be stored in an array of name arr

    public TreeNode rootnode;

    public String Build(List<Pair<Integer,Integer>> documents){
        // Implement your code here

        int n = documents.size();
        documents.sort((l1, l2) -> l1.get_first().compareTo(l2.get_first()));
        TreeNode nd = new TreeNode();
        nd.val = documents;
        nd.isLeaf = false;
        rootnode = nd;
        nd.parent = null;
        Pair<TreeNode,TreeNode> children = GetChildren(nd);
        nd.left = children.get_first();
        nd.right = children.get_second();
        nd.axis = 1;
        nd.numberLeaves = nd.left.numberLeaves + nd.right.numberLeaves;
        nd.xmin = Math.min(nd.left.xmin,  nd.right.xmin);
        nd.xmax = Math.max(nd.left.xmax , nd.right.xmax);
        nd.ymin = Math.min(nd.left.ymin , nd.right.ymin);
        nd.ymax = Math.max(nd.left.ymax , nd.right.ymax);
        
        return "";
    }

    public Pair<TreeNode,TreeNode> GetChildren(TreeNode curr){
        if(curr.val.size()> 1){
            List<Pair<Integer,Integer>> arr = curr.val;
            if(curr.axis == 1){
                curr.val.sort((l1, l2) -> l1.get_second().compareTo(l2.get_second()));
            }
            else{
                curr.val.sort((l1, l2) -> l1.get_first().compareTo(l2.get_first()));
            }
            int s = arr.size();

            int br = 0;
            if(s % 2 == 0){
                br = (int)(s/2);
            }
            else{
                br = (int)((s+1)/2);
            }
            TreeNode l = new TreeNode();
            TreeNode r = new TreeNode();
            l.parent = curr;
            r.parent = curr;
            curr.left = l;
            curr.right = r;
            l.isLeaf = false;
            r.isLeaf = false;
            l.numberLeaves = 1;
            r.numberLeaves = 1;
            l.axis = l.parent.axis * -1;
            r.axis = r.parent.axis * -1;
            

            List<Pair<Integer,Integer>> left_arr = arr.subList(0,br);
            List<Pair<Integer,Integer>> right_arr = arr.subList(br,s);
            l.val = left_arr;
            r.val = right_arr;

            Pair<TreeNode,TreeNode> left_children = GetChildren(l);
            l.left = left_children.get_first();
            l.right = left_children.get_second();
            if(l.left != null && l.right != null){
                l.numberLeaves = l.left.numberLeaves + l.right.numberLeaves;
                
                l.xmin = Math.min(l.left.xmin,l.right.xmin);
                l.xmax = Math.max(l.left.xmax , l.right.xmax);
                l.ymin = Math.min(l.left.ymin , l.right.ymin);
                l.ymax = Math.max(l.left.ymax , l.right.ymax);
            }            

            Pair<TreeNode,TreeNode> right_children = GetChildren(r);
            r.left = right_children.get_first();
            r.right = right_children.get_second();
            if(r.left != null & r.right != null){
                r.numberLeaves = r.left.numberLeaves + r.right.numberLeaves;
                
                r.xmin = Math.min(r.left.xmin,r.right.xmin);
                r.xmax = Math.max(r.left.xmax , r.right.xmax);
                r.ymin = Math.min(r.left.ymin , r.right.ymin);
                r.ymax = Math.max(r.left.ymax , r.right.ymax);
            }
            Pair<TreeNode,TreeNode> pr = new Pair<TreeNode,TreeNode>(l,r);
            return pr;
        }
        else{
            curr.isLeaf = true;
            curr.xmin = curr.val.get(0).get_first();
            curr.xmax = curr.xmin;
            curr.ymin = curr.val.get(0).get_second();
            curr.ymax = curr.ymin;
            Pair<TreeNode,TreeNode> pr = new Pair<TreeNode,TreeNode>(null,null);
            return pr;
        }
        
    }

    public static int check(Pair<Integer,Integer> pr,TreeNode curr){
        //Implement your code here

        int x = pr.get_first();
        int y = pr.get_second();
        int x_min = x - 100;
        int x_max = x + 100;
        int y_min = y - 100;
        int y_max = y + 100;

        if(curr == null){
            return 0;
        }
        else{
            if(curr.xmin >= x_min && curr.xmax <= x_max && curr.ymin >= y_min && curr.ymax <= y_max){
                return curr.numberLeaves;
            }
            else{
                int ans = check(pr,curr.left) + check(pr ,curr.right);
                return ans;
            }
        }
    }

    public static void main(String[] args){
        List<Pair<Integer,Integer>> documents = new ArrayList<Pair<Integer,Integer>>();
        try{
            File file=new File("restaurants.txt");
            FileReader fr=new FileReader(file);
            BufferedReader br=new BufferedReader(fr);
            StringBuffer sb=new StringBuffer();
            String line;
            line = br.readLine();  
            while((line=br.readLine())!=null){
                String l = line;
                String[] res = l.split("[,]" , 0);
                Pair<Integer,Integer> xy = new Pair<Integer,Integer>(Integer.valueOf(res[0]),Integer.valueOf(res[1])); 
                documents.add(xy);
            }
            fr.close();   
        }
        catch(IOException e){
            e.printStackTrace();
        }

        // System.out.println("building a tree!");
        
        kdtree tree = new kdtree();
        String summary = tree.Build(documents);
        // System.out.println("Tree Built!");

        // System.out.println("checking for no. of restaurants around!");

        List<Pair<Integer,Integer>> search = new ArrayList<Pair<Integer,Integer>>();
        try{
            File file=new File("queries.txt");
            FileReader fr=new FileReader(file);
            BufferedReader br=new BufferedReader(fr);
            StringBuffer sb=new StringBuffer();
            String line;
            line = br.readLine();  
            while((line=br.readLine())!=null){
                String l = line;
                String[] res = l.split("[,]" , 0);
                Pair<Integer,Integer> pin = new Pair<Integer,Integer>(Integer.valueOf(res[0]),Integer.valueOf(res[1])); 
                search.add(pin);
            }
            fr.close();   
        }
        catch(IOException e){
            e.printStackTrace();
        }

        int len = search.size();
        for (int i = 0 ; i < len ; i++){
            Pair<Integer,Integer> pin = search.get(i);
            int ans = check(pin , tree.rootnode);
            System.out.println(ans);
        }        
    }   
}