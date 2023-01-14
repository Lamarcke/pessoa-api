package pessoa.api.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pessoa.api.model.entity.Endereco;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "pessoa")
public class Pessoa {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "nome", nullable = false)
    @NotNull(message = "Nome não pode ser nulo")
    @NotBlank(message = "Nome não pode estar vazio")
    private String nome;

    @Column(name = "data_nascimento", nullable = false)
    @NotNull(message = "Data de nascimento não pode ser nula")
    private LocalDate dataNascimento;

    /*
     * A anotação @OneToMany mapeia uma relação unidirecional entre as
     * entidades Pessoa e Endereco.
     * Isso nos permite manipular endereços de usuarios em um repositório dedicado.
     * No contexto atual, One se refere a esta entidade (Pessoa), e Many à entidade
     * Endereco.
     * (Uma pessoa, vários endereços)
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull(message = "Pessoa deve ter pelo menos um endereço")
    @NotEmpty(message = "Pessoa deve ter pelo menos um endereço")
    private List<Endereco> enderecos;

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    @JsonGetter("dataNascimento")
    public String getDataNascimentoString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = dataNascimento.format(formatter);
        return formattedDate;
    }


    @JsonSetter("dataNascimento")
    public void setDataNascimento(String dataNascimento) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.dataNascimento = LocalDate.parse(dataNascimento, formatter);
    }


    @JsonSetter("enderecos")
    public void setEnderecos(List<Endereco> enderecos) {

        boolean enderecoPrincipalDefined = false;
        for (Endereco endereco : enderecos) {
            // Verifica se mais de um endereço é definido como principal.
            if (endereco.isEnderecoPrincipal()) {
                if (enderecoPrincipalDefined) {
                    throw new IllegalArgumentException("Apenas um endereço pode ser definido como principal");
                }
                enderecoPrincipalDefined = true;
            }
        }

        // Evita erros de referência ao utilizar a estratégia orphanRemoval.
        if (this.enderecos != null) {
            try {
                this.enderecos.clear();
                this.enderecos.addAll(enderecos);
            } catch (UnsupportedOperationException ignored) {
                // Lida com tentativa de limpar lista imutável.
                this.enderecos = enderecos;
            }

        } else {
            this.enderecos = enderecos;
        }

    }
}