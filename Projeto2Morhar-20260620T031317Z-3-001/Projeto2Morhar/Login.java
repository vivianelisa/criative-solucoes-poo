import entidades.Funcionario;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class Login {

    private BufferedReader scanner;
    private static final String SUPER_USER = "Admin";
    private static final String SUPER_SENHA = "1234";
    private static final int MAX_TENTATIVAS = 3;

    private List<Funcionario> listaFuncionarios;

    public Login(BufferedReader scanner, List<Funcionario> listaFuncionarios) {
        this.scanner = scanner;
        this.listaFuncionarios = listaFuncionarios;
    }

    public UsuarioLogado autenticar() throws IOException {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                ║");
        System.out.println("║             SISTEMA DE LOGIN - GESTÃO DE CHAMADOS              ║");
        System.out.println("║                                                                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println("\nPara acessar o sistema, você precisa fazer login.\n");

        int tentativas = 0;

        while (tentativas < MAX_TENTATIVAS) {
            System.out.print("Usuário: ");
            String usuario = scanner.readLine().trim();

            System.out.print("Senha: ");
            String senha = scanner.readLine().trim();

            // Verificar SuperUser
            if (usuario.equals(SUPER_USER) && senha.equals(SUPER_SENHA)) {
                System.out.println("\nLogin realizado com sucesso!");
                System.out.println("Bem-vindo, Superusuário!\n");
                return new UsuarioLogado(usuario, "superuser", "ADM-001");
            }

            // Verificar Funcionário
            UsuarioLogado usuarioFuncionario = verificarFuncionario(usuario, senha);
            if (usuarioFuncionario != null) {
                System.out.println("\nLogin realizado com sucesso!");
                System.out.println("Bem-vindo, " + usuario + "!\n");
                return usuarioFuncionario;
            }

            tentativas++;
            if (tentativas < MAX_TENTATIVAS) {
                System.out.println("\nUsuário ou senha incorretos!");
                System.out.println("Tentativas restantes: " + (MAX_TENTATIVAS - tentativas) + "\n");
            }
        }

        System.out.println("\nNúmero máximo de tentativas excedido!");
        System.out.println("Acesso negado. Sistema será encerrado.\n");
        return null;
    }

    private UsuarioLogado verificarFuncionario(String usuario, String senha) {
        // Funcionários registrados para teste
        if (usuario.equals("tecnico01") && senha.equals("senha123")) {
            return new UsuarioLogado(usuario, "funcionario", "TEC-001");
        }
        if (usuario.equals("tecnico02") && senha.equals("senha456")) {
            return new UsuarioLogado(usuario, "funcionario", "TEC-002");
        }
        if (usuario.equals("maria") && senha.equals("maria123")) {
            return new UsuarioLogado(usuario, "funcionario", "TEC-003");
        }
        if (usuario.equals("carlos") && senha.equals("carlos123")) {
            return new UsuarioLogado(usuario, "funcionario", "TEC-004");
        }

        // Verificar na lista de funcionários carregados
        for (Funcionario f : listaFuncionarios) {
            if (f.getUsuario() != null && f.getSenha() != null &&
                f.getUsuario().equals(usuario) && f.getSenha().equals(senha)) {
                return new UsuarioLogado(usuario, "funcionario", f.getId());
            }
        }

        return null;
    }

    public static void exibirCredenciaisDisponveis() {
        System.out.println("\n╔═════════════════════════════════════════════════════════════════╗");
        System.out.println("║                   CREDENCIAIS DISPONÍVEIS                        ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
        System.out.println("\nSUPERUSUÁRIO:");
        System.out.println("   Usuário: Admin");
        System.out.println("   Senha: 1234");
        System.out.println("\nFUNCIONÁRIOS:");
        System.out.println("   ├─ Usuário: tecnico01 | Senha: senha123");
        System.out.println("   ├─ Usuário: tecnico02 | Senha: senha456");
        System.out.println("   ├─ Usuário: maria    | Senha: maria123");
        System.out.println("   └─ Usuário: carlos   | Senha: carlos123");
        System.out.println();
    }

    public static class UsuarioLogado {
        public String usuario;
        public String tipo;
        public String id;

        public UsuarioLogado(String usuario, String tipo, String id) {
            this.usuario = usuario;
            this.tipo = tipo;
            this.id = id;
        }

        public boolean isSuperUser() {
            return tipo.equals("superuser");
        }

        public boolean isFuncionario() {
            return tipo.equals("funcionario");
        }

        @Override
        public String toString() {
            return "Usuário: " + usuario + " | Tipo: " + tipo;
        }
    }
}