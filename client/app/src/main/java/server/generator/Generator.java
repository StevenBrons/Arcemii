package server.generator;

import java.util.ArrayList;

import shared.entities.Entity;
import shared.general.Level;
import shared.tiles.Empty;
import shared.tiles.Tile;
import shared.tiles.Wall;

public class Generator {

  public Generator() {

  }

  public static Level testLevel() {
    Tile[][] tiles = new Tile[5][5];

    for (int x = 0; x < tiles.length; x++) {
      for (int y = 0; y < tiles[0].length; y++) {
        tiles[x][y] = Math.random() > 0.5 ? new Wall() : new Empty();
      }
    }

    Level l = new Level(tiles,new ArrayList<Entity>());
    return l;
  }

  Level generate() {
    return null;
  }
}
