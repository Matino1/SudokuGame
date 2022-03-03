package pl.first.firstjava;

import java.util.Random;

public enum Level {
    LOW(5),
    MEDIUM(10),
    HARD(15);

    private int amount;

    Level(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void removeFields(final SudokuBoard board) {
        Random random = new Random();
        for (int i = 0; i < amount; i++) {
            int x = random.nextInt(9);
            int y = random.nextInt(9);
            if (board.get(x, y) != 0) {
                board.set(x, y, 0);
            } else {
                i--;
            }
        }
    }
}
