import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import static java.lang.Math.*;

public class ChristmasTree {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_BROWN = "\u001B[33m";


    private static float sx, sy;

    private static float sdCircle(float px, float py, float r) {
        float dx = px - sx, dy = py - sy;
        return (float) (sqrt(dx * dx + dy * dy) - r);
    }

    private static float opUnion(float d1, float d2) {
        return d1 < d2 ? d1 : d2;
    }

    private static float f(float px, float py, float theta, float scale, int n) {
        float d = 0.0f;
        for (float r = 0.0f; r < 0.8f; r += 0.02f) {
            d = opUnion(d, sdCircle((float) (px + scale * r * cos(theta)), (float) (py + scale * r * Math.sin(theta)), 0.05f * scale * (0.95f - r)));
        }

        if (n > 0) {
            for (int t = -1; t <= 1; t += 2) {
                float tt = theta + t * 1.8f;
                float ss = scale * 0.9f;
                for (float r = 0.2f; r < 0.8f; r += 0.1f) {
                    d = opUnion(d, f((float) (px + scale * r * cos(theta)), (float) (py + scale * r * Math.sin(theta)), tt, ss * 0.5f, n - 1));
                    ss *= 0.8f;
                }
            }
        }

        return d;
    }

    private static int ribbon() {
        float x = ((sy % 0.1f) / 0.1f - 0.5f) * 0.5f;
        return sx >= x - 0.05f && sx <= x + 0.05f ? 1 : 0;
    }

    public static void main(String[] args) {
        int n = args.length > 0 ? atoi((args[0])) : 3;
        float zoom = (args.length > 1 ? Float.parseFloat(args[1]) : 1.0f);
        for (sy = 0.8f; sy > 0.0f; sy -= 0.02f / zoom, System.out.print('\n')) {
            for (sx = -0.35f; sx < 0.35f; sx += 0.01f / zoom) {
                if (f(0, 0, (float) (PI * 0.5f), 1.0f, n) < 0.0f) {
                    if (sy < 0.1f) {
                        System.out.print(ANSI_BROWN + '.' + ANSI_RESET);
                    } else if (ribbon() != 0) {
                        System.out.print(ANSI_RED + '=' + ANSI_RESET);
                    } else {
                        System.out.print(ANSI_GREEN + (("............................#j&o").charAt((random() & Integer.MAX_VALUE) % 32)) + ANSI_RESET);
                    }
                } else {
                    System.out.print(' ');
                }
            }
        }
        printYear();
    }

    private static int random() {
        return new Random().nextInt();
    }

    private static int atoi(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    private static void printYear() {
        int width = 150;
        int height = 30;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setFont(new Font("SansSerif", Font.BOLD, 24));

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("2020", 10, 20);

        for (int y = 0; y < height; y++) {
            StringBuilder builder = new StringBuilder();
            for (int x = 0; x < width; x++) {
                builder.append(image.getRGB(x, y) == -16777216 ? " " : "@");
            }
            System.out.println(ANSI_RED + builder + ANSI_RESET);
        }
    }

}
