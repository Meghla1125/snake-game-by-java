import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
public class SnakeGame extends JFrame {
    public SnakeGame() {
        setTitle("Snake Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        add(new Board());
        pack();
        setLocationRelativeTo(null);
    }
    public static void main(String[] args) { EventQueue.invokeLater(() -> new SnakeGame().setVisible(true)); }
}
class Board extends JPanel implements ActionListener {
    static final int SIZE = 24, COLS = 28, ROWS = 22, W = SIZE * COLS, H = SIZE * ROWS;
    final java.util.List<Point> snake = new ArrayList<>();
    final Random rand = new Random();
    final javax.swing.Timer timer = new javax.swing.Timer(150, this);
    Point food;
    int dx = 1, dy = 0, nextDx = 1, nextDy = 0, score = 0;
    boolean running = true, paused = false;
    Board() {
        setPreferredSize(new Dimension(W, H));
        setBackground(new Color(18, 24, 32));
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int k = e.getKeyCode();
                if (k == KeyEvent.VK_ENTER) start();
                if (k == KeyEvent.VK_SPACE && running) paused = !paused;
                if ((k == KeyEvent.VK_UP || k == KeyEvent.VK_W) && dy != 1) turn(0, -1);
                if ((k == KeyEvent.VK_DOWN || k == KeyEvent.VK_S) && dy != -1) turn(0, 1);
                if ((k == KeyEvent.VK_LEFT || k == KeyEvent.VK_A) && dx != 1) turn(-1, 0);
                if ((k == KeyEvent.VK_RIGHT || k == KeyEvent.VK_D) && dx != -1) turn(1, 0);
                repaint();
            }
        });
        start();
    }
    void start() {
        snake.clear();
        int x = COLS / 2, y = ROWS / 2;
        Collections.addAll(snake, new Point(x, y), new Point(x - 1, y), new Point(x - 2, y));
        dx = nextDx = 1; dy = nextDy = 0; score = 0; running = true; paused = false;
        spawnFood();
        timer.start();
    }
    void turn(int x, int y) { nextDx = x; nextDy = y; }
    public void actionPerformed(ActionEvent e) {
        if (!running || paused) return;
        dx = nextDx; dy = nextDy;
        Point head = snake.get(0), next = new Point(head.x + dx, head.y + dy);
        if (next.x < 0 || next.x >= COLS || next.y < 0 || next.y >= ROWS || snake.contains(next))
            { running = false; timer.stop(); repaint(); return; }
        snake.add(0, next);
        if (next.equals(food)) { score += 10; spawnFood(); }
        else snake.remove(snake.size() - 1);
        repaint();
    }
    void spawnFood() {
        do food = new Point(rand.nextInt(COLS), rand.nextInt(ROWS));
        while (snake.contains(food));
    }
    protected void paintComponent(Graphics gr) {
        super.paintComponent(gr);
        Graphics2D g = (Graphics2D) gr;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(28, 36, 47));
        for (int x = 0; x <= W; x += SIZE) g.drawLine(x, 0, x, H);
        for (int y = 0; y <= H; y += SIZE) g.drawLine(0, y, W, y);
        g.setColor(new Color(238, 83, 80));
        g.fillOval(food.x * SIZE + 4, food.y * SIZE + 4, SIZE - 8, SIZE - 8);
        for (int i = 0; i < snake.size(); i++) {
            Point p = snake.get(i);
            g.setColor(i == 0 ? new Color(92, 213, 129) : new Color(63, 176, 103));
            g.fillRoundRect(p.x * SIZE + 3, p.y * SIZE + 3, SIZE - 6, SIZE - 6, 8, 8);
        }
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 18));
        g.drawString("Score: " + score, 16, 28);
        g.setFont(new Font("SansSerif", Font.PLAIN, 13));
        g.drawString("Arrows/WASD: move   Space: pause   Enter: restart", 16, H - 14);
        if (paused) message(g, "Paused", "Press SPACE to continue");
        if (!running) message(g, "Game Over", "Press ENTER to restart");
    }
    void message(Graphics2D g, String title, String sub) {
        g.setColor(new Color(0, 0, 0, 170));
        g.fillRect(0, 0, W, H);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 42));
        g.drawString(title, (W - g.getFontMetrics().stringWidth(title)) / 2, H / 2);
        g.setFont(new Font("SansSerif", Font.PLAIN, 18));
        g.drawString(sub, (W - g.getFontMetrics().stringWidth(sub)) / 2, H / 2 + 36);
    }
}
