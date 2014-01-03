package com.hutchdesign.colortrap.model;

import android.content.Context;
import com.hutchdesign.colortrap.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public final class GameBoard {
    /** Default amount of tiles (including disabled). */
    private static final int DEFAULT_ROW_NUM = 6;
    private static final int DEFAULT_COL_NUM = 5;
    static final int PLAYER_ONE = 0;
    static final int PLAYER_TWO = 1;
    /** Tile positions disabled by default. */
    private static final int[] DEFAULT_DISABLED_TILES = {0, 2, 3, 4, 25};
    private Tile[][] mTiles;
    private int rowNum;
    private int colNum;
    private Player[] players;
    private State currentState = State.PLACE_PIECE1;

    public GameBoard(Context c){
        setupTiles(c);
        players = new Player[2];
        players[PLAYER_ONE] = null;
        players[PLAYER_TWO] = null;

        //setupPlayers(c);
    }

    public boolean setupPlayer(int player, int position) {
        if(player == PLAYER_ONE){
            players[player] = new Player(position, true);
            return true;
        }
        if(validStartSpace(position)){
            players[player] = new Player(position, false);
            return true;
        }
        return false;
    }

    // Second players start space can't be a winning position
    private boolean validStartSpace(int position){
        if(position == players[PLAYER_ONE].getPosition() ||
        getGridPosition(position).getColor() == getPlayerTileColor(players[PLAYER_ONE])){
            return false;
        }
        return true;
    }
    /*Initial Board setup Methods

    Creates board of row x col size Tiles, applies initial randomized colors and disabled tiles
    Board is a 2d List of Tiles (List of ArrayList of Tiles). Accessed similar to coordinates starting at 0,0
    in top left corner
    */
    private void setupTiles(Context c) {
        colNum = DEFAULT_COL_NUM;
        rowNum = DEFAULT_ROW_NUM;
        createTiles(rowNum, colNum, generateColors(c));
        assignDisabledTiles();
    }

    private void createTiles(int rowAmount, int colAmount, Stack<Integer> colors) {
        mTiles = new Tile[rowAmount][colAmount];
        for (int i = 0; i < rowAmount; ++i) {
            for (int j = 0; j < colAmount; ++j) {
                mTiles[i][j] = new Tile(colors.pop());
            }
        }
    }

    private void assignDisabledTiles() {
        assignDisabledTiles(DEFAULT_DISABLED_TILES);
    }

    private void assignDisabledTiles(int[] disabled) {
        for(int i=0; i<disabled.length; ++i) {
            getGridPosition(disabled[i]).disable();
        }
    }

    /** Creates a randomized stack of the color palette. */
    private Stack<Integer> generateColors(Context c) {
        int[] colors = getColors(c);
        Stack<Integer> colorsStack = new Stack<Integer>();

        // Convert colors array to a stack of colors equal to the amount of colors
        int i = 0;
        while(colorsStack.size() < boardSize()) {
            colorsStack.add(colors[i]);
            i = (i == colors.length - 1) ? 0 : i+1;
        }

        Collections.shuffle(colorsStack); // randomize colors
        return colorsStack;
    }

    /** @return the default color palette in the form of resource ("R") values. */
    private static int[] getColors(Context c) {
        return c.getResources().getIntArray(R.array.grid_colors);
    }


    /** Utility methods and getter/setters */

    public int getColNum(){
        return colNum;
    }

    public int getRowNum(){
        return rowNum;
    }

    //Gets tile position based on 1d position for utility purposes
    public Tile getGridPosition(int position){
        return mTiles[(position/colNum)]
        [(position%colNum)];
    }

    public int getTileRow(int position){return position/colNum;}
    public int getTileCol(int position){return position%colNum;}

    public int boardSize(){
        return colNum*rowNum;
    }

    public Tile getTile(int row, int col){
        return mTiles[(row)][(col)];
    }

    public Player getPlayer(int position) {
        if(players[PLAYER_ONE] != null && players[PLAYER_ONE].getPosition() == position){
                return players[PLAYER_ONE];
            }
        if(players[PLAYER_TWO] != null && players[PLAYER_TWO].getPosition() == position){
            return players[PLAYER_TWO];
        }
        return null;
    }

    private int getPlayerTileColor(Player player){
        return getGridPosition(player.getPosition()).getColor();
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    //Check the 3 win conditions (Same color squares, same space, or no valid moves)
    public boolean checkWin(int player){
        if(sameColor() || sameSpace() || noMoves(otherPlayer(player))){
            return true;
        }
        return false;
    }

    private boolean sameColor(){
        return getPlayerTileColor(players[PLAYER_ONE]) == getPlayerTileColor(players[PLAYER_TWO]) ? true : false;
    }
    private boolean sameSpace(){
        return players[PLAYER_ONE].getPosition() == players[PLAYER_TWO].getPosition() ? true : false;
    }
    private boolean noMoves(int player){
        return getValidMoves(players[player].getPosition()).isEmpty() ? true:false;
    }

    public boolean takeTurn(int player, int position) {
        List validMoves;
        validMoves = getValidMoves(players[player].getPosition());
        if(validMoves.contains(position)){
            getGridPosition(players[player].getPosition()).disable();
            players[player].setPosition(position);
            if(checkWin(player)){
                System.out.println("A winner is player " + player);
                //Win and reset?
            }
            return true;
        }
        return false;
    }
    private int otherPlayer(int player) {
        return player == 0 ? 1 : 0;
    }
    //Probably a fancier way to do this.
    private List getValidMoves(int position){
        System.out.println("Current position = " + position);
        List<Integer> validMoves = new ArrayList();
        //Right
        for(int i = position + 1; i%colNum != 0; i++){
            if(!getGridPosition(i).isDisabled()){
                validMoves.add(i);
                break;
            }
        }
        //Left
        for(int i = position - 1; (i+1)%colNum > 0; i--){
            System.out.println("left move i = " + i);
            if(!getGridPosition(i).isDisabled()){
                validMoves.add(i);
                break;
            }
        }
        //Up
        for(int i = position - colNum; i >= 0; i = i - colNum){
            if(!getGridPosition(i).isDisabled()){
                validMoves.add(i);
                break;
            }
        }
        //Down
        for(int i = position + colNum; i < boardSize(); i = i + colNum){
            System.out.println("Down move i = " + i);
            if(!getGridPosition(i).isDisabled()){
                validMoves.add(i);
                break;
            }
        }
        return validMoves;
    }
}