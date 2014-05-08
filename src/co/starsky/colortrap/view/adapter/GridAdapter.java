package co.starsky.colortrap.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import co.starsky.colortrap.R;
import co.starsky.colortrap.game.GameBoard;
import co.starsky.colortrap.model.Tile;

public class GridAdapter extends AnimatedAdapter {
    private GameBoard gameBoard;
    private Context context;
    private int disabledColor;

    public GridAdapter(Context c, GameBoard board) {
        super(c, R.animator.flip, 350);
        context = c;
        gameBoard = board;
        disabledColor = context.getResources().getColor(R.color.board_background);
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

        // Handle "dead" tiles
        if (currentTile.isDisabled()) {
            tileView.setOnClickListener(null);
            tileView.setFocusable(false);
            tileView.setBackgroundColor(disabledColor);
            return tileView;
        }

        tileView.setBackgroundColor(currentTile.getColor());

        doAnimation(tileView, position, currentTile.isDisabled());
        return tileView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public void onAnimate(View view) {
        view.setVisibility(View.VISIBLE);
    }

    public interface TileListener {
        public Tile getTile(int position);
    }
}