package co.starsky.colortrap.model;

import android.content.Context;
import android.util.Log;
import co.starsky.colortrap.R;
import co.starsky.colortrap.util.Shuffle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public final class GameBoard implements Serializable {
    private static final long serialVersionUID = -945511170198587443L;
    private static String TAG = GameBoard.class.getSimpleName();

    /** Default amount of tiles (including disabled). */
    private static final int DEFAULT_ROW_NUM = 6;
    private static final int DEFAULT_COL_NUM = 5;
    public static final float COL_NUM = (float) DEFAULT_COL_NUM;

    static final int PLAYER_ONE = 0;
    static final int PLAYER_TWO = 1;
    private int playerTurn;
    /** Tile positions disabled by default. */
    private static final int[] DEFAULT_DISABLED_TILES = {0, 2, 3, 4, 25};
    private Tile[][] mTiles;
    private int rowNum;
    private int colNum;
    private Player[] players;
    private State currentState;
    private Mode gameMode;

    public GameBoard(Context c, Mode mode) {
        setupTiles(c);
        players = new Player[2];
        players[PLAYER_ONE] = players[playerTurn] = new Player(-1, true);
        players[PLAYER_TWO] = mode == Mode.HOTSEAT ? new Player(-1, false) : new ComputerPlayer(-1, false);
        playerTurn = 0;
        setGameMode(mode);
        setCurrentState(State.PLACE_PIECE);
    }

    public boolean setupPlayer(int position) {
        if (playerTurn == PLAYER_ONE) {
            players[playerTurn].setPosition(position);
            playerTurn = otherPlayer(playerTurn);
            if (gameMode == Mode.COMPUTER) {
                setUpCompPlayer();
            }
            return true;
        }

        if (validStartSpace(position)) {
            players[playerTurn].setPosition(position);
            playerTurn = otherPlayer(playerTurn);
            setCurrentState(State.TURN_PLAYER);
            return true;
        }

        return false;
    }

    private void setUpCompPlayer() {
        Stack<Integer> startMoves = new Stack<Integer>();
        for(int position = 0; position < boardSize(); position++){
            if(validStartSpace(position) &&
               validStartColor(position))
            {
                startMoves.add(position);
            }
        }
        Collections.shuffle(startMoves);
        players[playerTurn].setPosition(startMoves.pop());
        setCurrentState(State.TURN_PLAYER);
        playerTurn = otherPlayer(playerTurn);

    }

    // Second players start space can't be a winning position
    private boolean validStartSpace(int position){
        return !(position == players[PLAYER_ONE].getPosition() ||
                getGridPosition(position).getColor() == getPlayerTileColor(players[PLAYER_ONE]) ||
                getValidMoves(position).contains(players[PLAYER_ONE].getPosition()) ||
                getGridPosition(position).isDisabled());
    }

    private boolean validStartColor(int position){
        for(int i : getValidMoves(players[PLAYER_ONE].getPosition())){
            if (getGridPosition(i).getColor() == getGridPosition(position).getColor()){
                return false;
            }
        }
        return true;
    }
    /* Initial Board setup Methods

    Creates board of row x col size Tiles, applies initial randomized colors and disabled tiles
    Board is a 2d List of Tiles (List of ArrayList of Tiles). Accessed similar to coordinates starting at 0,0x
    in top left corner
    */
    private void setupTiles(Context c) {
        colNum = DEFAULT_COL_NUM;
        rowNum = DEFAULT_ROW_NUM;
        createTiles(rowNum, colNum, Shuffle.shuffleInt(c.getResources().getIntArray(R.array.grid_colors2), boardSize()));
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
        for (int position : disabled) {
            getGridPosition(position).disable();
        }
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
    //Use the position sent for player (Can be temp position)
    public boolean checkWin(int position){
        return sameColor(position) || sameSpace(position) || noMoves(otherPlayer(playerTurn));
    }

    private boolean sameColor(int position){
        return getGridPosition(position).getColor() == getPlayerTileColor(players[otherPlayer(playerTurn)]);
        //return getPlayerTileColor(players[PLAYER_ONE]) == getPlayerTileColor(players[PLAYER_TWO]);
    }
    private boolean sameSpace(int position){
        return position == players[otherPlayer(playerTurn)].getPosition();
        //return players[PLAYER_ONE].getPosition() == players[PLAYER_TWO].getPosition();
    }
    private boolean noMoves(int player){
        return getValidMoves(players[player].getPosition()).isEmpty();
    }


    /** Makes a move for the current player a the given grid position. */
    public Triplet<Integer, Integer, Integer> takeTurn(int position) {
        int oldPosition = players[playerTurn].getPosition();
        List validMoves = getValidMoves(oldPosition);

        if (validMoves.contains(position)) {
            getGridPosition(oldPosition).disable();
            players[playerTurn].setPosition(position);
            Triplet<Integer, Integer, Integer> t = new Triplet<Integer, Integer, Integer>(playerTurn, oldPosition, position);

            if (checkWin(players[playerTurn].getPosition())) {
                Log.d(TAG, "A winner is player " + playerTurn);
                setCurrentState(State.GAME_OVER);
                return t;
            }

            playerTurn = otherPlayer(playerTurn);
            if (gameMode == Mode.COMPUTER && !players[playerTurn].isFirstPlayer()) {
                takeCompTurn();
            }
            return t;
        }
        return null;
    }

    private void takeCompTurn() {
        List<Integer> validMoves;
        validMoves = getValidMoves(players[playerTurn].getPosition());

        for(int position : validMoves){
            if(checkWin(position)){
                takeTurn(position);
                return;
            }
        }
        Collections.shuffle(validMoves);
        Triplet<Integer, Integer, Integer> turn = takeTurn(validMoves.get(0));
    }

    private int otherPlayer(int player) {
        return player == 0 ? 1 : 0;
    }
    //Probably a fancier way to do this.
    private List <Integer> getValidMoves(int position){
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
            if(!getGridPosition(i).isDisabled()){
                validMoves.add(i);
                break;
            }
        }
        return validMoves;
    }

    public void setGameMode(Mode currentMode) {
        this.gameMode = currentMode;
    }

    /** @return the name of the current Player. */
    public String getCurrentPlayerName() {
        return players[playerTurn].getName();
    }

    public Mode getMode() {
        return gameMode;
    }
}