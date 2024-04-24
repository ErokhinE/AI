import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Scanner;


public class Main {
    public static int size = 9;
    public static LinkedList<Cell> CellsToBack = new LinkedList<>();

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Cell[][] mapForGame = map(); //map for game
        Cell Tanos = new Cell("A"); //put tanos
        mapForGame[0][0] = Tanos;
        mapForGame[0][0].vis = true;

        int perception = in.nextInt();

        int XofStone = in.nextInt();
        int YofStone = in.nextInt();
        Cell stone = new Cell("I");
        mapForGame[XofStone][YofStone] = stone;// put stone on map
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                mapForGame[i][j].EndPath = Math.abs(XofStone - i) + Math.abs(YofStone - j);
                mapForGame[i][j].StartPath = 10000000;
                mapForGame[i][j].sum = 100000000;
            }
        } //Arrange entities of maps' cells
        mapForGame[0][0].StartPath = 0;
        mapForGame[0][0].sum = 0;


        while (true) { //searching stone 


            int X = 0, Y = 0;
            boolean notFin = false;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (mapForGame[i][j].name.equals("A")) {
                        X = i; //X of Tanos
                        Y = j; //Y of Tanos
                    }
                    if (mapForGame[i][j].name.equals("I")) {
                        notFin = true;
                    }
                }
            }
            if (notFin) { //if not found make move
                System.out.println("m " + X + " " + Y);
                mapForGame = makeMove(mapForGame, perception, X, Y);
            } else { //if found end program
                System.out.println("m " + X + " " + Y);
                System.out.println("e " + mapForGame[X][Y].sum);
                break;
            }
        }

    }

    public static Cell[][] map() { //method for cells of map
        Cell[][] mapwithout = new Cell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Cell cell = new Cell("none");
                mapwithout[i][j] = cell;
                mapwithout[i][j].vis = false;
            }
        }
        return mapwithout;
    }

    public static Cell[][] makeMove(Cell[][] mapForGame, int perception, int XofTan, int YofTan) {
        
        //Make move
        Scanner in = new Scanner(System.in);
        LinkedList<Integer> minMoveSum = new LinkedList<>();
        LinkedList<Integer> minMoveEnd = new LinkedList<>();


        ArrayList<Integer> index = new ArrayList<>();



        int answerNum = in.nextInt();
        for (int i = 0; i < answerNum; i++) {
            int XofObs = in.nextInt();
            int YofObs = in.nextInt();
            String nameOfObs = in.next();
            mapForGame = mapWithObs(mapForGame, XofObs, YofObs, nameOfObs);
            //put obstacles
        }
        
        //checking cells where we can go ( not out of bound not visited not an obstacle )
        //change start path if we need
        //change total sum
        //add to lists to future choice where to go
        //add cells to back if it  will be necessary

        if ((XofTan - 1 >= 0) && (!mapForGame[XofTan - 1][YofTan].vis) && 
                (Objects.equals(mapForGame[XofTan - 1][YofTan].name, "none") ||
                Objects.equals(mapForGame[XofTan - 1][YofTan].name, "I") || 
                Objects.equals(mapForGame[XofTan - 1][YofTan].name, "S"))) {
            if (mapForGame[XofTan - 1][YofTan].StartPath > mapForGame[XofTan][YofTan].countStartPath())
                mapForGame[XofTan - 1][YofTan].StartPath = mapForGame[XofTan][YofTan].countStartPath();
            mapForGame[XofTan - 1][YofTan].sum = mapForGame[XofTan - 1][YofTan].StartPath + mapForGame[XofTan - 1][YofTan].EndPath;
            minMoveEnd.add(mapForGame[XofTan - 1][YofTan].EndPath);
            minMoveSum.add(mapForGame[XofTan - 1][YofTan].sum);
            mapForGame[XofTan - 1][YofTan].parent = mapForGame[XofTan][YofTan];
            if (!CellsToBack.contains(mapForGame[XofTan - 1][YofTan])) {
                mapForGame[XofTan - 1][YofTan].X = XofTan - 1;
                mapForGame[XofTan - 1][YofTan].Y = YofTan;
                CellsToBack.add(mapForGame[XofTan - 1][YofTan]);
            }
            index.add(0);
        }
        if ((XofTan + 1 < size) && (!mapForGame[XofTan + 1][YofTan].vis) && (Objects.equals(mapForGame[XofTan + 1][YofTan].name, "none") ||
                Objects.equals(mapForGame[XofTan + 1][YofTan].name, "I") || Objects.equals(mapForGame[XofTan + 1][YofTan].name, "S"))) {
            if (mapForGame[XofTan + 1][YofTan].StartPath > mapForGame[XofTan][YofTan].countStartPath())
                mapForGame[XofTan + 1][YofTan].StartPath = mapForGame[XofTan][YofTan].countStartPath();
            mapForGame[XofTan + 1][YofTan].sum = mapForGame[XofTan + 1][YofTan].StartPath + mapForGame[XofTan + 1][YofTan].EndPath;
            minMoveEnd.add(mapForGame[XofTan + 1][YofTan].EndPath);
            minMoveSum.add(mapForGame[XofTan + 1][YofTan].sum);
            mapForGame[XofTan + 1][YofTan].parent = mapForGame[XofTan][YofTan];
            if (!CellsToBack.contains(mapForGame[XofTan + 1][YofTan])) {
                mapForGame[XofTan + 1][YofTan].X = XofTan + 1;
                mapForGame[XofTan + 1][YofTan].Y = YofTan;
                CellsToBack.add(mapForGame[XofTan + 1][YofTan]);
            }
            index.add(1);
        }
        if ((YofTan - 1 >= 0) && (!mapForGame[XofTan][YofTan - 1].vis) && (Objects.equals(mapForGame[XofTan][YofTan - 1].name, "none") ||
                Objects.equals(mapForGame[XofTan][YofTan - 1].name, "I") || Objects.equals(mapForGame[XofTan][YofTan - 1].name, "S"))) {
            if (mapForGame[XofTan][YofTan - 1].StartPath > mapForGame[XofTan][YofTan].countStartPath())
                mapForGame[XofTan][YofTan - 1].StartPath = mapForGame[XofTan][YofTan].countStartPath();
            mapForGame[XofTan][YofTan - 1].sum = mapForGame[XofTan][YofTan - 1].StartPath + mapForGame[XofTan][YofTan - 1].EndPath;
            minMoveEnd.add(mapForGame[XofTan][YofTan - 1].EndPath);
            minMoveSum.add(mapForGame[XofTan][YofTan - 1].sum);
            mapForGame[XofTan][YofTan - 1].parent = mapForGame[XofTan][YofTan];
            if (!CellsToBack.contains(mapForGame[XofTan][YofTan - 1])) {
                mapForGame[XofTan][YofTan - 1].X = XofTan;
                mapForGame[XofTan][YofTan - 1].Y = YofTan - 1;
                CellsToBack.add(mapForGame[XofTan][YofTan - 1]);
            }
            index.add(2);
        }

        if ((YofTan + 1 < size) && (!mapForGame[XofTan][YofTan + 1].vis) && (Objects.equals(mapForGame[XofTan][YofTan + 1].name, "none") ||
                Objects.equals(mapForGame[XofTan][YofTan + 1].name, "I") || Objects.equals(mapForGame[XofTan][YofTan + 1].name, "S"))) {
            if (mapForGame[XofTan][YofTan + 1].StartPath > mapForGame[XofTan][YofTan].countStartPath())
                mapForGame[XofTan][YofTan + 1].StartPath = mapForGame[XofTan][YofTan].countStartPath();
            mapForGame[XofTan][YofTan + 1].sum = mapForGame[XofTan][YofTan + 1].StartPath + mapForGame[XofTan][YofTan + 1].EndPath;
            minMoveEnd.add(mapForGame[XofTan][YofTan + 1].EndPath);
            minMoveSum.add(mapForGame[XofTan][YofTan + 1].sum);
            mapForGame[XofTan][YofTan + 1].parent = mapForGame[XofTan][YofTan];
            if (!CellsToBack.contains(mapForGame[XofTan][YofTan + 1])) {
                mapForGame[XofTan][YofTan + 1].X = XofTan;
                mapForGame[XofTan][YofTan + 1].Y = YofTan + 1;
                CellsToBack.add(mapForGame[XofTan][YofTan + 1]);
            }
            index.add(3);
        }
        
        
        //sort list according to sum
        if (index.size() != 1 && index.size() != 2) {
            for (int i = 0; i < minMoveSum.size() - 1; i++) {
                for (int j = i + 1; j < minMoveSum.size(); j++) {
                    if (minMoveSum.get(i) > minMoveSum.get(j)) {
                        int tmp = minMoveSum.get(i);
                        int tmp1 = minMoveEnd.get(i);
                        int tmp2 = index.get(i);
                        minMoveSum.set(i, minMoveSum.get(j));
                        minMoveSum.set(j, tmp);
                        index.set(i, index.get(j));
                        index.set(j, tmp2);
                        minMoveEnd.set(i, minMoveEnd.get(j));
                        minMoveEnd.set(j, tmp1);

                    }
                }
            }
        }

       
        int indexOfMove = -1;
        
        //Check can we reach stone
        if (index.size() == 0) {
            if (CellsToBack.size() == 0) {
                System.out.println("e -1");
                System.exit(0);
            } else {
                // if we have cells which are not visited go to it 
                for (int i = 0; i < CellsToBack.size() - 1; i++) {
                    if (CellsToBack.get(i).sum > CellsToBack.get(i + 1).sum) {
                        Cell tmp = CellsToBack.get(i);
                        CellsToBack.set(i, CellsToBack.get(i + 1));
                        CellsToBack.set(i + 1, tmp);

                    }
                }

                Cell elementToGo = CellsToBack.getFirst();
                mapForGame = goTo(elementToGo, XofTan, YofTan, mapForGame);
                return mapForGame;
            }


        }


        //sort list according to sum
        if (index.size() == 2) {
            if (minMoveSum.get(0) > minMoveSum.get(1)) {
                int tmp = minMoveSum.get(0);
                int tmp1 = minMoveEnd.get(1);
                int tmp2 = index.get(0);
                minMoveSum.set(0, minMoveSum.get(1));
                minMoveSum.set(1, tmp);
                index.set(0, index.get(1));
                index.set(1, tmp2);
                minMoveEnd.set(0, minMoveEnd.get(1));
                minMoveEnd.set(1, tmp1);
            }

        }
        
        //chose cell where we want to go according to min sum
        if (index.size() == 1) {
            indexOfMove = index.get(0);
        } else {
            for (int i = 0; i < minMoveSum.size() - 1; i++) {


                if (minMoveSum.get(i) == minMoveSum.get(i + 1)) {
                    if (minMoveEnd.get(i) <= minMoveEnd.get(i + 1)) {
                        indexOfMove = index.get(i);
                        break;
                    } else if (minMoveSum.size() - 1 == i + 1) {
                        indexOfMove = index.get(i + 1);
                        break;
                    } else {
                        continue;
                    }
                } else indexOfMove = index.get(i);
                break;
            }
        }
        
        //make move according to cell which we have chosen and make move
        switch (indexOfMove) {
            case 0: {
                mapForGame[XofTan - 1][YofTan].name = "A";
                mapForGame[XofTan][YofTan].name = "none";
                mapForGame[XofTan - 1][YofTan].vis = true;
                CellsToBack.remove(mapForGame[XofTan - 1][YofTan]);
                break;
            }
            case 1: {
                mapForGame[XofTan + 1][YofTan].name = "A";
                mapForGame[XofTan][YofTan].name = "none";
                mapForGame[XofTan + 1][YofTan].vis = true;
                CellsToBack.remove(mapForGame[XofTan + 1][YofTan]);
                break;
            }
            case 2: {
                mapForGame[XofTan][YofTan - 1].name = "A";
                mapForGame[XofTan][YofTan].name = "none";
                mapForGame[XofTan][YofTan - 1].vis = true;
                CellsToBack.remove(mapForGame[XofTan][YofTan - 1]);
                break;
            }
            case 3: {
                mapForGame[XofTan][YofTan + 1].name = "A";
                mapForGame[XofTan][YofTan].name = "none";
                mapForGame[XofTan][YofTan + 1].vis = true;
                CellsToBack.remove(mapForGame[XofTan][YofTan + 1]);
                break;
            }
        }


        return mapForGame;

    }

    public static Cell[][] mapWithObs(Cell[][] mapForGame, int XofObs, int YofObs, String nameOfObs) {
        mapForGame[XofObs][YofObs].name = nameOfObs;
        return mapForGame;
    }
    
    //if we have cells which we do not visited and it the current we have no possibility to 
    //move further make path to not teleport 

    public static Cell[][] goTo(Cell cellToGo, int XofStart, int YofStart, Cell[][] mapForGame) {
        Scanner in = new Scanner(System.in);

        while (true) {
            if ((XofStart == cellToGo.X + 1) && (YofStart == cellToGo.Y)) {
                mapForGame[cellToGo.X][cellToGo.Y].name = "A";
                mapForGame[XofStart][YofStart].name = "none";
                mapForGame[cellToGo.X][cellToGo.Y].vis = true;
                CellsToBack.remove(mapForGame[cellToGo.X][cellToGo.Y]);
                break;
            } else if ((XofStart == cellToGo.X - 1) && (YofStart == cellToGo.Y)) {
                mapForGame[cellToGo.X][cellToGo.Y].name = "A";
                mapForGame[XofStart][YofStart].name = "none";
                mapForGame[cellToGo.X][cellToGo.Y].vis = true;
                CellsToBack.remove(mapForGame[cellToGo.X][cellToGo.Y]);
                break;
            } else if ((XofStart == cellToGo.X) && (YofStart == cellToGo.Y + 1)) {
                mapForGame[cellToGo.X][cellToGo.Y].name = "A";
                mapForGame[XofStart][YofStart].name = "none";
                mapForGame[cellToGo.X][cellToGo.Y].vis = true;
                CellsToBack.remove(mapForGame[cellToGo.X][cellToGo.Y]);
                break;
            } else if ((XofStart == cellToGo.X) && (YofStart == cellToGo.Y - 1)) {
                mapForGame[cellToGo.X][cellToGo.Y].name = "A";
                mapForGame[XofStart][YofStart].name = "none";
                mapForGame[cellToGo.X][cellToGo.Y].vis = true;
                CellsToBack.remove(mapForGame[cellToGo.X][cellToGo.Y]);
                break;
            } else {
                mapForGame[XofStart][YofStart].name = "none";
                int XofStartNew = mapForGame[XofStart][YofStart].parent.X;
                int YofStartNew = mapForGame[XofStart][YofStart].parent.Y;
                mapForGame[XofStartNew][YofStartNew].name = "A";
                XofStart = XofStartNew;
                YofStart = YofStartNew;
                System.out.println("m " + XofStartNew + " " + YofStartNew);

                int num = in.nextInt();
                for (int i = 0; i < num; i++) {
                    int a = in.nextInt();
                    int b = in.nextInt();
                    String c = in.next();
                }
            }
        }
        return mapForGame;
    }


}

//Class cell with parent (from which cell we have arrived)
//start path end path sum for A* 
//name of object if it stands 


class Cell {
    Cell parent;
    int X;
    int Y;
    int StartPath;
    int EndPath;

    int sum;
    String name;

    boolean vis;

    Cell(String name) {
        this.name = name;
        this.vis = false;
    }

    int countStartPath() {
        return this.StartPath + 1;
    }
}
