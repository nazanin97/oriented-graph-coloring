import java.io.*;
import java.util.*;

/**
 * this is oriented coloring algorithm, the number of vertices of graph should start from 0 to n-1
 */
public class Main {

    //this function is to find what is the max color number of adjacent of currentV
    private static int findMax(int currentV, LinkedList<Integer>adj[], int[] result){

        int max = result[currentV];
        for (int i = 0; i < adj[currentV].size(); i++) {
            if (result[adj[currentV].get(i)] > max)
                max = result[adj[currentV].get(i)];
        }
        return max;
    }
    private static boolean isCyclicUtil(int i, boolean[] visited, boolean[] recStack, LinkedList<Integer>nodes[]) {

        // Mark the current node as visited and
        // part of recursion stack
        if (recStack[i])
            return true;

        if (visited[i])
            return false;

        visited[i] = true;

        recStack[i] = true;
        LinkedList<Integer> children = nodes[i];

        for (Integer c: children)
            if (isCyclicUtil(c, visited, recStack, nodes))
                return true;

        recStack[i] = false;

        return false;
    }

    private static Boolean hasCycle(int vNum, LinkedList<Integer>nodes[]) {

        // Mark all the vertices as not visited and not part of recursion stack
        boolean[] visited = new boolean[vNum];
        boolean[] recStack = new boolean[vNum];

        // Call the recursive helper function to detect cycle in different DFS trees
        for (int i = 0; i < vNum; i++)
            if (isCyclicUtil(i, visited, recStack, nodes))
                return true;

        return false;
    }

    public static void main(String[] args) {

        //to store edges
        LinkedList<Integer> nodes[];
        //to store edges undirected
        LinkedList<Integer> adj[];

        ArrayList<String>edges = new ArrayList<>();
        //for counting vertices(0 to n-1)
        HashSet<Integer> vertices = new HashSet<>();

        try {
            BufferedReader lineReader = new BufferedReader(new FileReader("in2.txt"));
            String line;
            while ((line = lineReader.readLine()) != null){
                String[] parts = line.split(" ");
                vertices.add(Integer.parseInt(parts[0]));
                vertices.add(Integer.parseInt(parts[1]));
                edges.add(parts[0] + " " + parts[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        nodes = new LinkedList[vertices.size()];
        adj = new LinkedList[vertices.size()];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new LinkedList<>();
            adj[i] = new LinkedList<>();
        }

        //store edges
        for (String edge:edges) {
            String[] part = edge.split(" ");
            int v1 = Integer.parseInt(part[0]);
            int v2 = Integer.parseInt(part[1]);
            adj[v1].add(v2);
            adj[v2].add(v1);
            nodes[v1].add(v2);
        }

        //if the graph has cycle stop the algorithm
        if (hasCycle(vertices.size(), nodes)){
            System.out.println("Wrong Input...Graphs shouldn't have cycle!");
            return;
        }

        //assign int to each vertex as it's color
        int[] result = new int[vertices.size()];
        Arrays.fill(result, -1);
        //color of first node is "1"
        result[0] = 0;

        // "available" array is for colors, we have vNum color maximum, all the colors are available at first
        boolean available[] = new boolean[vertices.size()];
        Arrays.fill(available, true);


        //assign colors to remaining vNum - 1 vertices
        for (int j = 1; j < adj.length; j++) {

            Iterator<Integer> it = adj[j].iterator() ;
            while (it.hasNext()) {
                int i = it.next();

                //if we assigned color to one of the adjacent before then it's color is not available anymore
                if (result[i] != -1)
                    available[result[i]] = false;
            }

            //colors are 0 to vNum - 1
            int cr;
            for (cr = 0; cr < vertices.size(); cr++){
                if (available[cr])
                    break;
            }

            //color found!
            result[j] = cr;

            //reset the values back to true for the next iteration
            Arrays.fill(available, true);
        }

        for (int i = 0; i < nodes.length; i++) {
            ArrayList<Integer>holdV = new ArrayList<>();
            for (int j = 0; j < nodes[i].size(); j++) {
                for (int k = 0; k < nodes[nodes[i].get(j)].size(); k++) {
                    holdV.add(nodes[nodes[i].get(j)].get(k));
                }
                for (int k = 0; k < result.length; k++) {
                    if (result[k] == result[nodes[i].get(j)]){
                        for (int l = 0; l < nodes[k].size(); l++) {
                            if (!holdV.contains(nodes[k].get(l)))
                                holdV.add(nodes[k].get(l));
                        }
                    }
                }
                for (int k = 0; k < holdV.size(); k++) {
                    if (result[holdV.get(k)] == result[i]){
                        result[holdV.get(k)] = 1 + findMax(holdV.get(k), adj, result);
                    }
                }
            }
        }

        //writing result to file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("out2.txt"))) {

            for (int i = 0; i < result.length; i++) {
                bw.write(i + "\t\t" + result[i]);
                bw.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

