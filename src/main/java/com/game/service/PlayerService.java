package com.game.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PlayerService implements PlayerServiceInterface {

    @Autowired
    private PlayerRepository repository;

    @Override
    public Player addPlayer(Player player) {
        Player savedPlayer = repository.save(player);
        return savedPlayer;
    }

    @Override
    public List<Player> getAllPlayers() {
        return repository.findAll();
    }

    @Override
    public Player getPlayerById(Long playerById) {
        return repository.findById(playerById).get();
    }

    @Override
    public void deletePlayerById(Long playerById) {
        repository.deleteById(playerById);
    }

    @Override
    public Player updatePlayerById(Player player, Long id) {
        repository.saveAndFlush(player);
        return null;
    }
}
