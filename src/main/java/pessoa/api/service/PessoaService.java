package pessoa.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pessoa.api.model.entity.Pessoa;
import pessoa.api.model.request.PessoaUpdateRequest;
import pessoa.api.repository.PessoaRepository;
import pessoa.api.utils.PessoaSearchOptions;

import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class PessoaService {
    @Autowired
    private PessoaRepository pessoaRepository;

    private boolean pessoaExists(Pessoa pessoa) {
        return pessoaRepository.existsById(pessoa.getId());
    }

    public Pessoa registerPessoa(Pessoa pessoa) {
        if (pessoa.getId() != null && this.pessoaExists(pessoa)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Uma pessoa com o " +
                    "mesmo ID já está cadastrada.");
        }

        try {
            return this.pessoaRepository.save(pessoa);
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data de nascimento " +
                    "deve estar no formato dd-MM-yyyy.");

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao cadastrar pessoa", e);
        }
    }

    public Pessoa getPessoa(int id) {
        try {
            return this.pessoaRepository.findById(id);
        } catch (Exception e) {
            String errorMessage = String.format("Erro ao recuperar informações da Pessoa de ID: %d", id);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, e);
        }

    }

    public List<Pessoa> getPessoas() {
        List<Pessoa> results;
        try {
            results = this.pessoaRepository.findAll();
        } catch (Exception e) {
            String errorMessage = String.format("Erro ao recuperar informações de pessoas cadastradas.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, e);
        }

        if (results == null || results.size() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma pessoa cadastrada.");
        }
        return results;
    }

    public List<Pessoa> getPessoas(List<Integer> ids) {
        try {
            return this.pessoaRepository.findAllByIdIn(ids);
        } catch (Exception e) {
            String errorMessage = String.format("Erro ao recuperar informações das Pessoas de IDs: %s", ids.toString());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, e);
        }
    }

    public void updatePessoa(int id, PessoaUpdateRequest pessoaUpdateRequest) {
        try {
            Pessoa pessoaTarget = this.pessoaRepository.findById(id);
            if (pessoaTarget == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada.");
            }

            if (pessoaUpdateRequest.getNome() != null) {
                pessoaTarget.setNome(pessoaUpdateRequest.getNome());
            }
            if (pessoaUpdateRequest.getDataNascimento() != null) {
                pessoaTarget.setDataNascimento(pessoaUpdateRequest.getDataNascimento());
            }
            if (pessoaUpdateRequest.getEnderecos() != null) {
                pessoaTarget.setEnderecos(pessoaUpdateRequest.getEnderecos());
            }

            this.pessoaRepository.save(pessoaTarget);

        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data de nascimento " +
                    "deve estar no formato dd/MM/yyyy.");
        } catch (Exception e) {
            String errorMessage = String.format("Erro ao atualizar informações da Pessoa de ID: %d",
                    id);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, e);
        }
    }

    public void deletePessoa(int id) {
        if (!this.pessoaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada.");
        }

        try {
            this.pessoaRepository.deleteById(id);
        } catch (Exception e) {
            String errorMessage = String.format("Erro ao deletar Pessoa de ID: %d", id);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, e);
        }
    }

    public List<Pessoa> searchPessoas(PessoaSearchOptions searchOption, String query) {
        List<Pessoa> results;
        try {
            switch (searchOption) {
                case nome -> results = this.pessoaRepository.searchAllByNomeIgnoreCase(query);
                case cep -> results = this.pessoaRepository.searchAllByEnderecos_CepIgnoreCase(query);
                case cidade -> results = this.pessoaRepository.searchAllByEnderecos_CidadeIgnoreCase(query);
                case logradouro -> results = this.pessoaRepository.searchAllByEnderecos_LogradouroIgnoreCase(query);
                default -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Um parametro de pesquisa deve ser" +
                            "especificado");
                }
            }

        } catch (Exception e) {
            String errorMessage = String.format("Erro ao pesquisar por pessoas usando %s como parametro: ",
                    searchOption.toString().toLowerCase());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, e);
        }

        if (results == null || results.size() == 0) {
            String errorMessage = String.format("Nenhum resultado encontrado.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);
        }

        return results;
    }


}
