//package ensta;

public class Board implements IBoard{
    private String name;
    private int size;
    private ShipState[][] ships;
    private Boolean[][] hits;

    public Board(String name, int size){
        this.name=name;
        this.size=size;
        ships=new ShipState[size][size];
        hits=new Boolean[size][size];

        for (int i=0; i<size; i++){
            for (int j=0; j<size; j++){
                ships[i][j]=new ShipState(null);
            }
        }
    }

    public Board(String name){
        this(name,10);
    }

    public void print(){

        System.out.println(name);

        int code_A=(int)'A';
        System.out.print("   ");
        for(int j=0;j<size;j ++){
            System.out.print((char)(code_A+j));
            System.out.print(" ");
        }

        System.out.print("    ");
        for(int j=0;j<size;j ++){
            System.out.print((char)(code_A+j));
            System.out.print(" ");
        }
        System.out.println();
        
        for (int i=0; i<size; i++){

            //Partie affichant les navires.
            if(i<10) System.out.print(' ');
            System.out.print(i);
            System.out.print(' ');
            for(int j=0;j<size ; j++){
                ShipState x=ships[i][j];
                if(x.getShip()==null){
                    System.out.print('.');
                }
                else{
                    System.out.print(x.toString());
                }
                System.out.print(' ');
            }

            //Affichage des frappes

            System.out.print(' ');
            if(i<10) System.out.print(' ');
            System.out.print(i);
            System.out.print(' ');
            for(int j=0;j<size ; j++){
                
                if(hits[i][j]==null){
                    System.out.print('.');
                }
                else if(hits[i][j]==Boolean.TRUE){
                    System.out.print(ColorUtil.colorize('X',ColorUtil.Color.RED));
                }
                else{
                    System.out.print('X');
                }
                System.out.print(' ');
            }
            System.out.println();
        }
    }

    public int getSize(){
        return(size);
    }

    public boolean hasShip(int x, int y){
        return (ships[y][x].getShip() != null);
    }

    public void putShipAux(AbstractShip ship, int x, int y, int ix, int iy)throws Exception {
        for (int i=0; i<ship.getSize(); i++){
            if (hasShip(x+ix*i, y+iy*i)){
                throw new Exception();
            }
        }
        for (int i=0; i<ship.getSize(); i++){
            ships[y+iy*i][x+ix*i]=new ShipState(ship);
        } 
    }

    public void putShip(AbstractShip ship, int x, int y) throws Exception{
        try {
            switch (ship.getOrientation()){
                case EAST : 
                    putShipAux(ship, x, y, 1, 0);
                break;
                case NORTH :
                    putShipAux(ship, x, y, 0, -1);
                break;
                case SOUTH : 
                    putShipAux(ship, x, y, 0, 1);
                break;
                case WEST :
                    putShipAux(ship, x, y, -1, 0);
                break;
            }
        } 
        catch (Exception e) {
            System.err.println("Le bateau sort du champ de bataille ou recouvre un autre bateau, essayez à nouveau.");
            throw e;
        };
    }


    public void setHit(boolean hit, int x, int y){
        if (hit){
            hits[y][x]=Boolean.TRUE;
        }
        else{
            hits[y][x]=Boolean.FALSE;
        }
    }

    public Boolean getHit(int x, int y){
        return(hits[y][x]);
    }

    public Hit sendHit(int x, int y) {
        if (hasShip(x, y)) {
            ships[y][x].addStrike();
            if (ships[y][x].getShip().isSunk()) {
                switch (ships[y][x].getShip().getLabel()) {
                    case 'D' : return Hit.DESTROYER;
                    case 'S' : return Hit.SUBMARINE;
                    case 'C' : return Hit.CARRIER;
                    case 'B' : return Hit.BATTLESHIP;
                }
            }
            else return Hit.STRIKE;
        }
        return Hit.MISS;
    }
}
