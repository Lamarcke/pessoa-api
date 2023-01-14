package pessoa.api.repository;

import org.springframework.data.repository.CrudRepository;
import pessoa.api.model.entity.Pessoa;

import java.util.List;

public interface PessoaRepository extends CrudRepository<Pessoa, Integer> {
    boolean existsById(int id);

    Pessoa findById(int id);

    List<Pessoa> findAllByIdIn(List<Integer> ids);

    // Pesquisa por nomes, não diferenciando maisculas de minusculas.
    List<Pessoa> searchAllByNomeIgnoreCase(String nome);

    List<Pessoa> searchAllByEnderecos_CidadeIgnoreCase(String cidade);

    List<Pessoa> searchAllByEnderecos_LogradouroIgnoreCase(String rua);

    List<Pessoa> searchAllByEnderecos_CepIgnoreCase(String cep);


    List<Pessoa> findAll();

}