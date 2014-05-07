package com.hutchdesign.colortrap.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hutchdesign.colortrap.R;
import com.hutchdesign.colortrap.model.GameBoard;
import com.hutchdesign.colortrap.model.Player;
import com.hutchdesign.colortrap.model.Tile;

public class GridAdapter extends AnimatedAdapter {
    private GameBoard gameBoard;
    private Context context;
    private View playerOneTile;
    private View playerTwoTile;

    public GridAdapter(Context c, GameBoard board) {
        super(c, R.animator.flip, 350);
        context = c;
        gameBoard = board;
        setDelay(50);
    }

    public int getCount() {
        return gameBoard.boardSize();
    }

    public Object getItem(int position) {
        return gameBoard.getGridPosition(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    /** Redraw & animate grid. */
    public void animate() {
        animate = true;
        notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View tileView = convertView;
        Tile currentTile = gameBoard.getGridPosition(position);

        if (convertView == null) {
            tileView = LayoutInflater.from(context).inflate(R.layout.tile, null);
        }

        // Show/hide player
        Player p = gameBoard.getPlayer(position);
        View playerView1 = tileView.findViewById(R.id.tile_player1);
        View playerView2 = tileView.findViewById(R.id.tile_player2);

        if (p == null) {
            playerView1.setVisibility(View.GONE);
            playerView2.setVisibility(View.GONE);

        } else if (p.isFirstPlayer()) {
            playerView1.setVisibility(View.VISIBLE);
            playerView2.setVisibility(View.GONE);
            playerOneTile = tileView;
        } else {
            playerView1.setVisibility(View.GONE);
            playerView2.setVisibility(View.VISIBLE);
            playerTwoTile = tileView;
        }

        // Handle "dead" tiles
        if (currentTile.isDisabled()) {
            tileView.setOnClickListener(null);
            tileView.setFocusable(false);
            tileView.findViewById(R.id.tile_layout).setBackgroundColor(getDisabledColor());
            return tileView;
        }

        tileView.findViewById(R.id.tile_layout).setBackgroundColor(currentTile.getColor());

        doAnimation(tileView, position, currentTile.isDisabled());
        return tileView;
    }

    @Override
    public void onAnimate(View view) {
        view.setVisibility(View.VISIBLE);
    }

    private int getDisabledColor() {
        return context.getResources().getColor(R.color.board_background);
    }

    /** @return the tile where Player 1 is currently drawn. */
    public View getPlayerOneTile() {
        return playerOneTile;
    }

    /** @return the tile where Player 1 is currently drawn. */
    public View getPlayerTwoTile() {
        return playerTwoTile;
    }

    public void hidePlayer(int player) {
        if (player == 0) {
            playerOneTile.findViewById(R.id.tile_player1).setVisibility(View.GONE);
        } else {
            playerTwoTile.findViewById(R.id.tile_player2).setVisibility(View.GONE);
        }
    }

}