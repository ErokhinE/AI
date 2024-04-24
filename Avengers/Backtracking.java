
import java.util.*; 

public class Main {
    public static int size = 9; //size of a board
    public static List<List<Cell>> mapFoGame = new ArrayList<>(size);//map for game
    public static Coordinates shieldPosition; //position of a shield
    public static boolean shieldFound = false;//have we found a shield?
    public static Map<Character, Boolean> obstacles = new HashMap<>();//map
    public static void main(String[] args) {
        Coordinates Tanos = new Coordinates(0, 0); //put Tanos
        Scanner in = new Scanner(System.in);
        int perceptionZone = in.nextInt();
        obstacles.put('I', false);//stone
        obstacles.put('P', false);//perception
        obstacles.put('H', false);//Hulk
        obstacles.put('T', false);//Thor
        obstacles.put('M', false);//Marvel
        obstacles.put('S', false);//Shield
        obstacles.put('.', true);//if no
        stonePosition = new Coordinates(0,0);//put stone
        stonePosition.x = in.nextInt();
        stonePosition.y = in.nextInt();
        // fill the map firstly by adding arrayLists, and adding Cells
        for (int i = 0; i < size; i++) {
            mapFoGame.add(new ArrayList<>());
            for (int j = 0; j < size; j++) {
                mapFoGame.get(i).add(new Cell());
            }
        }


        System.out.println("m 0 0");//first move
        fillMap();//fill the map
        mapFoGame.get(0).get(0).vis = true;

        //start backtracking algorithm without shield
        int withoutShield = backtracking(Tanos, 0, stonePosition, new ArrayList<>());
        //we have found path without shield
        shortestPath.clear();
        clearMap(0);

        int withShield = Integer.MAX_VALUE;
        if (shieldFound) {
            mapFoGame.get(0).get(0).vis = true;
            //maxValue = Integer.MAX_VALUE;
            //start backtracking to find the shortest path to shield
            int toShield = backtracking(Tanos, 0, shieldPosition, new ArrayList<>());
            //compare two shortest paths first without shield and second path to shield and distance
            //between shield and stone
            if ((toShield < Integer.MAX_VALUE) && (toShield +
                    (Math.abs(shieldPosition.x - stonePosition.x) +
                            Math.abs(shieldPosition.y - stonePosition.y))) < withoutShield) {
                clearMap(0);
                //go to shield not teleport
                for (int i = 1; i < shortestPath.size() - 1; i++) {
                    Coordinates pathTo = shortestPath.get(i);
                    System.out.println("m " + pathTo.x + " " + pathTo.y);
                    fillMap();
                }
                clearMap(1);
                //put Tanos on a shield cell
                System.out.println("m " + shieldPosition.x + " " + shieldPosition.y);
                fillMap();
                Tanos = new Coordinates(shieldPosition.x, shieldPosition.y);
                obstacles.put('S', true);
                shortestPath.clear();
                mapFoGame.get(shieldPosition.y).get(shieldPosition.x).vis = true;
                //maxValue = Integer.MAX_VALUE;
                //backtracking from shield to stone
                int secPart = backtracking(Tanos, 0, stonePosition, new ArrayList<>());
                if (secPart < Integer.MAX_VALUE) {
                    withShield = toShield + secPart;
                }
            }
        }
        //Compare to give answer

        int answer = Math.min(withShield, withoutShield);
        if (answer < Integer.MAX_VALUE) {
            System.out.println("e " + answer);
        } else {
            System.out.println("e -1");
        }
    }



    //fill map with obstacles
    public static void fillMap() {
        Scanner in  = new Scanner(System.in);
        int numOfAnswers = in.nextInt();
        for (int i = 0; i < numOfAnswers; i++) {
            Coordinates ofObstacle = new Coordinates(0,0);
            ofObstacle.x = in.nextInt();
            ofObstacle.y = in.nextInt();
            mapFoGame.get(ofObstacle.y).get(ofObstacle.x).name = in.next().charAt(0);
        }
    }

    //where Tanos can go
    public static List<Coordinates> diff = Arrays.asList(new Coordinates(0, 1),
            new Coordinates(0, -1), new Coordinates(1, 0), new Coordinates(-1, 0));

    public static int maxValue = Integer.MAX_VALUE;
    public static Coordinates stonePosition;
    public static List<Coordinates> shortestPath = new ArrayList<>();


    //backtracking
    public static int backtracking(Coordinates current, int pathSize, Coordinates target, List<Coordinates> path) {
        int length = Integer.MAX_VALUE;
        path.add(current);
        //if our stone is near to us
        if (Math.abs(current.x - target.x) + Math.abs(current.y - target.y) == 1) {
            path.add(target);
            if (shortestPath.isEmpty() || shortestPath.size() > path.size()) {
                shortestPath = new ArrayList<>(path);
            }
            return pathSize + 1;
        }


        if (!shortestPath.isEmpty() && shortestPath.size() <= pathSize) {
            return pathSize;
        }


        List<Coordinates> children = new ArrayList<>();
        for (Coordinates difference : diff) {
            int i = difference.x, j = difference.y;
            int x = current.x, y = current.y;
            if (x + i >= 0 && x + i < size && y + j >= 0 && y + j < size) {
                //add child
                if (obstacles.get(mapFoGame.get(y + j).get(x + i).name) &&
                        !mapFoGame.get(y + j).get(x + i).vis) {
                    children.add(new Coordinates(x + i, y + j));
                }
                //case find shield
                if (mapFoGame.get(y + j).get(x + i).name == 'S') {
                    shieldPosition = new Coordinates(x + i, y + j);
                    shieldFound = true;
                }
            }
        }
        //sort children according to distance
        children.sort(Comparator.comparingInt(a ->
                Math.abs(a.x - target.x) + Math.abs(a.y - target.y)));
        for (Coordinates child : children) {
            // mark all children as visited
            for (Coordinates c : children) {
                mapFoGame.get(c.y).get(c.x).vis = true;
            }
            mapFoGame.get(child.y).get(child.x).vis = true;
            System.out.println("m "+ child.x+" "+child.y);
            fillMap();
            //call backtrack for children and update the length of a path
            length = Math.min(length, backtracking(child, pathSize + 1, target, path));
            for (Coordinates c : children) {
                mapFoGame.get(c.y).get(c.x).vis = false;
            }
            mapFoGame.get(child.y).get(child.x).vis = false;
            System.out.println("m "+ current.x+" "+current.y);
            fillMap();
        }
        return length;
    }


    //Method to clear map

    public static void clearMap(int flag) {
        for (List<Cell> row : mapFoGame) {
            for (Cell entry : row) {
                entry.vis = false;
                entry.path.clear();
                entry.length = Integer.MAX_VALUE;
                entry.saved = false;
                if (flag == 1) {
                    entry.name = '.';
                }
            }
        }
    }
}

//Coordinates
class Coordinates {
    int x;
    int y;
    Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
class Cell {
    boolean vis;
    boolean saved;
    char name;
    int length;
    List<Coordinates> path;

    Cell() {
        vis = false;
        saved = false;
        name = '.';
        length = Integer.MAX_VALUE;
        path = new ArrayList<>();
    }
}
