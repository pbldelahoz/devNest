package es.urjc.dad.devNest.Internal_Services;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import es.urjc.dad.devNest.Database.Entities.UserEntity;
import es.urjc.dad.devNest.Database.Repositories.UserRepository;

@Component
@SessionScope
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private UserEntity myUser;

    public boolean login(String username, String password)
    {
        Optional<UserEntity> u = userRepository.findByAlias(username);

        if(u.isPresent())
        {
            if(u.get().getPassword().equals(password))
            {
                myUser = u.get();
                return true;
            }
            else
            {
                myUser = null;
                return false;
            }
        }
        else
        {
            return false;
        }       
    }

    public boolean register(String username, String password, String email)
    {
        Optional<UserEntity> u = userRepository.findByAlias(username);

        if(!u.isPresent())
        {
            myUser = new UserEntity(username, password, email);
            userRepository.save(myUser);
            return true; 
        }
        else
        {
            return false;
        }            
    }

    public void logout(HttpSession httpSession)
    {
        myUser = null;
        httpSession.invalidate();
    }

    public List<UserEntity> getAllUsers()
    {
        return userRepository.findAll();
    }

    public UserEntity getMyUser()
    {
        return myUser;
    }
}