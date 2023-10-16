package br.com.victor.todolist.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
//vamos criar o banco agora com a anotacao abaixo
@Entity(name="tb_users")
//usamos o name pra nomear a tabela
public class UserModel {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    //estou criando a coluna com um nome, se n colocar ele vai pelo no da variavel
    //@Column(name = "usuario")
    //Falando q n pode username igual
    @Column(unique = true)
    private String username ;

    private String name;


    private String password;

    //Gravar a hora que criou
    @CreationTimestamp
    private LocalDateTime createdAt;
}
