package com.codegym.games.game2048;
import java.util.Arrays;
import com.codegym.engine.cell.*;


public class Game2048 extends Game {
    private boolean isGameStopped = false;
    private static final int SIDE = 4;
    private int[][] gameField = new int[SIDE][SIDE];
    private int score = 0;
    @Override
    public void initialize() {
        setScreenSize(SIDE,SIDE);
        createGame();
        drawScene();
    }
    private void createGame(){
        int[][] blankCopy = new int[SIDE][SIDE];
        gameField = blankCopy;
        setScore(0);
        createNewNumber();
        createNewNumber(); 
    }
    private void drawScene(){
        for(int i = 0; i<SIDE; i++){
            for(int j = 0; j < SIDE; j++){
                setCellColoredNumber(i, j, gameField[j][i]);
            }
        }
    }
    private void createNewNumber(){
        int x = getRandomNumber(SIDE);
        int y = getRandomNumber(SIDE);
        if (gameField[x][y] == 0){
            if(getRandomNumber(10) == 9){
                gameField[x][y] = 4;  
            }
            else{
                gameField[x][y] = 2;
            }
        } 
        else{
            createNewNumber();
        }
        if (getMaxTileValue() == 2048 ){
            win();
        }
    }
    private Color getColorByValue(int value){
        Color temp = null;
        switch(value){
            case 0: temp = Color.WHITE;
                break;
            case 2: temp = Color.PINK;
                break;
            case 4: temp = Color.PURPLE;
                break;
            case 8: temp = Color.BLUE;
                break;
            case 16: temp = Color.CYAN;
                break;
            case 32: temp = Color.GREEN;
                break;
            case 64: temp = Color.YELLOW;
                break;
            case 128: temp = Color.LIGHTPINK;
                break;
            case 512: temp = Color.ORANGE;
                break;
            case 1024: temp = Color.RED;
                break;
            case 2048: temp = Color.MAGENTA;
                break;
        }
        return temp;
    }
    private void setCellColoredNumber(int x, int y, int value){
        Color temp = getColorByValue(value);
        if (value == 0){
            setCellValueEx(x, y, temp, "");
        }
        else{
            setCellValueEx(x, y, temp, Integer.toString(value));
        }
    }
    private boolean compressRow(int[] row){
        boolean check = true;
        int rowCopy[] = new int[row.length]; 
        for(int i = 0; i < row.length; i++){
            rowCopy[i]=row[i];
        }
        for (int i = 0; i < row.length; i++){
            for (int j = 0; j<row.length - i -1; j++){
                if (row[j]== 0){
                    row[j] = row[j+1];
                    row[j+1] = 0;
                }    
           }
       }
        if(Arrays.equals(row,rowCopy)){
            check = false;
        }    
        return check;
    }
    private boolean mergeRow(int[] row){
        boolean hasChanged = false;
        int rowCopy[] = new int[row.length]; 
        for(int i = 0; i < row.length; i++){
            rowCopy[i]=row[i];
        }
        for(int i = 0; i<row.length-1;i++){
            if((row[i] != 0) && (row[i] == row[i+1])){
                row[i] = row[i]*2;
                row[i+1] = 0;
                hasChanged = true;
                setScore(score + row[i]);
            }
        }
        return hasChanged;
    }
    public void onKeyPress(Key key){
        if (isGameStopped){
            if((key == Key.SPACE))
            {
                isGameStopped = false;
                createGame();
                drawScene();
            }
        }
        else if(!canUserMove()){
            gameOver();
        }
        else{
            if (key == Key.LEFT){
                moveLeft();
                drawScene();
            }
            else if(key == Key.RIGHT){
                moveRight();
                drawScene();
            }
            else if(key == Key.UP){
                moveUp();
                drawScene();
            }
            else if(key == Key.DOWN){
                moveDown();
                drawScene();
            }
            else{
                System.out.print("Error");
            }
        }    
    }
    private void moveLeft(){
        boolean one1 = true;
        boolean two2 = true;
        boolean three3 = true;
        int checker = 0;
        for (int i = 0; i< SIDE; i++){
            one1 = compressRow(gameField[i]);
            two2 = mergeRow(gameField[i]);
            three3 = compressRow(gameField[i]);
            if (one1||two2||three3){
                checker++;
            }
        }    
        if (checker > 0){
            createNewNumber();
        }
    }
    private void moveRight(){
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }
    private void moveUp(){
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
    }
    private void moveDown(){
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }
    private void rotateClockwise(){
        int[][] newgameField = new int[SIDE][SIDE];
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                newgameField[j][SIDE - 1 - i] = gameField[i][j];
            }
        }
        gameField = newgameField;
    }
    private int getMaxTileValue(){
        int max = 0;
        for(int i =0; i < SIDE; i++){
            for (int j = 0; j < SIDE; j++){
                if (max < gameField[i][j]){
                    max = gameField[i][j];
                }
            }
        }
        return max;
    }
    private void win(){
        isGameStopped = true;
        Color colorCell = Color.PINK;
        String message = "Congratulations! You won!!!!";
        Color colorText = Color.WHITE;
        int size = 12;
        showMessageDialog(colorCell, message, colorText, size);
    }
    private boolean canUserMove(){
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                if (gameField[y][x] == 0) {
                    return true;
                } else if (y < SIDE - 1 && gameField[y][x] == gameField[y + 1][x]) {
                    return true;
                } else if ((x < SIDE - 1) && gameField[y][x] == gameField[y][x + 1]) {
                    return true;
                }
            }
        }
        return false;
    }
    private void gameOver(){
       isGameStopped = true;
        Color colorCell = Color.RED;
        String message = "Sorry. You lost. Try again!";
        Color colorText = Color.BLACK;
        int size = 12;
        showMessageDialog(colorCell, message, colorText, size);
    }
    public void setScore(int newScore){
        score = newScore; 
    }
}

