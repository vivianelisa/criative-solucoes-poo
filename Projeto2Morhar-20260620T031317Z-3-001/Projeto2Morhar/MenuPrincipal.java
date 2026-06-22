import entidades.Chamado;
import entidades.Cliente;
import entidades.Funcionario;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MenuPrincipal {
    /** Mude para false para desativar os dados de teste. */
    private static final boolean MODO_TESTE = true;

    private static BufferedReader scanner;
    private static List<Cliente>    listaClientes;
    private static List<Funcionario> listaFuncionarios;
    private static List<Chamado>    listaChamados;

    public static void main(String[] args) throws IOException {
        scanner           = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        listaChamados     = GerenciadorArquivo.carregar();
        listaClientes     = GerenciadorCadastro.carregarClientes();
        listaFuncionarios = GerenciadorCadastro.carregarFuncionarios();

        if (MODO_TESTE && listaChamados.isEmpty()) {
            DadosTeste.popularChamados(listaChamados);
        }

        if (MODO_TESTE && listaClientes.isEmpty()) {
            DadosTeste.popularClientes(listaClientes);
        }

        while (true) {
            exibirMenuPrincipal();
        }
    }

    private static void exibirMenuPrincipal() throws IOException {
        System.out.println("\n================================================");
        System.out.println("   CRIATIVE SOLUÇÕES - INTEGRAÇÃO DE FLUXO");
        System.out.println("================================================\n");
        System.out.println("Escolha qual módulo deseja acessar:\n");
        System.out.println("1. Sistema de Cadastro");
        System.out.println("2. Gestão de Chamados");
        System.out.println("0. Sair\n");

        System.out.print("Digite sua escolha: ");
        String opcao = scanner.readLine().trim();

        switch (opcao) {
            case "1":
                executarSistemaCadastro();
                break;
            case "2":
                executarGestaoChamados();
                break;
            case "0":
                System.out.println("\nSistema encerrado. Até logo!\n");
                System.exit(0);
                break;
            default:
                System.out.println("\nOpção inválida! Tente novamente.\n");
        }
    }

    private static void executarSistemaCadastro() throws IOException {

        System.out.println("\n================================================");
        System.out.println("         INICIANDO SISTEMA DE CADASTRO");
        System.out.println("================================================\n");
        try {
            Login login = new Login(scanner, listaFuncionarios);
            Login.UsuarioLogado usuarioLogado = login.autenticar();
            if (usuarioLogado == null) {
                System.out.println("Acesso negado. Voltando ao menu principal.\n");
                return;
            }
            SistemaCadastro.executar(listaClientes, listaFuncionarios, scanner, usuarioLogado.tipo);
        } catch (Exception e) {
            System.out.println("Erro ao executar Sistema de Cadastro: " + e.getMessage());
        }
        System.out.println("\nVoltando ao menu principal...\n");
    }

    private static void executarGestaoChamados() throws IOException {
        System.out.println("\n================================================");
        System.out.println("         INICIANDO GESTÃO DE CHAMADOS");
        System.out.println("================================================\n");
        try {
            GestaoChamados.executar(listaChamados, listaClientes, listaFuncionarios, scanner);
        } catch (Exception e) {
            System.out.println("Erro ao executar Gestão de Chamados: " + e.getMessage());
        }
        System.out.println("\nVoltando ao menu principal...\n");
    }
}