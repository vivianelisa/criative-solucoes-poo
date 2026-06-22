package entidades;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Funcionario {
    private String id;
    private String nome;
    private String cpf;
    private String genero;
    private int idade;
    private String endereco;
    private String complemento;
    private String telefone;
    private String usuario;
    private String senha;
    private LocalDateTime dataHoraCadastro;

    public Funcionario(String nome, String cpf, String genero, int idade,
                       String endereco, String complemento, String telefone) {
        this.id = UUID.randomUUID().toString().substring(0, 6);
        this.nome = nome;
        this.cpf = cpf;
        this.genero = genero;
        this.idade = idade;
        this.endereco = endereco;
        this.complemento = complemento;
        this.telefone = telefone;
        this.dataHoraCadastro = LocalDateTime.now();
        this.usuario = null;
        this.senha = null;
    }

    // ==================== GETTERS ====================
    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getGenero() {
        return genero;
    }

    public int getIdade() {
        return idade;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getComplemento() {
        return complemento;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getSenha() {
        return senha;
    }

    public LocalDateTime getDataHoraCadastro() {
        return dataHoraCadastro;
    }

    // ==================== SETTERS ====================
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDataAbertura(LocalDateTime dataHoraCadastro) {
        this.dataHoraCadastro = dataHoraCadastro;
    }

    @Override
    public String toString() {
        DateTimeFormatter d = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter h = DateTimeFormatter.ofPattern("HH:mm:ss");

        return String.format(
            "ID: %s | Nome: %-40s | CPF: %s | Gênero: %s | Idade: %d | " +
            "Endereço: %s | Complemento: %s | Telefone: %s \nCadastrado em %s às %s\n\n",
            id, nome, cpf, genero, idade, endereco, complemento, telefone,
            d.format(dataHoraCadastro), h.format(dataHoraCadastro));
    }
}