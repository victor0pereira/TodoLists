package br.com.victor.todolist.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private  IUserRepository userRepository;

    //ResponseEntity ele vai retornar se deu certo ou nao, pra ser preciso criar uma if pra cada resposta de Http (suseccss or erro e tals)
    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel){

        var user = this.userRepository.findByUsername(userModel.getUsername());
        if (user != null) {
            System.out.println("Usuario Ja Existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario ja Existe");
        }

        //Crypitografando uma senha
        var passwordHashred = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        userModel.setPassword(passwordHashred);

        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
}
