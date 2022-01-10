package cabbageroll.notrisdefect.minecraft.unusedcode;

/*
public class SpectatorCode {
    public static void doit(ItemStack mapItem, MapView mapView, boolean isSingleplayer, boolean isRunning, List<Player> playerList, Map<Player,Table> playerboards, int playersAlive, List<Player> spectators) {
        boolean dontRender;
        
        if (Constants.iKnowWhatIAmDoing) {

            mapView = Bukkit.createMap(Bukkit.getWorld("world"));
            // BROKEJN ItemStack mapI = new ItemStack(Material.MAP, 1, mapView.getId());

            /*
             * playerList.length==1 maxScale=4 showStats=T/F true showOthers=T/F true
             * playerList.length>=2 maxScale=2 showStats=T/F true showOthers=T/F true
             * playerList.length>=3 maxScale=2 showStats=F false showOthers=T/F false
             * playerList.length>=5 maxScale=1 showStats=F false showOthers=F false
             

            mapView.getRenderers().clear();
            mapView.addRenderer(new MapRenderer() {
                @SuppressWarnings("deprecation")
                @Override
                public void render(MapView map, MapCanvas mapCanvas, Player player) {
                    if (dontRender) {
                        return;
                    }
                    int iter = 0;

                    if (isSingleplayer) {
                        if (!playerboards.get(playerList.get(0)).getGameover()) {
                            Table ta = playerboards.get(playerList.get(iter));
                            int pixelSize = 4;
                            Point topLeftCorner = new Point(64 - GameLogic.STAGESIZEX / 2 * pixelSize,
                                    64 - GameLogic.STAGESIZEY / 4 * pixelSize);
                            iter++;

                            for (int i = 20; i < 40; i++) {
                                for (int j = 0; j < 10; j++) {
                                    for (int k = 0; k < pixelSize; k++) {
                                        for (int l = 0; l < pixelSize; l++) {
                                            mapCanvas.setPixel(topLeftCorner.x + j * pixelSize + k,
                                                    topLeftCorner.y + (i - 20) * pixelSize + l,
                                                    MapPalette.matchColor(Constants.intToColor(ta.getStage()[i][j])));
                                        }
                                    }
                                }
                            }

                            for (int i = 0; i < GameLogic.NEXTPIECESMAX; i++) {
                                for (int j = 0; j < 4; j++) {
                                    for (int k = 0; k < 4; k++) {
                                        for (int l = 0; l < pixelSize; l++) {
                                            for (int m = 0; m < pixelSize; m++) {
                                                mapCanvas.setPixel(
                                                        topLeftCorner.x + GameLogic.STAGESIZEX * pixelSize
                                                                + pixelSize * 3 + j * pixelSize + l,
                                                        topLeftCorner.y + i * 4 * pixelSize + k * pixelSize + m,
                                                        MapPalette.matchColor(Constants.intToColor(7)));
                                            }
                                        }
                                    }
                                }

                                for (Point point : ta.getPiece(ta.getNextPieces().get(i), 0)) {
                                    for (int j = 0; j < pixelSize; j++) {
                                        for (int k = 0; k < pixelSize; k++) {
                                            mapCanvas.setPixel(
                                                    topLeftCorner.x + GameLogic.STAGESIZEX * pixelSize + pixelSize * 3
                                                            + point.x * pixelSize + j,
                                                    topLeftCorner.y + i * 4 * pixelSize + point.y * pixelSize + k,
                                                    MapPalette.matchColor(
                                                            Constants.intToColor(ta.getNextPieces().get(i))));
                                        }
                                    }
                                }
                            }
                            mapCanvas.drawText(0, 0, MinecraftFont.Font, playerList.get(0).getName());
                        }

                    } else if (playersAlive == 2) {
                        for (Player player2 : playerList) {
                            if (!playerboards.get(player2).getGameover()) {
                                Table ta = playerboards.get(playerList.get(iter));
                                int pixelSize = 2;
                                Point topLeftCorner = new Point(32 + 64 * iter - GameLogic.STAGESIZEX / 2 * pixelSize,
                                        64 - GameLogic.STAGESIZEY / 4 * pixelSize);
                                iter++;

                                for (int i = 20; i < 40; i++) {
                                    for (int j = 0; j < 10; j++) {
                                        for (int k = 0; k < pixelSize; k++) {
                                            for (int l = 0; l < pixelSize; l++) {
                                                mapCanvas.setPixel(topLeftCorner.x + j * pixelSize + k,
                                                        topLeftCorner.y + (i - 20) * pixelSize + l, MapPalette
                                                                .matchColor(Constants.intToColor(ta.getStage()[i][j])));
                                            }
                                        }
                                    }
                                }

                                for (int i = 0; i < GameLogic.NEXTPIECESMAX; i++) {
                                    for (int j = 0; j < 4; j++) {
                                        for (int k = 0; k < 4; k++) {
                                            for (int l = 0; l < pixelSize; l++) {
                                                for (int m = 0; m < pixelSize; m++) {
                                                    mapCanvas.setPixel(
                                                            topLeftCorner.x + GameLogic.STAGESIZEX * pixelSize
                                                                    + pixelSize * 3 + j * pixelSize + l,
                                                            topLeftCorner.y + i * 4 * pixelSize + k * pixelSize + m,
                                                            MapPalette.matchColor(Constants.intToColor(7)));
                                                }
                                            }
                                        }
                                    }

                                    for (Point point : ta.getPiece(ta.getNextPieces().get(i), 0)) {
                                        for (int j = 0; j < pixelSize; j++) {
                                            for (int k = 0; k < pixelSize; k++) {
                                                mapCanvas.setPixel(
                                                        topLeftCorner.x + GameLogic.STAGESIZEX * pixelSize
                                                                + pixelSize * 3 + point.x * pixelSize + j,
                                                        topLeftCorner.y + i * 4 * pixelSize + point.y * pixelSize + k,
                                                        MapPalette.matchColor(
                                                                Constants.intToColor(ta.getNextPieces().get(i))));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else if (playersAlive == 3) {
                        mapCanvas.drawText(0, 0, MinecraftFont.Font, "not implemented yet");
                        mapCanvas.drawText(30, 0, MinecraftFont.Font, "3 players alive");
                    } else if (playersAlive == 4) {
                        mapCanvas.drawText(0, 0, MinecraftFont.Font, "not implemented yet");
                        mapCanvas.drawText(30, 0, MinecraftFont.Font, "4 players alive");
                    } else if (playersAlive == 5) {
                        mapCanvas.drawText(0, 0, MinecraftFont.Font, "room is too big");
                        mapCanvas.drawText(30, 0, MinecraftFont.Font, "works only up to");
                        mapCanvas.drawText(60, 0, MinecraftFont.Font, "4 players");
                    }

                    dontRender = true;
                }
            });
            for (Player player : spectators) {
                // player.getInventory().setItemInOffHand(mapI);
                player.sendMap(mapView);
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!isRunning) {
                        this.cancel();
                    } else {
                        dontRender = false;
                    }
                }
            }.runTaskTimer(Main.gs, 0, 40);

        }
    }
}
 */
