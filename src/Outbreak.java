import java.util.*;

public class Outbreak {

    /**
     * Mission 5a. Dynamic programming on the tree of infections (Weighted Vertex Cover)
     * with solution reconstruction (which people are tested).
     */
    public static boolean[] cheapestTests(int n, int[][] nb, int[] cost) {
        // 1. Root the tree using BFS to get parent relationships and traversal order
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        List<Integer> order = new ArrayList<>();
        boolean[] visited = new boolean[n];

        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);
        visited[0] = true;

        while (!queue.isEmpty()) {
            int u = queue.poll();
            order.add(u);
            for (int v : nb[u]) {
                if (!visited[v]) {
                    visited[v] = true;
                    parent[v] = u;
                    queue.add(v);
                }
            }
        }

        // Post-order traversal (from leaves to root) for DP computation
        List<Integer> postOrder = new ArrayList<>(order);
        Collections.reverse(postOrder);

        // dp[u][0] = min cost if u is NOT tested
        // dp[u][1] = min cost if u IS tested
        long[][] dp = new long[n][2];

        for (int u : postOrder) {
            dp[u][0] = 0;
            dp[u][1] = cost[u];

            for (int v : nb[u]) {
                if (v != parent[u]) {
                    dp[u][0] += dp[v][1];
                    dp[u][1] += Math.min(dp[v][0], dp[v][1]);
                }
            }
        }

        // 2. Reconstruct the solution (determine who is tested)
        boolean[] tested = new boolean[n];
        tested[0] = dp[0][1] <= dp[0][0];

        // Top-down pass using BFS order to decide status of children based on parent
        for (int u : order) {
            for (int v : nb[u]) {
                if (v != parent[u]) {
                    if (!tested[u]) {
                        // If parent is not tested, child must be tested
                        tested[v] = true;
                    } else {
                        // If parent is tested, child is chosen based on optimal DP choice
                        tested[v] = dp[v][1] <= dp[v][0];
                    }
                }
            }
        }

        return tested;
    }

    /**
     * Mission 5b. The number of links on the longest chain of infections (Diameter of the tree).
     */
    public static int longestChain(int n, int[][] nb) {
        if (n <= 1) return 0;

        // Find the farthest node from the root (0)
        int[] firstBFS = bfsFarthest(0, n, nb);
        int u = firstBFS[0];

        // Find the farthest node from u (gives the tree diameter endpoints)
        int[] secondBFS = bfsFarthest(u, n, nb);
        return secondBFS[1]; // Maximum distance in edges
    }

    private static int[] bfsFarthest(int start, int n, int[][] nb) {
        int[] dist = new int[n];
        Arrays.fill(dist, -1);
        Queue<Integer> q = new LinkedList<>();

        q.add(start);
        dist[start] = 0;
        int farthestNode = start;
        int maxDist = 0;

        while (!q.isEmpty()) {
            int u = q.poll();
            if (dist[u] > maxDist) {
                maxDist = dist[u];
                farthestNode = u;
            }
            for (int v : nb[u]) {
                if (dist[v] == -1) {
                    dist[v] = dist[u] + 1;
                    q.add(v);
                }
            }
        }
        return new int[] { farthestNode, maxDist };
    }
}
