package co.starsky.colortrap.model;

import co.starsky.colortrap.model.player.Player;

import java.io.Serializable;

/**
 * @author alliecurry
 */
public class GameOverData implements Serializable {
    private static final long serialVersionUID = -2417816382573317519L;

    private Player winner;
    private Player loser;
    private WinReason winReason;

    public GameOverData(final Player winner, final Player loser, final WinReason reason) {
        this.winner = winner;
        this.loser = loser;
        this.winReason = reason;
    }

    public Player getWinner() {
        return winner;
    }

    public Player getLoser() {
        return loser;
    }

    public WinReason getWinReason() {
        return winReason;
    }
}
