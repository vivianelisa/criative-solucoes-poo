import entidades.Cliente;
import entidades.Funcionario;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorCadastro {

    private static final String ARQUIVO_CLIENTES    = "clientes.csv";
    private static final String ARQUIVO_FUNCIONARIOS = "funcionarios.csv";
    private static final String SEPARADOR = ";";

    // ==================== CLIENTES ====================

    public static void salvarClientes(List<Cliente> lista) {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(ARQUIVO_CLIENTES), StandardCharsets.UTF_8))) {
            pw.println("id;nome;cpf;genero;idade;endereco;complemento;telefone;dataHoraCadastro");
            for (Cliente c : lista) {
                pw.println(
                    escapar(c.getId())            + SEPARADOR +
                    escapar(c.getNome())          + SEPARADOR +
                    escapar(c.getCpf())           + SEPARADOR +
                    escapar(c.getGenero())        + SEPARADOR +
                    c.getIdade()                  + SEPARADOR +
                    escapar(c.getEndereco())      + SEPARADOR +
                    escapar(c.getComplemento())   + SEPARADOR +
                    escapar(c.getTelefone())      + SEPARADOR +
                    c.getDataHoraCadastro()
                );
            }
            System.out.println("Clientes salvos em '" + ARQUIVO_CLIENTES + "'.");
        } catch (IOException e) {
            System.out.println("Erro ao salvar clientes: " + e.getMessage());
        }
    }

    public static List<Cliente> carregarClientes() {
        List<Cliente> lista = new ArrayList<>();
        File arquivo = new File(ARQUIVO_CLIENTES);
        if (!arquivo.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ARQUIVO_CLIENTES), StandardCharsets.UTF_8))) {
            br.readLine(); // cabeçalho
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] p = linha.split(SEPARADOR, -1);
                if (p.length >= 9) {
                    Cliente c = new Cliente(p[1], p[2], p[3], Integer.parseInt(p[4]), p[5], p[6], p[7]);
                    c.setId(p[0]);
                    lista.add(c);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar clientes: " + e.getMessage());
        }
        return lista;
    }

    // ==================== FUNCIONÁRIOS ====================

    public static void salvarFuncionarios(List<Funcionario> lista) {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(ARQUIVO_FUNCIONARIOS), StandardCharsets.UTF_8))) {
            pw.println("id;nome;cpf;genero;idade;endereco;complemento;telefone;usuario;senha;dataHoraCadastro");
            for (Funcionario f : lista) {
                pw.println(
                    escapar(f.getId())            + SEPARADOR +
                    escapar(f.getNome())          + SEPARADOR +
                    escapar(f.getCpf())           + SEPARADOR +
                    escapar(f.getGenero())        + SEPARADOR +
                    f.getIdade()                  + SEPARADOR +
                    escapar(f.getEndereco())      + SEPARADOR +
                    escapar(f.getComplemento())   + SEPARADOR +
                    escapar(f.getTelefone())      + SEPARADOR +
                    escapar(f.getUsuario() != null ? f.getUsuario() : "") + SEPARADOR +
                    escapar(f.getSenha()    != null ? f.getSenha()    : "") + SEPARADOR +
                    f.getDataHoraCadastro()
                );
            }
            System.out.println("Funcionários salvos em '" + ARQUIVO_FUNCIONARIOS + "'.");
        } catch (IOException e) {
            System.out.println("Erro ao salvar funcionários: " + e.getMessage());
        }
    }

    public static List<Funcionario> carregarFuncionarios() {
        List<Funcionario> lista = new ArrayList<>();
        File arquivo = new File(ARQUIVO_FUNCIONARIOS);
        if (!arquivo.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ARQUIVO_FUNCIONARIOS), StandardCharsets.UTF_8))) {
            br.readLine(); // cabeçalho
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] p = linha.split(SEPARADOR, -1);
                if (p.length >= 11) {
                    Funcionario f = new Funcionario(p[1], p[2], p[3], Integer.parseInt(p[4]), p[5], p[6], p[7]);
                    f.setId(p[0]);
                    if (!p[8].isEmpty()) f.setUsuario(p[8]);
                    if (!p[9].isEmpty()) f.setSenha(p[9]);
                    lista.add(f);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar funcionários: " + e.getMessage());
        }
        return lista;
    }

    // ==================== UTILITÁRIO ====================

    /** Envolve o valor em aspas duplas se contiver o separador, para não quebrar o CSV. */
    private static String escapar(String valor) {
        if (valor == null) return "";
        if (valor.contains(SEPARADOR) || valor.contains("\"")) {
            return "\"" + valor.replace("\"", "\"\"") + "\"";
        }
        return valor;
    }
}
