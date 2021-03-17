import java.util.*;
import java.util.stream.Stream;

public class ReachPrinceByBowserBFS {
    public List<String>  findPath(Node[][] gridN,int sourceRow,int sourceCol,int targetRow,int targetCol) {


        int h = gridN.length;
        if (h == 0)
            return null;
        int l = gridN[0].length;

        Set<Node> visited=new HashSet<>();

        Queue<Node> queue = new LinkedList<>();
        Node source=gridN[sourceRow][sourceCol];
        queue.add(source);

        Node x=null;
        boolean sourceReached=false;
        while (queue.isEmpty() == false) {

            x = queue.remove();
            int row = x.row;
            int col = x.col;

            if (x.visited)
                continue;

            visited.add(x);
            x.visited=true;
            if(x.row==targetRow&&x.col==targetCol)
            {
                sourceReached=true;
                break;
            }


            Node left=addAdjucents(row,col,h,l,gridN,"LEFT",queue);
            Node right=addAdjucents(row,col,h,l,gridN,"RIGHT",queue);
            Node up=addAdjucents(row,col,h,l,gridN,"UP",queue);
            Node down=addAdjucents(row,col,h,l,gridN,"DOWN",queue);

            if(left!=null)
            {
                left.parent=x;
                left.distance=x.distance+1;
                queue.add(left);
            }
            if(right!=null)
            {
                right.parent=x;
                right.distance=x.distance+1;
                queue.add(right);
            }
            if(up!=null)
            {
                up.parent=x;
                up.distance=x.distance+1;
                queue.add(up);
            }
            if(down!=null)
            {
                down.parent=x;
                down.distance=x.distance+1;
                queue.add(down);
            }
        }
        if(!sourceReached)
        {
            return null;
        }
        List<String> directions=new ArrayList<>();
        printPath(source,x,directions);
        return directions;
    }



    public Node addAdjucents(int row,int col,int h,int l,Node[][] gridN,String direction,Queue queue )
    {
        int newRow=-1;
        int newCol=-1;
        if(direction.equalsIgnoreCase("LEFT"))
        {
            newRow=row;
            newCol=(col - 1);
        }else if(direction.equalsIgnoreCase("RIGHT"))
        {
            newRow=row;
            newCol=(col + 1);
        }else if(direction.equalsIgnoreCase("UP"))
        {
            newRow=(row-1);
            newCol=col ;
        }else if(direction.equalsIgnoreCase("DOWN"))
        {
            newRow=(row+1);
            newCol=col ;
        }
        Node nd=null;
        if (newRow >= 0 && newCol >= 0 && newRow < h && newCol < l &&!gridN[newRow][newCol].visited && gridN[newRow][newCol].val!='*' && !queue.contains(gridN[newRow][newCol]))
        {
            nd=gridN[newRow][newCol];
            nd.direction=direction;

        }
        return  nd;

    }

    public void printPath(Node source,Node lastNode,List<String> directions)
    {
        if(source.equals(lastNode))
        {
        }else if(lastNode.parent==null)
        {
            directions.clear();
        }else{
            directions.add(lastNode.direction);
            printPath(source,lastNode.parent,directions);

        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        char [][] grid = new char[N][N];
        int brow=-1;
        int bcol=-1;

        int mrow=-1;
        int mcol=-1;

        int prow=-1;
        int pcol=-1;
        for(int i=0;i<N;i++)
        {
            String line = scanner.next();
            if(line.indexOf('p')!=-1)
            {
                prow=i;
                pcol=line.indexOf('p');
            }
            if(line.indexOf('b')!=-1)
            {
                brow=i;
                bcol=line.indexOf('b');
            }
            if(line.indexOf('m')!=-1)
            {
                mrow=i;
                mcol=line.indexOf('m');
            }
            grid[i]=line.toCharArray();

        }
        scanner.close();

        ReachPrinceByBowserBFS d = new ReachPrinceByBowserBFS();

        Node[][] nodes=d.prepareNodes(grid);
        Node[][] nodesfor2=d.prepareNodes(grid);
        List<String> directionsmtob=d.findPath(nodes,mrow,mcol,brow,bcol);
        if(directionsmtob==null||directionsmtob.isEmpty())
        {
            System.out.println("NO PATH FOUND");
            return;
        }
        Collections.reverse(directionsmtob);



        List<String> directionsbtop=d.findPath(nodesfor2,brow,bcol,prow,pcol);
        if(directionsbtop==null||directionsbtop.isEmpty())
        {
            System.out.println("NO PATH FOUND");
            return;
        }
        Collections.reverse(directionsbtop);


        Stream.concat(
                directionsmtob.stream(),
                directionsbtop.stream()).forEach(s->{
            System.out.print(s+" ");
        });



    }
    public Node[][] prepareNodes(char[][] grid)
    {
        int h = grid.length;
        if (h == 0)
            return null;
        int l = grid[0].length;
        Node[][] gridN=new Node[h][l];
        for(int i=0;i<h;i++)
        {
            for(int j=0;j<l;j++)
            {
                gridN[i][j]=new Node(0,null,i,j,"SOURCE",grid[i][j]);
            }
        }
        return gridN;
    }
    class  Node{
        int distance;
        Node parent;
        int row;
        int col;
        String direction;
        boolean visited;
        char val;
        public Node(int distance,Node parent,int row,int col,String direction,char val)
        {
            this.distance=distance;
            this.parent=parent;
            this.row=row;
            this.col=col;
            this.val=val;
            this.direction=direction;
        }

        @Override
        public boolean equals(Object obj)
        {
            if(this == obj)
                return true;
            if(obj == null || obj.getClass()!= this.getClass())
                return false;
            Node node = (Node) obj;
            return (node.row == this.row && node.col == this.col);
        }

        @Override
        public int hashCode()
        {
            return (row+":"+col).hashCode();
        }
    }
}