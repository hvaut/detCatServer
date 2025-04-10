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
                        // add the player to the list
                        addPlayer(new Player(ip, port, data[1]));
                        // finish command
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
                // remove the player from the list
                removePlayer(player);
                // finish command
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
                        // create a game and add the player
                        Game game = new Game(data[1], this);
                        game.addPlayer(player);
                        player.setGame(game);
                        // add game to the list
                        games.append(game);
                        // send protocol messages                        
                        send(ip, port, "JOIN " + player.getName());
                        send(ip, port, "PLAYER " + game.getPlayers().getString());
                        // finish command
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
        } else if (data[0].equalsIgnoreCase("JOIN") && data.length == 2) {
            // join command: JOIN <Code>
            Player player = getPlayer(ip, port);
            if (player != null) {
                if (player.getGame() == null) {
                    Game game = getGame(data[1]);
                    if (game != null) {
                        if (game.getTurn() == null) {
                            if (game.getPlayers().getLength() < 4) {
                                // add player to the game
                                game.addPlayer(player);
                                player.setGame(game);
                                // update players for everyone inside the game
                                sendToGame(game, "JOIN " + player.getName());
                                // send everyone inside the game to the player
                                send(ip, port, "PLAYER " + game.getPlayers().getString());
                                // finish command
                                send(ip, port, "+OK Joined the game");
                                // check whether the game should start
                                if (game.getPlayers().getLength() == 4) {
                                    // fill the pile
                                    for (int i = 0; i < 6; i++) {
                                        game.getPile().append(new SkipCard());
                                        for (int j = 0; j < 6; j++) {
                                            game.getPile().append(new CatCard());
                                        }
                                    }
                                    for (int i = 0; i < 5; i++) {
                                        game.getPile().append(new SeeFutureCard());
                                        game.getPile().append(new AttackCard());
                                    }
                                    for (int i = 0; i < 4; i++) {
                                        game.getPile().append(new ShuffleCard());
                                    }
                                    // shuffle the pile
                                    game.getPile().shuffle();
                                    // fill every players hand
                                    game.getPlayers().toFirst();
                                    while (game.getPlayers().hasAccess()) {
                                        Player current = game.getPlayers().getContent();
                                        current.setAlive(true);
                                        // add 1 defuse card
                                        current.addCard(new DefuseCard());
                                        // add 7 random cards from the pile
                                        for (int i = 0; i < 7; i++) {
                                            game.getPile().toLast();
                                            current.addCard(game.getPile().getContent());
                                            game.getPile().remove();
                                        }
                                        // send card protocol message
                                        String cards = "";
                                        current.getCards().toFirst();
                                        while (current.getCards().hasAccess()) {
                                            cards += current.getCards().getContent().getId() + " ";
                                            current.getCards().next();
                                        }
                                        send(current.getIP(), current.getPort(), "CARD " + cards);
                                        game.getPlayers().next();
                                    }
                                    // add bombs to the pile
                                    for (int i = 0; i < game.getPlayers().getLength() - 1; i++) {
                                        game.getPile().append(new DetCatCard());
                                    }
                                    // add defuses to the pile
                                    game.getPile().append(new DefuseCard());
                                    game.getPile().append(new DefuseCard());
                                    // shuffle the pile
                                    game.getPile().shuffle();
                                    // start the first turn
                                    nextTurn(game);
                                }
                            } else {
                                send(ip, port, "-ERR Game is full");
                            }
                        } else {
                            send(ip, port, "-ERR Game started already");
                        }
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
                    // remove the player
                    removePlayerFromGame(player);
                    // finish command
                    send(ip, port, "+OK Left the game");
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
                    if (player.isAlive()) {
                        if (player.getGame().getTurn() == player) {
                            // check for detonating cat cards
                            Card bombCard = null;
                            player.getCards().toFirst();
                            while (player.getCards().hasAccess()) {
                                if (player.getCards().getContent() instanceof DetCatCard) {
                                    bombCard = player.getCards().getContent();
                                    break;
                                }
                                player.getCards().next();
                            }
                            if (bombCard == null) {
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
                                    // update cards on player hand
                                    String cards = "";
                                    player.getCards().toFirst();
                                    while (player.getCards().hasAccess()) {
                                        cards += player.getCards().getContent().getId() + " ";
                                        player.getCards().next();
                                    }
                                    send(ip, port, "CARD " + cards);
                                    // send protocol messages to everyone inside the game
                                    sendToGame(player.getGame(), "PLACE " + player.getName() + " " + card.getId());
                                    // execute the card
                                    card.onPlace(player.getGame());
                                    // finish command
                                    send(ip, port, "+OK Card placed");
                                } else {
                                    send(ip, port, "-ERR You do not have the card");
                                }
                            } else {
                                send(ip, port, "-ERR You can only defuse");
                            }
                        } else {
                            send(ip, port, "-ERR Not your turn");
                        }
                    } else {
                        send(ip, port, "-ERR You are dead");
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
                    if (player.isAlive()) {
                        if (player.getGame().getTurn() == player) {
                            // check for detonating cat cards
                            Card bombCard = null;
                            player.getCards().toFirst();
                            while (player.getCards().hasAccess()) {
                                if (player.getCards().getContent() instanceof DetCatCard) {
                                    bombCard = player.getCards().getContent();
                                    break;
                                }
                                player.getCards().next();
                            }
                            if (bombCard == null) {
                                // get top card from the pile
                                Card card = player.getGame().popPile();
                                player.addCard(card);
                                // send protocol messages to everyone inside the game
                                sendToGame(player.getGame(), "TAKE " + player.getName());
                                // update cards on player hand
                                String cards = "";
                                player.getCards().toFirst();
                                while (player.getCards().hasAccess()) {
                                    cards += player.getCards().getContent().getId() + " ";
                                    player.getCards().next();
                                }
                                send(ip, port, "CARD " + cards);
                                // check card and handle accordingly
                                if (card instanceof DetCatCard) {
                                    // check for defuse cards
                                    Card defuseCard = null;
                                    player.getCards().toFirst();
                                    while (player.getCards().hasAccess()) {
                                        if (player.getCards().getContent() instanceof DefuseCard) {
                                            defuseCard = player.getCards().getContent();
                                            break;
                                        }
                                        player.getCards().next();
                                    }
                                    if (defuseCard != null) {
                                        // use the defuse card (send protocol message)
                                        sendToGame(player.getGame(), "BOMB " + player.getGame().getTurn().getName());
                                    } else {
                                        // kill the player
                                        player.setAlive(false);
                                        // go to the next turn
                                        Game game = player.getGame();
                                        nextTurn(game);
                                        // send death protocol message
                                        sendToGame(game, "DEATH " + player.getName());
                                        // check how many players are alive and determine whether to stop the game or continue
                                        checkGameForEnd(game);
                                    }
                                } else {
                                    // go to the next turn
                                    nextTurn(player.getGame());
                                }
                                // finish command
                                send(ip, port, "+OK Took a card");
                            } else {
                                send(ip, port, "-ERR You can only defuse");
                            }
                        } else {
                            send(ip, port, "-ERR Not your turn");
                        }
                    } else {
                        send(ip, port, "-ERR You are dead");
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
                    if (player.isAlive()) {
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
                                // update cards on player hand
                                String cards = "";
                                player.getCards().toFirst();
                                while (player.getCards().hasAccess()) {
                                    cards += player.getCards().getContent().getId() + " ";
                                    player.getCards().next();
                                }
                                send(ip, port, "CARD " + cards);
                                // send protocol messages to everyone inside the game
                                sendToGame(player.getGame(), "PLACE " + player.getName() + " " + cardDefuse.getId());
                                // execute the defuse card
                                cardDefuse.onPlace(player.getGame());
                                // insert the bomb card
                                try {
                                    player.getGame().getPile().insert(cardBomb, (int) (index / 100.0 * player.getGame().getPile().getLength()));
                                } catch (IndexOutOfBoundsException e) {
                                    player.getGame().getPile().append(cardBomb);
                                    // send(ip, port, "-ERR Index is invalid and card has been added to the top");
                                }
                                // go to the next turn
                                nextTurn(player.getGame());
                                // finish command
                                send(ip, port, "+OK Bomb defused");
                            } else {
                                send(ip, port, "-ERR You do not have the cards");
                            }
                        } else {
                            send(ip, port, "-ERR Not your turn");
                        }
                    } else {
                        send(ip, port, "-ERR You are dead");
                    }
                } else {
                    send(ip, port, "-ERR Not in a game");
                }
            } else {
                send(ip, port, "-ERR Not logged in");
            }
        } else if (data[0].equalsIgnoreCase("PILE") && data.length == 1) {
            // take command: PILE
            Player player = getPlayer(ip, port);
            if (player != null) {
                if (player.getGame() != null) {
                    // send protocol message
                    send(ip, port, "PILE " + player.getGame().getPile().getLength());
                    // finish command
                    send(ip, port, "+OK Pile count");
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

    private void sendToGame(Game game, String message) {
        game.getPlayers().toFirst();
        while (game.getPlayers().hasAccess()) {
            Player current = game.getPlayers().getContent();
            send(current.getIP(), current.getPort(), message);
            game.getPlayers().next();
        }
    }

    private void nextTurn(Game game) {
        // go to the next turn
        game.changeTurn();
        // make sure the next turn is valid
        if (game.getTurn() != null) {
            // send turn update to everyone inside the game
            sendToGame(game, "TURN " + game.getTurn().getName());
        }
    }

    private void removePlayerFromGame(Player player) {
        // remove player from games
        Game game = player.getGame();
        // validate game
        if (game != null) {
            // update players for everyone inside the game
            sendToGame(game, "QUIT " + player.getName());
            // remove the player
            game.removePlayer(player);
            player.setGame(null);
            // check if player is the current turn
            if (game.getTurn() == player) {
                // go to the next turn
                nextTurn(game);
            }
            // check if the game should end
            checkGameForEnd(game);
        }
    }

    private void checkGameForEnd(Game game) {
        // check if there is someone inside the game
        if (!game.getPlayers().isEmpty()) {
            // return if the game has not started yet
            if (game.getTurn() != null) {
                // check how many players are alive and determine whether to stop the game or continue
                int alive = 0;
                game.getPlayers().toFirst();
                while (game.getPlayers().hasAccess()) {
                    Player current = game.getPlayers().getContent();
                    if (current.isAlive()) {
                        alive++;
                    }
                    game.getPlayers().next();
                }
                if (alive <= 1) {
                    if (alive == 1) {
                        // game has a winner
                        Player winner = game.getPlayers().getNext();
                        // send protocol messages to everyone inside the game
                        sendToGame(game, "WIN " + winner.getName());
                    }
                    // remove all players from the game
                    game.getPlayers().toFirst();
                    while (game.getPlayers().hasAccess()) {
                        Player current = game.getPlayers().getContent();
                        current.setGame(null);
                        // protocol message
                        send(current.getIP(), current.getPort(), "QUIT " + current.getName());
                        game.getPlayers().remove();
                    }
                    // remove the game
                    removeGame(game);
                }
            }
        } else {
            // remove the game
            removeGame(game);
        }
    }

    /**
<<<<<<< HEAD
<<<<<<< HEAD
     * Methode addGame fÃ¼gt das Spiel hinzu
=======
     * Fügt das Spiel hinzu
>>>>>>> 5e6deeba9af568c7c72864dcfbe5908bb4071211
=======
     * Fügt das Spiel hinzu
>>>>>>> 5e6deeba9af568c7c72864dcfbe5908bb4071211
     *
<<<<<<< HEAD
<<<<<<< HEAD
     * @param player das Spiel das hinzugefÃ¼gt werden soll
=======
     * @param game das Spiel das hinzugefügt werden soll
>>>>>>> 5e6deeba9af568c7c72864dcfbe5908bb4071211
=======
     * @param game das Spiel das hinzugefügt werden soll
>>>>>>> 5e6deeba9af568c7c72864dcfbe5908bb4071211
     */
    public void addGame(Game game) {
        games.append(game);
    }

    /**
     * Entfernt das Spiel aus der Spielliste
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
<<<<<<< HEAD
<<<<<<< HEAD
     * Methode getGame gibt das gesuchte Spiel zurÃ¼ck
=======
     * Gibt das gesuchte Spiel zurück
>>>>>>> 5e6deeba9af568c7c72864dcfbe5908bb4071211
=======
     * Gibt das gesuchte Spiel zurück
>>>>>>> 5e6deeba9af568c7c72864dcfbe5908bb4071211
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
<<<<<<< HEAD
<<<<<<< HEAD
     * Methode addPlayer fÃ¼gt den Spieler hinzu
=======
     * Fügt den Spieler hinzu
>>>>>>> 5e6deeba9af568c7c72864dcfbe5908bb4071211
=======
     * Fügt den Spieler hinzu
>>>>>>> 5e6deeba9af568c7c72864dcfbe5908bb4071211
     *
     * @param player der Spieler der hinzugefÃ¼gt werden soll
     */
    public void addPlayer(Player player) {
        players.append(player);
    }

    /**
     * Entfernt den Spieler aus der Spielerliste und allen Spiellisten
     *
     * @param player der Spieler der entfernt werden soll
     */
    public void removePlayer(Player player) {
        players.toFirst();
        while (players.hasAccess()) {
            if (players.getContent().getIP().equalsIgnoreCase(player.getIP()) && players.getContent().getPort() == player.getPort()) {
                players.remove();
                // remove player from games
                removePlayerFromGame(player);
                return;
            }
            players.next();
        }
    }

    /**
<<<<<<< HEAD
<<<<<<< HEAD
     * Methode getPlayer gibt den gesuchten Spieler zurÃ¼ck
=======
     * Gibt den gesuchten Spieler zurück
>>>>>>> 5e6deeba9af568c7c72864dcfbe5908bb4071211
=======
     * Gibt den gesuchten Spieler zurück
>>>>>>> 5e6deeba9af568c7c72864dcfbe5908bb4071211
     *
     * @param ip   des gesuchten Spielers
     * @param port des gesuchten Spielers
     * @return der Spieler mit den gesuchten Verbindungsdaten
     */
    public Player getPlayer(String ip, int port) {
        players.toFirst();
        while (players.hasAccess()) {
            if (players.getContent().getIP().equalsIgnoreCase(ip) && players.getContent().getPort() == port)
                return players.getContent();
            players.next();
        }
        return null;
    }

    /**
<<<<<<< HEAD
<<<<<<< HEAD
     * Methode getPlayer gibt den gesuchten Spieler zurÃ¼ck
=======
     * Gibt den gesuchten Spieler zurück
>>>>>>> 5e6deeba9af568c7c72864dcfbe5908bb4071211
=======
     * Gibt den gesuchten Spieler zurück
>>>>>>> 5e6deeba9af568c7c72864dcfbe5908bb4071211
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
<<<<<<< HEAD
<<<<<<< HEAD
     * Methode getGames gibt alle Games zurÃ¼ck
=======
     * Gibt alle Games zurück
>>>>>>> 5e6deeba9af568c7c72864dcfbe5908bb4071211
=======
     * Gibt alle Games zurück
>>>>>>> 5e6deeba9af568c7c72864dcfbe5908bb4071211
     *
     * @return alle Games auf dem Server
     */
    public List<Game> getGames() {
        return games;
    }

    /**
<<<<<<< HEAD
<<<<<<< HEAD
     * Methode getPlayers gibt alle Spieler zurÃ¼ck
=======
     * Gibt alle Spieler zurück
>>>>>>> 5e6deeba9af568c7c72864dcfbe5908bb4071211
=======
     * Gibt alle Spieler zurück
>>>>>>> 5e6deeba9af568c7c72864dcfbe5908bb4071211
     *
     * @return gibt alle aktuell angemeldeten Spieler zurÃ¼ck
     */
    public List<Player> getPlayers() {
        return players;
    }

}