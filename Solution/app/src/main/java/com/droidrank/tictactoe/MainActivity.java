package com.droidrank.tictactoe;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    boolean gameStarted = false;
    int totalNumberOfMovesMade = 0;

    private enum Player {PLAYER_1, PLAYER_2, NO_PLAYER}

    private Player currentPlayer = Player.NO_PLAYER;

    Button block1, block2, block3, block4, block5, block6, block7, block8, block9, restart;
    String[][] boardState = {{"", "", ""}, {"", "", ""}, {"", "", ""}};

    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        block1 = (Button) findViewById(R.id.bt_block1);
        block2 = (Button) findViewById(R.id.bt_block2);
        block3 = (Button) findViewById(R.id.bt_block3);
        block4 = (Button) findViewById(R.id.bt_block4);
        block5 = (Button) findViewById(R.id.bt_block5);
        block6 = (Button) findViewById(R.id.bt_block6);
        block7 = (Button) findViewById(R.id.bt_block7);
        block8 = (Button) findViewById(R.id.bt_block8);
        block9 = (Button) findViewById(R.id.bt_block9);
        result = (TextView) findViewById(R.id.tv_show_result);
        restart = (Button) findViewById(R.id.bt_restart_game);

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!gameStarted) {
                    gameStarted = true;
                    restart.setText(R.string.restart_button_text_in_middle_of_game);
                    changePlayerTo(Player.PLAYER_1);
                } else {
                    showRestartDialog();
                }
            }
        });

        //row 1
        setBoardOnClickListener(block1, 0, 0);
        setBoardOnClickListener(block2, 0, 1);
        setBoardOnClickListener(block3, 0, 2);

        //row 2
        setBoardOnClickListener(block4, 1, 0);
        setBoardOnClickListener(block5, 1, 1);
        setBoardOnClickListener(block6, 1, 2);

        //row 3
        setBoardOnClickListener(block7, 2, 0);
        setBoardOnClickListener(block8, 2, 1);
        setBoardOnClickListener(block9, 2, 2);
    }


    private void setBoardOnClickListener(final Button button, final int row, final int column) {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!gameStarted) {
                    return;
                }

                if (isValidMove(row, column)) {
                    updateBoard(row, column);
                    if (hasCurrentPlayerWon(row, column)) showWinnerDialog();
                    else if (totalNumberOfMovesMade >= 9) showDrawDialog();

                    if (currentPlayer == Player.PLAYER_1) {
                        button.setText(R.string.player_1_move);
                        changePlayerTo(Player.PLAYER_2);
                    } else {
                        button.setText(R.string.player_2_move);
                        changePlayerTo(Player.PLAYER_1);
                    }

                } else {
                    showInvalidMoveToast();
                }

            }
        });
    }

    private boolean isValidMove(int row, int column) {
        return boardState[row][column].equals("");
    }

    private void updateBoard(int row, int column) {

        if (currentPlayer == Player.PLAYER_1) {
            boardState[row][column] = getString(R.string.player_1_move);
        } else { //player 2
            boardState[row][column] = getString(R.string.player_2_move);
        }

        totalNumberOfMovesMade++;
    }

    //we only need to check the latest move is the winning move, hence we pass in coords
    private boolean hasCurrentPlayerWon(int row, int column) {
        String currentPlayersAnswers;

        if (currentPlayer == Player.PLAYER_1)
            currentPlayersAnswers = getString(R.string.player_1_move);
        else currentPlayersAnswers = getString(R.string.player_2_move);

        //check row
        for (int i = 0; i < boardState[row].length; i++) {
            if (!boardState[row][i].equals(currentPlayersAnswers)) break;
            if (i == 2) return true; //if it's reached this point we know the row is complete
        }

        //check column
        for (int i = 0; i < boardState.length; i++) {
            if (!boardState[i][column].equals(currentPlayersAnswers)) break;
            if (i == 2) return true;
        }

        //check diagonal
        if (boardState[0][0].equals(currentPlayersAnswers) && boardState[1][1].equals(currentPlayersAnswers)
                && boardState[2][2].equals(currentPlayersAnswers)) return true;
        else if (boardState[0][2].equals(currentPlayersAnswers) && boardState[1][1].equals(currentPlayersAnswers)
                && boardState[2][0].equals(currentPlayersAnswers)) return true;

        return false;
    }

    //switches player, changes title too (so we know who's turn it is)
    private void changePlayerTo(Player player) {
        currentPlayer = player;

        String title;
        if (currentPlayer == Player.PLAYER_1) title = getString(R.string.player_1);
        else title = getString(R.string.player_2);

        setTitle("Current player: " + title);
    }

    private void showInvalidMoveToast() {
        Toast.makeText(MainActivity.this, R.string.invalid_move, Toast.LENGTH_SHORT).show();
    }

    private void showDrawDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(R.string.draw).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                recreate();
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                recreate();
            }
        });
        builder.show();
    }

    private void showWinnerDialog() {
        String winner;

        if (currentPlayer == Player.PLAYER_1) {
            winner = getString(R.string.player_1_wins);
        } else {
            winner = getString(R.string.player_2_wins);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(winner).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                recreate();
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                recreate();
            }
        });
        builder.show();
    }

    private void showRestartDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(R.string.restart_message).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                recreate();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
