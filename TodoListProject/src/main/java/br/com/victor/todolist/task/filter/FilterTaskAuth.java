package br.com.victor.todolist.task.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.victor.todolist.user.IUserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

//Toda classe que eu quero q o spring gerencie, eu uso o uma anotacao, neste caso vamos usar @componet
@Component
public class FilterTaskAuth extends OncePerRequestFilter {
    //VAmos chamar a classe de usuarios pra ver se ele existe ou nao
    @Autowired
    private IUserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
            //Vou ter q fazer uma verificacao para saber se as minha autorizacoes sao task, pois se n, ele manda pra todos os pacotes
        var servetpath = request.getServletPath();
        if (servetpath.equals("/tasks/")){

            var authorization = request.getHeader("Authorization");

            //Pega usuario e senha
            var authEncoded = authorization.substring("Basic".length()).trim();

            byte [] authDecoded = Base64.getDecoder().decode(authEncoded);

            var authString = new String(authDecoded);
            String [] credetials = authString.split(":");
            String username = credetials[0];
            String password = credetials[1];

            System.out.println("Authorization");
            System.out.println(username);
            System.out.println(password);

            //Valida usuario
            var user = this.userRepository.findByUsername(username);
            if (user == null){
                response.sendError(401);
            }else {
                //Valida a senha
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if (passwordVerify.verified){
                    //Segue a Viagem
                    //Esta pegando o id user e jogando pra controller
                    request.setAttribute("idUser",user.getId());
                    chain.doFilter(request, response);
                }else {
                    response.sendError(401);
                }
            }
        }else {
            chain.doFilter(request, response);
        }
    }
}
