package com.game.service;


import com.game.entity.Player;

import java.util.List;

public interface PlayerServiceInterface {

    public Player addPlayer(Player player);

    public List<Player> getAllPlayers();

    public Player getPlayerById(Long playerById);

    public void deletePlayerById(Long playerById);

    public Player updatePlayerById(Player player, Long id);

}
