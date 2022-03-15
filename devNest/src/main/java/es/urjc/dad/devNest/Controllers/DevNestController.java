package es.urjc.dad.devNest.Controllers;

import es.urjc.dad.devNest.Database.Entities.GamejamEntity;
import es.urjc.dad.devNest.Database.Entities.TeamEntity;
import es.urjc.dad.devNest.Database.Entities.UserEntity;
import es.urjc.dad.devNest.Database.Entities.VideogameEntity;
import es.urjc.dad.devNest.Internal_Services.*;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.net.URI;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;


@Controller
public class DevNestController {

    @Autowired
    private UserService userService;
    @Autowired
    private GameJamService gameJamService;
    @Autowired
    private GameService gameService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RandomWord randomWord;


    //region ./ controller
    @GetMapping("/")
    public String home(Model model) {

        //Random generator
        randomWordAction(model);

        model.addAttribute("gamejams", gameJamService.getAllJams());

        UserEntity myUser = userService.getMyUser();
        model.addAttribute("userEntity", myUser);

        return "initialWeb";
    }

    //endregion      

    //region game controller
    @RequestMapping(value = "/game/{gId}")
    public String gamePage(Model model, @PathVariable long gId) {

        UserEntity myUser = userService.getMyUser();
        model.addAttribute("userEntity", myUser);
        model.addAttribute("game", gameService.getGame(gId));
        return "gameWeb";
    }

    @RequestMapping(value = "/createGame/{tName}")
    public ModelAndView createGame(@RequestParam String _title, @RequestParam String _descrition, @RequestParam String _category, @RequestParam String _platform, @RequestParam MultipartFile _file, @PathVariable String tName) throws IOException {
        //team by name
        TeamEntity team = gameJamService.getTeam(tName);
        //current date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();

        //file
        Blob file = null;
        URI location = fromCurrentRequest().build().toUri();
        if (!_file.isEmpty()) {
            file = BlobProxy.generateProxy(_file.getInputStream(), _file.getSize());
        }

        boolean result = false;
        if(file != null)
        {
            VideogameEntity videogame = new VideogameEntity(_title, dtf.format(now), _descrition, _category, _platform, team, location.toString(), file);
            result = gameService.addNewGame(videogame, tName);
        }
        
        if (result) {
            return new ModelAndView("redirect:/");
        } else {
            return new ModelAndView("redirect:/createGame/"+tName);
        }
    }


    @RequestMapping(value = "/gamejam/{gjId}/join+team/{tId}")
    public ModelAndView joinTeam(@PathVariable long gjId, @PathVariable long tId) {
        UserEntity myUser = userService.getMyUser();
        if (myUser != null) {
            gameJamService.joinTeam(gjId, tId, myUser);
        }
        return new ModelAndView("redirect:/gamejam/" + gjId);
    }

    @RequestMapping("/registerGame/{gId}")
    public ModelAndView goCreateGame(@PathVariable long gId) {
        UserEntity myUser = userService.getMyUser();
        GamejamEntity gj = gameJamService.getJam(gId);
        
        long check = gameJamService.checkIfIsInTeam(gj, myUser);
        if(check != -1)
        {
            return new ModelAndView("redirect:/uploadGame/"+check);
        }
        else
            return new ModelAndView("redirect:/gamejam/" + gId);        
    }

    @RequestMapping("/uploadGame/{tId}")
    public String uploadGamePage(Model model, @PathVariable long tId)
    {
        UserEntity myUser = userService.getMyUser();
        model.addAttribute("userEntity", myUser);        
        model.addAttribute("tName", gameJamService.getTeam(tId).getTeamName());
        return "createGame";
    }

    @RequestMapping("/game/{gId}/addComment")
    public ModelAndView addComment(@PathVariable long gId, @RequestParam String userCommentBox)
    {
        UserEntity myUser = userService.getMyUser();
        commentService.addComment(gId, myUser.getId(), userCommentBox);
        return new ModelAndView("redirect:/game/"+gId);
    }

    @RequestMapping("/game/{gId}/answerComment+{cId}")
    public ModelAndView answerComment(@PathVariable long gId, @PathVariable long cId, @RequestParam String userCommentBox)
    {
        UserEntity myUser = userService.getMyUser();
        commentService.answerComment(gId, myUser.getId(), cId, userCommentBox);
        return new ModelAndView("redirect:/game/"+gId);
    }
    //endregion

    //region PRIVATE METHODS
    private void randomWordAction(Model model) {
        //Random generator
        model.addAttribute("topic1", randomWord.getRandomWord());
        model.addAttribute("topic2", randomWord.getRandomWord());
    }
    //endregion
}