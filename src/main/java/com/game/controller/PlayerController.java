package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest")
public class PlayerController {

    @Autowired
    private PlayerServiceInterface playerServiceInterface;

    @GetMapping("/players")
    public ResponseEntity<List<Player>> getAllPlayers(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                      @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                      @RequestParam(value = "order", required = false) PlayerOrder order,
                                                      @RequestParam(value = "name", required = false) String name,
                                                      @RequestParam(value = "title", required = false) String title,
                                                      @RequestParam(value = "race", required = false) Race race,
                                                      @RequestParam(value = "profession", required = false) Profession profession,
                                                      @RequestParam(value = "after", required = false) Long after,
                                                      @RequestParam(value = "before", required = false) Long before,
                                                      @RequestParam(value = "banned", required = false) Boolean banned,
                                                      @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                                      @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                                      @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                                      @RequestParam(value = "maxLevel", required = false) Integer maxLevel
    ) {

        if (pageNumber == null) pageNumber = 0;
        if (pageSize == null) pageSize = 3;
        if (order == null) order = PlayerOrder.ID;

        List<Player> playerList = new ArrayList<>();

        if (order.equals(PlayerOrder.NAME))
            playerList = playerServiceInterface.getAllPlayers().stream().sorted((p1, p2) -> p1.getName().compareTo(p2.getName())).collect(Collectors.toList());
        if (order.equals(PlayerOrder.ID))
            playerList = playerServiceInterface.getAllPlayers().stream().sorted((p1, p2) -> p1.getId().compareTo(p2.getId())).collect(Collectors.toList());
        if (order.equals(PlayerOrder.EXPERIENCE))
            playerList = playerServiceInterface.getAllPlayers().stream().sorted((p1, p2) -> p1.getExperience().compareTo(p2.getExperience())).collect(Collectors.toList());
        if (order.equals(PlayerOrder.BIRTHDAY))
            playerList = playerServiceInterface.getAllPlayers().stream().sorted((p1, p2) -> p1.getBirthday().compareTo(p2.getBirthday())).collect(Collectors.toList());

        playerList = filterPlayerList(playerList, name, title, race, profession, before, after, banned, maxExperience, minExperience, maxLevel, minLevel);

        List<Player> playerPageList;

        playerPageList = playerInPage(pageNumber, pageSize, playerList);

        return new ResponseEntity<List<Player>>(playerPageList, HttpStatus.OK);
    }

    @GetMapping("/players/count")
    public Integer count(@RequestParam(value = "name", required = false) String name,
                         @RequestParam(value = "title", required = false) String title,
                         @RequestParam(value = "race", required = false) Race race,
                         @RequestParam(value = "profession", required = false) Profession profession,
                         @RequestParam(value = "after", required = false) Long after,
                         @RequestParam(value = "before", required = false) Long before,
                         @RequestParam(value = "banned", required = false) Boolean banned,
                         @RequestParam(value = "minExperience", required = false) Integer minExperience,
                         @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                         @RequestParam(value = "minLevel", required = false) Integer minLevel,
                         @RequestParam(value = "maxLevel", required = false) Integer maxLevel) {

        List<Player> playerList = filterPlayerList(playerServiceInterface.getAllPlayers(), name, title, race, profession, before, after, banned, maxExperience, minExperience, maxLevel, minLevel);

        return playerList.size();
    }


    private List<Player> filterPlayerList(List<Player> playerList, String name, String title, Race race, Profession profession,
                                          Long before, Long after, Boolean banned, Integer maxExperience,
                                          Integer minExperience, Integer maxLevel, Integer minLevel) {
        List<Player> filterPlayerList = playerList;

        filterPlayerList = filterPlayerList.stream().filter(player -> name == null || player.getName().contains(name))
                .filter(player -> title == null || player.getTitle().contains(title))
                .filter(player -> race == null || player.getRace().equals(race))
                .filter(player -> profession == null || player.getProfession().equals(profession))
                .filter(player -> after == null || player.getBirthday().getTime() > after)
                .filter(player -> before == null || player.getBirthday().getTime() < before)
                .filter(player -> banned == null || player.getBanned().equals(banned))
                .filter(player -> minExperience == null || player.getExperience() >= minExperience)
                .filter(player -> maxExperience == null || player.getExperience() <= maxExperience)
                .filter(player -> minLevel == null || player.getLevel() >= minLevel)
                .filter(player -> maxLevel == null || player.getLevel() <= maxLevel).collect(Collectors.toList());
     /*   if (name != null)
            filterPlayerList = filterPlayerList.stream().filter(player -> player.getName().contains(name)).collect(Collectors.toList());
        if (title != null)
            filterPlayerList = filterPlayerList.stream().filter(player -> player.getTitle().contains(title)).collect(Collectors.toList());
        if (race != null)
            filterPlayerList = filterPlayerList.stream().filter(player -> player.getRace().equals(race)).collect(Collectors.toList());
        if (profession != null)
            filterPlayerList = filterPlayerList.stream().filter(player -> player.getProfession().equals(profession)).collect(Collectors.toList());
        if (after != null)
            filterPlayerList = filterPlayerList.stream().filter(player -> (player.getBirthday().getTime() >= after)).collect(Collectors.toList());
        if (before != null)
            filterPlayerList = filterPlayerList.stream().filter(player -> (player.getBirthday().getTime() <= before)).collect(Collectors.toList());
        if (banned != null)
            filterPlayerList = filterPlayerList.stream().filter(player -> (player.getBanned().equals(banned))).collect(Collectors.toList());
        if (minExperience != null)
            filterPlayerList = filterPlayerList.stream().filter(player -> player.getExperience() >= minExperience).collect(Collectors.toList());
        if (maxExperience != null)
            filterPlayerList = filterPlayerList.stream().filter(player -> player.getExperience() <= maxExperience).collect(Collectors.toList());
        if (minLevel != null)
            filterPlayerList = filterPlayerList.stream().filter(player -> player.getLevel() >= minLevel).collect(Collectors.toList());
        if (maxLevel != null)
            filterPlayerList = filterPlayerList.stream().filter(player -> player.getLevel() <= maxLevel).collect(Collectors.toList());*/

        return filterPlayerList;
    }


