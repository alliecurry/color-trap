package co.starsky.colortrap.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import co.starsky.colortrap.R;
import co.starsky.colortrap.game.GameBoard;
import co.starsky.colortrap.model.Tile;
import co.starsky.colortrap.view.ColorHelper;

public class GridAdapter extends AnimatedAdapter {
    private GameBoard gameBoard;
    private Context context;
    private int disabledColor;
    private final TileListener listener;

    public GridAdapter(Context c, GameBoard board, TileListener listener) {
        super(c, R.animator.flip, 350);
        context = c;
        gameBoard = board;
        disabledColor = context.getResources().getColor(R.color.board_background);
        this.listener = listener;
        setDelay(50);
    }

    public int getCount() {
        return gameBoard.boardSize();
    }

    public Object getItem(int position) {
        return gameBoard.getGridPosition(position);
    }

    public long getItemId(int position) {
        return position;
    }

    /** Redraw & animate grid. */
    public void animate() {
        animate = true;
        notifyDataSetChanged();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final View tileView;
        final Tile currentTile = gameBoard.getGridPosition(position);

        if (convertView == null) {
            tileView = LayoutInflater.from(context).inflate(R.layout.tile, null);
        } else {
            tileView = convertView;
        }

        // Handle "dead" tiles
        if (currentTile.isDisabled()) {
            tileView.setOnClickListener(null);
            tileView.setFocusable(false);
            tileView.setBackgroundColor(disabledColor);
            return tileView;
        }

        tileView.setBackgroundColor(ColorHelper.getColorForTile(context, currentTile));
        tileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onTileClick(position, tileView);
            }
        });
        doAnimation(tileView, position, currentTile.isDisabled());
        return tileView;
    }

    @Override
    public void onAnimate(View view) {
        view.setVisibility(View.VISIBLE);
    }

    public interface TileListener {
        void onTileClick(int position, View view);
    }
}