package com.company;

//Koostada meetod, mis leiab etteantud sidusas lihtgraafis
//kahe etteantud tipu vahelise lühima tee.

//Kommentaar: Nime järgi tipu leidmine võiks olla eraldi meetod ning lühima tee meetodile peaks
// saama ette anda tipud Vertex-objektidena, mitte nime abil. Algtipp ja lõpptipp on mõlemad
// parameetritena etteantavad, praegu te eeldate, et algtipp on fikseeritud. Servade pikkused
// võivad olla ka ühest erinevad.

//Kommentaar: Tehke lisaks mingi selline näide ka, kus kaare pikkus oleks ühest erinev.
// Signatuurid on sobivad, main-meetodist väljatrükitav info võiks olla arusaadavamal kujul
// (meetod shortestTo ise ei peakski midagi printima, las see jääda väljakutsuja hooleks).

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Egle Arge
 */

public class GraphTask {

    public static void main (String[] args) {
        GraphTask a = new GraphTask();
        a.run();

    }

    public void run() {
        Graph g = new Graph ("G");
        g.createRandomSimpleGraph (32, 37);
        System.out.println (g);
        run2("v1", "v9", g);
        run2("v2", "v4", g);
        run2("v21", "v15", g);
        run2("v25", "v7", g);
        run2("v7", "v6", g);
        // TODO!!! Your experiments here
    }

    /**
     * Find shortest route between graph points and print out the solution.
     * @param name1 id of starting point
     * @param name2 id of end point
     * @param g graph to use
     */
    public void run2(String name1, String name2, Graph g){
        Vertex[] route = g.shortestTo(g.getByName(name1), g.getByName(name2));
        if (route == null)
            System.out.println("Teed ei leidunud");
        else if (route.length == 0){
            System.out.println("Teekonna algus ja lõpp on sama");
        }
        else {
            System.out.println("Teepikkus " + g.calcLenght(route));
            for (int i = 0; i < route.length - 1; i++) {
                System.out.print(" " + route[i].id);
                Arc arc = route[i].first;
                while (arc.target != route[i+1]){
                    arc = arc.next;
                }
                System.out.print(" -> (kaare pikkus = " + arc.info + ")");
            }
            System.out.print(" " + route[route.length - 1].id);
            System.out.println();
        }

    }


    class Vertex {

        private String id;
        private Vertex next;
        private Arc first;
        private int info = 0;

        Vertex (String s, Vertex v, Arc e) {
            id = s;
            next = v;
            first = e;
        }

        Vertex (String s) {
            this (s, null, null);
        }

        @Override
        public String toString() {
            return id;
        }

        // TODO!!! Your Vertex methods here!
    }


    class Arc {

        private String id;
        private Vertex target;
        private Arc next;
        private int info = 1;
        private Random rnd = new Random();

        Arc (String s, Vertex v, Arc a) {
            id = s;
            target = v;
            next = a;
            info = rnd.nextInt(10)+1;
        }

        Arc (String s) {
            this (s, null, null);
        }

        @Override
        public String toString() {
            return "pikkus " + info;
        }

        // TODO!!! Your Arc methods here!
    }


    class Graph {

        private String id;
        private Vertex first;
        private int info = 0;

        Graph (String s, Vertex v) {
            id = s;
            first = v;
        }

        Graph (String s) {
            this (s, null);
        }

        @Override
        public String toString() {
            String nl = System.getProperty ("line.separator");
            StringBuffer sb = new StringBuffer (nl);
            sb.append (id);
            sb.append (nl);
            Vertex v = first;
            while (v != null) {
                sb.append (v.toString());
                sb.append (" -->");
                Arc a = v.first;
                while (a != null) {
                    sb.append (" ");
                    sb.append (" (");
                    sb.append (v.toString());
                    sb.append ("--");
                    sb.append (a.toString());
                    sb.append ("->");
                    sb.append (a.target.toString());
                    sb.append (")");
                    a = a.next;
                }
                sb.append (nl);
                v = v.next;
            }
            return sb.toString();
        }

        public Vertex createVertex (String vid) {
            Vertex res = new Vertex (vid);
            res.next = first;
            first = res;
            return res;
        }

        public Arc createArc (String aid, Vertex from, Vertex to) {
            Arc res = new Arc (aid);
            res.next = from.first;
            from.first = res;
            res.target = to;
            return res;
        }

        /**
         * Create a connected undirected random tree with n vertices.
         * Each new vertex is connected to some random existing vertex.
         * @param n number of vertices added to this graph
         */
        public void createRandomTree (int n) {
            if (n <= 0)
                return;
            Vertex[] varray = new Vertex [n];
            for (int i = 0; i < n; i++) {
                varray [i] = createVertex ("v" + String.valueOf(n-i));
                if (i > 0) {
                    int vnr = (int)(Math.random()*i);
                    createArc ("a" + varray [vnr].toString() + "_"
                            + varray [i].toString(), varray [vnr], varray [i]);
                    createArc ("a" + varray [i].toString() + "_"
                            + varray [vnr].toString(), varray [i], varray [vnr]);
                } else {}
            }
        }

