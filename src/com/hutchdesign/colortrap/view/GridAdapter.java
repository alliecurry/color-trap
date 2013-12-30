package com.hutchdesign.colortrap.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.hutchdesign.colortrap.controller.TileClickListener;
import com.hutchdesign.colortrap.model.Player;
import com.hutchdesign.colortrap.R;
import com.hutchdesign.colortrap.model.Tile;
import com.hutchdesign.colortrap.model.GameBoard;

public class GridAdapter extends BaseAdapter {
    private GameBoard gameBoard;
    private Context context;
    private View.OnClickListener tileClickListener;

    public GridAdapter(Context c, GameBoard board) {
        context = c;
        gameBoard = board;
        tileClickListener = new TileClickListener();
    }

    public int getCount() {
        return gameBoard.boardSize();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View tileView = convertView;

        if (convertView == null) {
            tileView = LayoutInflater.from(context).inflate(R.layout.tile, null);

            // Update background color
            Tile currentTile = gameBoard.getGridPosition(position);

            if (currentTile.isDisabled()) {
                tileView.setOnClickListener(null);
                tileView.setFocusable(false);
                return tileView;
            }

            tileView.findViewById(R.id.tile_layout).setBackgroundColor(currentTile.getColor());
            tileView.setOnClickListener(tileClickListener);

            // Show/hide player
            Player p = gameBoard.getPlayer(position);
            View playerView1 = tileView.findViewById(R.id.tile_player1);
            View playerView2 = tileView.findViewById(R.id.tile_player2);

            if (p == null) {
                playerView1.setVisibility(View.GONE);
                playerView2.setVisibility(View.GONE);
            } else {
                boolean isFirstPlayer = p.isFirstPlayer();
                playerView1.setVisibility(isFirstPlayer ? View.VISIBLE : View.GONE);
                playerView2.setVisibility(isFirstPlayer ? View.GONE : View.VISIBLE);
            }
        }

        return tileView;
    }

}