    private List<Player> playerInPage(Integer pageNumber, Integer pageSize, List<Player> playerList) {

        Player[] players = new Player[playerList.size()];
        playerList.toArray(players);
        int max = pageNumber * pageSize + (pageSize - 1);
        int min = max - (pageSize - 1);
        List<Player> playerPageList = new ArrayList<>();
        if (max >= players.length) max = players.length - 1;
        for (int i = min; i <= max; i++) {
            playerPageList.add(players[i]);
        }
        return playerPageList;
    }


    @PostMapping("/players")
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        ResponseEntity<Player> playerResponseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (!isPlayerValid(player)) return playerResponseEntity;

        player.setLevel((int) ((Math.sqrt(2500 + 200 * player.getExperience()) - 50) / 100));
        player.setUntilNextLevel(50 * (player.getLevel() + 1) * (player.getLevel() + 2) - player.getExperience());

        Player savedPlayer = playerServiceInterface.addPlayer(player);

        playerResponseEntity = new ResponseEntity<Player>(savedPlayer, HttpStatus.OK);

        return playerResponseEntity;
    }

    private Boolean isPlayerValid(Player player) {

        Date birthdayAfter = new GregorianCalendar(2000, 0, 1).getTime();
        Date birthdayBefore = new GregorianCalendar(3001, 0, 1).getTime();

        if (player.getName() == null || player.getExperience() == null || player.getTitle() == null ||
                player.getBirthday() == null || player.getRace() == null || player.getProfession() == null)
            return false;

        return !player.getName().equals("") && !player.getName().trim().equals("") && player.getName().length() <= 12 &&
                !player.getTitle().equals("") && player.getTitle().length() <= 30 && player.getBirthday().getTime() >= 0 &&
                player.getBirthday().getTime() > birthdayAfter.getTime() && player.getBirthday().getTime() <= birthdayBefore.getTime() &&
                player.getExperience() >= 0 && player.getExperience() <= 10000000;
    }

    @GetMapping("/players/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable("id") Long id) {
        if (id <= 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Player addPlayer = null;
        try {
            addPlayer = playerServiceInterface.getPlayerById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(addPlayer, HttpStatus.OK);
    }

    @PostMapping("/players/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable("id") Long id, @RequestBody Player player) {

        Date birthdayAfter = new GregorianCalendar(2000, 0, 1).getTime();
        Date birthdayBefore = new GregorianCalendar(3001, 0, 1).getTime();

        if (id <= 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Player updatePlayer = null;
        try {
            updatePlayer = playerServiceInterface.getPlayerById(id);
            if (player.getName() != null) updatePlayer.setName(player.getName());
            if (player.getTitle() != null) updatePlayer.setTitle(player.getTitle());
            if (player.getRace() != null) updatePlayer.setRace(player.getRace());
            if (player.getProfession() != null) updatePlayer.setProfession(player.getProfession());

            if (player.getBirthday() != null && player.getBirthday().getTime() > birthdayAfter.getTime() && player.getBirthday().getTime()<birthdayBefore.getTime()) updatePlayer.setBirthday(player.getBirthday());
            else if(player.getBirthday() != null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

            if (player.getBanned() != null) updatePlayer.setBanned(player.getBanned());

            if (player.getExperience() != null && player.getExperience()>0 && player.getExperience() < 10000000) {
                updatePlayer.setLevel((int) ((Math.sqrt(2500 + 200 * player.getExperience()) - 50) / 100));
                updatePlayer.setUntilNextLevel(50 * (updatePlayer.getLevel() + 1) * (updatePlayer.getLevel() + 2) - player.getExperience());
                updatePlayer.setExperience(player.getExperience());
            }
            else if (player.getExperience() != null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);


            updatePlayer = playerServiceInterface.addPlayer(updatePlayer);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(updatePlayer, HttpStatus.OK);
    }

    @DeleteMapping("/players/{id}")
    public ResponseEntity deletePlayer(@PathVariable Long id){

        if (id <= 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        try {
            playerServiceInterface.deletePlayerById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
