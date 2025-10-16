import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage; // Importa para trabalhar com imagens.


public class Player extends GameObject {
	public int life;
	
	private float iFrameSeconds = 0;
	private final float shootDelay = 0.5f;
	private float shootTimer = 0;
	
	private BufferedImage sprites[];
	private int imgIndex = 0;
	private float changeSpriteDelay = 0.1f;
	private float changeSpriteTimer = changeSpriteDelay;

	public Player(Vec2D pos, Vec2D size,
	              Vec2D direction, int speed,
	              BufferedImage sprite, Color fallback_color,
		      int life) {
		super(pos, size, direction, speed, sprite, fallback_color);
		this.life = life;
	}
	public Player(
			Vec2D pos, BufferedImage img1,
			BufferedImage img2, BufferedImage img3) {
		this(
			pos, new Vec2D(75, 75),
			new Vec2D(0, -1), 500, 
			img1, Color.GREEN,
			5
		);
		
		this.sprites = new BufferedImage[]{img1, img2, img3};
	}

	@Override
	public void update(float delta) {
		if (iFrameSeconds > 0) {
			iFrameSeconds -= delta;
		}

		if (shootTimer > 0) {
			shootTimer -= delta;
		}
		if (direction.x != 0) {
			if (changeSpriteTimer > 0) {
				changeSpriteTimer -= delta;
			} else {
				if (imgIndex != sprites.length - 1) {
					imgIndex++;
				}
				changeSpriteTimer = changeSpriteDelay;
			}
		} else {
			changeSpriteTimer = changeSpriteDelay;
			imgIndex = 0;
		}
	};
	
	@Override
	public void draw(Graphics2D g2) {
		if (sprite == null) {
			draw_fallback(g2);
		} else if (this.direction.x >= 0) {
			g2.drawImage(
				sprite,
				(int) pos.x, (int) pos.y,
				(int) size.x, (int) size.y,
				null
			);
		} else {
			g2.drawImage(
				sprite,
				(int) pos.x + (int)size.x, (int) pos.y,
				(int) size.x * -1, (int) size.y,
				null
			);
		}
	}
	
	public void move(float velocity) {
		this.pos.x += velocity;
		this.sprite = this.sprites[imgIndex];
	}
	
	public void makeInvencible(float seconds) {
		iFrameSeconds = seconds;
	}
	
	public Boolean canShoot() {
		return shootTimer <= 0;
	}
	
	public void resetShootTimer() {
		shootTimer = shootDelay;
	}

	public boolean takeDamage() {
		if (this.life > 1 && this.iFrameSeconds <= 0) {
			this.life--;
			makeInvencible(1);
			return false;
		} else if (this.life == 1) {
			this.life--;
			return true;
		}

		return false;
	}

	public void heal(int lifeToHeal) {
		this.life += lifeToHeal;
	}

	public void resetLife() {
		this.life = 3;
	}
}