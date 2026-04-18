package dp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FloydWarshall {

    public static final long INF = Long.MAX_VALUE / 2;

    /**
     * All-pairs shortest paths — O(V^3) tiempo, O(V^2) espacio.
     * @param  w  Matriz de adyacencia: w[i][j]=peso, INF si no hay arista, 0 en diagonal
     * @return dist[i][j] = distancia mínima de i a j, o INF si no hay camino
     * @throws IllegalStateException si se detecta ciclo negativo
     */
    public static long[][] solve(long[][] w) {
        int V = w.length;
        long[][] dist = new long[V][V];
        for (int i = 0; i < V; i++) dist[i] = w[i].clone();
        for (int k = 0; k < V; k++)
            for (int i = 0; i < V; i++)
                for (int j = 0; j < V; j++)
                    if (dist[i][k] < INF && dist[k][j] < INF)
                        dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
        // Detectar ciclos negativos
        for (int i = 0; i < V; i++)
            if (dist[i][i] < 0)
                throw new IllegalStateException(
                    "Ciclo negativo detectado alcanzable desde el vertice " + i);
        return dist;
    }

    /** Retorna true si existe ciclo negativo accesible en el grafo */
    public static boolean hasNegativeCycle(long[][] w) {
        try { solve(w); return false; }
        catch (IllegalStateException e) { return true; }
    }

    /**
     * Reconstruye el camino mínimo de s a t usando matriz de sucesores.
     * @return lista de vértices del camino (incluye s y t), o lista vacía si no hay camino
     * @throws IllegalStateException si se detecta ciclo negativo
     */
    public static List<Integer> reconstructPath(long[][] w, int s, int t) {
        int V = w.length;
        long[][] dist = new long[V][V];
        int[][] next = new int[V][V];

        for (int i = 0; i < V; i++) {
            dist[i] = w[i].clone();
            for (int j = 0; j < V; j++)
                next[i][j] = (w[i][j] < INF && i != j) ? j : -1;
        }

        for (int k = 0; k < V; k++)
            for (int i = 0; i < V; i++)
                for (int j = 0; j < V; j++)
                    if (dist[i][k] < INF && dist[k][j] < INF &&
                            dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k];
                    }

        // Detectar ciclos negativos
        for (int i = 0; i < V; i++)
            if (dist[i][i] < 0)
                throw new IllegalStateException(
                    "Ciclo negativo detectado alcanzable desde el vertice " + i);

        if (next[s][t] == -1) return Collections.emptyList();

        List<Integer> path = new ArrayList<>();
        int cur = s;
        path.add(cur);
        while (cur != t) {
            cur = next[cur][t];
            path.add(cur);
        }
        return path;
    }
}