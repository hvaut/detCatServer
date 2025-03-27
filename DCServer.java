public class DCServer extends Server {

    private List<Player> players;
    private List<Game> games;

    public DCServer(int port) {
        super(port);

        players = new List<>();
        games = new List<>();
    }

    public void processNewConnection(String ip, int port) {
        send(ip, port, "+OK Willkommen auf Detonating Cats");
    }

    public void processClosingConnection(String ip, int port) {
        Player player = getPlayer(ip, port);

        if (player != null) {
            removePlayer(player);
        }
    }

    public void processMessage(String ip, int port, String message) {
        String[] data = message.split(" ");

        // make sure the array is not empty
        if (data.length < 1) {
            send(ip, port, "-ERR Wrong command");
            return;
        }

        // handle every command
        if (data[0].equalsIgnoreCase("LOGIN") && data.length == 2) {
            // login command: LOGIN <Username>
            if (!data[1].isBlank() && data[1].length() >= 3 && data[1].length() <= 16) {
                if (getPlayer(ip, port) == null) {
                    if (getPlayer(data[1]) == null) {
                        addPlayer(new Player(ip, port, data[1]));
                        send(ip, port, "+OK Logged in");
                    } else {
                        send(ip, port, "-ERR Name already taken");
                    }
                } else {
                    send(ip, port, "-ERR Already logged in");
                }
            } else {
                send(ip, port, "-ERR Invalid name");
            }
        } else if (data[0].equalsIgnoreCase("LOGOUT") && data.length == 1) {
            // logout command: LOGOUT
            Player player = getPlayer(ip, port);
            if (player != null) {
                removePlayer(player);
                send(ip, port, "+OK Logged out");
            } else {
                send(ip, port, "-ERR Not logged in");
            }
        } else if (data[0].equalsIgnoreCase("HOST") && data.length == 2) {
            // host command: HOST <Code>
            Player player = getPlayer(ip, port);
            if (player != null) {
                if (player.getGame() == null) {
                    if (getGame(data[1]) == null) {
                        Game game = new Game(data[1], this);
                        game.addPlayer(player);
                        games.append(game);
                        send(ip, port, "+OK Game hosted");
                    } else {
                        send(ip, port, "-ERR Invalid id");
                    }
                } else {
                    send(ip, port, "-ERR Already in a game");
                }
            } else {
                send(ip, port, "-ERR Not logged in");
            }
        } else if (data[0].equalsIgnoreCase("START") && data.length == 1) {
            // start command: START
            Player player = getPlayer(ip, port);
            if (player != null) {
                Game game = player.getGame();
                if (game != null) {
                    // fill the pile
                    for (int i = 0; i < 4; i++) {
                        game.getPile().append(new SkipCard());
                        for (int j = 0; j < 4; j++) {
                            game.getPile().append(new CatCard());
                        }
                    }
                    // shuffle the pile
                    game.getPile().shuffle();
                    // fill every players hand
                    game.getPlayers().toFirst();
                    while (game.getPlayers().hasAccess()) {
                        Player current = game.getPlayers().getContent();
                        // add 1 defuse card
                        current.addCard(new DefuseCard());
                        // add 7 random cards from the pile
                        for (int i = 0; i < 7; i++) {
                            game.getPile().toLast();
                            current.addCard(game.getPile().getContent());
                            game.getPile().remove();
                        }
                        game.getPlayers().next();
                    }
                    // add bombs to the pile
                    for (int i = 0; i < game.getPlayers().getAnzahl() - 1; i++) {
                        game.getPile().append(new DetCatCard());
                    }
                    // add defuses to the pile
                    game.getPile().append(new DefuseCard());
                    game.getPile().append(new DefuseCard());
                    // shuffle the pile
                    game.getPile().shuffle();
                    // start the first turn
                    player.getGame().changeTurn();
                    send(ip, port, "-OK Started the game");
                } else {
                    send(ip, port, "-ERR Not in a game");
                }
            } else {
                send(ip, port, "-ERR Not logged in");
            }
        } else if (data[0].equalsIgnoreCase("JOIN") && data.length == 2) {
            // join command: JOIN <Code>
            Player player = getPlayer(ip, port);
            if (player != null) {
                if (player.getGame() == null) {
                    Game game = getGame(data[1]);
                    if (game != null) {
                        game.addPlayer(player);
                        send(ip, port, "+OK Joined the game");
                    } else {
                        send(ip, port, "-ERR Invalid id");
                    }
                } else {
                    send(ip, port, "-ERR Already in a game");
                }
            } else {
                send(ip, port, "-ERR Not logged in");
            }
        } else if (data[0].equalsIgnoreCase("QUIT") && data.length == 1) {
            // quit command: QUIT
            Player player = getPlayer(ip, port);
            if (player != null) {
                if (player.getGame() != null) {
                    player.getGame().removePlayer(player);
                    player.setGame(null);
                    send(ip, port, "-OK Left the game");
                } else {
                    send(ip, port, "-ERR Not in a game");
                }
            } else {
                send(ip, port, "-ERR Not logged in");
            }
        } else if (data[0].equalsIgnoreCase("PLACE") && data.length == 2) {
            // place command: PLACE <CardId>
            Player player = getPlayer(ip, port);
            if (player != null) {
                if (player.getGame() != null) {
                    if (player.getGame().getTurn() == player) {
                        // find and validate the card
                        Card card = null;
                        player.getCards().toFirst();
                        while (player.getCards().hasAccess()) {
                            if (player.getCards().getContent().getId().equalsIgnoreCase(data[1])) {
                                card = player.getCards().getContent();
                                break;
                            }
                            player.getCards().next();
                        }
                        if (card != null) {
                            // remove the card
                            player.removeCard(card);
                            // send protocol messages to everyone inside the game
                            player.getGame().getPlayers().toFirst();
                            while (player.getGame().getPlayers().hasAccess()) {
                                Player current = player.getGame().getPlayers().getContent();
                                send(current.getIp(), current.getPort(), "PLACE " + player.getName() + " " + card.getId());
                                player.getGame().getPlayers().next();
                            }
                            // execute the card
                            card.doEf(player.getGame());
                        } else {
                            send(ip, port, "-ERR You do not have the card");
                        }
                        send(ip, port, "-OK Placed a card");
                    } else {
                        send(ip, port, "-ERR Not your turn");
                    }
                } else {
                    send(ip, port, "-ERR Not in a game");
                }
            } else {
                send(ip, port, "-ERR Not logged in");
            }
        } else if (data[0].equalsIgnoreCase("TAKE") && data.length == 1) {
            // take command: TAKE
            Player player = getPlayer(ip, port);
            if (player != null) {
                if (player.getGame() != null) {
                    if (player.getGame().getTurn() == player) {
                        Card card = player.getGame().popPile();
                        player.addCard(card);
                        // send protocol messages to everyone inside the game
                        player.getGame().getPlayers().toFirst();
                        while (player.getGame().getPlayers().hasAccess()) {
                            Player current = player.getGame().getPlayers().getContent();
                            send(current.getIp(), current.getPort(), "TAKE " + player.getName());
                            player.getGame().getPlayers().next();
                        }
                        // check card and handle accordingly
                        if (card instanceof DetCatCard) {
                            // check for defuse cards
                            Card defuseCard = null;
                            player.getCards().toFirst();
                            while (player.getCards().hasAccess()) {
                                if (player.getCards().getContent().getId().equalsIgnoreCase(data[1])) {
                                    defuseCard = player.getCards().getContent();
                                    break;
                                }
                                player.getCards().next();
                            }
                            if (defuseCard != null) {
                                // use the defuse card
                                send(ip, port, "BOMB");
                            } else {
                                // kill the player
                                player.setAlive(false);
                                // check how many players are alive and determine whether to stop the game or continue
                                int alive = 0;
                                player.getGame().getPlayers().toFirst();
                                while (player.getGame().getPlayers().hasAccess()) {
                                    if (player.getGame().getPlayers().getContent().getAlive()) {
                                        alive++;
                                    }
                                    // protocol message
                                    send(ip, port, "DEATH " + player.getName());
                                    player.getGame().getPlayers().next();
                                }
                                if (alive < 1) {
                                    if (alive == 1) {
                                        // game has a winner
                                        Player winner = player.getGame().getPlayers().getNextLeb();
                                        // send protocol messages to everyone inside the game
                                        player.getGame().getPlayers().toFirst();
                                        while (player.getGame().getPlayers().hasAccess()) {
                                            Player current = player.getGame().getPlayers().getContent();
                                            send(current.getIp(), current.getPort(), "WIN " + winner.getName());
                                            player.getGame().getPlayers().next();
                                        }
                                    }
                                    // remove all players from the game
                                    player.getGame().getPlayers().toFirst();
                                    while (player.getGame().getPlayers().hasAccess()) {
                                        Player current = player.getGame().getPlayers().getContent();
                                        current.setGame(null);
                                        // protocol message
                                        send(current.getIp(), current.getPort(), "QUIT " + current.getName());
                                        player.getGame().getPlayers().remove();
                                    }
                                    // remove the game
                                    removeGame(player.getGame());
                                }
                            }
                        } else {
                            // go to the next turn
                            player.getGame().changeTurn();
                        }
                        send(ip, port, "-OK Took a card");
                    } else {
                        send(ip, port, "-ERR Not your turn");
                    }
                } else {
                    send(ip, port, "-ERR Not in a game");
                }
            } else {
                send(ip, port, "-ERR Not logged in");
            }
        } else if (data[0].equalsIgnoreCase("DEFUSE") && data.length == 2) {
            // defuse command: DEFUSE <Index>
            int index = -1;
            try {
                index = Integer.parseInt(data[1]);
            } catch (NumberFormatException e) {
            }
            Player player = getPlayer(ip, port);
            if (player != null) {
                if (player.getGame() != null) {
                    if (player.getGame().getTurn() == player) {
                        // find and validate the cards
                        Card cardDefuse = null, cardBomb = null;
                        player.getCards().toFirst();
                        while (player.getCards().hasAccess()) {
                            if (player.getCards().getContent() instanceof DefuseCard) {
                                cardDefuse = player.getCards().getContent();
                            } else if (player.getCards().getContent() instanceof DetCatCard) {
                                cardBomb = player.getCards().getContent();
                            }
                            // break condition to save some performance
                            if (cardDefuse != null && cardBomb != null) {
                                break;
                            }
                            player.getCards().next();
                        }
                        if (cardDefuse != null && cardBomb != null) {
                            // remove the cards
                            player.removeCard(cardBomb);
                            player.removeCard(cardDefuse);
                            // send protocol messages to everyone inside the game
                            player.getGame().getPlayers().toFirst();
                            while (player.getGame().getPlayers().hasAccess()) {
                                Player current = player.getGame().getPlayers().getContent();
                                send(current.getIp(), current.getPort(), "PLACE " + player.getName() + " " + cardDefuse.getId());
                                player.getGame().getPlayers().next();
                            }
                            // execute the defuse card
                            cardDefuse.doEf(player.getGame());
                            // insert the bomb card
                            try {
                                player.getGame().getPile().insert(cardBomb, (int) (index / 100.0 * player.getGame().getPile().getLength()));
                            } catch (IndexOutOfBoundsException e) {
                                player.getGame().getPile().append(cardBomb);
                                // send(ip, port, "-ERR Index is invalid and card has been added to the top");
                            }
                            // go to the next turn
                            player.getGame().changeTurn();
                        } else {
                            send(ip, port, "-ERR You do not have the cards");
                        }
                        send(ip, port, "-OK Placed a card");
                    } else {
                        send(ip, port, "-ERR Not your turn");
                    }
                } else {
                    send(ip, port, "-ERR Not in a game");
                }
            } else {
                send(ip, port, "-ERR Not logged in");
            }
        } else {
            send(ip, port, "-ERR Wrong command");
        }
    }

    /**
     * Methode addGame fügt das Spiel hinzu
     *
     * @param player das Spiel das hinzugefügt werden soll
     */
    public void addGame(Game game) {
        games.append(game);
    }

    /**
     * Methode removeGame entfernt das Spiel aus der Spielliste
     *
     * @param game das Spiel das entfernt werden soll
     */
    public void removeGame(Game game) {
        games.toFirst();
        while (games.hasAccess()) {
            if (games.getContent().getId() == game.getId()) {
                games.remove();
                return;
            }
            games.next();
        }
    }

    /**
     * Methode getGame gibt das gesuchte Spiel zurück
     *
     * @param id des gesuchten Spiels
     * @return das Spiel mit der gesuchten id
     */
    public Game getGame(String id) {
        games.toFirst();
        while (games.hasAccess()) {
            if (games.getContent().getId().equalsIgnoreCase(id))
                return games.getContent();
            games.next();
        }
        return null;
    }

    /**
     * Methode addPlayer fügt den Spieler hinzu
     *
     * @param player der Spieler der hinzugefügt werden soll
     */
    public void addPlayer(Player player) {
        players.append(player);
    }

    /**
     * Methode removePlayer entfernt den Spieler aus der Spielerliste und allen Spiellisten
     *
     * @param player der Spieler der entfernt werden soll
     */
    public void removePlayer(Player player) {
        players.toFirst();
        while (players.hasAccess()) {
            if (players.getContent().getIp().equalsIgnoreCase(player.getIp()) && players.getContent().getPort() == player.getPort()){
                players.remove();

                // remove player from games
                if (player.getGame() != null) {
                    player.getGame().removePlayer(player);
                }
                return;
            }
            players.next();
        }
    }

    /**
     * Methode getPlayer gibt den gesuchten Spieler zurück
     *
     * @param ip des gesuchten Spielers
     * @param port des gesuchten Spielers
     * @return der Spieler mit den gesuchten Verbindungsdaten
     */
    public Player getPlayer(String ip, int port) {
        players.toFirst();
        while (players.hasAccess()) {
            if (players.getContent().getIp().equalsIgnoreCase(ip) && players.getContent().getPort() == port)
                return players.getContent();
            players.next();
        }
        return null;
    }

    /**
     * Methode getPlayer gibt den gesuchten Spieler zurück
     *
     * @param name des gesuchten Spielers
     * @return der Spieler mit dem gesuchten Namen
     */
    public Player getPlayer(String name) {
        players.toFirst();
        while (players.hasAccess()) {
            if (players.getContent().getName().equalsIgnoreCase(name))
                return players.getContent();
            players.next();
        }
        return null;
    }

    /**
     * Methode getGames gibt alle Games zurück
     *
     * @return alle Games auf dem Server
     */
    public List<Game> getGames() {
        return games;
    }

    /**
     * Methode getPlayers gibt alle Spieler zurück
     *
     * @return gibt alle aktuell angemeldeten Spieler zurück
     */
    public List<Player> getPlayers() {
        return players;
    }

}