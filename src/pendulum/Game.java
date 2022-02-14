package pendulum;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {

  private final int WIDTH = 720, HEIGHT = WIDTH / 12 * 9;
  private boolean running;
  private Thread thread;

  public Game() {
    createWindow();
  }

  public synchronized void start() {
    thread = new Thread(this);
    thread.start();
    running = true;
  }

  public void createWindow() {
    JFrame jf = new JFrame("Pendulum");

    jf.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    jf.setMaximumSize(new Dimension(WIDTH, HEIGHT));
    jf.setMinimumSize(new Dimension(WIDTH, HEIGHT));

    jf.setDefaultCloseOperation(3);
    jf.setResizable(false);
    jf.setLocationRelativeTo(null);
    jf.add(this);
    jf.setVisible(true);

    start();
  }

  @Override
  public void run() {
    long lastTime = System.nanoTime();
    double amoutOfTicks = 60.0;
    double ns = 1000000000 / amoutOfTicks;
    double delta = 0;
    long timer = System.currentTimeMillis();
    int frames = 0;
    while (running) {
      long now = System.nanoTime();
      delta += (now - lastTime) / ns;
      lastTime = now;
      while (delta >= 1) {
        tick();
        delta--;
      }
      if (running)
        render();
      frames++;

      if (System.currentTimeMillis() - timer > 1000) {
        timer += 1000;
        System.out.println("FPS: " + frames);
        frames = 0;
      }
      try {
        Thread.sleep(1);
      } catch (InterruptedException ignored) {
      }
    }
  }

  private void tick() {

  }

  private void render() {
    BufferStrategy bs = this.getBufferStrategy();
    if (bs == null) {
      this.createBufferStrategy(3);
      return;
    }

    Graphics2D g2d = (Graphics2D)bs.getDrawGraphics();
    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    g2d.setColor(Color.black);
    g2d.fillRect(0, 0, WIDTH, HEIGHT);


    g2d.dispose();
    bs.show();
  }

  public static void main(String[] args) {
    new Game();
  }
}
