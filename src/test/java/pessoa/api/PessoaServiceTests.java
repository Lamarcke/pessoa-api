package pessoa.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pessoa.api.model.entity.Endereco;
import pessoa.api.model.entity.Pessoa;
import pessoa.api.service.PessoaService;
import pessoa.api.utils.PessoaSearchOptions;
import pessoa.api.utils.PlaceholderValues;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@SpringBootTest
public class PessoaServiceTests {

    @Autowired
    PessoaService pessoaService;

    /**
     * Testa o cadastro de uma pessoa ficticia.
     * Também verifica se a nova entidade Pessoa foi salva corretamente.
     */
    @Test
    public void testRegisterPessoa() {
        Pessoa testPessoa = PlaceholderValues.getPlaceholderPessoa();
        // Persiste a nova entidade Pessoa no banco de dados.
        pessoaService.registerPessoa(testPessoa);

        // Verifica se a nova entidade foi adiciona corretamente.
        Integer testPessoaId = testPessoa.getId();
        Pessoa savedPessoa = pessoaService.getPessoa(testPessoaId);
        assert savedPessoa != null;
    }

    /*
     * Testa o retorno do método get de todas as entidades "Pessoa" no banco de dados.
     */
    @Test
    public void testGetPessoas() {
        Pessoa pessoa = PlaceholderValues.getPlaceholderPessoa();
        pessoaService.registerPessoa(pessoa);
        List<Pessoa> pessoas = pessoaService.getPessoas();
        assertThat(pessoas).isNotNull();
        assertThat(pessoas.size()).isGreaterThan(0);

    }

    /*
     * Testa a busca de uma pessoa pelo nome.
     */
    @Test
    public void testSearchPessoa() {
        Pessoa testPessoa = PlaceholderValues.getPlaceholderPessoa();
        pessoaService.registerPessoa(testPessoa);

        String testPessoaName = testPessoa.getNome();
        List<Pessoa> pessoas = pessoaService.searchPessoas(PessoaSearchOptions.nome, testPessoaName);
        assertThat(pessoas).isNotNull();
        assertThat(pessoas.size()).isGreaterThan(0);
    }

    /*
     * Testa a busca por pessoas utilizando partes do endereço como base.
     */
    @Test
    public void testSearchPessoasUsingEndereco() {
        Pessoa testPessoa = PlaceholderValues.getPlaceholderPessoa();
        pessoaService.registerPessoa(testPessoa);

        Endereco testPessoaEndereco = testPessoa.getEnderecos().get(0);
        String testPessoaCidade = testPessoaEndereco.getCidade();
        String testPessoaLogradouro = testPessoaEndereco.getLogradouro();
        String testPessoaCep = testPessoaEndereco.getCep();

        List<Pessoa> pessoasCidadeSearch = this.pessoaService.searchPessoas(PessoaSearchOptions.cidade,
                testPessoaCidade);
        List<Pessoa> pessoasLogradouroSearch = this.pessoaService.searchPessoas(PessoaSearchOptions.logradouro,
                testPessoaLogradouro);
        List<Pessoa> pessoasCepSearch = this.pessoaService.searchPessoas(PessoaSearchOptions.cep,
                testPessoaCep);

        assertThat(pessoasCidadeSearch).isNotNull();
        assertThat(pessoasCidadeSearch.size()).isGreaterThan(0);
        assertThat(pessoasLogradouroSearch).isNotNull();
        assertThat(pessoasLogradouroSearch.size()).isGreaterThan(0);
        assertThat(pessoasCepSearch).isNotNull();
        assertThat(pessoasCepSearch.size()).isGreaterThan(0);

    }
}
