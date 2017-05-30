package info.overrideandroid.connect4.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import info.overrideandroid.connect4.R;
import info.overrideandroid.connect4.rules.GameRules;
import info.overrideandroid.connect4.rules.Player;

import static info.overrideandroid.connect4.controller.GameBoardController.COLS;
import static info.overrideandroid.connect4.controller.GameBoardController.ROWS;

/**
 * Created by Rahul on 30/05/17.
 */

public class GameView extends RelativeLayout {


    /**
     * view holder for player information
     */
    private class PlayerInformation {
        public TextView name;
        public ImageView disc;
        public View turnIndicator;

        public PlayerInformation(int player_name_id, int player_disc_id, int player_indicator_id) {
            name = (TextView) findViewById(player_name_id);
            disc = (ImageView) findViewById(player_disc_id);
            turnIndicator = findViewById(player_indicator_id);
        }
    }

    private PlayerInformation player1;
    private PlayerInformation player2;

    /**
     * Array to hold all discs dropped
     */
    private ImageView[][] cells;

    private View boardView;

    private Context mContext;


    public GameView(Context context) {
        super(context);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        this.mContext = context;
        inflate(context, R.layout.game_board, this);
        player1 = new PlayerInformation(R.id.player1_name, R.id.player1_disc, R.id.player1_indicator);
        player2 = new PlayerInformation(R.id.player2_name, R.id.player2_disc, R.id.player2_indicator);
        boardView = findViewById(R.id.game_board);
    }

    public void initialize(GameRules gameRules) {
        setPlayer1(gameRules);
        setPlayer2(gameRules);
        buildCells();
    }

    /**
     * initialize player1 information with Gamerules
     */
    public void setPlayer1(GameRules gameRules) {
        player1.disc.setImageResource(gameRules.getRule(GameRules.DISC));
        player1.name.setText(gameRules.getRule(GameRules.OPPONENT) == R.string.opponent_ai ?
                mContext.getString(R.string.you) : mContext.getString(R.string.player1));
        player1.turnIndicator.setVisibility(gameRules.getRule(GameRules.FIRST_TURN) == Player.PLAYER1 ? VISIBLE : INVISIBLE);
    }

    /**
     * initialize player2 information with Gamerules
     */
    public void setPlayer2(GameRules gameRules) {
        player2.disc.setImageResource(gameRules.getRule(GameRules.DISC2));
        player2.name.setText(gameRules.getRule(GameRules.OPPONENT) == R.string.opponent_ai ?
                mContext.getString(R.string.opponent_ai) : mContext.getString(R.string.player2));
        player2.turnIndicator.setVisibility(gameRules.getRule(GameRules.FIRST_TURN) == Player.PLAYER2 ? VISIBLE : INVISIBLE);

    }

    /**
     * build and clear board cells
     */
    private void buildCells() {
        cells = new ImageView[ROWS][COLS];
        for (int r = 0; r < ROWS; r++) {
            ViewGroup row = (ViewGroup) ((ViewGroup) boardView).getChildAt(r);
            row.setClipChildren(false);
            for (int c = 0; c < COLS; c++) {
                ImageView imageView = (ImageView) row.getChildAt(c);
                imageView.setImageResource(android.R.color.transparent);
                cells[r][c] = imageView;
            }
        }
    }

    /**
     * Drop a disc of the current player at available row of selected column
     *
     * @param col
     * @param row
     */
    public void dropDisc(int col,int row) {
        final ImageView cell = cells[row][col];
        float move = -(cell.getHeight() * row + cell.getHeight() + 15);
        cell.setY(move);
        cell.setImageResource(R.drawable.red);
        TranslateAnimation anim = new TranslateAnimation(0, 0, 0, Math.abs(move));
        anim.setDuration(850);
        anim.setFillAfter(true);
        cell.startAnimation(anim);
    }

    public int colAtX(float x) {
        float colWidth = cells[0][0].getWidth();
        int col = (int) x / (int) colWidth;
        if (col < 0 || col > 6)
            return -1;
        return col;
    }
}
