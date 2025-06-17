<?php

// require_once 'classes/Usuario.php';
// require_once 'classes/Curso.php';

// $erros = [];

// $apiCursos = new Curso();
// $data = $apiCursos->getCursos();
// $cursos = $data['cursos'] ?? [];

// if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['submit'])) {

//     $nome = $_POST['nome'];
//     $email = $_POST['email'];
//     $senha_hash = $_POST['senha'];
//     $curso_id = $_POST['curso_id'];

//     $usuario = new Usuario();
//     $response = $usuario->cadastrar($nome, $email, $senha_hash, $curso_id);


//     if (isset($response['errors']) && is_array($response['errors'])) {

//         foreach ($response['errors'] as $erro_obj) {

//             if (isset($erro_obj['message'])) {
//                 $erros[] = $erro_obj['message'];
//             }
//         }
//     }

//     if (empty($erros)) {
//         header("location: ./login.php");
//         exit;
//     }

// }


require_once 'classes/Usuario.php';
require_once 'classes/Curso.php';

$erros = [];

$apiCursos = new Curso();
$data = $apiCursos->getCursos();
$cursos = $data['cursos'] ?? [];

// Verificação do formulário quando enviado
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['submit'])) {

    // 1. Coleta segura dos dados do formulário
    $nome = $_POST['nome'] ?? '';
    $email = $_POST['email'] ?? '';
    $senha = $_POST['senha'] ?? '';
    $confirmar_senha = $_POST['confirmar_senha'] ?? ''; // Coleta o novo campo de confirmação
    $curso_id = $_POST['curso_id'] ?? '';

    // 2. Bloco de Validações (antes de qualquer ação com o banco ou API)
    if (empty($nome) || empty($email) || empty($senha) || empty($curso_id)) {
        $erros[] = 'Todos os campos são obrigatórios.';
    }

    if ($senha !== $confirmar_senha) {
        $erros[] = 'As senhas não conferem. Por favor, tente novamente.';
    }

    if (strlen($senha) < 6) {
        $erros[] = 'A senha deve ter no mínimo 6 caracteres.';
    }
    
    // 3. Se não houver erros de validação, prossiga com o cadastro
    if (empty($erros)) {
        
        // Criptografa a senha ANTES de enviar para a API/banco
        $senha_hash = password_hash($senha, PASSWORD_DEFAULT);

        // Cria o usuário e chama o método de cadastro
        $usuario = new Usuario();
        $response = $usuario->cadastrar($nome, $email, $senha_hash, $curso_id);

        // Verifica a resposta da API (ex: email já cadastrado)
        if (isset($response['errors']) && is_array($response['errors'])) {
            foreach ($response['errors'] as $erro_obj) {
                if (isset($erro_obj['message'])) {
                    $erros[] = $erro_obj['message'];
                }
            }
        } else {
            // Se a API não retornou erros e o cadastro foi um sucesso, redireciona
            header("location: ./login.php");
            exit;
        }
    }
    // Se a variável $erros tiver conteúdo (seja da validação inicial ou da API),
    // o script continuará para o HTML e exibirá os erros no local que você já definiu.
}

?>
<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Unialfa eventos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-4Q6Gf2aSP4eDXB8Miphtr37CMZZQ5oXLH2yaXMJ2w8e2ZtHTl7GptT4jmndRuHDT" crossorigin="anonymous"
        defer>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-j1CDi7MgGQ12Z7Qab0qlWQ/Qqz24Gc6BM0thvEMVjHnfYGF0rmFCozFSxQBxwHKO" crossorigin="anonymous"
        defer></script>
    <link rel="stylesheet" href="css/style.css">
    <link rel="icon" type="image/png" href="images/favicon.png">
</head>

<body>

    <div class="container col-4">

        <div id="logo" class="d-flex justify-content-center align-items-center mt-5 mb-3">
            <img src="images/logoUnialfa.png" alt="logo da Unialfa" width="412" height="150">
        </div>
        <form method="POST">

            <div>
                <div class="mb-3">
                    <label for="nome" class="form-label">Nome:</label>
                    <input type="text" value="<?= $_POST['nome'] ?? '' ?>" class="form-control border-dark" id="nome"
                        name="nome" required>
                </div>
                <div class="mb-3">
                    <label for="email" class="form-label">E-mail:</label>
                    <input type="email" value="<?= $_POST['email'] ?? '' ?>" class="form-control border-dark" id="email"
                        name="email" aria-describedby="emailHelp" required>
                </div>
                <div class="mb-3">
                    <label for="senha" class="form-label">Senha:</label>
                    <input type="password" value="<?= $_POST['senha'] ?? '' ?>" class="form-control border-dark"
                        id="senha" name="senha" required>
                </div>
                <div class="mb-3">
                    <label for="confirmar_senha" class="form-label">Confirme sua Senha:</label>
                    <input type="password" class="form-control border-dark" id="confirmar_senha" name="confirmar_senha"
                        required>
                </div>
                <div class="mb-3">
                    <label for="curso_id" class="form-label">Selecione um Curso:</label>
                    <select class="form-select border-dark" id="curso_id" name="curso_id" required>
                        <?php if (empty($cursos)) { ?>
                            <option value="" selected disabled>Sem cursos cadastrados</option>
                        <?php } else { ?>
                            <option value="" selected disabled>Clique para selecionar um curso</option>
                            <?php foreach ($cursos as $curso) { ?>
                                <option value="<?= htmlspecialchars($curso['id']) ?>">
                                    <?= htmlspecialchars($curso['nome']) ?>
                                </option>
                            <?php } ?>
                        <?php } ?>
                    </select>
                </div>
                <ul>
                    <li id="msgErro" class="text-danger"> <?php if (!empty($erros)): ?>
                            <?php foreach ($erros as $erro): ?>
                                <?= htmlspecialchars($erro) ?> <a href="login.php">Ir para login.</a><br>
                            <?php endforeach; ?>
                        <?php endif; ?>
                    </li>
                </ul>
                <div class="cointainer-btn">
                    <button type="submit" name="submit" class="btn" id="btnLgn">Casdastrar</button>
                </div>
            </div>
        </form>
    </div>

</body>

</html>