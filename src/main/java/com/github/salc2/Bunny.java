package com.github.salc2;

public class Bunny {
    Vector position;
    double speedX;
    double speedY;
    final Frame frame;

    public Bunny(Vector position, double speedX, double speedY, Frame frame) {
        this.position = position;
        this.speedX = speedX;
        this.speedY = speedY;
        this.frame = frame;
    }

    public void update(Bound bounds, double delta){
        this.position.x += (this.speedX * delta);
        this.position.y += (this.speedY * delta);
        this.speedY += Main.GRAVITY;
        if (this.position.x > bounds.right)
        {
            this.speedX *= -1;
            this.position.x = bounds.right;
        }
        else if (this.position.x < bounds.left)
        {
            this.speedX *= -1;
            this.position.x = bounds.left;
        }

        if (this.position.y > bounds.bottom)
        {
            this.speedY *= -0.85;
            this.position.y = bounds.bottom;
            if (Math.random() > 0.5)
            {
                this.speedY -= Math.random() * 6;
            }
        }
        else if (this.position.y < bounds.top)
        {
            this.speedY = 0;
            this.position.y = bounds.top;
        }
    }
}

class Frame{
    final double sx;
    final double sy;
    final double sw;
    final double sh;

    public Frame(double sx, double sy, double sw, double sh) {
        this.sx = sx;
        this.sy = sy;
        this.sw = sw;
        this.sh = sh;
    }
}

class Vector {
    double x;
    double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }
}

class Bound{
    double right = 0;
    double left = 0;
    double top = 16;
    double bottom = 0;
    public void update(double right, double bottom){
        this.right = right;
        this.bottom = bottom;
    }
}
