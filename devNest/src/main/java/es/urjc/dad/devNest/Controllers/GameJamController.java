package es.urjc.dad.devNest.Controllers;

import es.urjc.dad.devNest.Database.Entities.UserEntity;
import es.urjc.dad.devNest.Internal_Services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class GameJamController {

    @Autowired
    private UserService userService;
    @Autowired
    private GameJamService gameJamService;
    @Autowired
    private RandomWord randomWord;     

    //region gamejam controller
    @RequestMapping("/gamejam/{gjId}")
    public String jamPage(Model model, @PathVariable long gjId) {
        UserEntity myUser = userService.getMyUser();
        model.addAttribute("userEntity", myUser);

        gameJamService.deleteEmptyTeams(gjId);
        model.addAttribute("gamejam", gameJamService.getJam(gjId));
        return "gameJamWeb";
    }


    @RequestMapping(value = "/gamejam/{gjId}/register+team")
    public ModelAndView registerTeam(@PathVariable long gjId, @RequestParam String teamname) {
        UserEntity myUser = userService.getMyUser();
        if (myUser != null) {
            gameJamService.addNewTeam(gjId, teamname, myUser);
        }
        return new ModelAndView("redirect:/gamejam/" + gjId);
    }
    //endregion

    //region register jam controller
    @GetMapping("/registerJam")
    public String goToOrganizeJam(Model model) {
        UserEntity myUser = userService.getMyUser();
        model.addAttribute("userEntity", myUser);

        randomWordAction(model);
        return "createJam";
    }

    @RequestMapping(value = "/registerGameJam")
    public ModelAndView createAJam(@RequestParam String jamName, @RequestParam String description, @RequestParam String topic, @RequestParam String sDate, @RequestParam String eDate) {
        boolean result = gameJamService.addNewJam(jamName, description, userService.getMyUser(), topic, sDate, eDate);
        if (result) {
            return new ModelAndView("redirect:/");
        } else {
            return new ModelAndView("redirect:/createJam");
        }
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