        /**
         * Create an adjacency matrix of this graph.
         * Side effect: corrupts info fields in the graph
         * @return adjacency matrix
         */
        public int[][] createAdjMatrix() {
            info = 0;
            Vertex v = first;
            while (v != null) {
                v.info = info++;
                v = v.next;
            }
            int[][] res = new int [info][info];
            v = first;
            while (v != null) {
                int i = v.info;
                Arc a = v.first;
                while (a != null) {
                    int j = a.target.info;
                    res [i][j]++;
                    a = a.next;
                }
                v = v.next;
            }
            return res;
        }

        /**
         * Create a connected simple (undirected, no loops, no multiple
         * arcs) random graph with n vertices and m edges.
         * @param n number of vertices
         * @param m number of edges
         */
        public void createRandomSimpleGraph (int n, int m) {
            if (n <= 0)
                return;
            if (n > 2500)
                throw new IllegalArgumentException ("Too many vertices: " + n);
            if (m < n-1 || m > n*(n-1)/2)
                throw new IllegalArgumentException
                        ("Impossible number of edges: " + m);
            first = null;
            createRandomTree (n);       // n-1 edges created here
            Vertex[] vert = new Vertex [n];
            Vertex v = first;
            int c = 0;
            while (v != null) {
                vert[c++] = v;
                v = v.next;
            }
            int[][] connected = createAdjMatrix();
            int edgeCount = m - n + 1;  // remaining edges
            while (edgeCount > 0) {
                int i = (int)(Math.random()*n);  // random source
                int j = (int)(Math.random()*n);  // random target
                if (i==j)
                    continue;  // no loops
                if (connected [i][j] != 0 || connected [j][i] != 0)
                    continue;  // no multiple edges
                Vertex vi = vert [i];
                Vertex vj = vert [j];
                createArc ("a" + vi.toString() + "_" + vj.toString(), vi, vj);
                connected [i][j] = 1;
                createArc ("a" + vj.toString() + "_" + vi.toString(), vj, vi);
                connected [j][i] = 1;
                edgeCount--;  // a new edge happily created
            }
        }

        /**
         * Find graph point by id.
         * @param name point to find
         * @return point if found
         */
        public Vertex getByName(String name){
            Vertex next = first;
            while (next != null){
                if (next.id.equals(name))
                    return next;
                next = next.next;
            }
            throw new RuntimeException("Graafi punkt puudub: " + name);
        }

        /**
         * Find shortest route between points.
         * @param from starting point
         * @param to end point
         * @return array of points in shortest route
         */
        public Vertex[] shortestTo(Vertex from, Vertex to){
            if (to == null) throw new RuntimeException("Lõpp punkt võrdub null");
            if (from == null) throw new RuntimeException("Alguspunkti pole olemas");
            if (from == to)
                return new Vertex[0];
            else {
                ArrayList<Vertex> points = new ArrayList<Vertex>();
                Vertex[] route = lookFrom(from, to, points);
                return route;

            }

        }

        /**
         * Calculate the lenght of the route.
         * @param points points of the route
         * @return lenght of the route
         */
        public int calcLenght(Vertex[] points){
            int sum = 0;
            for (int i = 0; i < points.length - 1; i++) {
                Vertex from = points[i];
                Vertex to = points[i+1];
                Arc arc = from.first;
                while (arc.target != to){
                    arc = arc.next;
                }
                sum += arc.info;
            }
            return sum;
        }

        /**
         * Recursive method to find the rest of the route to the end point.
         * @param current the point from where to continue
         * @param to end point
         * @param points points traveled
         * @return points of the route
         */
        private Vertex[] lookFrom(Vertex current, Vertex to, ArrayList<Vertex> points) {
            if (current == to){
                Vertex[] route = points.toArray(new Vertex[points.size() + 1]);
                route[points.size()] = current;
                return route;
            }
            if (points.contains(current)){
                return null;
            }
            points.add(current);
            Arc next = current.first;
            int best = Integer.MAX_VALUE;
            Vertex[] bestroute = null;
            while (next != null){
                Vertex[] ret = lookFrom(next.target, to, points);
                if (ret != null){
                    int length = calcLenght(ret);
                    if (length < best){
                        best = length;
                        bestroute = ret;
                    }
                }
                next = next.next;
            }
            points.remove(current);
            return bestroute;
        }

        // TODO!!! Your Graph methods here!
    }

}
